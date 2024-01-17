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

    public Product reviewProduct(Product product, Rating rating, String comments) {

        List<Review> listOfCurrentReviews = products.get(product);

        /*
         * Later on shall be re-implemented via "throws"
         * */
        if (listOfCurrentReviews == null) {
            System.out.println("ERROR! Our system can not find the product you are trying to review.\n" +
                    "As a result Your review was not stored.\n" +
                    "Please, try again later.\n");
            return null;
        }

        listOfCurrentReviews.add(new Review(rating, comments));

        int sumOfStarts = 0, avgRating = 0, reviewsCounter = listOfCurrentReviews.size();
        for (Review currReview : listOfCurrentReviews) {
            sumOfStarts += currReview.getRating().ordinal();
        }
        avgRating = Math.round(sumOfStarts / (float) reviewsCounter);
        Product updatedProduct = product.applyRating(avgRating);

        //overwrite the old product with the new one, due to the immutability of the Product line design.
        products.remove(product);
        products.put(updatedProduct, listOfCurrentReviews);

        return updatedProduct;
    }

    public void printProductReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(MessageFormat.format(resources.getString("product"),
                product.getName(),
                moneyFormat.format(product.getPrice()),
                product.getRating().getStars(),
                dateFormat.format(product.getBestBefore())));
        sb.append('\n');

        if (reviewsArr[0] == null) {
            sb.append(resources.getString("no.reviews"));
            sb.append("\n");
        } else {
            for (Review review : reviewsArr) {
                if (review == null) {
                    break;
                }
                sb.append(MessageFormat.format(resources.getString("review"),
                        review.getRating().getStars(),
                        review.getComments()));
                sb.append('\n');
            }
        }

        System.out.println(sb);
    }
}

