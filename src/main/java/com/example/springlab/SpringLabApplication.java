package com.example.springlab;

import com.example.springlab.service.HelloService;
import com.example.springlab.service.LabMessageFormatter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpringLabApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SpringLabApplication.class, args);
		HelloService service = context.getBean(HelloService.class);

	}
}

/*
/*LabMessageFormatter component =
				context.getBean("labMessageFormatter",
						LabMessageFormatter.class);

		LabMessageFormatter configured =
				context.getBean("configuredMessageFormatter",
						LabMessageFormatter.class);

		System.out.println(component.format("Hello"));
		System.out.println(configured.format("Hello"));


component == configured   will return false
Why? Each registration has its own bean definition and, with the default singleton scope, its own managed instance.
This is a subtle but important lesson: singleton means one instance per bean definition in a given container, not one instance per Java class.


*/
