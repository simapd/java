package br.com.simapd.simapd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SimapdApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimapdApplication.class, args);
	}

}
