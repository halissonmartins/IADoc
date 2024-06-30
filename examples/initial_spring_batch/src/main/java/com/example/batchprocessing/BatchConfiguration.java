package com.example.batchprocessing;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import jakarta.annotation.PreDestroy;

@Configuration
public class BatchConfiguration {
	
	private static final Logger log = LoggerFactory.getLogger(BatchConfiguration.class);
	
	private static final String JOB_NAME = "customerReportJob";
	
	private final JobExplorer jobs;
	
	public BatchConfiguration(JobExplorer jobs) {
		super();
		this.jobs = jobs;
	}

	@Bean
	FlatFileItemReader<Person> reader() {
	  return new FlatFileItemReaderBuilder<Person>()
	    .name("personItemReader")
	    .resource(new ClassPathResource("sample-data.csv"))
	    .delimited()
	    .names("firstName", "lastName")
	    .targetType(Person.class)
	    .build();
	}

	@Bean
	PersonItemProcessor processor() {
	  return new PersonItemProcessor();
	}

	@Bean
	JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
	  return new JdbcBatchItemWriterBuilder<Person>()
	    .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
	    .dataSource(dataSource)
	    .beanMapped()
	    .build();
	}
	
	@Bean
	Job importUserJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
	  return new JobBuilder(JOB_NAME, jobRepository)
	    .listener(listener)
	    .start(step1)
	    .build();
	}

	@Bean
	Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
	          FlatFileItemReader<Person> reader, PersonItemProcessor processor, JdbcBatchItemWriter<Person> writer) {
	  return new StepBuilder("step1", jobRepository)
	    .<Person, Person> chunk(3, transactionManager)
	    .reader(reader)
	    .processor(processor)
	    .writer(writer)
	    .build();
	}
	
	@PreDestroy
    public void destroy() throws NoSuchJobException {
		
        jobs.getJobNames().forEach(name -> log.info("job name: {}", name));
        long jobInstanceCount = jobs.getJobInstanceCount(JOB_NAME);
        
		jobs.getJobInstances(JOB_NAME, 0, (int)jobInstanceCount).forEach(
            jobInstance -> {
                log.info("job instance id {}", jobInstance.getInstanceId());
            }
        );
    }
	
}
