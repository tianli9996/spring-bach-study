package day1.study;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Day1Application {
	public static Logger logger = LoggerFactory.getLogger(Day1Application.class);
	public static void main(String[] args) {
		logger.debug("debug");
		logger.info("info");
		SpringApplication.run(Day1Application.class, args);
	}
}
