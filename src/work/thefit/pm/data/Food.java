package work.thefit.pm.data;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * @author YKisyov
 * @version 0.6.2.(3)
 */
public class Food extends Product {
    private final LocalDate bestBefore;

    Food(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
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

    /**
     * Apply 10% discount if this is last day this food can be consumed by customers safely;
     *
     * @return the value of 10% discount as defined by the Product class only if the
     * current Food type's expiration date is today.
     */
    @Override
    public BigDecimal getDiscount() {
        return getBestBefore().isEqual(LocalDate.now()) ? super.getDiscount() : BigDecimal.ZERO;
    }
}
