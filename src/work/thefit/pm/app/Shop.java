package work.thefit.pm.app;

import work.thefit.pm.data.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;

/**
 * {@code Shop} class is used to represent the Product Management System app.
 *
 * @author YKisyov
 * @version 0.7.1
 */
public class Shop {
    public static void main(String[] args) {

        ProductManager pm = new ProductManager("bg-BG");

        pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);

        pm.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea");
        pm.reviewProduct(101, Rating.TWO_STAR, "Nice hot cup of tea");
        pm.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea");
        pm.reviewProduct(101, Rating.TWO_STAR, "Rather weak tea");
        pm.reviewProduct(101, Rating.FOUR_STAR, "Fine tea");
        pm.reviewProduct(101, Rating.FOUR_STAR, "Good tea");
        pm.reviewProduct(101, Rating.FIVE_STAR, "Perfect tea");
        pm.reviewProduct(101, Rating.THREE_STAR, "Just add some lemon");

        pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
        pm.reviewProduct(102, Rating.THREE_STAR, "Coffee was OK");
        pm.reviewProduct(102, Rating.ONE_STAR, "Where is the milk?!");
        pm.reviewProduct(102, Rating.FIVE_STAR, "It's perfect with 10 spoons of sugar");
        pm.reviewProduct(102, Rating.FIVE_STAR, "It's perfect with 10 spoons of sugar");

        pm.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.NOT_RATED, LocalDate.now().plusDays(2));
        pm.reviewProduct(103, Rating.FIVE_STAR, "Very nice cake");
        pm.reviewProduct(103, Rating.FOUR_STAR, "It is good, but I've expected more chocolate");
        pm.reviewProduct(103, Rating.FIVE_STAR, "Tis cake is perfect");

        pm.createProduct(104, "Cookie", BigDecimal.valueOf(2.5), Rating.NOT_RATED, LocalDate.now());
        pm.reviewProduct(104, Rating.THREE_STAR, "Just another cookie");
        pm.reviewProduct(104, Rating.THREE_STAR, "OK");
        pm.reviewProduct(104, Rating.THREE_STAR, "OK");

        pm.createProduct(105, "Hot Chocolate", BigDecimal.valueOf(2.5), Rating.NOT_RATED);
        pm.reviewProduct(105, Rating.FOUR_STAR, "Tasty!");
        pm.reviewProduct(105, Rating.FOUR_STAR, "Not bad at all");

        pm.createProduct(106, "Chocolate", BigDecimal.valueOf(2.5), Rating.NOT_RATED, LocalDate.now().plusDays(3));
        pm.reviewProduct(106, Rating.TWO_STAR, "Two sweet");
        pm.reviewProduct(106, Rating.THREE_STAR, "Better than cookie");
        pm.reviewProduct(106, Rating.TWO_STAR, "Too bitter");
        pm.reviewProduct(106, Rating.ONE_STAR, "I don't get it!");

        pm.createProduct(107, "Cake", BigDecimal.valueOf(0.5), Rating.NOT_RATED, LocalDate.now().plusDays(3));
        pm.reviewProduct(107, Rating.FIVE_STAR, "Very tasty for the price!");
        pm.reviewProduct(107, Rating.FIVE_STAR, "Very good");
        pm.reviewProduct(107, Rating.FIVE_STAR, "Too bad that I purchased the whole box");

        //        String[] localeArr = {"en-GB", "bg-BG", "ja-JP"};
        //        for (int i = 0; i < localeArr.length; i++) {
        //            pm.changeLocale(localeArr[i]);
        //            System.out.println("-=:: Locale is now Japanese ::=-");
        //            for (int j = 1; j <= 6; j++) {
        //                pm.printProductReport(100 + j);
        //            }
        //        }

        //Some lambda comparators
        Comparator<Product> sortById = (p1, p2) -> p1.getId() - p2.getId();
        Comparator<Product> sortByName = (p1, p2) -> p1.getName().compareTo(p2.getName());
        Comparator<Product> sortByPrice = (p1, p2) -> (p1.getPrice().subtract(p2.getPrice())).intValue();
        Comparator<Product> sortByRating = (p1, p2) -> p1.getPrice().compareTo(p2.getPrice());
        Comparator<Product> sortByBestBefore = (p1, p2) -> p1.getBestBefore().compareTo(p2.getBestBefore());

        pm.printProduct(sortByName.thenComparing(sortByPrice.reversed()));

    }

}
