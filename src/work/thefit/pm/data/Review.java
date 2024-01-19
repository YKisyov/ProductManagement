package work.thefit.pm.data;

/**
 * This class will hold the review data like Rating and comments.
 * This class is immutable, so no setter methods!
 *
 * @version 0.7.2
 */
public class Review implements Comparable<Review> {
    private Rating rating;
    private String comments;

    public Review(Rating rating, String comments) {
        this.rating = rating;
        this.comments = comments;
    }

    public Rating getRating() {
        return rating;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "Review{" + "rating=" + ", comments=" + comments + "}";
    }

    /**
     * Compares reviews in descending order of stars. (Most stars are listed first)
     */
    @Override
    public int compareTo(Review review) {
        // Implemented for descending order.
        return review.getRating().ordinal() - this.getRating().ordinal();
    }
}
