package com.getAcessToken.demo.service;

import com.getAcessToken.demo.entity.ChildrenData;
import com.getAcessToken.demo.repository.ChildrenDataRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Queue;

@Service
public class ChildrenDataService {
    private ChildrenDataRepository childrenDataRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ChildrenDataService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void postToReddit(String accessToken, String subreddit, String title, String content, String kind) {
        try {
            String url = "https://oauth.reddit.com/api/submit";

            String requestBody  = "sr="+subreddit+"&title="+title+"&text="+content+"&kind="+kind;
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
//            headers.put("User-Agent", Collections.singletonList("tomcat:com.getAccessToken.demo.DemoApplication:v1.0 (by /u/Overall-Dimension354)"));
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            System.out.println("Post submitted successfully!");
        } catch (Exception e) {
            // Handle exception or error response
            e.printStackTrace();
        }
    }

    public String addData(ChildrenData childrenData){
        try{
            childrenDataRepository.save(childrenData);
            return "Added Successfully";
        } catch (Exception e){
            e.printStackTrace();
            return "Failed with the error " + e;
        }
    }

    public String addAllData(List<ChildrenData> childrenDataList){
        try{
            childrenDataRepository.saveAll(childrenDataList);
            return "Added Successfully";
        } catch (Exception e){
            e.printStackTrace();
            return "Failed with the error " + e;
        }
    }

    public List<ChildrenData> findAll(){
        return childrenDataRepository.findAll();
    }

    public ChildrenData findById(String Id){
        return childrenDataRepository.findById(Id).get();
    }

    public List<ChildrenData> findByTitle(String title){
        return childrenDataRepository.findByTitle(title);
    }



    public List<ChildrenData> findByPattern(String pattern){
        Query query = new Query();
        query.addCriteria(Criteria.where("title").regex(pattern));
        return mongoTemplate.find(query,ChildrenData.class);
    }

    public List<ChildrenData> sortBySubredditSubscribers(){
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC,"subredditSubscribers"));
        return mongoTemplate.find(query, ChildrenData.class);
    }

    public String updateChildrenData(ChildrenData childrenData){
        ChildrenData existingChildrenData = childrenDataRepository.findById(childrenData.getId()).get();
        BeanUtils.copyProperties(childrenData,existingChildrenData);
        return "Updated";
    }

    public ChildrenData deleteChildrenData(String Id){
        ChildrenData childrenData = childrenDataRepository.findById(Id).get();
        childrenDataRepository.deleteById(Id);
        return childrenData;
    }

    public List<ChildrenData> deleteChildrenDataByTitle(String title){
        List<ChildrenData> childrenDataList = childrenDataRepository.findByTitle(title);
        for(ChildrenData childrenData : childrenDataList){
            childrenDataRepository.deleteById(childrenData.getId());
        }
        return childrenDataList;
    }
}
