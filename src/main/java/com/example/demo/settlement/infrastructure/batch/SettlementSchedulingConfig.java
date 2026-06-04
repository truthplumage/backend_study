package com.example.demo.settlement.infrastructure.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.EnableJdbcJobRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableBatchProcessing
@EnableJdbcJobRepository(databaseType = "postgres")
@EnableScheduling
public class SettlementSchedulingConfig {
}
