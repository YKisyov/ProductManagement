package work.thefit.pm.app;

import work.thefit.pm.data.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
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
        Locale locJP = new Locale("ja", "JP");
        ProductManager pmBG = new ProductManager(locBG);
        ProductManager pmEN = new ProductManager(locEN);
        ProductManager pmJP = new ProductManager(locJP);

        Product p1 = pmBG.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
        pmBG.printProductReport();
        p1 = pmBG.reviewProduct(p1, Rating.FOUR_STAR, "Nice hot cup of tea");
        p1 = pmBG.reviewProduct(p1, Rating.TWO_STAR, "Rather weak tea");
        p1 = pmBG.reviewProduct(p1, Rating.FOUR_STAR, "Fine tea");
        p1 = pmBG.reviewProduct(p1, Rating.FOUR_STAR, "Good tea");
        p1 = pmBG.reviewProduct(p1, Rating.FIVE_STAR, "Perfect tea");
        p1 = pmBG.reviewProduct(p1, Rating.THREE_STAR, "Just add some lemon");
        pmBG.printProductReport();
        pmEN.reviewProduct(p1, Rating.FIVE_STAR, "Awesome tea");
        pmEN.printProductReport();
        pmJP.reviewProduct(p1, Rating.FIVE_STAR, "Awesome tea");
        pmJP.printProductReport();
    }


}
