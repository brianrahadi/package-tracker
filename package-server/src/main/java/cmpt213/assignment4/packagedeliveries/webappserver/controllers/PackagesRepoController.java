package cmpt213.assignment4.packagedeliveries.webappserver.controllers;

import cmpt213.assignment4.packagedeliveries.webappserver.control.PackageTracker;
import cmpt213.assignment4.packagedeliveries.webappserver.model.Book;
import cmpt213.assignment4.packagedeliveries.webappserver.model.Electronic;
import cmpt213.assignment4.packagedeliveries.webappserver.model.Package;
import cmpt213.assignment4.packagedeliveries.webappserver.model.Perishable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Main class used to respond a variety of get and post requests. The main logic is handled by PackageTracker.
 * This class gives 12 warnings because it is never used. It is technically on the server, so not visibly used.
 */
@RestController
public class PackagesRepoController {
    private final PackageTracker packageTracker;

    public PackagesRepoController() {
        this.packageTracker = new PackageTracker();
    }

    /**
     * Check if server is online
     * @return "System is Up!" string
     */
    @GetMapping("/ping")
    public String ping() {
        return "System is up!";
    }

    /**
     * Return all packages in form of JSON string
     * @return allPackages in JSON string
     */
    @GetMapping("/listAll")
    public String listAllPackages()  {
        return packageTracker.listAllPackages();
    }

    /**
     * Add book to the packages
     * @return Book the request body book itself
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/addBook")
    public Book addBook(@RequestBody Book book)  {
        return packageTracker.addBook(book);
    }

    /**
     * Add perishable to the packages
     * @return Perishable the request body perishable itself
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/addPerishable")
    public Perishable addPerishable(@RequestBody Perishable perishable) {
        return packageTracker.addPerishable(perishable);
    }

    /**
     * Add electronic to the packages
     * @return Electronic the request body electronic itself
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/addElectronic")
    public Electronic addElectronic(@RequestBody Electronic electronic)  {
        return packageTracker.addElectronic(electronic);
    }

    /**
     * remove packages based on it's unique package id
     * @return all packages after the specific package has been removed
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/removePackage/{id}")
    public List<Package> removePackage(@PathVariable("id") long packageId) {
        return packageTracker.removePackage(packageId);
    }

    /**
     * Return overdue packages in form of JSON string
     * @return overduePackages in JSON string
     */
    @GetMapping("/listOverduePackage")
    public String listOverduePackages() {
        return packageTracker.listOverduePackages();
    }

    /**
     * returns upcoming packages in form of JSON string
     * @return upcomingPackages in JSON string
     */
    @GetMapping("/listUpcomingPackage")
    public String listUpcomingPackages() {
        return packageTracker.listUpcomingPackages();
    }

    /**
     * Marks package as delivered/ undelivered based on its previous package status
     * @param packageId the unique atomic long package id
     * @return all packages after the specific package has been marked
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/markPackageAsDelivered/{id}")
    public List<Package> markPackageAsDelivered(@PathVariable("id") long packageId) {
        return packageTracker.markPackageAsDelivered(packageId);
    }

    /**
     * Called automatically when client exits to save the current all packages as JSON in list.json
     */
    @GetMapping("/exit")
    public void exit() {
        packageTracker.saveGson();
    }

}
