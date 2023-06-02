package com.paglaai.estimaai;

import jakarta.annotation.sql.DataSourceDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EstimaaiApplication {

    public static void main(String[] args) {
        System.out.println("Test from EstimaAI");
        SpringApplication.run(EstimaaiApplication.class, args);
    }

}
