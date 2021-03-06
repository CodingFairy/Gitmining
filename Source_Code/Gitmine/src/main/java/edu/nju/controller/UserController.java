package edu.nju.controller;

import edu.nju.common.Const;
import edu.nju.common.SortType;
import edu.nju.common.SortTypeBuilder;
import edu.nju.entity.TblUser;
import edu.nju.entity.UserLocationEntity;
import edu.nju.model.pojo.*;
import edu.nju.model.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by cuihao on 2016/5/4.
 * this is the controller for user part
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private int totalPage = 0;

    private UserModelService userModelImpl;

    @Resource
    private UserStatsService userStatsImpl;

    @Resource
    private MapModelService mapModelService;

    @Resource
    private HobbyModelService hobbyModelImpl;

    @Resource
    private LocationModelService locationModelService;

    @Resource
    private  LongTailModelService longTailModelService;

    @Autowired
    public UserController(UserModelService userModelImpl) {
        this.userModelImpl = userModelImpl;
        this.totalPage = userModelImpl.getTotalPage();
    }

    @RequestMapping(value = "/recommend")
    @ResponseBody
    public List<UserVO> recommend(@RequestParam(required = false,defaultValue = "0") int offset,
                    @RequestParam(required = false,defaultValue = "5") int maxResults,
                    HttpSession session){

        List<UserVO> recommend;
        if (session.getAttribute("webUsername") == null){
            recommend = userModelImpl.getPopularUser(offset,maxResults);
        }

        else {
            String webUsername = (String) session.getAttribute("webUsername");
            recommend = userModelImpl.getRecommendUser(webUsername,offset,maxResults);
            HashSet<String> staredUser = (HashSet<String>) session.getAttribute("staredUser");
            for (UserVO vo:recommend){
                if (staredUser.contains(vo.getLogin())){
                    vo.setIsStared(true);
                }
            }
        }

        return recommend;
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Map list(@RequestParam int pageNum,
                    @RequestParam(required = false, defaultValue = "user_name") String sortType,
                    @RequestParam(required = false, defaultValue = "false") boolean isDesc,
                    HttpSession session){
        System.out.println("sort:==================="+sortType);
        Map<String,Object> map = new HashMap<>();
        List<UserVO> userVOs = null;
        if (pageNum<=totalPage){
            SortType type = SortTypeBuilder.getSortType(sortType);
            if (type == null){
                type = SortType.User_Name;
            }
            if (pageNum<1)  pageNum=1;
            userVOs = userModelImpl.getUsers(type,isDesc,(pageNum-1)*Const.ITEMS_PER_PAGE,Const.ITEMS_PER_PAGE);
            if (session.getAttribute("webUsername") != null){
                HashSet<String> staredUser = (HashSet<String>) session.getAttribute("staredUser");
                for (UserVO vo:userVOs){
                    if (staredUser.contains(vo.getLogin())){
                        vo.setIsStared(true);
                    }
                }
            }
        }
        map.put("totalPage", this.totalPage);
        map.put("currentPage", pageNum);
        map.put("userList", userVOs);
        return map;
    }

    @RequestMapping(value = "/search")
    @ResponseBody
    public Map getSearchResult(@RequestParam String keyword,
                                        @RequestParam(required = false, defaultValue = "user_name") String sortType,
                                        @RequestParam(required = false,defaultValue = "false") boolean reverse,
                                        @RequestParam int pageNum,
                                        @RequestParam boolean isKeyChanged,
                                        HttpSession session){
        List<UserVO> resultList = userModelImpl.getSearchResult(keyword,sortType,pageNum,reverse);
        if (session.getAttribute("webUsername") != null){
            HashSet<String> staredUser = (HashSet<String>) session.getAttribute("staredUser");
            for (UserVO vo:resultList){
                if (staredUser.contains(vo.getLogin())){
                    vo.setIsStared(true);
                }
            }
        }
        int totalSearchPage = 0;
        if (isKeyChanged){
            totalSearchPage = userModelImpl.getSearchPage(keyword);
            System.out.println("the total page is "+totalSearchPage);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("totalPage",totalSearchPage);
        map.put("currentPage",pageNum);
        map.put("userList",resultList);
        System.out.println("out!");
        return map;
    }

    @RequestMapping(value = "/{username:.+}/basic")
    @ResponseBody
    public Map getUserInfo(@PathVariable String username,
                           HttpSession session){
        Map<String,Object> map = new HashMap<>();
        UserVO userVO = userModelImpl.getUserBasicInfo(username);
        if (session.getAttribute("webUsername") != null){
            HashSet<String> staredUser = (HashSet<String>) session.getAttribute("staredUser");

            if ((staredUser!=null)&&(staredUser.contains(userVO.getLogin()))){
                userVO.setIsStared(true);
            }

        }
        RadarChart radarChart = userModelImpl.getUserRadarChart(username);
        SimpleChart languageChart = userModelImpl.getUserLanguage(username);
        List<UserLocationEntity> neighbors = this.locationModelService.getNeighbors(username);

        map.put("basicInfo",userVO);
        map.put("radarChart",radarChart);
        map.put("languageChart",languageChart);
        map.put("neighbors",neighbors);
        return map;
    }


    @RequestMapping(value = "/neighbors")
    @ResponseBody
    public List<UserLocationEntity> getNerghbors(@RequestParam String name,
                                                 @RequestParam double longitude,
                                                 @RequestParam double latitude,
                                                 HttpSession session){
        return locationModelService.getNeighbors(name,longitude,latitude);
    }

//    @RequestMapping(value = "/{username:.+}/relatedUser")
//    @ResponseBody
//    public SimpleChart getRelatedUsers(@PathVariable("username") String username,
//                                       @RequestParam(required = false,defaultValue = "30")int limitResulsts){
//        return userModelImpl.getRelatedUser(username, limitResulsts);
//    }

    @RequestMapping(value = "/{username:.+}/contribute")
    @ResponseBody
    public List<SimpleRepoVO> getUserContribute(@PathVariable("username") String username,
                                  @RequestParam(required = false,defaultValue = "5") int maxResults,
                                                HttpSession session){
        List<SimpleRepoVO> list = userModelImpl.getContributeRepo(username, maxResults);
        if (session.getAttribute("webUsername") != null){
            HashSet<String> staredRepo = (HashSet<String>) session.getAttribute("staredRepo");

            for (SimpleRepoVO simpleVO: list){
                if (staredRepo.contains(simpleVO.getOwnerName()+"/"+simpleVO.getReponame())){
                    simpleVO.setIsStared(true);
                }
            }

        }
        return list;
    }

    @RequestMapping(value = "/{username:.+}/subscribe")
    @ResponseBody
    public List<SimpleRepoVO> getUserSubscribe(@PathVariable("username") String username,
                                               @RequestParam(required = false,defaultValue = "5")int maxResults,
                                               HttpSession session){
        List<SimpleRepoVO> list = userModelImpl.getSubscribeRepo(username,maxResults);
        if (session.getAttribute("webUsername") != null){
            HashSet<String> staredRepo = (HashSet<String>) session.getAttribute("staredRepo");

            for (SimpleRepoVO simpleVO: list){
                if (staredRepo.contains(simpleVO.getOwnerName()+"/"+simpleVO.getReponame())){
                    simpleVO.setIsStared(true);
                }
            }

        }
        return list;
    }

    @RequestMapping(value = "/{username:.+}/relationGraph")
    @ResponseBody
    public List<RelationVO> getRelationGraph(@PathVariable("username") String username,
                                             @RequestParam(required = false,defaultValue = "10") int maxResults){
        return userModelImpl.getRelatedUser(username,maxResults);
    }

    @RequestMapping(value = "/statistic/type")
    @ResponseBody
    public SimpleChart statUserType(){
        return userStatsImpl.statsUserType();
    }

    @RequestMapping(value = "/statistic/public_repo")
    @ResponseBody
    public SimpleChart statUserRepos(){
        return userStatsImpl.statsUserRepo();
    }

    @RequestMapping(value = "/statistic/public_gist")
    @ResponseBody
    public SimpleChart statUserGists(){
        return userStatsImpl.statsUserGist();
    }

    @RequestMapping("/statistic/follower")
    @ResponseBody
    public SimpleChart statUserFollower(){
        return userStatsImpl.statsUserFollower();
    }

    @RequestMapping(value = "/statistic/create_at")
    @ResponseBody
    public SimpleChart statUserCreateTime(){
        return userStatsImpl.statsUserCreateTime();
    }

    @RequestMapping(value = "/statistic/email")
    @ResponseBody
    public SimpleChart statUserEmail(){
        return userStatsImpl.statsUserEmail();
    }

    @RequestMapping(value = "/statistic/company")
    @ResponseBody
    public SimpleChart statUserCompany(){
        return userStatsImpl.statsUserCompany();
    }

    @RequestMapping(value = "/statistic/twenty")
    @ResponseBody
    public long[] statTwenty(){
        return longTailModelService.twentyEightyRate();
    }

    @RequestMapping(value = "/statistic/followerLong")
    @ResponseBody
    public List<Integer> statFollowerLong(){
        return longTailModelService.longtailDistribution();
    }

    @RequestMapping(value = "/statistic/distribution")
    @ResponseBody
    public List<MapVO> statUserDistribution(){
        return mapModelService.getUserDistribution();
    }

    @RequestMapping(value = "/star")
    @ResponseBody
    public boolean starUser(@RequestParam String username, HttpSession session){
        if (session.getAttribute("webUsername") == null){
            return false;
        }
        else {
            String webUsername = session.getAttribute("webUsername").toString();
            boolean result = hobbyModelImpl.starUser(username,webUsername);
            if (result){
                HashSet<String> staredUser = (HashSet<String>) session.getAttribute("staredUser");
                staredUser.add(username);
            }
            return result;
        }
    }

    @RequestMapping(value = "/unstar")
    @ResponseBody
    public boolean unstarUser(@RequestParam String username, HttpSession session){
        if (session.getAttribute("webUsername") == null){
            return false;
        }
        else {
            String webUsername = session.getAttribute("webUsername").toString();
            boolean result = hobbyModelImpl.unStarUser(username,webUsername);
            if (result){
                HashSet<String> staredUser = (HashSet<String>) session.getAttribute("staredUser");
                staredUser.remove(username);
            }
            return result;
        }
    }
}
