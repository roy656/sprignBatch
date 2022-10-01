package practice.springbatch.sprignbatch.basic_job_and_step;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class HelloConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    // Job 은 스프링배치 의 실행 단위
    @Bean
    public Job helloJob() {
        return jobBuilderFactory.get("helloJob")    // name 은 스프링 배치를 실행 할 수 있는 Key 이기도 함.
                .incrementer(new RunIdIncrementer())    // Job 이 실행 될때마다 새로운 파라미터 Id를 생성해주어 항상 새로운 Job 인스턴스를 만들도록 돕는다
                .start(this.helloStep())    // Job 실행시 최초로 실행될 step 을 설정하는 메소드.
                .build();
    }

    // Step 은 Job 의 실행 단위
    @Bean
    public Step helloStep() {
        return stepBuilderFactory.get("helloJob")
                .tasklet((contribution, chunkContext) -> {
                    log.info("hello spring batch!!!");
                    return RepeatStatus.FINISHED;
                }).build();
    }

}
