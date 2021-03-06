package day2.study.jobLauncher;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CsvFileToDatabaseJobLauncher {

	public static final Logger LOGGER = LoggerFactory.getLogger(CsvFileToDatabaseJobLauncher.class);

	private final Job job;

	private final JobLauncher jobLauncher;

	@Autowired
	CsvFileToDatabaseJobLauncher(@Qualifier("csvFileToDatabaseJob") Job job, JobLauncher jobLauncher) {
		LOGGER.info("CsvFileToDatabaseJobLauncher 初始化");
		this.job = job;
		this.jobLauncher = jobLauncher;
	}

	@Scheduled(fixedRate = 10000)
	void launchCsvFileToDatabaseJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
			JobRestartException, JobInstanceAlreadyCompleteException {
		LOGGER.info("Starting csvFileToDatabase job");

		jobLauncher.run(job, newExecution());

		LOGGER.info("Stopping csvFileToDatabase job");
	}

	private JobParameters newExecution() {
		Map<String, JobParameter> parameters = new HashMap<>();

		JobParameter parameter = new JobParameter(new Date());
		parameters.put("currentTime", parameter);

		return new JobParameters(parameters);
	}
}
