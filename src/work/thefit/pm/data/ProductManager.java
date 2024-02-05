package work.thefit.pm.data;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
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

    //private ResourceFormatter formatter;
    private static final Map<String, ResourceFormatter> formatters = Map.of(
            "en_GB", new ResourceFormatter(new Locale("en", "GB")),
            "en_US", new ResourceFormatter(new Locale("en", "US")),
            "bg_BG", new ResourceFormatter(new Locale("bg", "BG")),
            "ja_JP", new ResourceFormatter(new Locale("ja", "JP"))
    );
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private Lock readLock = rwLock.readLock();
    private Lock writeLock = rwLock.writeLock();
    private static final String DEFAULT_LANGUAGE_TAG = "en_GB";
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Map<Product, List<Review>> products = new HashMap<>();
    private final ResourceBundle configParser = ResourceBundle.getBundle("work.thefit.pm.data.configParser");
    private MessageFormat productFormatForCSV = new MessageFormat(configParser.getString("product.data.format"));
    private MessageFormat reviewFormatForCSV = new MessageFormat(configParser.getString("review.data.format"));

    private static final String userHomeDirectory = ""; //System.getProperty("user.home");
    private final Path reportsFolder = Path.of(userHomeDirectory, configParser.getString("reports.folder"));
    private final Path dataFolder = Path.of(userHomeDirectory, configParser.getString("data.folder"));
    private final Path tempFolder = Path.of(userHomeDirectory, configParser.getString("temp.folder"));
    private static final ProductManager pmSingletonInstance = new ProductManager();

    /**
     * Method {@link #changeLocale(String languageTag)} is used to change the current locale of the ProductManager class.
     *
     * @param languageTag, shall be a String in the format of two lower case letters, representing the country code of the
     *                     requested locale, imitatively followed by an underscore symbol and then two uppercase letters that represent the
     *                     country. For example "en-GB" will return English local and formatting specific for Great Britain.
     *                     {@since} version 0.10.0;
     */
    /*public void changeLocale(String languageTag) {
        this.formatter = formatters.getOrDefault(languageTag, formatters.get("en_GB"));
    }*/

    /**
     * This method is used to ask the {@link ProductManager} what are the supported locales.
     *
     * @return a set of all supported locales.
     * @since version 0.10.0;
     */
    public static Set<String> getSupportedLocales() {
        return formatters.keySet();
    }

    private ProductManager() {
        // changeLocale(languageTag);
        loadAllData();
        // restoreData();
    }

    public static ProductManager getInstance() {
        return pmSingletonInstance;
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        Product product = null;

        try {
            writeLock.lock();
            product = new Food(id, name, price, rating, bestBefore);
            products.putIfAbsent(product, new ArrayList<>());
        } catch (Exception e) {
            logger.log(Level.INFO, "Error adding product " + e.getMessage());
            return null;
        } finally {
            writeLock.unlock();
        }
        return product;
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        Product product = null;
        try {
            writeLock.unlock();
            product = new Drink(id, name, price, rating);
            products.putIfAbsent(product, new ArrayList<>());
        } catch (Exception e) {
            logger.log(Level.INFO, "Error adding product" + e.getMessage());
            return null;
        } finally {
            writeLock.unlock();
        }
        return product;
    }

    public Product reviewProduct(int productID, Rating rating, String comments) {
        try {
            writeLock.lock();
            return reviewProduct(findProduct(productID), rating, comments);
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
            return null;
        } finally {
            writeLock.unlock();
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
    private Product reviewProduct(Product product, Rating rating, String comments) {

        /*
         * As all keys in our design are immutable, we will have to update the key. This is needed due to the fact,
         * that applyRating() method of the Product class actually uses the average of all ratings and store the value
         * as Product member. Since Products are immutable, the updated average rating can come to live only via
         * new instance of the old state of the object.
         * This is why we 1st remove the old state of the KEY product and later on put it back in the Map.
         */
        Product updatedProduct = null;
        try {
            writeLock.lock();
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

            updatedProduct = averageRating.isEmpty() ? product.applyRating(rating)
                    : product.applyRating(Ratable.convert((int) Math.round(averageRating.getAsDouble())));

            products.put(updatedProduct, listOfCurrentReviews);
        } finally {
            writeLock.unlock();
        }
        return updatedProduct;
    }


    public Product findProduct(int id) throws ProductManagerException {
        try {
            readLock.lock();
            return products.keySet().stream()
                    .parallel()
                    .filter(product -> product.getId() == id)
                    .findAny()  //I chose this method due to the parallel call of Stream API in this method.
                    .orElseThrow(() -> new ProductManagerException("Product with ID " + id + " was not found."));
        } finally {
            readLock.unlock();
        }
    }

    public void printProductReport(int productId, String languageTag, String client) {
        ResourceFormatter formatter = formatters.getOrDefault(languageTag, formatters.get(DEFAULT_LANGUAGE_TAG));
        try {
            readLock.lock();
            printProductReport(findProduct(productId), languageTag, client);
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
        } finally {
            readLock.unlock();
        }
    }

 /*   @Override
    public String toString() {
        return "ProductManager{" +
                "formatter=" + formatter +
                '}';
    }*/

    private void printProductReport(Product product, String languageTag, String client) throws ProductManagerException {

        ResourceFormatter formatter = formatters.getOrDefault(languageTag, formatters.get(DEFAULT_LANGUAGE_TAG));
        //TODO figure out what shall we do in case null is returned by the products.get(product) call

        try {
            writeLock.lock();
            List<Review> listOfReviews = products.get(product);

            Path productFile = reportsFolder.resolve(MessageFormat.format(configParser.getString("report.file"),
                    product.getId(), client));
            try {
                Files.createDirectories(reportsFolder);
            } catch (IOException e) {
                throw new ProductManagerException("Error while creating directory: " + "\"" + reportsFolder + "\"." +
                        System.lineSeparator() + e.getMessage());
            }
            try (PrintWriter writeTextToFile =
                         new PrintWriter(
                                 new OutputStreamWriter(
                                         Files.newOutputStream(
                                                 productFile, StandardOpenOption.CREATE), StandardCharsets.UTF_8
                                 ))) {

                writeTextToFile.append(formatter.formatProduct(product))
                        .append(System.lineSeparator());
                Collections.sort(listOfReviews);

                if (listOfReviews.isEmpty()) {
                    writeTextToFile.append(formatter.getText("no.reviews"))
                            .append(System.lineSeparator());
                } else {
                    writeTextToFile.append(listOfReviews.stream()
                            .parallel()
                            .map(currReview -> formatter.formatReview(currReview) + System.lineSeparator())
                            .collect(Collectors.joining())
                    );
                }
            } catch (IllegalArgumentException | UnsupportedOperationException e) {
                logger.log(Level.SEVERE, "Illegal argument exception, double check the options passed to the PrintWriter object" + e.getMessage());
            } catch (SecurityException e) {
                logger.log(Level.SEVERE, "Security issues with local File System. Please check WRITE permission for this volume at " + productFile.toString() + "  and this user/groups.\nOriginal Error Msg:\n" + e.getMessage());
            } catch (IOException e) {
                var customException = new ProductManagerException("Saving product to file was unsuccessful due to I/O error." + e.getMessage(), e);
                logger.log(Level.INFO, customException.getMessage());
                throw customException;
            }
/*
            IllegalArgumentException – if options contains an invalid combination of options
            UnsupportedOperationException – if an unsupported option is specified
            FileAlreadyExistsException – If a file of that name already exists and the CREATE_NEW option is specified (optional specific exception)
            IOException – if an I/O error occurs
            SecurityException – In the case of the default provider, and a security manager is installed, the checkWrite method is invoked to check write access to the file. The checkDelete method is invoked to check delete access if the file is opened with*/

            //   System.out.println(sb);
        } finally {
            writeLock.unlock();
        }

    }

    public void printProduct(Predicate<Product> filter, Comparator<Product> sorter, String languageTag) {

        StringBuilder sb = new StringBuilder();
        ResourceFormatter formatter = formatters.getOrDefault(languageTag, formatters.get(DEFAULT_LANGUAGE_TAG));
        try {
            readLock.lock();
            sb.append(products.keySet().stream()
                    .parallel()
                    .sorted(sorter)
                    .filter(filter)
                    .map(product -> formatter.formatProduct(product) + System.lineSeparator())
                    .collect(Collectors.joining())
            );
        } finally {
            readLock.unlock();
        }
        System.out.println(sb.toString());

    }

    /**
     * @param csvText made of comma separated values in the following format.
     */
    private Review parseReview(String csvText) {
        //TODO move to json, or trim the values[] as parse() method doesn't do any trimming and as a result passing " 3" will trow NumberFormatException.
        Review parsedReview = null;
        try {
            Object[] values = reviewFormatForCSV.parse(csvText);
            parsedReview = new Review(Ratable.convert(Integer.parseInt((String) values[0])),
                    (String) values[1]);
        } catch (ParseException | NumberFormatException e) {
            logger.log(Level.WARNING, "Error parsing review " + csvText, e.getMessage());
        }
        return parsedReview;
    }

    private Product parseProduct(String csvText) {
        Product parsedProduct = null;
        try {
            Object[] values = productFormatForCSV.parse(csvText);
            //expected format is: "F,103,Cake,3.99,0,2024-03-03"
            char productType = ((String) values[0]).charAt(0);
            switch (productType) {
                case 'F':
                    parsedProduct = new Food(
                            Integer.parseInt((String) values[1]),
                            (String) values[2],
                            BigDecimal.valueOf(Double.parseDouble((String) values[3])),
                            Ratable.convert(Integer.parseInt((String) values[4])),
                            LocalDate.parse((String) values[5]));
                    break;
                case 'D':
                    parsedProduct = new Drink(
                            Integer.parseInt((String) values[1]),
                            (String) values[2],
                            BigDecimal.valueOf(Double.parseDouble((String) values[3])),
                            Ratable.convert(Integer.parseInt((String) values[4])));
                    break;
                default:
                    //supplied an unsupported Product type.
                    throw new IllegalArgumentException("Wrong product type was supplied (" + productType + ") during object creation. " +
                            "Please, try with some of the supported product types.");
            }
        } catch (ParseException | NumberFormatException | DateTimeParseException e) {
            logger.log(Level.WARNING, "No product was created. Error parsing product \"" + csvText + "\". " + e.getMessage(), e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "No product was created. Error parsing product. Reason: " + e.getMessage(), e.getStackTrace());
        }
        return parsedProduct;
    }

    public Map<String, String> getDiscounts(String languageTag) {

        try {
            readLock.lock();
            ResourceFormatter formatter = formatters.getOrDefault(languageTag, formatters.get(DEFAULT_LANGUAGE_TAG));
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

        } finally {
            readLock.unlock();
        }

    }

    private List<Review> loadReviews(Product product) {
        List<Review> loadedReviews = null;
        Path fileWithReviews = dataFolder.resolve(MessageFormat.format(configParser.getString("reviews.data.file"), product.getId()));
        if (Files.exists(fileWithReviews)) {
            try {
                loadedReviews = Files.lines(fileWithReviews, StandardCharsets.UTF_8)
                        .map(text -> parseReview(text))
                        .filter(review -> review != null)
                        .collect(Collectors.toList());
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error loading reviews "
                        + e.getMessage());
            }
        } else {
            loadedReviews = new ArrayList<>();
        }
        return loadedReviews;
    }

    private Product loadProduct(Path pathToProductsSavedAsCSV) {
        Product product = null;
        try {
            product = parseProduct(Files.lines(dataFolder.resolve(pathToProductsSavedAsCSV), StandardCharsets.UTF_8)
                    .findFirst().orElseThrow());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error reading file with product data " + pathToProductsSavedAsCSV
                    + System.lineSeparator()
                    + e.getMessage());
        } catch (NoSuchElementException e) {
            logger.log(Level.WARNING, "The supplied file \""
                    + pathToProductsSavedAsCSV + "\" is empty. The product was not added to the platform. Please, provide a valid file."
                    + System.lineSeparator()
                    + e.getMessage());
        }
        return product;
    }

    private void loadAllData() {
        try {
            products = Files.list(dataFolder)
                    .filter(file -> file.getFileName().toString().startsWith("product"))
                    .map(file -> loadProduct(file))
                    .filter(product -> product != null)
                    .collect(Collectors.toMap(
                            (product) -> product, product -> loadReviews(product)));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading data from files."
                    + System.lineSeparator()
                    + e.getMessage());
        }
        recalculateAverageProductRatingBasedOnAllReviews();
    }


    //TODO add this method as lambda to the addAllData() prior to putting values in the map
    private void recalculateAverageProductRatingBasedOnAllReviews() {

        Map<Product, Integer> productsMapUpdated = new HashMap<>();

        var iterator = products.entrySet().iterator();
        while (iterator.hasNext()) {

            var currentEntry = iterator.next();
            List<Review> currentReviewsList = currentEntry.getValue();
            Product currentProduct = currentEntry.getKey();
            int avgRatingAsInteger = (int) Math.round(currentReviewsList.stream()
                    .mapToInt(reviews -> reviews.getRating().ordinal())
                    .average().orElse(0));
            if (currentProduct.getRating().ordinal() != avgRatingAsInteger) {
                Product updatedProductWithAccurateAvgRating = currentProduct.applyRating(avgRatingAsInteger);
                productsMapUpdated.put(updatedProductWithAccurateAvgRating, avgRatingAsInteger);
            }

        }
        for (Map.Entry<Product, Integer> productWithAccurateAverageRating : productsMapUpdated.entrySet()) {
            List<Review> listOfSubmittedReviewsForProduct = products.remove(productWithAccurateAverageRating.getKey());
            products.put(productWithAccurateAverageRating.getKey(), listOfSubmittedReviewsForProduct);
        }

    }

    private void dumpData() {
        //TODO: 1) convert the object into ByteArray[] for instance using the ByteArrayOutputStream
        //TODO: 2) Append some TAG ID that will show  type of the object and the generics it was using.
        //TODO: 2.1) Maybe adding some header with that tag.
        //TODO: 3) Wrap the byteArray into some temperChecker like hashingMethod or something like this that will be
        //TODO  appended at the ByteArray's end.
        //TODO: 4) Encrypt that byteArray via AES using messageDigest();
        //TODO: 5) Then passing this ByteArray to this dumpData method. Key shall be generated and stored by the admin calling dumpData();

        try {
            if (Files.notExists(tempFolder)) {
                Files.createDirectories(tempFolder);
            }
            Path tempFile = tempFolder.resolve(MessageFormat.format(configParser.getString("temp.file"), Instant.now().toString().replaceAll(":", "")));
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(tempFile, StandardOpenOption.CREATE))) {
                out.writeObject(products);
                //    products = new HashMap<>();
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error dumping data." + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void restoreData() {
        try {
            Path tempFile = Files.list(tempFolder)
                    .filter(files -> files.getFileName().toString().endsWith(".tmp"))
                    .findFirst().orElseThrow();
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(tempFile, StandardOpenOption.DELETE_ON_CLOSE))) {
                products = (HashMap) in.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "System can not load data from file. Make sure the file is present and has .tmp extension. Make sure the file is not corrupted and that it was created with the same version" +
                    "if the System.");
        }
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