package work.thefit.pm.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static java.math.RoundingMode.HALF_UP;


/**
 * {@code Product} class represent a very basic concept of products and their properties and behaviors
 * it also offers {@code DISCOUNT_RATE BigDecimal} static class member that shows 10% product discount;
 * <p>
 * This class is abstract since v 0.6.1;
 *
 * @author YKisyov
 * @version 0.7.1
 */
public abstract class Product implements Ratable<Product> {
    private final static BigDecimal DISCOUNT_RARE = BigDecimal.valueOf(0.1);
    private final int id;
    private final String name;
    private final BigDecimal price;
    private final Rating rating;

    Product(int id, String name, BigDecimal price, Rating rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    Product(int id, String name, BigDecimal price) {
        this(id, name, price, Rating.NOT_RATED);
    }

    @Override
    public Rating getRating() {
        return rating;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    /**
     * {@code getDiscount} is used to calculate the amount that has to be removed from the original Product price in
     * order to get a discount af a rate defined in DISCOUNT_RATE.
     *
     * @return BigDecimal showing the rounded-up discount value based on DISCOUNT_RATE.
     */
    public BigDecimal getDiscount() {

        return getPrice().multiply(DISCOUNT_RARE).setScale(2, HALF_UP);
    }

    /**
     * Get the value of the best before date for the product.*
     *
     * @return the value of bestBefore as LocalDate.
     */
    public LocalDate getBestBefore() {
        return LocalDate.now();
    }

    @Override
    public String toString() {
        return id + ", " + name + ", " + price + ", " + getDiscount() + ", " + rating.getStars() + ", " + getBestBefore();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
