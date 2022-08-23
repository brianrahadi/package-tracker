package cmpt213.assignment4.packagedeliveries.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A class to store information of a perishable. It has all the perishable package has and also an additional
 * expiry date time field
 * @author brianrahadi
 */
public class Perishable extends Package {

    private LocalDateTime expiryDateTime;

    /**
     * Constructor for perishable
     * @param name name of the perishable
     * @param notes notes of the perishable
     * @param priceInDollar price of the perishable (in dollars)
     * @param weightInKg weight of the perishable (in kg)
     * @param delivered status of the perishable delivery (delivered or not)
     * @param expectedDeliveryDateTime expected perishable arrival date and time
     */
    public Perishable(String name, String notes, double priceInDollar, double weightInKg, boolean delivered,
                      LocalDateTime expectedDeliveryDateTime) {
        super(name, notes, priceInDollar, weightInKg, delivered, expectedDeliveryDateTime);
    }

    /**
     * sets the electronic expiry date and time
     * @param expiryDateTime electronic expiry date and time
     */
    public void setExpiryDateTime(LocalDateTime expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
    }


    /**
     * gives warning, but needed for gson
     * @return expiry date time
     */
    public LocalDateTime getExpiryDateTime() {
        return expiryDateTime;
    }

    /**
     * A toString method to display all the information of the perishable that overrides its parent method
     * @return string of the perishable's information with each info in one line.
     */
    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return "\nPackage Type: Perishable" + super.toString()
                + "\nExpiry Date: " + this.expiryDateTime.format(dateTimeFormatter);
    }
}