package work.thefit.pm.playGround;

import work.thefit.pm.data.Product;

import java.util.function.Predicate;

public class LongProductsNamesPredicate  implements Predicate<Product> {
    /**
     * @param product the input argument
     * @return true if Product's name length is grater than 3;
     */
    @Override
    public boolean test(Product product) {
        return product.getName().length() > 3;
    }
}
