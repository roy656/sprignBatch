package practice.springbatch.sprignbatch.springbatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import practice.springbatch.sprignbatch.entity.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SpringBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job Job1() throws Exception {
        return this.jobBuilderFactory.get("Job1")
                .incrementer(new RunIdIncrementer())
                .start(this.step1())
                .next(this.csvFileStep())
                .build();
    }

    @Bean
    public Step step1() {
        return this.stepBuilderFactory.get("step1")
                .<Person, Person>chunk(10)
                .reader(new CustomItemReader<>(getItems()))
                .writer(itemWriter())
                .build();
    }

    @Bean       // 밑에서 생성한 csvFileItemReader 를 reader 에 추가해 Step 을 하나 더 만든뒤 Jop 에 추가 해준다.
    public Step csvFileStep() throws Exception {
        return this.stepBuilderFactory.get("csvFileStep")
                .<Person, Person>chunk(10)
                .reader(this.csvFileItemReader())
                .writer(itemWriter())
                .build();
    }

    // *********** FlatFileItemReader 추가 분 ************

        // CSV File 을 읽어서 Person 클래스로 Mapping 할 수 있는 FlatFileItemReader 메소드
    public FlatFileItemReader<Person> csvFileItemReader() throws Exception {     // 데이터를 읽을수 있게 설정을 해야 하는데
        DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();   // CSV 파일을 한줄씩 읽을 수 있게 설정을 하는 LineMapper 객체를 생성한다.
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();    // 그리고 CSV 파일을 Person 객체에 맵핑하기 위해
                                                                            // Person 필드명을 설정하는 Tokenizer 객체가 필요하다.

        tokenizer.setNames("id", "name", "sex", "address");       // tokenizer 로 Person 의 필드명들을 설정.
        lineMapper.setLineTokenizer(tokenizer);                   // 필드명을 설정한 tokenizer 를 lineMapper 에 주입한다.

        lineMapper.setFieldSetMapper(fieldSet -> {                // setFieldSetMapper 메소드를 통해서 field 를 받고
            int id = fieldSet.readInt("id");                // CSV 파일의 숫자를 id 라는 이름으로 읽을 수 있게된다.
            String name = fieldSet.readString("name");      // 각각의 값을 읽을수 있게 한다.
            String sex = fieldSet.readString("sex");
            String address = fieldSet.readString("address");

            return new Person(id, name, sex, address);      // CSV 파일에서 읽은 값들을 Person 객체에 맵핑 하여 반환 한다.
                                                            // 여기까지가 CSV 파일과 Person 객체의 맵핑 설정.
        });

        FlatFileItemReader<Person> itemReader = new FlatFileItemReaderBuilder<Person>()
                .name("csvFileItemReader")  // itemReader 의 이름은 위와 똑같이 정해주었다.
                .encoding("UTF-8")
                .resource(new ClassPathResource("test.csv"))  // ClassPathResource 는 스프링에서 제공하는 resources 디렉토리 밑의 파일을 읽을수 있는 클래스
                .linesToSkip(1)     // CSV 파일 내의 1 번째 로우는 데이터가 아니라 필드명이 적혀있기 때문에 1 번째 줄을 skip 하라는 메소드.
                .lineMapper(lineMapper)     // 위에서 Person 객체에 맵핑 설정 한 lineMapper 를 설정 해 준다.
                .build();
        itemReader.afterPropertiesSet();    // afterPropertiesSet 메소드는 itemReader 에서 필요한 필수 설정값이 정상 설정 되었는지 검증하는 메소드

        return itemReader;
    }


    private ItemWriter<Person> itemWriter() {
        return items -> log.info(items.stream()
                .map(Person::getName)
                .collect(Collectors.joining(", ")));
    }

    private List<Person> getItems() {
        List<Person> items = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            items.add(new Person(i + 1, "test name" + i, "test sex", "test age"));
        }

        return items;
    }


}
