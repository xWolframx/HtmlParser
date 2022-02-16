package ru.egor.kulizhnyh.htmlparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.egor.kulizhnyh.htmlparser.DAO.DAO;

@SpringBootApplication
public class HtmlparserApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(HtmlparserApplication.class, args);

		DAO.createDB();
	}

}
