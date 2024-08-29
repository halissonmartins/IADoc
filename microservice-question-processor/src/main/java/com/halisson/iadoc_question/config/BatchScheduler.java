package com.halisson.iadoc_question.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class BatchScheduler {
	
	private static final Logger log = LoggerFactory.getLogger(BatchScheduler.class);

	private final JobLauncher jobLauncher;
    private final Job job;
    
	public BatchScheduler(JobLauncher jobLauncher, Job job) {
		super();
		this.jobLauncher = jobLauncher;
		this.job = job;
	}

    @Scheduled(fixedRate = 50000)
    public void run() throws Exception {
    	log.info("JOB startup");
        var execution = jobLauncher.run(
        	job,
            new JobParametersBuilder().addLong("uniqueness", System.nanoTime()).toJobParameters()
        );
        log.info("Exit status: {}", execution.getStatus());
    }

}
