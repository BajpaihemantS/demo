package com.getAcessToken.demo.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getAcessToken.demo.entity.Children;
import com.getAcessToken.demo.entity.ChildrenData;
import com.getAcessToken.demo.entity.DataResponse;
import com.getAcessToken.demo.service.ChildrenDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/")
public class ChildrenDataController {
    private ChildrenDataService childrenDataService;

    @Autowired
    public ChildrenDataController(ChildrenDataService childrenDataService) {
        this.childrenDataService = childrenDataService;
    }

    @GetMapping()
    public String setDataResponse() throws IOException {

        Scanner sc =  new Scanner(System.in);
        System.out.println("Enter the subreddit");
        String s = sc.next();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String authToken = getAuthToken();
        System.out.println(authToken);
        String url = "https://oauth.reddit.com/r/"+s+"?limit=5&count=5";
        headers.setBearerAuth(authToken);
        headers.put("User-Agent", Collections.singletonList("tomcat:com.getAccessToken.demo.DemoApplication:v1.0 (by /u/Overall-Dimension354)"));
        HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        ObjectMapper om = new ObjectMapper();
        try{
            DataResponse dataResponse = om.readValue(response.getBody(), DataResponse.class);
            List<Children> childrenList = dataResponse.getData().getChildren();
            List<ChildrenData> childrenDataList = childrenList.stream().map(Children::getData).toList();
            createChildrenDataList(childrenDataList);
            for(ChildrenData childrenData : childrenDataList) System.out.println(childrenData.getSubredditSubscribers());
            return "Data Copied Successfully";
        } catch (Exception e) {
            return "Data Copying successfully Failed";
        }
    }

    @PostMapping()
    private String getAuthToken(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //Different login details as I had to re-create the app
        headers.setBasicAuth("gTZ-HoY36VB00kPKl6Wnnw", "CGFSOa1LiSyFsie1w7dwWhLtmyG28Q");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.put("User-Agent", Collections.singletonList("tomcat:com.getAccessToken.demo.DemoApplication:v1.0 (by /u/Overall-Dimension354)"));
        String body = "grant_type=password&username=Overall-Dimension354&password=hemanthemant";
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        String authUrl = "https://www.reddit.com/api/v1/access_token";
        ResponseEntity<String> response = restTemplate.postForEntity(authUrl, request, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        try {
            map.putAll(mapper.readValue(response.getBody(), new TypeReference<Map<String,Object>>(){}));
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Failed with the error " + e);
        }
//        System.out.println(response.getBody());
        return String.valueOf(map.get("access_token"));
    }

    @PostMapping("/post")
    public String makePost(){
        Scanner sc = new Scanner(System.in);

        String authToken = getAuthToken();
        System.out.println("Please enter Subreddit, then title, then content.");
        String subreddit = sc.nextLine();
        String title = sc.nextLine();
        String content = sc.nextLine();
        String kind = "self";
        try {
            childrenDataService.postToReddit(authToken, subreddit, title, content, kind);
            return "Content added successfully";
        } catch (Exception e){
            e.printStackTrace();
            return "Content addition failed successfully";
        }
    }

    @PostMapping("/ChildrenDataList")
    @ResponseStatus(HttpStatus.CREATED)
    public String createChildrenData(@RequestBody ChildrenData childrenData){
        return childrenDataService.addData(childrenData);
    }

    @PostMapping("/ChildrenData")
    @ResponseStatus(HttpStatus.CREATED)
    public String createChildrenDataList(@RequestBody List<ChildrenData> childrenDataList){
        return childrenDataService.addAllData(childrenDataList);
    }

    @GetMapping("/ChildrenData")
    public List<ChildrenData> findAllChildrenData(){
        return childrenDataService.findAll();
    }

    @GetMapping("/ChildrenData/Sort")
    public List<ChildrenData> sortedData(){
        return childrenDataService.sortBySubredditSubscribers();
    }

    @GetMapping("/ChildrenData/title/{title}")
    public List<ChildrenData> findAllByTitle(@PathVariable String title){
        return childrenDataService.findByTitle(title);
    }

    @GetMapping("/ChildrenData/pattern/{pattern}")
    public List<ChildrenData> findAllByPattern(@PathVariable String pattern){
        return childrenDataService.findByPattern(pattern);
    }

    @DeleteMapping("/ChildrenData/{title}")
    public List<ChildrenData> deleteByTitle(@PathVariable String title){
        return childrenDataService.deleteChildrenDataByTitle(title);
    }


    @GetMapping("/ChildrenData/{Id}")
    public ChildrenData findChildrenDataById(@PathVariable String Id){
        return childrenDataService.findById(Id);
    }

    @PutMapping("/ChildrenData")
    public String updateChildrenData(@RequestBody ChildrenData childrenData){
        return childrenDataService.updateChildrenData(childrenData);
    }

    @DeleteMapping("/ChildrenData")
    public ChildrenData deleteChildrenData(@PathVariable String Id){
        return childrenDataService.deleteChildrenData(Id);
    }
}
