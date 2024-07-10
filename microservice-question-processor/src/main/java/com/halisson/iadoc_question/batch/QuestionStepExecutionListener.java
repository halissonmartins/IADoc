package com.halisson.iadoc_question.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.stereotype.Component;

import com.halisson.iadoc_question.enums.StepBatchStatus;

@Component
public class QuestionStepExecutionListener implements StepExecutionListener {
	
	private final JobExplorer jobExplorer;
	
	public QuestionStepExecutionListener(JobExplorer jobExplorer) {
		super();
		this.jobExplorer = jobExplorer;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		
		int runningJobsCount = jobExplorer.findRunningJobExecutions(
				stepExecution.getJobExecution().getJobInstance().getJobName()).size();
		
		if(runningJobsCount > 1){
			return new ExitStatus(StepBatchStatus.FINISHED.name());
		}else {
			return new ExitStatus(StepBatchStatus.CONTINUE.name());
		}
	}
	

}
