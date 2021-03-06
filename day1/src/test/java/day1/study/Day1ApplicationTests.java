package day1.study;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day1ApplicationTests {
	public static Logger logger = LoggerFactory.getLogger(Day1ApplicationTests.class);
	@Test
	public void contextLoads() {
		 logger.trace("trace");
	        logger.debug("debug");
	        logger.info("info");
	        logger.warn("warn");
	        logger.error("error");
	}

}
