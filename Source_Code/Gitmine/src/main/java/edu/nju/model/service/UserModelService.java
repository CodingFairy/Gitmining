package edu.nju.model.service;

import edu.nju.common.SortType;
import edu.nju.entity.TblUser;
import edu.nju.model.pojo.*;

import java.util.List;

/**
 * Created by Harry on 2016/5/12.
 * this class is the model part of MVC for github user
 */
public interface UserModelService {

    /**
     * recommend user for the logined web user
     * @param webUsername
     * @return
     */
    public List<UserVO> getRecommendUser(String webUsername,int offset,int maxResults);

    /**
     * get the user similar to the given user
     * @param username
     * @return
     */
    public List<RelationVO> getRelatedUser(String username, int limitResults);

    /**
     * get the contribute repositories for the given user
     * @param username
     * @return
     */
    public List<SimpleRepoVO> getContributeRepo(String username, int maxResults);

    /**
     * get the subscribe repos for the given user
     * @param username
     * @return
     */
    public List<SimpleRepoVO> getSubscribeRepo(String username,int maxResults);

    /**
     * get the user has the most followers
     * @return
     */
    public List<UserVO> getPopularUser(int offset,int maxResults);

    /**
     * get the total page count of all users
     * @return
     */
    public int getTotalPage();

    public int getSearchPage(String keyword);

    /**
     * get the user list
     * @param sortType
     * @param isDesc
     * @param offset
     * @param maxNum
     * @return
     */
    public List<UserVO> getUsers(SortType sortType, boolean isDesc, int offset, int maxNum);

    /**
     * get the user(s) whose name matches the keyword<br/>
     * the function can only search name of a user, not the bio or other information.
     * @param keyword
     * @return
     */
    public List<UserVO> getSearchResult(String keyword, String sortType, int pageNum, boolean reverse);

    /**
     * get the basic information of a user, which means the name, email, etc.
     * @param username
     * @return
     */
    public UserVO getUserBasicInfo(String username);

    /**
     * get the data for radar chart of a user
     * @param username
     * @return
     */
    public RadarChart getUserRadarChart(String username);

    /**
     * get the frequent usage of language for a user
     * @param login
     * @return
     */
    public SimpleChart getUserLanguage(String login);

}
