package com.paglaai.estimaai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EstimaaiApplication {

  public static void main(String[] args) {
    SpringApplication.run(EstimaaiApplication.class, args);
  }
}
