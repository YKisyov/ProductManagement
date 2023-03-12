package work.thefit.pm.app;

import work.thefit.pm.data.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * {@code Shop} class is used to represent the Product Management System app.
 *
 * @author YKisyov
 * @version 0.6.2
 */
public class Shop {
    public static void main(String[] args) {
        Product p1 = new Drink(101, "Tea", BigDecimal.valueOf(1.99));
        Product p2 = new Drink(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
        Product p3 = new Food(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
        //Product p4 = new Product();
        Product p5 = p3.applyRating(Rating.THREE_STAT);
        Product p6 = new Drink(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
        Product p7 = new Food(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR,
                    LocalDate.now());


        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        //System.out.println(p4);
        System.out.println(p5);
        System.out.println(p6);
        System.out.println(p7);

        System.out.println("This is how the getClass() of p6 looks like as a string/text: " + p6.getClass());
        System.out.println("This is how the getClass() of p7 looks like as a string/text: " + p7.getClass());

        System.out.println(p6.equals(p2));
    }
}
