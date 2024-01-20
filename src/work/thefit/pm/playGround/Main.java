package work.thefit.pm.playGround;

import work.thefit.pm.data.Food;
import work.thefit.pm.data.Product;
import work.thefit.pm.data.ProductManager;
import work.thefit.pm.data.Rating;

import java.math.BigDecimal;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        ProductManager pm = new ProductManager(Locale.GERMAN);
        List<Product> menu = new ArrayList<>();
        menu.add(pm.createProduct(100, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED));
        menu.add(pm.createProduct(201, "Coca-Cola", BigDecimal.valueOf(2.99), Rating.NOT_RATED));
        menu.add(pm.createProduct(300, "Sprite", BigDecimal.valueOf(2.59), Rating.NOT_RATED));

        menu.removeIf(new LongProductsNamesPredicate());
        for (Product p : menu) {
            System.out.println(p.getName());
        }

        List<String> myList = Arrays.asList("Test999", "AAA", "aaa", "Lambdazzz", "z");
        myList.sort((s1, s2) -> s1.length() - s2.length());
        //Make a real list that can change its size and remove/add methods does not throw UnsupportedOperationException
        ArrayList<String> trueList = new ArrayList<>(myList);

        System.out.println("True list looks like: " + trueList);
        trueList.removeIf(s1 -> s1.length() <= 2);
        System.out.println("Here is how the trueList looks like after the removeIf() was applied " + trueList);

        String[] namesArr = {"Marry", "Jane", "Elizabeth", "Jo"};
        Arrays.sort(namesArr, new Compare());

        for (String nameElement : namesArr) {
            System.out.print(nameElement + " ");
        }
    }
}
