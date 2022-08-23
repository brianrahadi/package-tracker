package cmpt213.assignment4.packagedeliveries.view;

import cmpt213.assignment4.packagedeliveries.controller.PackageTracker;
import cmpt213.assignment4.packagedeliveries.model.Book;
import cmpt213.assignment4.packagedeliveries.model.Electronic;
import cmpt213.assignment4.packagedeliveries.model.PackageFactory;
import cmpt213.assignment4.packagedeliveries.model.Perishable;
import com.github.lgooddatepicker.components.DateTimePicker;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Objects;


/**
 * AddPackageFormSwing. The dialog swing frame used to add package.
 */
public class AddPackageFormSwing extends JDialog{

    PackageTracker packageTracker;
    JPanel contentAddPane;

    public static int SUCCESS = 1;

    public static int CANCEL = 0;

    private int result = -1;

    private JComboBox<String> typeAnswer;
    private JTextField nameAnswer;
    private JTextField notesAnswer;

    private JTextField priceAnswer;

    private JTextField weightAnswer;
    private DateTimePicker deliveryDateAnswer;
    private JLabel specificPackageLabel;
    private JTextField specialAuthorAnswer;
    private JTextField specialEnvHandlingFeeAnswer;
    private DateTimePicker specialExpiryDateTimeAnswer;

    /**
     * Constructor for AddPackageFormSwing
     * @param parent the parent frame that calls this
     * @param packageTracker packageTracker to later be added with the newly created packages
     */
    public AddPackageFormSwing(Frame parent, PackageTracker packageTracker) {
        super(parent, true);
        this.packageTracker = packageTracker; // shallow copy since we want to reference the same package tracker
        setTitle("Add Package");
        addToContentPane();
    }

    /**
     * Validate all the inputted values
     * @return errorMessage. All error found will be returned. Empty string means success and no error.
     */
    private String validateAnswers() {
        String errorMessage = "";
        String type = Objects.requireNonNull(typeAnswer.getSelectedItem()).toString();
        String name = nameAnswer.getText();
        String price = priceAnswer.getText();
        String weight = weightAnswer.getText();
        LocalDateTime deliveryDateTime = deliveryDateAnswer.getDateTimeStrict();

        // validate name
        if (name.trim().isEmpty()) {
            errorMessage += "- Name cannot be empty string or only has spaces\n";
        }

        if (price.trim().isEmpty()) {
            errorMessage += "- Price cannot be empty\n";
        } else  {
            try {
                if (Double.parseDouble(price) < 0) {
                    // passed
                    errorMessage += "- Price cannot be negative\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "- Price must be a number\n";
            }
        }

        if (weight.trim().isEmpty()) {
            errorMessage += "- Weight cannot be empty\n";
        } else  {
            try {
                if (Double.parseDouble(weight) < 0) {
                    // passed
                    errorMessage += "- Weight cannot be negative\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "- Weight must be a number\n";
            }
        }

        if (deliveryDateTime == null) {
            errorMessage += "- Delivery date and time must be filled\n";
        }

        if (type.equalsIgnoreCase("PERISHABLE")) {
            if (specialExpiryDateTimeAnswer.getDateTimeStrict() == null) {
                errorMessage += "- Expiry date and time must be filled\n";
            }
        } else if (type.equalsIgnoreCase("ELECTRONIC")) {
            String fee = specialEnvHandlingFeeAnswer.getText();
            if (fee.trim().isEmpty()) {
                errorMessage += "- Environmental Handling Fee must not be empty\n";
            } else  {
                try {
                    if (Double.parseDouble(fee) < 0) {
                        // passed
                        errorMessage += "- Environmental Handling Fee cannot be negative\n";
                    }
                } catch (NumberFormatException e) {
                    errorMessage += "- Environmental Handling Fee must be a number\n";
                }
            }
        }
        if (!errorMessage.isEmpty()) {
            return "Error: \n" + errorMessage;
        }
        return errorMessage;
    }

    /**
     * Method to add the validated package into packageTracker
     */
    private void addToPackageTracker() {
        String type = Objects.requireNonNull(typeAnswer.getSelectedItem()).toString();
        String name = nameAnswer.getText();
        String notes = notesAnswer.getText();
        double price = Double.parseDouble(priceAnswer.getText());
        double weight = Double.parseDouble(weightAnswer.getText());
        LocalDateTime deliveryDateTime = deliveryDateAnswer.getDateTimeStrict();
        boolean isDelivered = false;
        if (type.equalsIgnoreCase("BOOK")) {
            Book book = (Book) PackageFactory.getInstance(name, notes, price, weight, isDelivered, deliveryDateTime, type);
            book.setAuthor(specialAuthorAnswer.getText());
            packageTracker.addBook(book);
        } else if (type.equalsIgnoreCase("PERISHABLE")) {
            Perishable perishable = (Perishable) PackageFactory.getInstance(name, notes, price, weight, isDelivered, deliveryDateTime, type);
            perishable.setExpiryDateTime(specialExpiryDateTimeAnswer.getDateTimeStrict());
            packageTracker.addPerishable(perishable);
        } else {
            // can instantly be in else case as it has been validated
            Electronic electronic = (Electronic) PackageFactory.getInstance(name, notes, price, weight, isDelivered, deliveryDateTime, type);
            electronic.setEnvHandlingFee(Double.parseDouble(specialEnvHandlingFeeAnswer.getText()));
            packageTracker.addElectronic(electronic);
        }
    }

    /**
     * Allow the parent frame to not be clicked while still the add package frame is still visible
     * @return result whether success or cancel button is clicked. 1 is SUCCESS. 0 is CANCEL.
     */
    public int showConfirmDialog() {
        contentAddPane.removeAll();
        addToContentPane();
        setVisible(true);
        return result;
    }

    /**
     * Helper method to add all the components and panels into the form.
     */
    private void addToContentPane() {
        contentAddPane = new JPanel();
        contentAddPane.setLayout(new BoxLayout(contentAddPane, BoxLayout.Y_AXIS));

        JPanel addPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipady = 20;

        gbc.gridx = 1;
        gbc.gridy = 1;
        JLabel typeLabel = new JLabel("Type: ");
        addPanel.add(typeLabel, gbc);

        gbc.gridx = 2;
        String[] packageTypes = { "Book", "Perishable", "Electronic" };
        typeAnswer = new JComboBox<>(packageTypes);
        typeAnswer.addActionListener(e -> {

            String selectedItem = Objects.requireNonNull(typeAnswer.getSelectedItem()).toString();
            String specificLabel;
            specialAuthorAnswer.setVisible(false);
            specialEnvHandlingFeeAnswer.setVisible(false);
            specialExpiryDateTimeAnswer.setVisible(false);
            if (selectedItem.equalsIgnoreCase("BOOK")) {
                specificLabel = "Author: ";
                specialAuthorAnswer.setVisible(true);
            } else if (selectedItem.equalsIgnoreCase("PERISHABLE")) {
                specificLabel = "Expiry Date: ";
                specialExpiryDateTimeAnswer.setVisible(true);
            } else {
                // must be ELECTRONIC
                specificLabel = "Env Handling Fee: ";
                specialEnvHandlingFeeAnswer.setVisible(true);
            }
            specificPackageLabel.setText(specificLabel);
        });
        addPanel.add(typeAnswer, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JLabel nameLabel = new JLabel("Name: ");
        addPanel.add(nameLabel, gbc);

        gbc.gridx = 2;
        nameAnswer = new JTextField();
        nameAnswer.setPreferredSize(new Dimension(200, 20));
        addPanel.add(nameAnswer, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JLabel notesLabel = new JLabel("Notes: ");
        addPanel.add(notesLabel, gbc);

        gbc.gridx = 2;
        notesAnswer = new JTextField();
        notesAnswer.setPreferredSize(new Dimension(200, 20));
        addPanel.add(notesAnswer, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        JLabel priceLabel = new JLabel("Price (in $): ");
        addPanel.add(priceLabel, gbc);

        gbc.gridx = 2;
        priceAnswer = new JTextField();
        priceAnswer.setPreferredSize(new Dimension(200, 20));
        addPanel.add(priceAnswer, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        JLabel weightLabel = new JLabel("Weight (in kg): ");
        addPanel.add(weightLabel, gbc);

        gbc.gridx = 2;
        weightAnswer = new JTextField();
        weightAnswer.setPreferredSize(new Dimension(200, 20));
        addPanel.add(weightAnswer, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        JLabel deliveryDateLabel = new JLabel("Expected delivery date: ");
        addPanel.add(deliveryDateLabel, gbc);

        gbc.gridx = 2;
        deliveryDateAnswer = new DateTimePicker();
        addPanel.add(deliveryDateAnswer, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        specificPackageLabel = new JLabel("Author");
        addPanel.add(specificPackageLabel, gbc);

        gbc.gridx = 2;
        specialAuthorAnswer= new JTextField();
        specialAuthorAnswer.setPreferredSize((new Dimension(200, 20)));
        addPanel.add(specialAuthorAnswer, gbc);

        specialEnvHandlingFeeAnswer = new JTextField();
        specialEnvHandlingFeeAnswer.setPreferredSize((new Dimension(200, 20)));
        specialEnvHandlingFeeAnswer.setVisible(false);
        addPanel.add(specialEnvHandlingFeeAnswer, gbc);

        specialExpiryDateTimeAnswer = new DateTimePicker();
        specialExpiryDateTimeAnswer.setVisible(false);
        addPanel.add(specialExpiryDateTimeAnswer, gbc);

        JPanel horizontalPanel = new JPanel();
        horizontalPanel.setLayout(new BoxLayout(horizontalPanel, BoxLayout.LINE_AXIS));

        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> {
            String errorMessage = validateAnswers();

            if (!errorMessage.isEmpty()) {
                JOptionPane.showMessageDialog(contentAddPane, errorMessage);
            } else {
                addToPackageTracker();
                result = SUCCESS;
                setVisible(false);
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            result = CANCEL;
            setVisible(false);
        });
        horizontalPanel.add(createButton);
        horizontalPanel.add(cancelButton);


        contentAddPane.add(addPanel);
        contentAddPane.add(horizontalPanel);
        setContentPane(contentAddPane);

        setSize(600, 500);
        setVisible(false);
    }

}
