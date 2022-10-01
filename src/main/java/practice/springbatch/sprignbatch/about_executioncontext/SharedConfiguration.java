package practice.springbatch.sprignbatch.about_executioncontext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SharedConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job sharedJob() {
        return jobBuilderFactory.get("sharedJob")
                .incrementer(new RunIdIncrementer())
                .start(this.shareStep())    // 최초로 시작하는 메소드
                .next(this.shareStep2())      // 이후 next() 메소드로 다음 순서로 순차적 실행
                .build();
    }

    @Bean
    public Step shareStep() {
        return stepBuilderFactory.get("shareStep")
                .tasklet((contribution, chunkContext) -> {
                    StepExecution stepExecution = contribution.getStepExecution();
                    ExecutionContext stepExecutionContext = stepExecution.getExecutionContext();
                    stepExecutionContext.putString("stepKey", "step execution context");

                    JobExecution jobExecution = stepExecution.getJobExecution();
                    JobInstance jobInstance = jobExecution.getJobInstance();
                    ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();
                    jobExecutionContext.putString("jobKey", "job execution context");
                    JobParameters jobParameters = jobExecution.getJobParameters();

                    log.info("jobName : {}, stepName : {}, parameter: {}",
                            jobInstance.getJobName(),
                            stepExecution.getStepName(),
                            jobParameters.getLong("run.id"));

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step shareStep2() {
        return stepBuilderFactory.get("shareStep2")
                .tasklet((contribution, chunkContext) -> {
                    StepExecution stepExecution = contribution.getStepExecution();
                    ExecutionContext stepExecutionContext = stepExecution.getExecutionContext();

                    JobExecution jobExecution = stepExecution.getJobExecution();
                    ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();

                    log.info("jobKey : {}, stepKey : {}",
                            jobExecutionContext.getString("jobKey", "emptyJobKey"),
                            stepExecutionContext.getString("stepKey", "emptyStepKey"));

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    // ExecutionContext 는 Job 과 Step 의 context 를 관리하는 객체,  이 객체를 통해서 데이터를 서로 공유할 수 있다.
    // tasklet 실행시 contribution 를 이용해서 StepExecution 객체를 꺼낼 수 있고,
    // stepExecution 을 이용해서 stepExecutionContext 과 jobExecutionContext 를 꺼낼 수 있다.
    // 그 stepExecutionContext 과 jobExecutionContext 에 Key, Value 값을 넣어 저장 할 수 있다.

    // * JobExecutionContext 는 하나의 Job 안에서 공유되고, StepExecutionContext 는 하나의 Step 안에만 공유되고 Step 끼리의 공유 불가.
}
