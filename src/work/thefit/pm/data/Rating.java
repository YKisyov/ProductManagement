package work.thefit.pm.data;

public enum Rating {
    /**
     * "\u2606" -> ☆";
     * "\u2605" -> ★
     */
    NOT_RATED("☆☆☆☆☆"),
    ONE_STAR("★☆☆☆☆"),

    TWO_STAR("★★☆☆☆"),

    THREE_STAT("★★★☆☆"),

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
