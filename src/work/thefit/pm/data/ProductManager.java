package work.thefit.pm.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Used for the Factory method pattern. Added in ver. 0.6.3;
 *
 * @version 0.7.2 - adds a Product and Review instance variables and inits them in the createProduct methods.
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


    private Product product;
    private final int INCREMENT_STEP = 5;
    private Review[] reviewsArr = new Review[INCREMENT_STEP];

    public ProductManager(Locale locale) {
        this.locale = locale;
        resources = ResourceBundle.getBundle("work.thefit.pm.data.resources", locale);
        dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
        moneyFormat = NumberFormat.getCurrencyInstance(locale);

    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        product = new Food(id, name, price, rating, bestBefore);
        return product;
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        product = new Drink(id, name, price, rating);
        return product;
    }

    public Product reviewProduct(Product product, Rating rating, String coments) {
        if (reviewsArr[reviewsArr.length - 1] != null) {
            reviewsArr = Arrays.copyOf(reviewsArr, reviewsArr.length + INCREMENT_STEP);
        }
        int sumOfRatingValuesInStars = 0, currentReviewNumber = 0;
        boolean isReviewed = false;

        while (currentReviewNumber < reviewsArr.length && !isReviewed) {
            if (reviewsArr[currentReviewNumber] == null) {
                reviewsArr[currentReviewNumber] = new Review(rating, coments);
                isReviewed = true;
            }
            sumOfRatingValuesInStars += reviewsArr[currentReviewNumber].getRating().ordinal();
            currentReviewNumber++;
        }
        int avgRating = Math.round(sumOfRatingValuesInStars / (float) currentReviewNumber);
        this.product = product.applyRating(avgRating);
        return this.product;
    }

    public void printProductReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(MessageFormat.format(resources.getString("product"),
                product.getName(),
                moneyFormat.format(product.getPrice()),
                product.getRating().getStars(),
                dateFormat.format(product.getBestBefore())));
        sb.append('\n');

        for (Review review : reviewsArr) {
            if (review == null) {
                break;
            }
            sb.append(MessageFormat.format(resources.getString("review"),
                    review.getRating().getStars(),
                    review.getComments()));
            sb.append('\n');
        }
        if (reviewsArr[0] == null) {
            sb.append(resources.getString("no.reviews"));
            sb.append("\n");
        }
        System.out.println(sb);
    }
}

