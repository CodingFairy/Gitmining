package edu.nju.git.data.rmiservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import edu.nju.git.PO.RepoPO;
import edu.nju.git.VO.BranchVO;
import edu.nju.git.VO.CommitVO;
import edu.nju.git.VO.IssueVO;
import edu.nju.git.VO.RepoBriefVO;
import edu.nju.git.VO.RepoVO;
import edu.nju.git.VO.UserBriefVO;
import edu.nju.git.datavisitors.repovisitors.RepoVisitor;
import edu.nju.git.type.SortType;

/**
 * The interface defines all the service that RepoData module must provide.
 * <p>
 * Repo data module gets data from outer api, and provide it to business logic module.
 * <p>
 *
 * @author benchaodong
 * @date 2016-03-03 14:20:49
 */
public interface RepoDataRMIService extends Remote{
    /**
     * Get repositories meeting demands of a search.
     * @param regex :
     * 			the regex representation of the keyword you search
     * @return List of {@link RepoBriefVO}:
     * 			brief info of a repository
     */
    public List<RepoBriefVO> getSearchResult(String regex) throws RemoteException;

    /**
     * get the total count of repositories.
     * @return the number of repositories
     */
    public int getTotalCount()throws RemoteException;

    /**
     * get the user po list in the order specified by parameter <tt>sortType</tt>
     * @param sortType which type of list to get
     * @return the reference to the list
     */
    public List<RepoPO> getRepoPOs(SortType sortType)throws RemoteException;

    /**
     * use a visitor to access the data and return the wanted value.
     * @see RepoVisitor
     * @param visitor the visitor
     * @return list of repo vo
     */
    public List<RepoBriefVO> acceptVisitor(RepoVisitor visitor)throws RemoteException;

    /**
     * Get <b>detailed</b> info of a repository.
     * @param owner
     * 			name of owner of the repository
     * @param repoName
     * 			name of the repository
     * @return {@link RepoVO}:
     * 			detailed info of a repository
     */
    public RepoVO getRepoBasicInfo(String owner, String repoName)throws RemoteException;

    /**
     * Get brief info of contributors.
     * @param owner
     * 			name of the owner of the repository
     * @param repoName
     * 			name of the repository
     * @return list of {@link String}
     * 			list of name of contributors.
     * 			The return value will be null if the is no such repository.
     */
    public List<String> getRepoContributor(String owner, String repoName)throws RemoteException;

    /**
     * Get brief info of collaborators.
     * @param owner
     * 			name of the owner of the repository
     * @param repoName
     * 			name of the repository
     * @return list of {@link UserBriefVO}
     * 			list of brief info of collaborators.
     */
    public List<String> getRepoCollaborator(String owner, String repoName)throws RemoteException;

    /**
     * get all subscribers' names of the given repo
     * @param owner
     * @param repoName
     * @return
     * @throws RemoteException
     */
    public List<String> getRepoSubscriber(String owner, String repoName) throws RemoteException;

    /*
     * Get {@code List} of info of branches.
     *
     * @param owner
     * 			name of the owner of the repository
     * @param repoName
     * 			name of the repository
     * @return list of {@link BranchVO}
     * 			list of info of branches.
     */
//    public List<BranchVO> getRepoBranch(String owner, String repoName)throws RemoteException;

    /*
     * Get {@code List} of repositories which fork the project.
     * @param owner
     * 			name of the owner of the repository
     * @param repoName
     * 			name of the repository
     * @return list of {@link RepoBriefVO}
     * 			list of brief info of repositories.
     */
//    public List<RepoBriefVO> getRepoFork(String owner, String repoName)throws RemoteException;

    /*
     * Get {@code List} of commit info of the repository.
     * @param owner
     * 			name of the owner of the repository
     * @param repoName
     * 			name of the repository
     * @return list of {@link CommitVO}
     * 			list of info of commits.
     */
//    public List<CommitVO> getRepoCommit(String owner, String repoName)throws RemoteException;

    /*
     * Get {@code List} of issues of the repository.
     * @param owner
     * 			name of the owner of the repository
     * @param repoName
     * 			name of the repository
     * @return list of {@link IssueVO}
     * 			list of info of issues.
     */
//    public List<IssueVO> getRepoIssue(String owner, String repoName)throws RemoteException;
}
