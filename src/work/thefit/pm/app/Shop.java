package work.thefit.pm.app;

import work.thefit.pm.data.*;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@code Shop} class is used to represent the Product Management System app.
 *
 * @author YKisyov
 * @version 0.7.1
 */
public class Shop {
    public static <itn> void main(String[] args) {
        AtomicInteger clientCount = new AtomicInteger(0);
        ProductManager pm = ProductManager.getInstance();
        pm.printProductReport(169, "", "DanchoShop");

        Callable<String> client = () -> {
            String clientID = "Client " + clientCount.incrementAndGet();
            String threadName = Thread.currentThread().getName();
            int productID = ThreadLocalRandom.current().nextInt(101, 143);
            String languageTag = ProductManager.getSupportedLocales()
                    .stream()
                    .skip(ThreadLocalRandom.current().nextInt(
                            ProductManager.getSupportedLocales().size()))
                    .findFirst().get(); // No way to throe NoSuchElementException
            StringBuilder log = new StringBuilder();
            System.out.println("Tread " + threadName + " is running now.");
            log.append(clientID
                    + " "
                    + threadName
                    + System.lineSeparator()
                    + "\tstart of log\t"
                    + System.lineSeparator());

            log.append(pm.getDiscounts(languageTag)
                    .entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + "\t" + entry.getValue())
                    .collect(Collectors.joining(System.lineSeparator()))
            );

            Product product = pm.reviewProduct(productID, Rating.FOUR_STAR, "Yet another review.");
            log.append((product != null)
                    ? System.lineSeparator() + "Product " + productID + "reviewed"
                    : System.lineSeparator() + "Product " + productID + "not reviewed");

            pm.printProductReport(productID, languageTag, clientID);
            log.append(clientID + " generated report for " + productID + " product");

            log.append(System.lineSeparator()
                    + "-\tend of log\t-"
                    + System.lineSeparator()
            );

            return log.toString();
        };

        List<Callable<String>> clients = Stream.
                generate(() -> client)
                .limit(5)
                .collect(Collectors.toList());

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        try {
            List<Future<String>> results = executorService.invokeAll(clients);
            executorService.shutdown(); //Not thies will not halt the executor. It will only stop accepting new task.
            results.stream().forEach( result -> {
                try {
                    System.out.println(result.get());
                } catch (InterruptedException | ExecutionException e){
                    Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, e);
                }
            });
        } catch (InterruptedException e) {
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, "Error invoking clients.");
        }

    }

}