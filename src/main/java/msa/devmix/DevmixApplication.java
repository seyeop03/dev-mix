package msa.devmix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DevmixApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevmixApplication.class, args);
	}

}
