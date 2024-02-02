package work.thefit.pm.playGround;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.*;

public class ProductMocker {
    private List<String> foodNames = new ArrayList<>(Arrays.asList("Cake",
            "Croissant",
            "Ice Cream",
            "Donut",
            "Sandwich",
            "Burger",
            "Pizza",
            "Pasta",
            "Salad",
            "Fries",
            "Chicken Wings",
            "Steak",
            "Fish and Chips",
            "Tacos",
            "Sushi",
            "Ramen",
            "Curry",
            "Falafel",
            "Bagel"
    ));
    private List<String> drinksNames = new ArrayList<>(Arrays.asList("Water",
            "Coffee",
            "Tea",
            "Milk",
            "Orange Juice",
            "Apple Juice",
            "Lemonade",
            "Grape Juice",
            "Pineapple Juice",
            "Tomato Juice",
            "Coca-Cola",
            "Pepsi",
            "Sprite",
            "Fanta",
            "Root Beer",
            "Iced Tea",
            "Hot Chocolate",
            "Espresso",
            "Cappuccino",
            "Latte",
            "Mocha",
            "Green Tea",
            "Black Tea",
            "Oolong Tea",
            "Chamomile Tea",
            "Mint Tea",
            "Ginger Tea",
            "Chai Tea",
            "Rooibos Tea",
            "Yerba Mate"
    ));
    private String rawReviewData =
            "1, I wasn't impressed with the product.\n" +
                    "2, The service was okay, but I've seen better.\n" +
                    "3, Average experience. Nothing to write home about.\n" +
                    "4, Good quality! I'm pretty satisfied.\n" +
                    "5, Excellent! I would definitely recommend this.\n" +
                    "1, Not my cup of tea. I wouldn't buy it again.\n" +
                    "2, It was fine, but I think there's room for improvement.\n" +
                    "3, Decent product. Met my expectations.\n" +
                    "4, Really good! Almost perfect.\n" +
                    "5, Outstanding! Exceeded all my expectations.\n" +
                    "1, I didn't like it. Needs a lot of work.\n" +
                    "2, It was okay, but not great.\n" +
                    "3, Not bad. I might buy it again.\n" +
                    "4, I liked it. Would probably buy again.\n" +
                    "5, Loved it! I'll be a repeat customer for sure.\n" +
                    "1, Disappointing. I expected better.\n" +
                    "2, It's okay, but not what I was hoping for.\n" +
                    "3, It's good, but not great.\n" +
                    "4, I'm happy with it. Good purchase.\n" +
                    "5, Fantastic! I couldn't be happier.\n" +
                    "1, Not worth the money. I regret buying it.\n" +
                    "2, It's decent, but I wouldn't buy it again.\n" +
                    "3, It's alright. I might consider buying again.\n" +
                    "4, I'm quite satisfied. Good value for money.\n" +
                    "5, Absolutely perfect! Worth every penny.\n" +
                    "1, I didn't enjoy it. I wouldn't recommend it.\n" +
                    "2, It's passable, but there are better options out there.\n" +
                    "3, It's good. I have no major complaints.\n" +
                    "4, I'm impressed. I would recommend it.\n" +
                    "5, It's amazing! I highly recommend it.\n" +
                    "1, I didn't enjoy it. I wouldn't recommend it.\n" +
                    "2, It's passable, but there are better options out there.\n" +
                    "3, It's good. I have no major complaints.\n" +
                    "4, I'm impressed. I would recommend it.\n" +
                    "5, It's amazing! I highly recommend it.\n" +
                    "1, Not worth the money. I regret buying it.\n" +
                    "2, It's decent, but I wouldn't buy it again.\n" +
                    "3, It's alright. I might consider buying again.\n" +
                    "4, I'm quite satisfied. Good value for money.\n" +
                    "5, Absolutely perfect! Worth every penny.\n" +
                    "1, Disappointing. I expected better.\n" +
                    "2, It's okay, but not what I was hoping for.\n" +
                    "3, It's good, but not great.\n" +
                    "4, I'm happy with it. Good purchase.\n" +
                    "5, Fantastic! I couldn't be happier.\n";
    private int productIdStartIndex, getProductIdEndIndex;
    private double lowestPrice = 0.99d, highestPrice = 39.89d;
    private static Random randomInstance = new Random();
    private DecimalFormat decimalFormatter;
    private LocalDate localDate;


    public ProductMocker(int productIdStartIndex, int productIdEndIndex) {
        this.productIdStartIndex = productIdStartIndex;
        this.getProductIdEndIndex = productIdEndIndex;
        decimalFormatter = new DecimalFormat("#.##");
        decimalFormatter.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        localDate = LocalDate.now();
    }

    private List<String> reviewsList = Arrays.stream(rawReviewData.split("\\n"))
            .map(line -> line.substring(0, line.indexOf(',') + 1).concat(line.substring(line.indexOf(' ') + 1)))
            .toList();

    public List<String> getReviewDataAsCSVlist(int numberOfReviews) {
        List<String> resultingList = new ArrayList<>(numberOfReviews);
        for (int i = 0; i < numberOfReviews; i++) {
            int randomizedReviewIndex = getRandomInteger(0, reviewsList.size() - 1);
            resultingList.add(reviewsList.get(randomizedReviewIndex));
        }
        return resultingList;
    }

    public List<String> getProductDataAsCSVlist() {
        List<String> listOfProductAsCSV = new ArrayList<>();
        for (int i = productIdStartIndex; i <= getProductIdEndIndex; i++) {
            listOfProductAsCSV.add(assembleMockProduct(i));
        }
        return listOfProductAsCSV;
    }

    private String assembleMockProduct(int productID) {
        //HAve to build something like this:
        // "D,101,Tea,1.99,0,2024-01-24"

        StringBuilder productAsCSV = new StringBuilder();
        String productType = getRandomizedProductType();
        productAsCSV.append(getRandomizedProductType());
        productAsCSV.append(",");
        productAsCSV.append(productID);
        productAsCSV.append(",");
        productAsCSV.append(getRandomizedProductName(productType));
        productAsCSV.append(",");
        productAsCSV.append(decimalFormatter.format(getRandomDouble(lowestPrice, highestPrice)));
        productAsCSV.append(",");
        productAsCSV.append(0);
        productAsCSV.append(",");
        if (productType.equals("D")) {
            productAsCSV.append(convertDate(localDate));
        } else {
            int addSomeRandomNumberOfDays = getRandomInteger(1, 90);
            productAsCSV.append(convertDate(localDate.plusDays(addSomeRandomNumberOfDays)));
        }
        //productAsCSV.append("2024-").append(getRandomInteger(1, 12)).append("-").append(getRandomInteger(1, 28));
        return productAsCSV.toString();
    }

    /**
     * Returns randomized product name based on the product type.
     * For type "F" ("Food") it will return a product name suitable for food.
     * For type "D" ("Drink") it will return a random name suitable for drinks.
     *
     * @param productType A string representing "D" or "F";
     * @return Random product name out of predefined list.
     */
    private String getRandomizedProductName(String productType) {
        switch (productType) {
            case "F":
                return foodNames.get(getRandomInteger(0, foodNames.size() - 1));
            case "D":
                return drinksNames.get(getRandomInteger(0, drinksNames.size() - 1));
            default:
                return null; // throw new IllegalArgumentException("No such product type");
        }
    }

    private String getRandomizedProductType() {
        return getRandomInteger(0, 1) == 1 ? "D" : "F";
    }


    public int getRandomInteger(int minBound, int maxBound) {
        int randomNumber;
        randomNumber = randomInstance.nextInt((maxBound - minBound) + 1) + minBound;
        return randomNumber;
    }

    public double getRandomDouble(double minBound, double maxBound) {
        return randomInstance.nextDouble(minBound, maxBound + 0.1);
    }

    private String convertDate(LocalDate localDate) {
        StringBuilder sb = new StringBuilder();
        sb.append(localDate.getYear());
        sb.append("-");
        sb.append(String.format("%02d", localDate.getMonthValue()));
        sb.append("-");
        sb.append(String.format("%02d", localDate.getDayOfMonth()));
        return sb.toString();
    }
}
