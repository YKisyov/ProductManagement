package work.thefit.pm.data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author YKisyov
 * @version 0.6.1
 */
public class Food extends Product {

    private final LocalDate bestBefore;

                    

    public Food(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        super(id, name, price, rating);
        this.bestBefore = bestBefore;
    }

    /**
     * Get the value of the best before date for the product.
     *
     * @return the value of bestBefore.
     */
    public LocalDate getBestBefore() {
        return bestBefore;
    }
}
