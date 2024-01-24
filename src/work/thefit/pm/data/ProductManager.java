package work.thefit.pm.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * All loops will be swapped with Stream APIs and lambda expressions to shorten code and increase readability.
 *
 * @version 0.11.0 - taking advantage of some Java 8 features like lambda expressions and Stream API.
 */

public class ProductManager {

    private ResourceFormatter formatter;
    private static final Map<String, ResourceFormatter> formatters = Map.of(
            "en_GB", new ResourceFormatter(new Locale("en", "GB")),
            "en_US", new ResourceFormatter(new Locale("en", "US")),
            "bg_BG", new ResourceFormatter(new Locale("bg", "BG")),
            "ja_JP", new ResourceFormatter(new Locale("ja", "JP"))
    );
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Map<Product, List<Review>> products = new HashMap<>();
    private final ResourceBundle configParser = ResourceBundle.getBundle("work.thefit.pm.data.configParser");
    private MessageFormat productFormatForCSV = new MessageFormat(configParser.getString("product.data.format"));
    private MessageFormat reviewFormatForCSV = new MessageFormat(configParser.getString("review.data.format"));

    /**
     * Method {@link #changeLocale(String languageTag)} is used to change the current locale of the ProductManager class.
     *
     * @param languageTag, shall be a String in the format of two lower case letters, representing the country code of the
     *                     requested locale, imitatively followed by an underscore symbol and then two uppercase letters that represent the
     *                     country. For example "en-GB" will return English local and formatting specific for Great Britain.
     *                     {@since} version 0.10.0;
     */
    public void changeLocale(String languageTag) {
        this.formatter = formatters.getOrDefault(languageTag, formatters.get("en_GB"));
    }

    /**
     * This method is used to ask the {@link ProductManager} what are the supported locales.
     *
     * @return a set of all supported locales.
     * @since version 0.10.0;
     */
    public static Set<String> getSupportedLocales() {
        return formatters.keySet();
    }

    public ProductManager(String languageTag) {
        changeLocale(languageTag);
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        Product product = new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        Product product = new Drink(id, name, price, rating);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product reviewProduct(int productID, Rating rating, String comments) {
        try {
            return reviewProduct(findProduct(productID), rating, comments);
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
            return null;
        }
    }

    /**
     * This method is used to review a Product. If the product is <b>known </b> to the ProductManager, then it can be reviewed.
     * In order for a product to be known to the {@link ProductManager} it must be already either created by the
     * {@link #createProduct(int, String, BigDecimal, Rating)}, or imported via {@link #parseProduct(String text)} methods.
     * <p>
     * <p>
     * parsed used {link #}
     *
     * @param product  could be any instance of concrete {@link Product} objects.
     * @param rating   any value of the {@link Rating} type.
     * @param comments text describing of reviews left by the customer/client.
     * @return a new instance of the immutable Product with updated review properties or {@link null} if you try
     * to review unexciting Product that is yet unknown to the Product manager.
     */
    public Product reviewProduct(Product product, Rating rating, String comments) {

        /*
         * As all keys in our design are immutable, we will have to update the key. This is needed due to the fact,
         * that applyRating() method of the Product class actually uses the average of all ratings and store the value
         * as Product member. Since Products are immutable, the updated average rating can come to live only via
         * new instance of the old state of the object.
         * This is why we 1st remove the old state of the KEY product and later on put it back in the Map.
         */
        List<Review> listOfCurrentReviews = products.remove(product);

        /** TODO Later on shall be re-implemented via "throws"
         *
         * */
        if (listOfCurrentReviews == null) {
            System.out.println("ERROR! Our system can not find the product you are trying to review.\n" +
                    "As a result Your review was not stored.\n" +
                    "Please, try again later.\n");
            return null;
        }

        listOfCurrentReviews.add(new Review(rating, comments));
        OptionalDouble averageRating = listOfCurrentReviews.stream()
                .mapToInt((currReview) -> currReview.getRating().ordinal())
                .average();

        Product updatedProduct = averageRating.isEmpty() ? product.applyRating(rating)
                : product.applyRating(Ratable.convert((int) Math.round(averageRating.getAsDouble())));

        products.put(updatedProduct, listOfCurrentReviews);
        return updatedProduct;

    }

    public Product findProduct(int id) throws ProductManagerException {

        return products.keySet().stream()
                .parallel()
                .filter(product -> product.getId() == id)
                .findAny()  //I chose this method due to the parallel call of Stream API in this method.
                .orElseThrow(() -> new ProductManagerException("Product with ID " + id + " was not found."));
    }

    public void printProductReport(int productId) {
        try {
            printProductReport(findProduct(productId));
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "ProductManager{" +
                "formatter=" + formatter +
                '}';
    }

    public void printProductReport(Product product) {

        //TODO figure out what shall we do in case null is returned by the products.get(product) call
        List<Review> listOfReviews = products.get(product);

        StringBuilder sb = new StringBuilder();
        sb.append(formatter.formatProduct(product));
        sb.append(System.lineSeparator());

        Collections.sort(listOfReviews);

        if (listOfReviews.isEmpty()) {
            sb.append(formatter.getText("no.reviews"))
                    .append(System.lineSeparator());
        } else {
            sb.append(listOfReviews.stream()
                    .parallel()
                    .map(currReview -> formatter.formatReview(currReview) + System.lineSeparator())
                    .collect(Collectors.joining())
            );

        }
        System.out.println(sb);

    }

    public void printProduct(Predicate<Product> filter, Comparator<Product> sorter) {

        StringBuilder sb = new StringBuilder();
        sb.append(products.keySet().stream()
                .parallel()
                .sorted(sorter)
                .filter(filter)
                .map(product -> product.toString() + System.lineSeparator())
                .collect(Collectors.joining())
        );
        System.out.println(sb.toString());

    }

    /**
     * @param csvText made of comma separated values in the following format.
     */
    public void parseReview(String csvText) {
        //TODO move to json, or trim the values[] as parse() method doesn't do any trimming and as a result passing " 3" will trow NumberFormatException.
        try {

            Object[] values = reviewFormatForCSV.parse(csvText);
            reviewProduct(Integer.parseInt((String) values[0]),
                    Ratable.convert(Integer.parseInt((String) values[1])),
                    (String) values[2]);
        } catch (ParseException | NumberFormatException e) {
            logger.log(Level.WARNING, "Error parsing review " + csvText, e.getMessage());
        }
    }

    public void parseProduct(String csvText) {

        try {
            Object[] values = productFormatForCSV.parse(csvText);
            //expected format is: "F, 103, Cake, 3.99, 0, 2024-03-03"
            char productType = ((String) values[0]).charAt(0);
            switch (productType) {
                case 'F':
                    createProduct(
                            Integer.parseInt((String) values[1]),
                            (String) values[2],
                            BigDecimal.valueOf(Double.parseDouble((String) values[3])),
                            Ratable.convert(Integer.parseInt((String) values[4])),
                            LocalDate.parse((String) values[5]));
                    break;
                case 'D':
                    createProduct(
                            Integer.parseInt((String) values[1]),
                            (String) values[2],
                            BigDecimal.valueOf(Double.parseDouble((String) values[3])),
                            Ratable.convert(Integer.parseInt((String) values[4])));
                    break;
                default:
                    //Squared an unsupported Product type.
                    throw new IllegalArgumentException("Wrong product type was supplied (" + productType + ") during object creation. " +
                            "Please, try with some of the supported product types.");
            }
        } catch (ParseException | NumberFormatException | DateTimeParseException e) {
            logger.log(Level.WARNING, "No product was created. Error parsing product \"" + csvText + "\". " + e.getMessage(), e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "No product was created. Error parsing product. Reason: " + e.getMessage(), e.getStackTrace());
        }
    }

    public Map<String, String> getDiscounts() {
        /*Map<String, String> avgDiscountBasedOnRating = new HashMap<>(Rating.values().length);

        for (Rating currentRating : List.of(Rating.values())) {
            String currentRatingAsString = currentRating.getStars();
            int currentRatingAsInt = currentRating.ordinal();

            avgDiscountBasedOnRating.put(currentRatingAsString, formatter.backEndDecimalFormatForInternalUsage.format(
                    products.keySet()
                            .stream()
                            .parallel()
                            .filter(p -> p.getRating().ordinal() == currentRatingAsInt)
                            .mapToDouble(p -> p.getDiscount().doubleValue())
                            .average()
                            .orElse(0.d)
            ));
        }
        return avgDiscountBasedOnRating;*/

        return products.keySet()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                product -> product.getRating().getStars(), //Here we get the groupingBy element, then we list the rest of the elements per group.
                                Collectors.collectingAndThen( //here we tell JRE how to "create" the elements for each group.
                                        Collectors.summingDouble(product -> product.getDiscount().doubleValue()),
                                        discount -> formatter.moneyFormat.format(discount)  //Sort of the finished action.
                                )
                        )
                );
    }

    private static class ResourceFormatter {
        private Locale locale;
        private ResourceBundle resources;
        private DateTimeFormatter dateFormat;
        private NumberFormat moneyFormat;
        private NumberFormat backEndDecimalFormatForInternalUsage; //For internal uses during tests and plays.

        private ResourceFormatter(Locale locale) {
            this.locale = locale;
            resources = ResourceBundle.getBundle("work.thefit.pm.data.resources", locale);
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
            moneyFormat = NumberFormat.getCurrencyInstance(locale);

            backEndDecimalFormatForInternalUsage = DecimalFormat.getInstance();
            backEndDecimalFormatForInternalUsage.setMaximumFractionDigits(2);
            backEndDecimalFormatForInternalUsage.setRoundingMode(RoundingMode.HALF_UP);
        }

        private String formatProduct(Product product) {
            return MessageFormat.format(resources.getString("product"),
                    product.getName(),
                    moneyFormat.format(product.getPrice()),
                    product.getRating().getStars(),
                    dateFormat.format(product.getBestBefore()));
        }

        private String formatReview(Review review) {
            return MessageFormat.format(resources.getString("review"),
                    review.getRating().getStars(),
                    review.getComments());
        }

        /**
         * Gets you out any text from the resource bundle files, that does not require any formatting or substitutions.
         *
         * @param key is used to search through the resource bundles file and find a matching key.
         *            Once found it will return the value that's held for that key. For example
         *            the last line of the default resource bundle has "no.reviews=Not reviewed".
         *            If you call this method and pass the "no.review" as argument, the method will return the
         *            "Not reviewed" or whatever value the concrete locale file has for this key.
         * @return the value that's associated with that key as per resource bundle file.
         */
        private String getText(String key) {
            return resources.getString(key);
        }
    }
}