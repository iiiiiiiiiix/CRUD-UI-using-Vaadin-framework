package crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner loadData(StudentRepository repository) {
		return (args) -> {

			repository.save(new Student("Josephine", "Andriani"));
			repository.save(new Student("Mike", "Dirnt"));
			repository.save(new Student("Rob", "Scallon"));
			repository.save(new Student("Kate", "Stone"));
			repository.save(new Student("Andrew", "Huang"));

		};
	}

}
