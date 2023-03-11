package work.thefit.pm.data;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

/**
 * {@code Product} class represent a very basic concept of products and their properties and behaviors
 * it also offers {@code DISCOUNT_RATE BigDecimal} static class member that shows 10% product discount;
 */
public class Product {
    private final static BigDecimal DISCOUNT_RARE = BigDecimal.valueOf(0.1);
    private int id;
    private String name;
    private BigDecimal price;
    private Rating rating;

    public Rating getRating()  {
        return rating;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    /**
     * {@code getDiscountRate} is used to calculate the amount that has to be removed from the original Product price in
     * order to get a discount af a rate defined in DISCOUNT_RATE.
     *
     * @return BigDecimal showing the rounded discount value based on DISCOUNT_RATE.
     */
    public BigDecimal getDiscountRate() {
        return getPrice().multiply(DISCOUNT_RARE).setScale(2, HALF_UP);
    }
}
