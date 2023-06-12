package com.getAcessToken.demo.repository;

import com.getAcessToken.demo.entity.ChildrenData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChildrenDataRepository extends MongoRepository<ChildrenData,String> {
    List<ChildrenData> findByTitle(String title);
}
