package com.microsoft.learn.azure.cosmosdb.nosql.quickstart;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;

@Configuration
@EnableCosmosRepositories
public class CosmosConfiguration extends AbstractCosmosConfiguration {

    @Value("#{environment.AZURE_COSMOS_DB_NOSQL_ENDPOINT}")
    private String uri;

    @Bean
    public CosmosClientBuilder getCosmosClientBuilder() {
        // <create_client>
        DefaultAzureCredential azureTokenCredential = new DefaultAzureCredentialBuilder()
            .build();
            
        return new CosmosClientBuilder()
            .endpoint(uri)
            .credential(azureTokenCredential);
        // </create_client>
    }

    @Override
    public CosmosConfig cosmosConfig() {
        return CosmosConfig.builder()
            .enableQueryMetrics(true)
            .build();
    }

    @Override
    protected String getDatabaseName() {
        return "cosmicworks";
    }
}
