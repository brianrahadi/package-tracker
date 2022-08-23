package cmpt213.assignment4.packagedeliveries.model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A class to store information of a package including name, notes, price, weight, isisDelivered, and expected delivery
 * date.
 * @author brianrahadi
 */
public class Package implements Comparable<Package> {

    private long id; // gives warning, but mandatory. Added automatically in GSON.
    private final String name;
    private final String notes;
    private final double priceInDollar;
    private final double weightInKg;
    private final boolean delivered;
    private final LocalDateTime expectedDeliveryDateTime;

//    private final String type = "Package";

    // Note: these final variable names are not in uppercase, because it's not a public static final uppercase.
    // I made final on them to remove the warning and there is no need for setters method for them at the moment
    // If there are setters method, I will remove final on these variables.

    /** Constructor for Package
     * @param name name of the package
     * @param notes notes of the package
     * @param priceInDollar price of the package (in dollars)
     * @param weightInKg weight of the package (in kg)
     * @param delivered status of the package delivery (delivered or not)
     * @param expectedDeliveryDateTime expected package arrival date and time
     */
    public Package(String name, String notes, double priceInDollar, double weightInKg, boolean delivered,
                   LocalDateTime expectedDeliveryDateTime) {
        this.name = name;
        this.notes = notes;
        this.priceInDollar = priceInDollar;
        this.weightInKg = weightInKg;
        this.delivered = delivered;
        this.expectedDeliveryDateTime = expectedDeliveryDateTime;
    }

    /**
     * gets id of current package
     * @return id current id
     */
    public long getId() {
        return id;
    }

    /**
     * @return status whether the package has been delivered or not
     */
    public boolean getDelivered() {
        return delivered;
    }

    /**
     * @return expected delivery date and time of the package
     */
    public LocalDateTime getExpectedDeliveryDateTime() {
        return expectedDeliveryDateTime;
    }

    /**
     * @return string of the package's information with each info in one line.
     */
    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return "\nName: " + this.name
                + "\nNotes: " + this.notes
                + "\nPrice: $" + this.priceInDollar
                + "\nWeight: " + this.weightInKg + "kg"
                + "\nExpected Delivery Date: " + this.expectedDeliveryDateTime.format(dateTimeFormatter);
    }

    /**
     *
     * @param otherPackage the object to be compared.
     * @return 1 if the package's expected delivery date time is after the other's expected delivery date, -1 otherwise
     */
    @Override
    public int compareTo(Package otherPackage) {
        return this.getExpectedDeliveryDateTime().isAfter(otherPackage.getExpectedDeliveryDateTime()) ? 1 : -1;
    }

}
