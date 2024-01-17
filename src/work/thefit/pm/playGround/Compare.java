package work.thefit.pm.playGround;

import java.util.Comparator;

public class Compare implements Comparator<String> {

    /**
     * @param s1 the first object to be compared.
     * @param s2 the second object to be compared.
     * @return
     */
    @Override
    public int compare(String s1, String s2) {
        return s1.length() - s2.length();
    }
}
