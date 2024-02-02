package work.thefit.pm.playGround;



import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        ProductMocker productMocker = new ProductMocker(1, 10_000);

        Path path = Path.of("d:/projectPM/data");

        List<String> productsCSVlist = productMocker.getProductDataAsCSVlist();
        for (int i = 0; i < productsCSVlist.size(); i++) {
            try {
                Files.write(path.resolve("product" + (1 + i) + ".txt"), productsCSVlist.get(i).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                System.out.println("Error creating file with file name: " + "product" + 101 + i + ".txt" + System.lineSeparator() + e.getMessage());
            }
        }

        List<String> reviewsAsCSV = productMocker.getReviewDataAsCSVlist(30_000);
        for (int i = 0; i < reviewsAsCSV.size(); i++) {
            int reviewForProductID = productMocker.getRandomInteger(1, 150);
            try {
                Path fileName = path.resolve("reviews" + reviewForProductID + ".txt");
                if (Files.exists(fileName)) {
                    Files.writeString(fileName, reviewsAsCSV.get(i).concat(System.lineSeparator()), StandardOpenOption.APPEND);
                } else {
                    Files.writeString(fileName, reviewsAsCSV.get(i).concat(System.lineSeparator()), StandardOpenOption.CREATE);
                }
            } catch (IOException e) {
                System.out.println("Can't create or update file with name: " + "review" + reviewForProductID + ".txt"
                        + System.lineSeparator() + e.getMessage());
            }
        }


    }
}
