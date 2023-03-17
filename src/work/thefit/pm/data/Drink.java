package work.thefit.pm.data;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * @author YKisyov
 * @version 0.6.2(3)
 */
public class Drink extends Product {
    Drink(int id, String name, BigDecimal price, Rating rating) {
        super(id, name, price, rating);
    }

    /**
     * This method calls the getDiscount of the Product class. Only if the method is executed
     * during a happy hour. The so-called Happy Hour start at: 16:30h and ends at 17:30h.
     * Please note, that Happy Hour is based on local time!
     *
     * @return a ZERO discount if time of purchase is not a happy hour or the value of the
     * discount as defined in {@link work.thefit.pm.data.Product.getDiscount()} if purchase of the Drink was made
     * during the Happy Hour.
     */
    @Override
    public BigDecimal getDiscount() {
        final LocalTime CURRENT_TIME = LocalTime.now();
        final LocalTime HAPPY_HOUR_STARTS_AT = LocalTime.of(16, 30);
        final LocalTime HAPPY_HOUR_ENDS_AT = LocalTime.of(17, 30);
        final boolean isHappyHour = CURRENT_TIME.isAfter(HAPPY_HOUR_STARTS_AT)
                && CURRENT_TIME.isBefore(HAPPY_HOUR_ENDS_AT);

        return isHappyHour ? super.getDiscount() : BigDecimal.ZERO;
    }

    @Override
    public Product applyRating(Rating newRating) {
        return new Drink(getId(), getName(), getPrice(), newRating);
    }

}
