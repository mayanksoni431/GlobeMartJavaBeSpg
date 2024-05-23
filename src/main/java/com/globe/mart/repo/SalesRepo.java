package com.globe.mart.repo;

// import org.springframework.data.mongodb.repository.MongoRepository;

import com.globe.mart.beans.Sales;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SalesRepo extends MongoRepository<Sales, String> {

}
