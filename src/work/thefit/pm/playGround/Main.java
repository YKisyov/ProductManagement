package work.thefit.pm.playGround;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        String[] namesArr = {"Marry", "Jane", "Elizabeth", "Jo"};
        Arrays.sort(namesArr, new Compare());

        for (String nameElement : namesArr) {
            System.out.print(nameElement + " ");
        }
        System.out.println();
        int sum = Arrays.stream(namesArr).mapToInt((s) -> s.length())
                .filter((i) -> i <= 2)
                .sum();
        System.out.println("Sum is " + sum);
        int numberOfElementsWithLengthSmalledThenTwo = (int) Arrays.stream(namesArr).mapToInt((s) -> s.length())
                .filter((i) -> i <= 2)
                .count();
        System.out.println("There are total of " + numberOfElementsWithLengthSmalledThenTwo + " element with length smaller or equal to two");

        //To Upper Case:
        namesArr = Arrays.stream(namesArr)
                .map((s) -> (s.toUpperCase()))
                .sorted(String::compareTo)
                .toArray(String[]::new);

        System.out.println(String.join(", ", namesArr));

    }
}
