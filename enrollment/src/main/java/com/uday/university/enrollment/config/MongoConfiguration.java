/**
 * 
 */
package com.uday.university.enrollment.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author udaybhanuprasad
 *
 */
@Configuration
@EnableMongoRepositories(basePackages={"com.uday.university.enrollment.repository"})
public class MongoConfiguration {

}
