package practice.springbatch.sprignbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class SprignBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SprignBatchApplication.class, args);
    }

}
