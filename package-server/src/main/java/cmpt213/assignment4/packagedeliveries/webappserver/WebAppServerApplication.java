package cmpt213.assignment4.packagedeliveries.webappserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class used to run the server program
 */
@SpringBootApplication
public class WebAppServerApplication {

	/**
	 * Main method to run the program
	 * @param args no args used in this program
	 */
	public static void main(String[] args) {
		SpringApplication.run(WebAppServerApplication.class, args);
	}

}
