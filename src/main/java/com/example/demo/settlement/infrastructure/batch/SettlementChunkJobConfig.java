package com.example.demo.settlement.infrastructure.batch;

import com.example.demo.order.domain.model.Order;
import com.example.demo.order.infrastructure.persistence.OrderJpaRepository;
import com.example.demo.settlement.domain.model.SettlementItem;
import com.example.demo.settlement.infrastructure.acl.SettlementOrderTranslator;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.data.RepositoryItemReader;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.infrastructure.repeat.policy.SimpleCompletionPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SettlementChunkJobConfig {

    public static final String SETTLEMENT_CHUNK_JOB_NAME = "settlementChunkJob";
    public static final String SETTLEMENT_CHUNK_STEP_NAME = "settlementChunkStep";

    private static final int CHUNK_SIZE = 1000;

    private final EntityManagerFactory entityManagerFactory;
    private final SettlementOrderTranslator settlementOrderTranslator;

    @Bean
    public Job settlementChunkJob(JobRepository jobRepository, Step settlementChunkStep) {
        return new JobBuilder(SETTLEMENT_CHUNK_JOB_NAME, jobRepository)
                .start(settlementChunkStep)
                .build();
    }

    @Bean
    @SuppressWarnings("removal")
    public Step settlementChunkStep(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager,
                                    JpaPagingItemReader<Order> settlementOrderReader,
                                    ItemProcessor<Order, SettlementItem> settlementItemProcessor,
                                    ItemWriter<SettlementItem> settlementItemWriter) {
        return new StepBuilder(SETTLEMENT_CHUNK_STEP_NAME, jobRepository)
                .<Order, SettlementItem>chunk(new SimpleCompletionPolicy(CHUNK_SIZE), transactionManager)
                .reader(settlementOrderReader)
                .processor(settlementItemProcessor)
                .writer(settlementItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Order> settlementOrderReader(
//            OrderJpaRepository orderJpaRepository
            @Value("#{jobParameters['settlementDate']}") String settlementDate
    ) {

//        RepositoryItemReader<Order> reader = new RepositoryItemReader<>();
//        reader.setRepository(orderJpaRepository);
//        reader.setMethodName("findByStatusAndSettledFalseAndPaidAtGreaterThanEqualAndPaidAtLessThan");
//        reader.setArguments(List.of("PAID", from, to));
//        reader.setPageSize(1000);
//        reader.setSort(Map.of("paidAt", Sort.Direction.ASC));
//        return reader;

        LocalDate targetDate = settlementDate == null ? LocalDate.now() : LocalDate.parse(settlementDate);
        return new JpaPagingItemReaderBuilder<Order>()
                .name("settlementOrderReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("""
                        select o
                        from com.example.demo.order.domain.model.Order o
                        where o.status = :status
                          and o.settled = false
                          and o.paidAt >= :fromInclusive
                          and o.paidAt < :toExclusive
                        order by o.paidAt asc, o.id asc
                        """)
                .parameterValues(Map.of(
                        "status", "PAID",
                        "fromInclusive", targetDate.atStartOfDay(),
                        "toExclusive", targetDate.plusDays(1).atStartOfDay()
                ))
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean
    public ItemProcessor<Order, SettlementItem> settlementItemProcessor() {
        return order -> SettlementItem.from(settlementOrderTranslator.translate(order));
    }

    @Bean
    public ItemWriter<SettlementItem> settlementItemWriter() {
        return items -> log.info("Settlement chunk item write placeholder. itemCount={}", items.size());
    }
}
