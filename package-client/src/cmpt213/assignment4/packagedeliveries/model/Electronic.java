package cmpt213.assignment4.packagedeliveries.model;

import java.time.LocalDateTime;

/**
 * A class to store information of an electronic. It has all the information package has and also an additional
 * environmental handling fee field
 * @author brianrahadi
 */
public class Electronic extends Package {

    private double envHandlingFeeInDollar;

    /**
     * Constructor for electronic
     * @param name name of the electronic
     * @param notes notes of the electronic
     * @param priceInDollar price of the electronic (in dollars)
     * @param weightInKg weight of the electronic (in kg)
     * @param delivered status of the electronic delivery (delivered or not)
     * @param expectedDeliveryDateTime expected electronic arrival date and time
     */
    public Electronic(String name, String notes, double priceInDollar, double weightInKg, boolean delivered,
                      LocalDateTime expectedDeliveryDateTime) {
        super(name, notes, priceInDollar, weightInKg, delivered, expectedDeliveryDateTime);
    }

    /**
     * sets the environmental handling fee
     * @param envHandlingFeeInDollar environmental handling fee in dollar
     */
    public void setEnvHandlingFee(double envHandlingFeeInDollar) {
        this.envHandlingFeeInDollar = envHandlingFeeInDollar;
    }

    /**
     * gives warning, but needed for gson
     * gets the environmental handling fee
     * @return envHandlingFeeInDollar environmental handling fee in dollar
     */
    public double getEnvHandlingFeeInDollar() {
        return envHandlingFeeInDollar;
    }

    /**
     * A toString method to display all the information of the electronic that overrides its parent method
     * @return string of the electronic item information with each info in one line.
     */
    @Override
    public String toString() {
        return "\nPackage Type: Electronic" + super.toString() +
        "\nEnvironmental Handling Fee: $" + this.envHandlingFeeInDollar;
    }
}