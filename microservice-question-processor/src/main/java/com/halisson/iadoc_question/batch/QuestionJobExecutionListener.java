package com.halisson.iadoc_question.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.stereotype.Component;

@Component
public class QuestionJobExecutionListener implements JobExecutionListener {
	
	private final JobExplorer jobExplorer;
	
	public QuestionJobExecutionListener(JobExplorer jobExplorer) {
		this.jobExplorer = jobExplorer;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		int runningJobsCount = jobExplorer.findRunningJobExecutions(jobExecution.getJobInstance().getJobName()).size();
	    if(runningJobsCount > 1){
	        throw new UnexpectedJobExecutionException("There are already active running instances of this job, Please wait those executions first.");
	    }
	}

}
