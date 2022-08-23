package cmpt213.assignment4.packagedeliveries.view;

import cmpt213.assignment4.packagedeliveries.controller.PackageTracker;

import cmpt213.assignment4.packagedeliveries.model.Package;


import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

/**
 * MainPackage swing used as the entry point for the program GUI
 */
public class MainPackageSwing {
    //Components of the application
    private final PackageTracker packageTracker;

    private final JFrame frame;

    private final Container contentPane;
    private final JButton allBtn;
    private final JButton overdueBtn;
    private JScrollPane scrollingPackagePanel;

    /**
     * Default constructor for MainPackageSwing.
     * This will make all the panel and components and also the packageTracker
     */
    public MainPackageSwing() {
        packageTracker = new PackageTracker();
        frame = new JFrame("Amazing Package Deliveries Tracker");
        AddPackageFormSwing addPackageFrame = new AddPackageFormSwing(frame, packageTracker);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        allBtn = new JButton("All");
        allBtn.setOpaque(false);
        allBtn.setForeground(Color.GRAY);
        allBtn.setPreferredSize(new Dimension(166, 50));

        overdueBtn = new JButton("Overdue");
        overdueBtn.setPreferredSize(new Dimension(166, 50));

        JButton upcomingBtn = new JButton("Upcoming");
        // upcoming is the only local JButton as it is in the else case in other method
        upcomingBtn.setPreferredSize(new Dimension(166, 50));

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(allBtn, BorderLayout.LINE_START);
        buttonPanel.add(overdueBtn, BorderLayout.CENTER);
        buttonPanel.add(upcomingBtn, BorderLayout.LINE_END);

        JButton[] buttons = {allBtn, overdueBtn, upcomingBtn};
        for (int i = 0; i < buttons.length; i++) {
            final int index = i;
            buttons[i].addActionListener(e -> {
                buttons[index].setForeground(Color.GRAY);
                buttons[(index + 1) % buttons.length].setForeground(Color.BLACK);
                buttons[(index + 2) % buttons.length].setForeground(Color.BLACK);
                refreshPackagePanel();
            });
        }

        refreshPackagePanel();

        JButton addPackageBtn = new JButton("Add Package");
        addPackageBtn.setPreferredSize(new Dimension(500, 50));

        addPackageBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            int result = addPackageFrame.showConfirmDialog();
            if (result == AddPackageFormSwing.SUCCESS) {
                refreshPackagePanel();
            }
        }));

        contentPane.add(buttonPanel, BorderLayout.NORTH);
        contentPane.add(addPackageBtn, BorderLayout.SOUTH);
        frame.setVisible(true);
        WindowListener windowListener = new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            /**
             * @param e the event to be processed
             */
            @Override
            public void windowClosing(WindowEvent e) {
               packageTracker.saveGson();
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        };
        frame.addWindowListener(windowListener);

    }

    /**
     * Method to refresh the package panel as it should be able to be dynamically modified
     */
    public void refreshPackagePanel() {
        if (scrollingPackagePanel != null) {
            contentPane.remove(scrollingPackagePanel);
        }
        JPanel packagePanel = new JPanel();
        packagePanel.setLayout(new BoxLayout(packagePanel, BoxLayout.Y_AXIS));
        ArrayList<Package> packageArrayList; // later will be reinitialized. It is okay as Java has GC
        if (allBtn.getForeground() == Color.GRAY) {
            packageArrayList = packageTracker.listAllPackages();
        } else if (overdueBtn.getForeground() == Color.GRAY) {
            packageArrayList = packageTracker.listOverduePackages();
        } else {
            packageArrayList = packageTracker.listUpcomingPackages();
        }

        if (packageArrayList.size() == 0) {
            JPanel panel = new JPanel();
            JTextPane textPane = new JTextPane();
            Font font = new Font("SANS_SERIF", Font.BOLD, 36);
            textPane.setFont(font);
            textPane.setText("No packages to show");

            panel.add(textPane);
            packagePanel.add(panel);
        }
        int totalHeight = 0;
        for (int i = 0; i < packageArrayList.size(); i++) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setPreferredSize(new Dimension(400, 80));

            JTextArea textArea = new JTextArea();
            textArea.append("Package #" + (i + 1) + packageArrayList.get(i).toString());
//
            textArea.setEditable(false);
            textArea.setBackground(Color.GRAY);

            JPanel horizontalPanel = new JPanel();
            horizontalPanel.setLayout(new BoxLayout(horizontalPanel, BoxLayout.LINE_AXIS));
            horizontalPanel.setBackground(Color.GRAY);

            JCheckBox deliveredCheckBox = new JCheckBox("Delivered");
            deliveredCheckBox.setSelected(packageArrayList.get(i).getDelivered());
            final int finalI = i;
            deliveredCheckBox.addActionListener(e -> {
                packageTracker.markPackageAsDelivered(packageArrayList.get(finalI));
                refreshPackagePanel();
            });

            JButton removeBtn = new JButton("Remove");
            removeBtn.addActionListener(e -> {
                packageTracker.removePackage(packageArrayList.get(finalI));
                refreshPackagePanel();
            });

            horizontalPanel.add(deliveredCheckBox);
            horizontalPanel.add(removeBtn);

            panel.add(textArea);
            panel.add(horizontalPanel);
            panel.setBackground(Color.GRAY);
            textArea.setPreferredSize(new Dimension(400, 150));
            horizontalPanel.setPreferredSize(new Dimension(400, 20));
            panel.setPreferredSize(new Dimension(400, 170));
            packagePanel.add(panel);
            packagePanel.add(Box.createRigidArea(new Dimension(400, 30)));
            totalHeight += 200;
        }
        if (totalHeight < 600) {
            packagePanel.add(Box.createRigidArea(new Dimension(400, 600 - totalHeight)));
            // to make sure panel in packagePanel do not stretch horizontally
        }
        scrollingPackagePanel = new JScrollPane(packagePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollingPackagePanel.setVisible(true);
        contentPane.add(scrollingPackagePanel, BorderLayout.CENTER);
        contentPane.setPreferredSize(new Dimension(450, 700));
        frame.pack();
    }


}

