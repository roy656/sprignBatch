//package practice.springbatch.sprignbatch.quartz;
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.configuration.JobLocator;
//import org.springframework.batch.core.configuration.JobRegistry;
//import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
//import org.springframework.scheduling.quartz.JobDetailFactoryBean;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@RequiredArgsConstructor
//public class QuartzConfig {
//
//
//    private JobLauncher jobLauncher;
//
//    private JobLocator jobLocator;
//
//
//
//    @Bean
//    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
//
//        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
//        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
//
//        return jobRegistryBeanPostProcessor;
//    }
//
//    @Bean
//    public JobDetailFactoryBean jobDetailFactoryBean() {
//        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
//        jobDetailFactoryBean.setJobClass(QuartzJobLauncher.class);
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("jobName", "Job1");
//        map.put("jobLauncher", jobLauncher);
//        map.put("jobLocator", jobLocator);
//
//        jobDetailFactoryBean.setJobDataAsMap(map);
//
//        return jobDetailFactoryBean;
//    }
//
//    @Bean
//    public CronTriggerFactoryBean cronTriggerFactoryBean() {
//        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
//        cronTriggerFactoryBean.setJobDetail(jobDetailFactoryBean().getObject());
//        cronTriggerFactoryBean.setStartDelay(3000);
//        cronTriggerFactoryBean.setCronExpression("*/10 * * * * ?"); // 매 10초 마다 실행
//
//        return cronTriggerFactoryBean;
//    }
//
//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean() {
//        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
//        schedulerFactoryBean.setTriggers(cronTriggerFactoryBean().getObject());
//
//        return schedulerFactoryBean;
//    }
//}
