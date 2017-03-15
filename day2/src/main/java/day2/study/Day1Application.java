package day2.study;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableBatchProcessing
public class Day1Application {
	public static Logger logger = LoggerFactory.getLogger(Day1Application.class);

	public static void main(String[] args) {
		logger.info("进入main函数");
		SpringApplication.run(Day1Application.class, args);
	}
}
