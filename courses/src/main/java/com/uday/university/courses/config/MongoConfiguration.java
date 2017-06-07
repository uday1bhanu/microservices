/**
 * 
 */
package com.uday.university.courses.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author udaybhanuprasad
 *
 */
@Configuration
@EnableMongoRepositories(basePackages={"com.uday.university.courses.repository"})
public class MongoConfiguration {

}
