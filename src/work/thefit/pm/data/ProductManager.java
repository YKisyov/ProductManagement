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
 * @version 0.9.0 - Adding HashMap to keep a single instance of each Product as key and an ArrayList for storing all Reviews.
 * The reviewsArr[] is going to be deprecated as of now on and will be completely removed from the app design.
 * Removed the logic for extending the reviews[] array from the reviewProduct(), thus INCRIMENT_STEP is also removed, as
 * its services won't be needed any more. Now we will have an List<Review> and the Java Collections' API will take care for
 * all the housekeeping and serving the Review's entity.
 */

/*
TODO: A more generic (alternative) design of this application could have been used a type Ratable instead of Product
   for both instance variables and method argument, to enable application to create reviews of any other object
   that implements Ratable interface.
   */
public class ProductManager {

    private Locale locale;
    private ResourceBundle resources;
    private DateTimeFormatter dateFormat;
    private NumberFormat moneyFormat;
    private Map<Product, List<Review>> products = new HashMap<>();


    /*
     * Deprecated as of ver. 0.9.0.
     * Code to be completely removed in the near future.
     *
     * private Product product;
     * private Review[] reviewsArr = new Review[INCREMENT_STEP];
     * private final int INCREMENT_STEP = 5;
     *
     * */


    public ProductManager(Locale locale) {
        this.locale = locale;
        resources = ResourceBundle.getBundle("work.thefit.pm.data.resources", locale);
        dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
        moneyFormat = NumberFormat.getCurrencyInstance(locale);

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
        sb.append(MessageFormat.format(resources.getString("product"),
                product.getName(),
                moneyFormat.format(product.getPrice()),
                product.getRating().getStars(),
                dateFormat.format(product.getBestBefore())));
        sb.append(System.lineSeparator());

        Collections.sort(listOfReviews);
        for (Review currReview : listOfReviews) {
            sb.append(MessageFormat.format(resources.getString("review"),
                    currReview.getRating().getStars(),
                    currReview.getComments()));
            sb.append(System.lineSeparator());
        }
        if (listOfReviews.isEmpty()) {
            sb.append(resources.getString("no.reviews"));
            sb.append(System.lineSeparator());
        }

        System.out.println(sb);
    }
}

