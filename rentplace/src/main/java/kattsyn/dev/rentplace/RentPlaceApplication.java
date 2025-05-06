package kattsyn.dev.rentplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.yml")
public class RentPlaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentPlaceApplication.class, args);
	}

}
