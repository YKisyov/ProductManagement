package work.thefit.pm.app;

import work.thefit.pm.data.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
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

        pmEN.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
        pmEN.printProductReport(101);

        pmEN.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea");
        pmEN.reviewProduct(101, Rating.TWO_STAR, "Nice hot cup of tea");
        pmEN.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea");
        pmEN.reviewProduct(101, Rating.TWO_STAR, "Rather weak tea");
        pmEN.reviewProduct(101, Rating.FOUR_STAR, "Fine tea");
        pmEN.reviewProduct(101, Rating.FOUR_STAR, "Good tea");
        pmEN.reviewProduct(101, Rating.FIVE_STAR, "Perfect tea");
        pmEN.reviewProduct(101, Rating.THREE_STAR, "Just add some lemon");
        pmEN.printProductReport(101);

        pmEN.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
        pmEN.reviewProduct(102, Rating.THREE_STAR, "Coffee was OK");
        pmEN.reviewProduct(102, Rating.ONE_STAR, "Where is the milk?!");
        pmEN.reviewProduct(102, Rating.FIVE_STAR, "It's perfect with 10 spoons of sugar");
        pmEN.reviewProduct(102, Rating.FIVE_STAR, "It's perfect with 10 spoons of sugar");
        pmEN.printProductReport(102);

        pmEN.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.NOT_RATED, LocalDate.now().plusDays(2));
        pmEN.reviewProduct(103, Rating.FIVE_STAR, "Very nice cake");
        pmEN.reviewProduct(103, Rating.FOUR_STAR, "It is good, but I've expected more chocolate");
        pmEN.reviewProduct(103, Rating.FIVE_STAR, "Tis cake is perfect");
        pmEN.printProductReport(103);

        pmEN.createProduct(104, "Cookie", BigDecimal.valueOf(2.5), Rating.NOT_RATED, LocalDate.now());
        pmEN.reviewProduct(104, Rating.THREE_STAR, "Just another cookie");
        pmEN.reviewProduct(104, Rating.THREE_STAR, "OK");
        pmEN.reviewProduct(104, Rating.THREE_STAR, "OK");
        pmEN.printProductReport(104);

        pmEN.createProduct(105, "Hot Chocolate", BigDecimal.valueOf(2.5), Rating.NOT_RATED);
        pmEN.reviewProduct(105, Rating.FOUR_STAR, "Tasty!");
        pmEN.reviewProduct(105, Rating.FOUR_STAR, "Not bad at all");
        pmEN.printProductReport(105);

        pmEN.createProduct(106, "Chocolate", BigDecimal.valueOf(2.5), Rating.NOT_RATED, LocalDate.now().plusDays(3));
        pmEN.reviewProduct(106, Rating.TWO_STAR, "Two sweet");
        pmEN.reviewProduct(106, Rating.THREE_STAR, "Better than cookie");
        pmEN.reviewProduct(106, Rating.TWO_STAR, "Too bitter");
        pmEN.reviewProduct(106, Rating.ONE_STAR, "I don't get it!");
        pmEN.printProductReport(106);

        String[] textArr = {"Bla, Bla"};
        Arrays.asList(textArr);

        /*Product p1 = pmBG.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
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
        pmJP.printProductReport(p3);*/
    }


}
