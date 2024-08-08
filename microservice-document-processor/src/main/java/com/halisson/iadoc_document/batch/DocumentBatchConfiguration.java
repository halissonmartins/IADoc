package com.halisson.iadoc_document.batch;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;

import com.halisson.iadoc_document.entity.Document;
import com.halisson.iadoc_document.repository.DocumentRepository;
import com.halisson.iadoc_library.enums.EnumDocumentStatus;
import com.halisson.iadoc_library.enums.StepBatchStatus;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DocumentBatchConfiguration {

	private static final String JOB_NAME = "DocumentProcessorJob";

	private final JobExplorer jobs;
	private final VectorStore vectorStore;
	private final PdfDocumentReaderConfig pdfConfig;
	private final String receivedDir;
	private final String processedDir;

	public DocumentBatchConfiguration(
			JobExplorer jobs, 
			VectorStore vectorStore, 
			PdfDocumentReaderConfig pdfConfig,
			DocumentRepository documentRepository,
			@Value("${UPLOAD_RECEIVED_DIR}") String receivedDir, 
			@Value("${PROCESSED_DIR}") String processedDir) {
		super();
		this.jobs = jobs;
		this.vectorStore = vectorStore;
		this.pdfConfig = pdfConfig;
		this.receivedDir = receivedDir;
		this.processedDir = processedDir;
	}

	@Bean
	@Qualifier("processingStartedItemReader")
	RepositoryItemReader<Document> readerProcessingStarted(DocumentRepository repository) {

		log.debug("DocumentReader injected {}", LocalDateTime.now());

		return createRepositoryItemReader(repository, EnumDocumentStatus.IMPORTED);
	}
	
	@Bean
	@Qualifier("importedItemReader")
	RepositoryItemReader<Document> readerImported(DocumentRepository repository) {
		
		log.debug("DocumentReader injected {}", LocalDateTime.now());
		
		return createRepositoryItemReader(repository, EnumDocumentStatus.PROCESSING_STARTED);
	}

	private RepositoryItemReader<Document> createRepositoryItemReader(DocumentRepository repository, EnumDocumentStatus status) {
		Map<String, Direction> sortMap = new HashMap<>();
		sortMap.put("id", Direction.DESC);

		return new RepositoryItemReaderBuilder<Document>()
				.name("documentItemReader")
				.repository(repository)
				.methodName("findByDocumentStatusCode")
				.arguments(status.getCode())
				.sorts(sortMap)
				.saveState(false)
				.build();
	}

	@Bean
	DocumentInitializeItemProcessor initializerDocument() {
		return new DocumentInitializeItemProcessor();
	}
	
	@Bean
	DocumentItemProcessor processorDocument() {
		return new DocumentItemProcessor(vectorStore, pdfConfig, receivedDir, processedDir);
	}
	
	@Bean
	RepositoryItemWriter<Document> writer(DocumentRepository repository) {

		log.debug("DocumentWriter injected {}", LocalDateTime.now());

		return new RepositoryItemWriterBuilder<Document>()
				.repository(repository)
				.methodName("save")
				.build();
	}

	@Bean
	Job importUserJob(JobRepository jobRepository, 
			@Qualifier("initilizerStep") Step step1, 
			@Qualifier("processorStep") Step step2, 
			JobCompletionNotificationListener listener) {
		return new JobBuilder(JOB_NAME, jobRepository)
				//.listener(new DocumentJobExecutionListener(jobs))
				.start(step1).on(StepBatchStatus.CONTINUE.name()).to(step2)
				.from(step1).on(StepBatchStatus.FINISHED.name()).end()
				.next(step2)
				.build()
				.listener(listener)
				.build();
	}

	@Bean
	@Qualifier("initilizerStep")
	Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			@Qualifier("processingStartedItemReader") RepositoryItemReader<Document> reader, 
			DocumentInitializeItemProcessor processor, 
			RepositoryItemWriter<Document> writer) {
		return new StepBuilder("step1", jobRepository)
				.<Document, Document> chunk(1, transactionManager)
				.listener(new DocumentStepExecutionListener(jobs))
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.build();
	}
	
	@Bean
	@Qualifier("processorStep")
	Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			@Qualifier("importedItemReader") RepositoryItemReader<Document> reader, 
			DocumentItemProcessor processor, 
			RepositoryItemWriter<Document> writer) {
		return new StepBuilder("step2", jobRepository)
				.<Document, Document> chunk(1, transactionManager)
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
