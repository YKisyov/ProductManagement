package work.thefit.pm.app;

import work.thefit.pm.data.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

/**
 * {@code Shop} class is used to represent the Product Management System app.
 *
 * @author YKisyov
 * @version 0.7.1
 */
public class Shop {
    public static void main(String[] args) {

        Locale locBG = new Locale("bg", "BG");
        Locale locEN = new Locale("en", "GB");
        ProductManager pm = new ProductManager(locBG);
        ProductManager pm2 = new ProductManager(locEN);

        Product p1 = pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
        pm.printProductReport();
        p1 = pm.reviewProduct(p1, Rating.FOUR_STAR, "Nice hot cup of tea");
        p1 = pm.reviewProduct(p1, Rating.TWO_STAR, "Rather weak tea");
        p1 = pm.reviewProduct(p1, Rating.FOUR_STAR, "Fine tea");
        p1 = pm.reviewProduct(p1, Rating.FOUR_STAR, "Good tea");
        p1 = pm.reviewProduct(p1, Rating.FIVE_STAR, "Perfect tea");
        p1 = pm.reviewProduct(p1, Rating.THREE_STAR, "Just add some lemon");
        pm.printProductReport();
        pm2.reviewProduct(p1, Rating.FIVE_STAR, "Awesome tea");
        pm2.printProductReport();

    }
}
