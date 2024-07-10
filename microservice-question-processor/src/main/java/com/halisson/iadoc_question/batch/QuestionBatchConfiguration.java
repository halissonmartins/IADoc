package com.halisson.iadoc_question.batch;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;

import com.halisson.iadoc_question.entity.Question;
import com.halisson.iadoc_question.enums.EnumDocumentStatus;
import com.halisson.iadoc_question.enums.EnumQuestionStatus;
import com.halisson.iadoc_question.enums.StepBatchStatus;
import com.halisson.iadoc_question.repository.QuestionRepository;
import com.halisson.iadoc_question.service.ChatService;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class QuestionBatchConfiguration {

	private static final String JOB_NAME = "QuestionProcessorJob";

	private final JobExplorer jobs;
	private final ChatService chatService;
	
	public QuestionBatchConfiguration(JobExplorer jobs, ChatService chatService) {
		super();
		this.jobs = jobs;
		this.chatService = chatService;
	}

	@Bean
	@Qualifier("processingStarted-QuestionWithOutDocument-ItemReader")
	RepositoryItemReader<Question> readerProcessingStartedQuestionWithOutDocument(QuestionRepository repository) {

		log.debug("DocumentReader injected {}", LocalDateTime.now());

		return createQuestionWithOutDocumentRepositoryItemReader(repository, EnumQuestionStatus.CREATED);
	}
	
	@Bean
	@Qualifier("imported-QuestionWithOutDocument-ItemReader")
	RepositoryItemReader<Question> readerImportedQuestionWithOutDocument(QuestionRepository repository) {
		
		log.debug("DocumentReader injected {}", LocalDateTime.now());
		
		return createQuestionWithOutDocumentRepositoryItemReader(repository, EnumQuestionStatus.PROCESSING_STARTED);
	}

	private RepositoryItemReader<Question> createQuestionWithOutDocumentRepositoryItemReader(
			QuestionRepository repository, EnumQuestionStatus status) {
		
		Map<String, Direction> sortMap = new HashMap<>();
		sortMap.put("id", Direction.DESC);

		return new RepositoryItemReaderBuilder<Question>()
				.name("question-WithOutDocument-ItemReader")
				.repository(repository)
				.methodName("findByQuestionStatusCodeAndDocumentIsNull")
				.arguments(status.getCode())
				.sorts(sortMap)
				.saveState(false)
				.build();
	}
	
	@Bean
	@Qualifier("processingStarted-QuestionWithDocument-ItemReader")
	RepositoryItemReader<Question> readerProcessingStartedQuestionWithDocument(QuestionRepository repository) {

		log.debug("DocumentReader injected {}", LocalDateTime.now());

		return createQuestionWithDocumentRepositoryItemReader(repository, EnumQuestionStatus.CREATED);
	}
	
	@Bean
	@Qualifier("imported-QuestionWithDocument-ItemReader")
	RepositoryItemReader<Question> readerImportedQuestionWithDocument(QuestionRepository repository) {
		
		log.debug("DocumentReader injected {}", LocalDateTime.now());
		
		return createQuestionWithDocumentRepositoryItemReader(repository, EnumQuestionStatus.PROCESSING_STARTED);
	}

	private RepositoryItemReader<Question> createQuestionWithDocumentRepositoryItemReader(
			QuestionRepository repository, EnumQuestionStatus status) {
		
		Map<String, Direction> sortMap = new HashMap<>();
		sortMap.put("id", Direction.DESC);

		return new RepositoryItemReaderBuilder<Question>()
				.name("question-WithDocument-ItemReader")
				.repository(repository)
				.methodName("findByQuestionStatusCodeAndDocumentDocumentStatusCode")
				.arguments(status.getCode(), EnumDocumentStatus.SUCCESSFULLY_PROCESSED.getCode())
				.sorts(sortMap)
				.saveState(false)
				.build();
	}
	
	@Bean
	QuestionInitializeItemProcessor initializerDocument() {
		return new QuestionInitializeItemProcessor();
	}
	
	@Bean
	QuestionItemProcessor processorDocument() {
		return new QuestionItemProcessor(chatService);
	}
	
	@Bean
	RepositoryItemWriter<Question> writer(QuestionRepository repository) {

		log.debug("DocumentWriter injected {}", LocalDateTime.now());

		return new RepositoryItemWriterBuilder<Question>()
				.repository(repository)
				.methodName("save")
				.build();
	}

	@Bean
	Job importUserJob(JobRepository jobRepository, 
			@Qualifier("initilizerStepQuestionWithOutDocument") Step step1, 
			@Qualifier("processorStepQuestionWithOutDocument") Step step2, 
			@Qualifier("initilizerStepQuestionWithDocument") Step step3, 
			@Qualifier("processorStepQuestionWithDocument") Step step4, 
			JobCompletionNotificationListener listener) {
		return new JobBuilder(JOB_NAME, jobRepository)
				//.listener(new QuestionJobExecutionListener(jobs))
				.listener(listener)
				.start(step1).on(StepBatchStatus.CONTINUE.name()).to(step2)
				.from(step1).on(StepBatchStatus.FINISHED.name()).end()
				.from(step2).on("*").to(step3)
				.from(step3).on("*").to(step4)
				.end()
				.build();
	}

	@Bean
	@Qualifier("initilizerStepQuestionWithOutDocument")
	Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			@Qualifier("processingStarted-QuestionWithOutDocument-ItemReader") RepositoryItemReader<Question> reader, 
			QuestionInitializeItemProcessor processor, 
			RepositoryItemWriter<Question> writer) {
		return new StepBuilder("step1", jobRepository)
				.<Question, Question> chunk(1, transactionManager)
				.listener(new QuestionStepExecutionListener(jobs))
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.build();
	}
	
	@Bean
	@Qualifier("processorStepQuestionWithOutDocument")
	Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			@Qualifier("imported-QuestionWithOutDocument-ItemReader") RepositoryItemReader<Question> reader, 
			QuestionItemProcessor processor, 
			RepositoryItemWriter<Question> writer) {
		return new StepBuilder("step2", jobRepository)
				.<Question, Question> chunk(1, transactionManager)
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.build();
	}
	
	@Bean
	@Qualifier("initilizerStepQuestionWithDocument")
	Step step3(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			@Qualifier("processingStarted-QuestionWithDocument-ItemReader") RepositoryItemReader<Question> reader, 
			QuestionInitializeItemProcessor processor, 
			RepositoryItemWriter<Question> writer) {
		return new StepBuilder("step3", jobRepository)
				.<Question, Question> chunk(1, transactionManager)
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.build();
	}
	
	@Bean
	@Qualifier("processorStepQuestionWithDocument")
	Step step4(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			@Qualifier("imported-QuestionWithDocument-ItemReader") RepositoryItemReader<Question> reader, 
			QuestionItemProcessor processor, 
			RepositoryItemWriter<Question> writer) {
		return new StepBuilder("step4", jobRepository)
				.<Question, Question> chunk(1, transactionManager)
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
