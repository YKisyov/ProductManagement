package work.thefit.pm.data;

import java.math.BigDecimal;

/**
 *
 * @author YKisyov
 * @version 0.6.1
 */
public class Drink extends Product {
    public Drink(int id, String name, BigDecimal price, Rating rating) {
        super(id, name, price, rating);
    }
}
