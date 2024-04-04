package com.locke.babelrecords;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(scanBasePackages = {
    "com.locke.babelrecords.controllers",
    "com.locke.babelrecords.services",
    "com.locke.babelrecords.repositories",
    "com.locke.babelrecords.security",
    "com.locke.babelrecords.utils" },
    exclude = { DataSourceAutoConfiguration.class
    })
@EnableMongoAuditing
public class BabelrecordsApplication {

  public static void main(String[] args) {
    SpringApplication.run(BabelrecordsApplication.class, args);
  }

}
