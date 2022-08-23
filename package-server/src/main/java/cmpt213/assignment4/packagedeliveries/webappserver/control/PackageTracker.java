package cmpt213.assignment4.packagedeliveries.webappserver.control;

import cmpt213.assignment4.packagedeliveries.webappserver.gson.extras.RuntimeTypeAdapterFactory;
import cmpt213.assignment4.packagedeliveries.webappserver.model.Book;
import cmpt213.assignment4.packagedeliveries.webappserver.model.Electronic;
import cmpt213.assignment4.packagedeliveries.webappserver.model.Package;
import cmpt213.assignment4.packagedeliveries.webappserver.model.Perishable;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * PackageTracker used to keep track of the packages in array list and return the correct packages requested by the view
 */
public class PackageTracker {
    private ArrayList<Package> packages;

    private final AtomicLong id;

    private File file;

    private Gson gson;


    /**
     * Default constructor for package tracker
     * Generate the packages and fill it with object from list.json if needed
     */
    public PackageTracker() {
        packages = new ArrayList<>();
        id = new AtomicLong();
        loadGson();
    }

    /**
     * Main method that handles the logic of listing all packages
     * @return allPackages in JSON
     */
    public String listAllPackages() {
        String allPackagesJsonArr = gson.toJson(packages, ArrayList.class);
        return validatesJsonArray(allPackagesJsonArr);
    }

    /**
     * Main method that handles the logic of listing overdue packages
     * @return overduePackages in JSON
     */
    public String listOverduePackages() {
        ArrayList<Package> overduePackages = new ArrayList<>();
        for (Package aPackage : packages) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expectedDate = aPackage.getExpectedDeliveryDateTime();
            boolean isOverdue = expectedDate.isBefore(now) && !aPackage.getDelivered();
            if (isOverdue) {
                overduePackages.add(aPackage);
            }
        }
        String overduePackagesJsonArr = gson.toJson(overduePackages, ArrayList.class);
        return validatesJsonArray(overduePackagesJsonArr);
    }

    /**
     * Main method that handles the logic of listing upcoming packages
     * @return upcomingPackages arraylist with all upcoming packages
     */
    public String listUpcomingPackages() {
        ArrayList<Package> upcomingPackages = new ArrayList<>();

        for (Package aPackage : packages) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expectedDate = aPackage.getExpectedDeliveryDateTime();
            boolean isUpcoming = (expectedDate.isAfter(now) || expectedDate.isEqual(now)) && !aPackage.getDelivered();
            if (isUpcoming) {
                upcomingPackages.add(aPackage);
            }
        }

        String upcomingPackagesJsonArr = gson.toJson(upcomingPackages, ArrayList.class);
        return validatesJsonArray(upcomingPackagesJsonArr);
    }

    /**
     * Main method that handles the logic of adding book
     * @return the book itself
     */
    public Book addBook(Book book)  {
        book.setId(id.incrementAndGet());
        addPackageInOrder(book);
        return book;
    }

    /**
     * Main method that handles the logic of adding perishable
     * @return the perishable itself
     */
    public Perishable addPerishable(Perishable perishable) {
        perishable.setId(id.incrementAndGet());
        addPackageInOrder(perishable);
        return perishable;
    }

    /**
     * Main method that handles the logic of adding electronic
     * @return the electronic itself
     */
    public Electronic addElectronic(Electronic electronic)  {
        electronic.setId(id.incrementAndGet());
        addPackageInOrder(electronic);
        return electronic;
    }

    /**
     * removes package in packages based on its unique package id
     * @param packageId id of the package to be removed
     */
    public ArrayList<Package> removePackage(long packageId) {
        for (int i = 0; i < packages.size(); i++) {
            if (packages.get(i).getId() == packageId) {
                packages.remove(packages.get(i));
            }
        }
        return packages;
    }

    /**
     * Main method that handles the logic of marking the package as delivered/ undelivered.
     * If previously package was delivered, it is not marked as undelivered (or unmarked)
     * Else which is if package was undelivered, package will be marked as delivered
     * @return all the packages
     */
    public List<Package> markPackageAsDelivered(long packageId) {
        for (Package aPackage : packages) {
            if (aPackage.getId() == packageId) {
                boolean currentStatus = aPackage.getDelivered();
                aPackage.setDelivered(!currentStatus);
            }
        }
        return packages;
    }

    /**
     * Called by exit method in PackagesRepoController.
     * This is the main method that handles the logic of saving all the packages
     */
    public void saveGson() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            String validatedPackages = listAllPackages();
            fileWriter.write(validatedPackages);
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Error in writing the file");
        }
    }

    /**
     * Load the saved packages in list.json to packages array list.
     * Automatically called by the constructor on the start of program
     */
    private void loadGson() {
        try {

            file = new File("list.json");

            RuntimeTypeAdapterFactory<Package> packageAdapterFactory =
                    RuntimeTypeAdapterFactory.of(Package.class, "type")
                            .registerSubtype(Book.class, "Book")
                            .registerSubtype(Perishable.class, "Perishable")
                            .registerSubtype(Electronic.class, "Electronic");

            gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                            new TypeAdapter<LocalDateTime>() {
                                @Override
                                public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
                                    jsonWriter.value(localDateTime.toString());
                                }

                                @Override
                                public LocalDateTime read(JsonReader jsonReader) throws IOException {
                                    return LocalDateTime.parse(jsonReader.nextString());
                                }
                            })
                    .registerTypeAdapterFactory(packageAdapterFactory)
                    .setPrettyPrinting()
                    .create();

            if (file.exists()) {
                Type type = new TypeToken<ArrayList<Package>>(){}.getType();
                FileReader fileReader = new FileReader(file);

                packages = gson.fromJson(fileReader, type);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long maxId = 0;
        for (Package aPackage : packages) {
            if (aPackage.getId() > maxId) {
                maxId = aPackage.getId();
            }
        }
        id.set(maxId);

    }
    /**
     * adds package in order
     * @param newPackage the package to be added in order
     */
    private void addPackageInOrder(Package newPackage) {
        int i;
        for (i = 0; i < packages.size(); i++) {
            if (newPackage.compareTo(packages.get(i)) < 0) {
                break;
            }
        }

        if (i < packages.size())
            packages.add(i, newPackage);
        else
            packages.add(newPackage); // package is the newest
    }

    /**
     * Make sure the package has the exact attribute.
     * @param packages in string
     * @return validated JSON in string
     */
    private String validatesJsonArray(String packages) {
        JsonArray jsonArray = JsonParser.parseString(packages).getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            if (!jsonObject.has("type")) {
                if (jsonObject.has("author")) {
                    jsonObject.addProperty("type", "Book");
                } else if (jsonObject.has("expiryDateTime")) {
                    jsonObject.addProperty("type", "Perishable");
                } else if (jsonObject.has("envHandlingFeeInDollar")) {
                    jsonObject.addProperty("type", "Electronic");
                }
            }
        }

        return jsonArray.toString();
    }



}
