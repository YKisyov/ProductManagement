package work.thefit.pm.data;

public enum Rating {
    /**
     * "\u2606" stands for an empty (white) star -> ☆". <BR>
     * "\u2605" stand for a filled (colorful) star -> ★
     */
    NOT_RATED("☆☆☆☆☆"),
    ONE_STAR("★☆☆☆☆"),

    TWO_STAR("★★☆☆☆"),

    THREE_STAR("★★★☆☆"),

    FOUR_STAR("★★★★☆"),

    FIVE_STAR("★★★★★");

    private String stars;

    private Rating(String starRating) {
        stars = starRating;
    }

    public String getStars() {
        return stars;
    }
}
