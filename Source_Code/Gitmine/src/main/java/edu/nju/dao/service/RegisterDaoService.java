package edu.nju.dao.service;

import edu.nju.entity.*;

import java.util.List;

/**
 * login and register
 */
public interface RegisterDaoService {
    /**
     * used in register
     * @param name
     * @return whether exist the user
     */
    public boolean existName(String name);

    /**
     * when register a new user, check that the email and username don't exist
     * @param username
     * @param email
     * @return
     */
    public boolean existUser(String username, String email);

    /**
     * login
     * @param userName
     * @param password
     * @return TblRegister
     */
    public boolean login(String userName, String password);

    /**
     * registe
     * @param userName
     * @param passWord
     * @return isSucceed
     */
    public boolean register(String userName, String passWord, String email);

    /**
     * get register labels
     * @param userName
     *      username of the register
     * @return
     *      registerLabel
     */
    public SecRegisterLabelEntity getRegisterInterest(String userName);

    /**
     * save register labels
     * @param registerLabel
     * @return
     *      is succeed.
     */
    public boolean saveOrUpdateRegisterInterest(SecRegisterLabelEntity registerLabel);

    /**
     * star for a repo
     * @param webUsername
     * @param ownername
     * @param reponame
     * @return
     */
    public boolean starRepo(String webUsername, String ownername, String reponame);

    /**
     * unstar for a repo
     * @param webUsername
     * @param ownername
     * @param reponame
     * @return
     */
    public boolean unStarRepo(String webUsername, String ownername, String reponame);

    /**
     * get the repos stared by the user before
     * @param webUsername
     * @return
     */
    public List<SecRepoEntity> getStaredRepos(String webUsername, int offset, int maxResults);

    /**
     * get the count of a web user stared repos
     * @param webUsername
     * @return
     */
    public long getStaredRepoCount(String webUsername);

    public boolean starUser(String username, String webUsername);

    public boolean unStarUser(String useraname, String webUsername);

    public List<SecUserEntity> getStaredUsers(String webUsername, int offset, int maxResults);

    public long getStaredUserCount(String webUsername);

    public List<Object[]> getStaredReponame(String webUsername);

    public List<String> getStaredUsername(String webUsername);
}
