package com.mappy.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.Mongo;

@Configuration
public class AppConfig {

	/*
	 * Factory bean that creates the com.mongodb.Mongo instance
	 */
	/*
	 * public @Bean MongoFactoryBean mongo() { MongoFactoryBean mongo = new
	 * MongoFactoryBean(); mongo.setHost("localhost"); return mongo; }
	 * 
	 * public @Bean MongoTemplate mongoTemplate() throws Exception { return new
	 * MongoTemplate(mongoDbFactory()); }
	 */

	public @Bean MongoDbFactory mongoDbFactory() throws Exception {
		//UserCredentials userCredentials = new UserCredentials("joe", "secret");
		/*return new SimpleMongoDbFactory(new Mongo(), "database",
				userCredentials);*/
		return new SimpleMongoDbFactory(new Mongo(), "database");
	}

	public @Bean
	MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongoDbFactory());
	}
}