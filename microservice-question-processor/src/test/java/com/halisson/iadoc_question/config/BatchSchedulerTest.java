package com.halisson.iadoc_question.config;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

@ExtendWith(MockitoExtension.class)
class BatchSchedulerTest {

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job job;

    @InjectMocks
    private BatchScheduler batchScheduler;

    @Test
    void testRun() throws Exception {
        JobExecution jobExecution = new JobExecution(1L);
        when(jobLauncher.run(eq(job), any(JobParameters.class))).thenReturn(jobExecution);

        batchScheduler.run();

        verify(jobLauncher).run(eq(job), any(JobParameters.class));
    }
}
