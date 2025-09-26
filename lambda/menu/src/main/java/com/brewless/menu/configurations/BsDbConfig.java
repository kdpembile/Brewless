package com.brewless.menu.configurations;

import com.mongodb.ConnectionString;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(
    basePackages = {"com.brewless.menu.repositories.bs"},
    reactiveMongoTemplateRef = BsDbConfig.MONGO_BS_TEMPLATE
)
public class BsDbConfig {

  public static final String MONGO_BS_TEMPLATE = "bsMongoTemplate";

  public static final String BEAN_NAME_MONGO_BS_PROPERTIES = "bsProperties";

  public static final String MONGO_BS_PREFIX = "spring.data.mongodb.bs";

  public static final String BEAN_NAME_MONGO_ENT_FACTORY = "bsMongoDatabaseFactory";

  @Primary
  @Bean(name = BEAN_NAME_MONGO_BS_PROPERTIES)
  @ConfigurationProperties(prefix = MONGO_BS_PREFIX)
  public MongoProperties getBsProps() {
    return new MongoProperties();
  }

  @Primary
  @Bean(name = BEAN_NAME_MONGO_ENT_FACTORY)
  public ReactiveMongoDatabaseFactory bsMongoDatabaseFactory() {
    return new SimpleReactiveMongoDatabaseFactory(new ConnectionString(getBsProps().getUri()));
  }

  @Primary
  @Bean(name = MONGO_BS_TEMPLATE)
  public ReactiveMongoTemplate bsMongoTemplate() {
    return new ReactiveMongoTemplate(bsMongoDatabaseFactory());
  }

}
