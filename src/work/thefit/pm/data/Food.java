package work.thefit.pm.data;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * @author YKisyov
 * @version 0.6.2
 */
public class Food extends Product {
    private final LocalDate bestBefore;

    public Food(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        super(id, name, price, rating);
        this.bestBefore = bestBefore;
    }

    /**
     * Get the value of the best before date for the product.*
     *
     * @return the value of bestBefore as LocalDate.
     */
    public LocalDate getBestBefore() {
        return bestBefore;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + getBestBefore();
    }

    @Override
    public Product applyRating(Rating newRating) {
        return new Food(getId(), getName(), getPrice(), newRating, getBestBefore());
    }
}
