package work.thefit.pm.data;

/**
 * @param <T> used to extend the use of Ratable, so later on things like Shops and cashes and what not could be rated.
 */

@FunctionalInterface
public interface Ratable<T> {
    public static final Rating DEFAULT_RATING = Rating.NOT_RATED;

    T applyRating(Rating rating);

    public default Rating getRating() {
        return DEFAULT_RATING;
    }

    default T applyRating(int stars) {
        return applyRating(convert(stars));
    }

    public static Rating convert(int stars) {
        return stars >= 0 && stars <= 5 ? Rating.values()[stars] : DEFAULT_RATING;
    }

}
