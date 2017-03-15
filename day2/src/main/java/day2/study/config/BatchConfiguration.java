package day2.study.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import day2.study.model.CreditBill;
import day2.study.processor.CreditBillProcessor;

@Configuration
// @EnableBatchProcessing
public class BatchConfiguration {
	public static final Logger LOGGER = LoggerFactory.getLogger(BatchConfiguration.class);
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	ItemReader<CreditBill> csvFileItemReader() {
		LOGGER.info("reader 初始化");
		FlatFileItemReader<CreditBill> csvFileReader = new FlatFileItemReader<>();
		csvFileReader.setResource(new ClassPathResource("data/credit-card-bill-201303.csv"));
		csvFileReader.setLinesToSkip(1);

		LineMapper<CreditBill> studentLineMapper = createLineMapper();
		csvFileReader.setLineMapper(studentLineMapper);

		return csvFileReader;
	}

	private LineMapper<CreditBill> createLineMapper() {
		DefaultLineMapper<CreditBill> studentLineMapper = new DefaultLineMapper<>();

		LineTokenizer lineTokenizer = createLineTokenizer();
		studentLineMapper.setLineTokenizer(lineTokenizer);

		FieldSetMapper<CreditBill> filedsMapper = createFieldsMapper();
		studentLineMapper.setFieldSetMapper(filedsMapper);

		return studentLineMapper;
	}

	private LineTokenizer createLineTokenizer() {
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setNames(new String[] { "accountID", "name", "amount", "date", "address" });
		return lineTokenizer;
	}

	private FieldSetMapper<CreditBill> createFieldsMapper() {
		BeanWrapperFieldSetMapper<CreditBill> filedsMapper = new BeanWrapperFieldSetMapper<>();
		filedsMapper.setTargetType(CreditBill.class);
		return filedsMapper;
	}

	@Bean
	ItemProcessor<CreditBill, CreditBill> csvFileItemProcessor() {
		LOGGER.info("processor 初始化");
		return new CreditBillProcessor();
	}

	@Bean
	ItemWriter<CreditBill> csvItemWriter() {
		FlatFileItemWriter<CreditBill> csvFileWriter = new FlatFileItemWriter<>();

		csvFileWriter.setResource(new FileSystemResource("D:/test.csv"));

		LineAggregator<CreditBill> lineAggregator = createLineAggregator();
		csvFileWriter.setLineAggregator(lineAggregator);

		return csvFileWriter;
	}

	private LineAggregator<CreditBill> createLineAggregator() {
		DelimitedLineAggregator<CreditBill> lineAggregator = new DelimitedLineAggregator<>();
		lineAggregator.setDelimiter(";");

		FieldExtractor<CreditBill> fieldExtractor = createFieldExtractor();
		lineAggregator.setFieldExtractor(fieldExtractor);

		return lineAggregator;
	}

	private FieldExtractor<CreditBill> createFieldExtractor() {
		BeanWrapperFieldExtractor<CreditBill> extractor = new BeanWrapperFieldExtractor<>();
		extractor.setNames(new String[] { "accountID", "name", "amount", "date", "address" });
		return extractor;
	}

	@Bean
	Step csvFileToDatabaseStep(ItemReader<CreditBill> csvFileItemReader,
			ItemProcessor<CreditBill, CreditBill> csvFileItemProcessor, ItemWriter<CreditBill> csvItemWriter,
			StepBuilderFactory stepBuilderFactory) {
		LOGGER.info("step 初始化");
		return stepBuilderFactory.get("csvFileToDatabaseStep").<CreditBill, CreditBill>chunk(1)
				.reader(csvFileItemReader).processor(csvFileItemProcessor).writer(csvItemWriter).build();
	}

	@Bean
	Job csvFileToDatabaseJob(JobBuilderFactory jobBuilderFactory,
			@Qualifier("csvFileToDatabaseStep") Step csvStudentStep) {
		LOGGER.info("job 初始化");
		return jobBuilderFactory.get("csvFileToDatabaseJob").incrementer(new RunIdIncrementer()).flow(csvStudentStep)
				.end().build();
	}
}
