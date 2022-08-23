package cmpt213.assignment4.packagedeliveries.model;


import java.time.LocalDateTime;

/**
 * PackageFactory. Class used to instantiate all the subclasses of package.
 */
public class PackageFactory {

    /**
     * getInstance method which instantiate subclasses of package based on the type of the string. Incorrect type will
     * return null
     * @param name name of the book
     * @param notes notes of the book
     * @param priceInDollar price of the book (in dollars)
     * @param weightInKg weight of the book (in kg)
     * @param isDelivered status of the book delivery (delivered or not)
     * @param expectedDeliveryDateTime expected book arrival date and time
     */
    public static Package getInstance(String name, String notes, double priceInDollar, double weightInKg, boolean isDelivered,
                                      LocalDateTime expectedDeliveryDateTime, String type) {
        if (type == null) return null;

        if (type.equalsIgnoreCase("BOOK")) {
            return new Book(name, notes, priceInDollar, weightInKg, isDelivered, expectedDeliveryDateTime);
        } else if (type.equalsIgnoreCase("PERISHABLE")) {
            return new Perishable(name, notes, priceInDollar, weightInKg, isDelivered, expectedDeliveryDateTime);
        } else if (type.equalsIgnoreCase("ELECTRONIC")){
            return new Electronic(name, notes, priceInDollar, weightInKg, isDelivered, expectedDeliveryDateTime);
        }
        return null;
    }

}
