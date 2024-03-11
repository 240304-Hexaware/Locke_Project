package com.locke.babelrecords;

import com.locke.babelrecords.models.User;
import com.locke.babelrecords.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = {
		"com.locke.babelrecords.controllers",
		"com.locke.babelrecords.services",
		"com.locke.babelrecords.repositories"},
		exclude = {DataSourceAutoConfiguration.class
})
public class BabelrecordsApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(BabelrecordsApplication.class, args);
	}

}
