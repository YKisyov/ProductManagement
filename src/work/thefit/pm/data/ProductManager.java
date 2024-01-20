package work.thefit.pm.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

/**
 * Used for the Factory method pattern. Added in ver. 0.6.3;
 *
 * @version 0.10.0 - Here we are going to try to segregate the business logic from the locale/print
 * logic needed for displaying the data. In order to do so, I am going to implement an inner
 * class - it will be private, nested class with name ResourceFormatter. The main purpose
 * of the ResourceFormatter inner class will be to split the business logic from the Formatting
 * logic and do some sort of housekeeping and organizing the Product class in more maintainable
 * manner. On top of that, we plan to add some more sorting version via lambda functions and
 * method referenced.
 * While this update is minor and does not intend to introduce new features, it will make
 * the code a bit more organized and structured.
 */

/*
TODO: A more generic (alternative) design of this application could have been used a type Ratable instead of Product
   for both instance variables and method argument, to enable application to create reviews of any other object
   that implements Ratable interface.
   */
public class ProductManager {


    /*
     * TsODO check if this can be implemented eventually via ENUM and moved to Shop class,
     *  as locale data is generally a specific feature (problem of the shop, as shops are
     *  bounded to specific regions and nations, that use different locales). Any ways, will
     *  leave this here to think about it...
     * */

    private ResourceFormatter formatter;
    private static final Map<String, ResourceFormatter> formatters = Map.of(
            "en-GB", new ResourceFormatter(new Locale("en", "GB")),
            "en-US", new ResourceFormatter(new Locale("en", "US")),
            "bg-BG", new ResourceFormatter(new Locale("bg", "BG")),
            "ja-JP", new ResourceFormatter(new Locale("ja", "JP"))
    );
    private Map<Product, List<Review>> products = new HashMap<>();

    /**
     * Method {@Link changeLocale(String languageTag)} is used to change the current locale of the ProductManager class.
     *
     * @param languageTag shall be a String in the format of two lower case letters, representing the country code of the
     *                    requested locale, imitatively followed by an underscore symbol and then two uppercase letters that represent the
     *                    country. For example "en-GB" will return English local and formatting specific for Great Britain.
     * @Since version 0.10.0;
     */
    public void changeLocale(String languageTag) {
        this.formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));
    }

    /**
     * This method is used to ask the {@Link ProductManager} what are the supported locales.
     *
     * @return a set of all supported locales.
     * @since version 0.10.0;
     */
    public static Set<String> getSupportedLocales() {
        return formatters.keySet();
    }

    public ProductManager(Locale locale) {
        // my own code was like this: changeLocale(locale.getLanguage() + "_" + locale.getCountry());
        this(locale.toLanguageTag());
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
        return reviewProduct(findProduct(productID), rating, comments);
    }

    public Product reviewProduct(Product product, Rating rating, String comments) {

        /*
         * As all keys in our design are immutable, we will have to update the key. This is needed due to the fact,
         * that applyRating() method of the Product class actually uses the average of all ratings and store the value
         * as Product member. Since Products are immutable, the updated average rating can come to live only via
         * new instance of the old state of the object.
         * This is why we 1st remove the old state of the KEY product and later on put it back in the Map.
         */

        List<Review> listOfCurrentReviews = products.remove(product);

        /*
         * TODO Later on shall be re-implemented via "throws"
         *
         * */
        if (listOfCurrentReviews == null) {
            System.out.println("ERROR! Our system can not find the product you are trying to review.\n" +
                    "As a result Your review was not stored.\n" +
                    "Please, try again later.\n");
            return null;
        }

        listOfCurrentReviews.add(new Review(rating, comments));

        int sumOfStarts = 0, avgRating = 0;
        int reviewsCounter = listOfCurrentReviews.size();

        for (Review currReview : listOfCurrentReviews) {
            sumOfStarts += currReview.getRating().ordinal();
        }
        avgRating = Math.round(sumOfStarts / (float) reviewsCounter);
        Product updatedProduct = product.applyRating(avgRating);

        //putting the new product as new key in the map.
        products.put(updatedProduct, listOfCurrentReviews);
        return updatedProduct;
    }

    public Product findProduct(int id) {
        Product searchedProduct = null;
        for (Product currProduct : products.keySet()) {
            if (currProduct.getId() == id) {
                searchedProduct = currProduct;
                break;
            }
        }
        return searchedProduct;
    }

    public void printProductReport(int productId) {
        printProductReport(findProduct(productId));
    }

    public void printProductReport(Product product) {

        //TODO figure out what shall we do in case null is returned by the products.get(product) call
        List<Review> listOfReviews = products.get(product);

        StringBuilder sb = new StringBuilder();
        sb.append(formatter.formatProduct(product));
        sb.append(System.lineSeparator());

        Collections.sort(listOfReviews);
        for (Review currReview : listOfReviews) {
            sb.append(formatter.formatReview(currReview));
            sb.append(System.lineSeparator());
        }
        if (listOfReviews.isEmpty()) {
            sb.append(formatter.getText("no.reviews"));
            sb.append(System.lineSeparator());
        }

        System.out.println(sb);
    }

    public void printProduct(Comparator<Product> sorter) {
        List<Product> productList = new ArrayList<>(products.keySet());
        productList.sort(sorter);
        StringBuilder sb = new StringBuilder();
        for (Product product : productList) {
            sb.append(product);
            sb.append(System.lineSeparator());
        }

        System.out.println(sb);
    }

    private static class ResourceFormatter {
        private Locale locale;
        private ResourceBundle resources;
        private DateTimeFormatter dateFormat;
        private NumberFormat moneyFormat;

        private ResourceFormatter(Locale locale) {
            this.locale = locale;
            resources = ResourceBundle.getBundle("work.thefit.pm.data.resources", locale);
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
            moneyFormat = NumberFormat.getCurrencyInstance(locale);
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

