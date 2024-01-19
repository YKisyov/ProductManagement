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
        pmBG.printProductReport(p1);
        p1 = pmBG.reviewProduct(p1, Rating.FOUR_STAR, "Nice hot cup of tea");
        p1 = pmBG.reviewProduct(p1, Rating.TWO_STAR, "Rather weak tea");
        p1 = pmBG.reviewProduct(p1, Rating.FOUR_STAR, "Fine tea");
        p1 = pmBG.reviewProduct(p1, Rating.FOUR_STAR, "Good tea");
        p1 = pmBG.reviewProduct(p1, Rating.FIVE_STAR, "Perfect tea");
        p1 = pmBG.reviewProduct(p1, Rating.THREE_STAR, "Just add some lemon");
        pmBG.printProductReport(p1);

        Product p2 = pmEN.createProduct(201, "Coca-Cola", BigDecimal.valueOf(0.99), Rating.NOT_RATED);
        pmEN.reviewProduct(p2, Rating.FIVE_STAR, "Awesome drink");
        pmEN.printProductReport(p2);

        Product p3 = pmJP.createProduct(404, "kombucha", BigDecimal.valueOf(797.195), Rating.NOT_RATED);
        pmJP.reviewProduct(p3, Rating.FIVE_STAR, "Refreshing as always! (いつものようにリフレッシュ)[“itsumo no you ni reflesh”]");
        pmJP.printProductReport(p3);
    }


}
