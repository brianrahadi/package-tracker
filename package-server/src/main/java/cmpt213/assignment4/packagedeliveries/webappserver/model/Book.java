package cmpt213.assignment4.packagedeliveries.webappserver.model;

import java.time.LocalDateTime;

/**
 * A class to store information of a book. It has all the information package has and also an additional author field
 * @author brianrahadi
 */
public class Book extends Package {
    private String author;

    /**
     * Constructor for book
     * @param name name of the book
     * @param notes notes of the book
     * @param priceInDollar price of the book (in dollars)
     * @param weightInKg weight of the book (in kg)
     * @param delivered status of the book delivery (delivered or not)
     * @param expectedDeliveryDateTime expected book arrival date and time
     */
    public Book(String name, String notes, double priceInDollar, double weightInKg, boolean delivered,
                LocalDateTime expectedDeliveryDateTime) {
        super(name, notes, priceInDollar, weightInKg, delivered, expectedDeliveryDateTime);
    }

    /**
     * this gives warning. But I still keep it to keep the file similar with client.
     * Furthermore, even if I remove this. It will give warning as private field is 'never' used.
     * Removing the private field will be a bad idea because it is used in the Gson (again, not visibly used).
     * sets the author name
     * @param author set author name
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * A toString method to display all the information of the book that overrides its parent method
     * @return string of the book's information with each info in one line.
     */
    @Override
    public String toString() {
        return "\nPackage Type: Book" + super.toString() + "\nAuthor: " + (author.trim().isEmpty() ? "Unknown" : author);
    }


}
