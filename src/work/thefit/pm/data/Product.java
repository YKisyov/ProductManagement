package work.thefit.pm.data;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

public class Product {
    private final static BigDecimal DISCOUNT_RARE = BigDecimal.valueOf(0.1);
    private int id;
    private String name;
    private BigDecimal price;

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

    public BigDecimal getDiscountRare() {
        return getPrice().multiply(DISCOUNT_RARE).setScale(2, HALF_UP);
    }
}
