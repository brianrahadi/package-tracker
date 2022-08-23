package cmpt213.assignment4.packagedeliveries.controller;

import cmpt213.assignment4.packagedeliveries.gson.extras.RuntimeTypeAdapterFactory;
import cmpt213.assignment4.packagedeliveries.model.Book;
import cmpt213.assignment4.packagedeliveries.model.Electronic;
import cmpt213.assignment4.packagedeliveries.model.Package;

import cmpt213.assignment4.packagedeliveries.model.Perishable;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * PackageTracker used to keep track of the packages in array list and return the correct packages requested by the view
 */
public class PackageTracker {
    private ArrayList<Package> packages;

    private Gson gson;

    private Type type;


    /**
     * Default constructor for package tracker
     * Generate the packages and fill it with object from list.json if needed
     */
    public PackageTracker() {
        packages = new ArrayList<>();
        initializeGson();
    }

    /**
     * @return allPackages new object (deep copy)
     */
    public ArrayList<Package> listAllPackages() {
        return listPackagesHelper("listAll");
    }

    /**
     * returns overdue packages
     * @return overduePackages arraylist with all overdue packages
     */
    public ArrayList<Package> listOverduePackages() {
        return listPackagesHelper("listOverduePackage");
    }

    /**
     * returns upcoming packages
     * @return upcomingPackages arraylist with all upcoming packages
     */
    public ArrayList<Package> listUpcomingPackages() {
        return listPackagesHelper("listUpcomingPackage");
    }

    /**
     * Add a book to the server
     * @param bookJson Book in JSON format
     */
    public void addBook(Book bookJson) {
        addPackageHelper(bookJson, "addBook");
    }

    /**
     * Add a perishable to the server
     * @param perishableJson Perishable in JSON format
     */
    public void addPerishable(Perishable perishableJson) {
        addPackageHelper(perishableJson, "addPerishable");
    }

    /**
     * Add an electronic to the server
     * @param electronicJson Electronic in JSON format
     */
    public void addElectronic(Electronic electronicJson) {
        addPackageHelper(electronicJson, "addElectronic");
    }

    /**
     * removes package in packages using object references
     *
     * @param removedPackage package to be removed
     */
    public void removePackage(Package removedPackage) {
        try {
            long removedId = removedPackage.getId();
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost postRequest = new HttpPost("http://localhost:8080/removePackage/" + removedId);
            client.execute(postRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * removes package in packages using object references
     * @param markedPackage package to be removed
     */
    public void markPackageAsDelivered(Package markedPackage) {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            long markedId = markedPackage.getId();
            HttpPost postRequest = new HttpPost("http://localhost:8080/markPackageAsDelivered/" + markedId);
            client.execute(postRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper method to lessen line of codes
     * @param pathURL Path url in string
     * @return the packages based on pathURL
     */
    private ArrayList<Package> listPackagesHelper(String pathURL) {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet getRequest = new HttpGet("http://localhost:8080/" + pathURL);
            CloseableHttpResponse response = client.execute(getRequest);
            String responseString = new BasicResponseHandler().handleResponse(response);
            packages = gson.fromJson(responseString, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packages;
    }

    /**
     * Helper method to simplify code
     * @param packageJson package information in JSON format
     * @param pathURL the path of the post request
     */
    private void addPackageHelper(Package packageJson, String pathURL) {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpPost postRequest = new HttpPost("http://localhost:8080/" + pathURL);
            String JSON_STRING= gson.toJson(packageJson);
            HttpEntity stringEntity = new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON);
            postRequest.setEntity(stringEntity);
            client.execute(postRequest);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method to initialize a list the json and populate the packages if list.json is filled
     */
    private void initializeGson() {

        // code used to register the subclasses
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
                .create();

        type = new TypeToken<ArrayList<Package>>(){}.getType();
    }

    /**
     * Call the server to save current package.
     * Automatically called when client exits.
     */
    public void saveGson() {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet getRequest = new HttpGet("http://localhost:8080/exit/");
            client.execute(getRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
