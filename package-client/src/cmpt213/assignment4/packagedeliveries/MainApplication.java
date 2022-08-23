package cmpt213.assignment4.packagedeliveries;

import cmpt213.assignment4.packagedeliveries.view.MainPackageSwing;

import javax.swing.*;


/**
 * Main class that utilizes MainMenu class and PackageInfo class for Package Delivery Tracker program.
 * @author brianrahadi
 */
public class MainApplication {

    /**
     * main running method
     * @param args string of args. Not really used for this program.
     */
    public static void main(String[] args) {
            SwingUtilities.invokeLater(MainPackageSwing::new);

    }

}
