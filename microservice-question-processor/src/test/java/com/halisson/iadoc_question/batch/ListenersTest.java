package com.halisson.iadoc_question.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.explore.JobExplorer;

import com.halisson.iadoc_library.enums.StepBatchStatus;

@ExtendWith(MockitoExtension.class)
class ListenersTest {

    @Mock
    private JobExplorer jobExplorer;

    private QuestionJobExecutionListener jobListener;
    private QuestionStepExecutionListener stepListener;
    private JobCompletionNotificationListener completionListener;

    @BeforeEach
    void setUp() {
        jobListener = new QuestionJobExecutionListener(jobExplorer);
        stepListener = new QuestionStepExecutionListener(jobExplorer);
        completionListener = new JobCompletionNotificationListener();
    }

    @Test
    void testJobExecutionListener_NoRunningJobs_ShouldNotThrowException() {
        JobInstance jobInstance = new JobInstance(1L, "TestJob");
        JobExecution jobExecution = new JobExecution(jobInstance, 1L, null);

        when(jobExplorer.findRunningJobExecutions(anyString())).thenReturn(Collections.emptySet());

        jobListener.beforeJob(jobExecution);
    }

    @Test
    void testJobExecutionListener_WithRunningJobs_ShouldThrowException() {
        JobInstance jobInstance = new JobInstance(1L, "TestJob");
        JobExecution jobExecution1 = new JobExecution(jobInstance, 1L, null);
        JobExecution jobExecution2 = new JobExecution(jobInstance, 2L, null);

        when(jobExplorer.findRunningJobExecutions(anyString()))
            .thenReturn(Set.of(jobExecution1, jobExecution2));

        assertThrows(UnexpectedJobExecutionException.class, 
            () -> jobListener.beforeJob(jobExecution1));
    }

    @Test
    void testStepExecutionListener_NoRunningJobs_ReturnsContinue() {
        JobInstance jobInstance = new JobInstance(1L, "TestJob");
        JobExecution jobExecution = new JobExecution(jobInstance, 1L, null);
        StepExecution stepExecution = new StepExecution("testStep", jobExecution);

        when(jobExplorer.findRunningJobExecutions(anyString()))
            .thenReturn(Set.of(jobExecution));

        ExitStatus result = stepListener.afterStep(stepExecution);

        assertEquals(StepBatchStatus.CONTINUE.name(), result.getExitCode());
    }

    @Test
    void testStepExecutionListener_WithRunningJobs_ReturnsFinished() {
        JobInstance jobInstance = new JobInstance(1L, "TestJob");
        JobExecution jobExecution1 = new JobExecution(jobInstance, 1L, null);
        JobExecution jobExecution2 = new JobExecution(jobInstance, 2L, null);
        StepExecution stepExecution = new StepExecution("testStep", jobExecution1);

        when(jobExplorer.findRunningJobExecutions(anyString()))
            .thenReturn(Set.of(jobExecution1, jobExecution2));

        ExitStatus result = stepListener.afterStep(stepExecution);

        assertEquals(StepBatchStatus.FINISHED.name(), result.getExitCode());
    }

    @Test
    void testJobCompletionNotificationListener_CompletedStatus() {
        JobInstance jobInstance = new JobInstance(1L, "TestJob");
        JobExecution jobExecution = new JobExecution(jobInstance, 1L, null);
        jobExecution.setStatus(BatchStatus.COMPLETED);

        completionListener.afterJob(jobExecution);
    }

    @Test
    void testJobCompletionNotificationListener_FailedStatus() {
        JobInstance jobInstance = new JobInstance(1L, "TestJob");
        JobExecution jobExecution = new JobExecution(jobInstance, 1L, null);
        jobExecution.setStatus(BatchStatus.FAILED);

        completionListener.afterJob(jobExecution);
    }
}
