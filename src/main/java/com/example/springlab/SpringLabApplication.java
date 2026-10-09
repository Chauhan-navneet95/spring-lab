package com.example.springlab;

import com.example.springlab.service.HelloService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringLabApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SpringLabApplication.class, args);
		HelloService service =
				context.getBean(HelloService.class);

		System.out.println(service);
	}


}
