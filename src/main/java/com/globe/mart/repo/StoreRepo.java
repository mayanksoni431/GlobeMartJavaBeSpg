package com.globe.mart.repo;

 import org.springframework.data.mongodb.repository.MongoRepository;

import com.globe.mart.beans.Store;

public interface StoreRepo extends MongoRepository<Store, String>{

}
