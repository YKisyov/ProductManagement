package work.thefit.pm.app;

import work.thefit.pm.data.Drink;
import work.thefit.pm.data.Food;
import work.thefit.pm.data.Product;
import work.thefit.pm.data.Rating;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * {@code Shop} class is used to represent the Product Management System app.
 *
 * @author YKisyov
 * @version 0.6.1
 */
public class Shop {
    public static void main(String[] args) {
        Product p1 = new Product(101, "Tea", BigDecimal.valueOf(1.99));
        Product p2 = new Drink(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
        Product p3 = new Food(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
        Product p4 = new Product();
        Product p5 = p3.applyRating(Rating.THREE_STAT);

        System.out.println(p1.getId() + " " + p1.getName() + " " + p1.getPrice() + " " + p1.getDiscountRate()
                + " and has raring of: " + p1.getRating().getStars());
        System.out.println(p2.getId() + " " + p2.getName() + " " + p2.getPrice() + " " + p2.getDiscountRate()
                + " and has raring of: " + p2.getRating().getStars());
        System.out.println(p3.getId() + " " + p3.getName() + " " + p3.getPrice() + " " + p3.getDiscountRate()
                + " and has raring of: " + p3.getRating().getStars());
        System.out.println(p4.getId() + " " + p4.getName() + " " + p4.getPrice() + " " + p4.getDiscountRate()
                + " and has raring of: " + p4.getRating().getStars());
        System.out.println(p5.getId() + " " + p5.getName() + " " + p5.getPrice() + " " + p5.getDiscountRate()
                + " and has raring of: " + p5.getRating().getStars());

    }
}
