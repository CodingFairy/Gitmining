package org.GitServer.dataread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.GitServer.cacheinit.DataEncapsulation;

import edu.nju.git.PO.RepoPO;
import edu.nju.git.PO.UserPO;
import edu.nju.git.VO.chartvo.MyChartVO;
import edu.nju.git.VO.chartvo.RepoCollaChartVO;
import edu.nju.git.VO.chartvo.RepoContriChartVO;
import edu.nju.git.VO.chartvo.RepoCreateTimeChartVO;
import edu.nju.git.VO.chartvo.RepoForkChartVO;
import edu.nju.git.VO.chartvo.RepoLanguChartVO;
import edu.nju.git.VO.chartvo.RepoSizeChartVO;
import edu.nju.git.VO.chartvo.RepoStarChartVO;
import edu.nju.git.VO.chartvo.RepoSubscChartVO;
import edu.nju.git.VO.chartvo.UserCollaRepoChartVO;
import edu.nju.git.VO.chartvo.UserCompanyChartVO;
import edu.nju.git.VO.chartvo.UserContriRepoChartVO;
import edu.nju.git.VO.chartvo.UserCreateTimeChartVO;
import edu.nju.git.VO.chartvo.UserEmailChartVO;
import edu.nju.git.VO.chartvo.UserFollowerChartVO;
import edu.nju.git.VO.chartvo.UserGistChartVO;
import edu.nju.git.VO.chartvo.UserOwnRepoChartVO;
import edu.nju.git.VO.chartvo.UserSubscrRepoChartVO;
import edu.nju.git.VO.chartvo.UserTypeChartVO;
import edu.nju.git.comparators.repocomparators.po.RepoPOForkComparator;
import edu.nju.git.comparators.repocomparators.po.RepoPONameComparator;
import edu.nju.git.comparators.repocomparators.po.RepoPOStarComparator;
import edu.nju.git.comparators.repocomparators.po.RepoPOSubscrComparator;
import edu.nju.git.comparators.repocomparators.po.RepoPOUpdateComparator;
import edu.nju.git.comparators.usercomparators.po.UserPOFollowerComparator;
import edu.nju.git.comparators.usercomparators.po.UserPOFollowingComparator;
import edu.nju.git.comparators.usercomparators.po.UserPONameComparator;
import edu.nju.git.comparators.usercomparators.po.UserPORepoNumComparator;
import edu.nju.git.type.MostType;

/**
 * read local data form txt file.
 *
 * When the server is launched, it read all data from disk and compute some(actually very much!) second-hand data
 *
 * @author daixinyan
 * @date 2016-03-20
 */
public class ReaderAndCount {

	private static ReaderAndCount uniqueInstance = null;

	public static ReaderAndCount instance(){
		if (uniqueInstance == null) {
			uniqueInstance = new ReaderAndCount();
		}
		return uniqueInstance;
	}

	private ReaderAndCount() {
		loadAllData();
	}

	private List<RepoPO> nameOrderRepoPOs;
	private List<RepoPO> starOrderRepoPOs;
	private List<RepoPO> forkOrderRepoPOs;
	private List<RepoPO> subscrOrderRepoPOs;
    private List<RepoPO> updateOrderRepoPOs;

	private List<UserPO> nameOrderUsers;
	private List<UserPO> followerOrderUsers;
    private List<UserPO> repoNumOrderUsers;
	private List<UserPO> followingOrderUsers;

	private RepoPO mostSizeRepo;
	private RepoPO mostActivityRepo;
	private RepoPO mostComplexRepo;
	private RepoPO mostPopularRepo;
	private RepoPO mostContrbuteRepo;
	private RepoPO mostCollaborateRepo;

	private UserPO mostValueUser;
	private UserPO mostActivityUser;
	private UserPO mostGistUser;

	private List<UserPO> allUserPOs;

	/**
	 * new add 
	 * @
	 */
	private Map<String,Map<String, Integer>> repoLanguages;
	
	private Map<String, RepoPO>  nameToRepo ;
	private Map<String, UserPO>  nameToUser ;

	private Map<String, List<String>> userToOwnerRepo ;
	private Map<String, List<String>> userToCollabRepo ;
	private Map<String, List<String>> userToContribute ;
	private Map<String, List<String>> userToSubscribeRepo ;

	private Map<String, List<String>> repoToContributor ;
	private Map<String, List<String>> repoToCollab ;
	private Map<String, List<String>> repoToSubscriber;
	private Map<String, List<String>> repoToCommit;
	private Map<String, List<String>> repoToIssue;
	private Map<String, List<String>> repoToPull;

	private RepoCreateTimeChartVO repoCreateTime;	//for create time bar chart
	private RepoSizeChartVO repoSize;
	private RepoForkChartVO forkCount;	//for fork times of a repo bar chart
	private RepoStarChartVO starCount;	//for star number of a repo bar chart
	private RepoContriChartVO repoContributors;//number of repo's contributors
	private RepoCollaChartVO repoCollabrators;//
	private RepoSubscChartVO repoSubscribers;
	private RepoLanguChartVO language;		//for language pie chart

	private UserTypeChartVO statUserType;  //for user type pie chart
	private UserCreateTimeChartVO statUserCreateTime;	//for user create time  line chart
	private UserOwnRepoChartVO statUserOwnRepo;	//for the number of user owns repos bar chart
	private UserCollaRepoChartVO statUserCollaborateRepo;//for the number of user collaborate repos bar chart
	private UserContriRepoChartVO statUserContributorRepo;
	private UserSubscrRepoChartVO statUserSubscriberRepo;
	private UserGistChartVO statUserGist;

	//the following two charts must user there
	private UserCompanyChartVO statCompanyUser;	//for how many users a company has bar chart
	private UserEmailChartVO statUserEmail;

	private UserFollowerChartVO statUserFolllowers;

	/**
	 * this method load all data from disk.
	 * <p>
	 *     there are 8 lists variables in this class, but only two list is to be load from disk,<br>
	 *         other lists is resorted by this two list.
	 * </p>
	 * <p>
	 *     the map <tt>nameToRepo</tt> and map <tt>nameToUser</tt> is generated from list.
	 * </p>
	 * <p>
	 *     all the MyChartVOs are calculated by lists and maps loaded from disk.
	 * </p>
	 *
	 */
	public void loadAllData() {
		System.out.println("read txt file start!========");
		DataEncapsulation allData = new Reader().excute();
		System.out.println("read txt file end!========");

		this.nameOrderRepoPOs = allData.nameOrderRepoPOs;
		this.nameOrderUsers = allData.nameOrderUserPOs;

		this.allUserPOs = allData.allUserPOs;

		this.repoLanguages = allData.repoLanguages;
		
		this.userToOwnerRepo = allData.userToOwnerRepo;
		this.userToContribute = allData.userToContribute;
		this.userToCollabRepo = allData.userToCollabRepo;
		this.userToSubscribeRepo = allData.userToSubscribeRepo;

		this.repoToCollab = allData.repoToCollab;
		this.repoToContributor = allData.repoToContributor;
		this.repoToSubscriber = allData.repoToSubscriber;

		this.repoToCommit = allData.repoToCommit;
		this.repoToPull = allData.repoToPull;
		this.repoToIssue = allData.repoToIssue;

		//the 4 methods below can not reverse!!!!!!
		System.out.println("set count start!========");
		setCounts();

		System.out.println("sort list start!========");
		sortList();

		System.out.println("generate map start!========");
		generateMap();

		System.out.println("set user value start!========");
		setUserValue();

		System.out.println("initChart start!========");
		initChart();
		System.out.println("initChart end!========");

		setMost();
	}

	/**
	 * this method traverse all repo and user to generate the bar chart, pie chart and so on
	 */
	private void initChart() {
		repoCreateTime = new RepoCreateTimeChartVO();
		repoSize = new RepoSizeChartVO();
		forkCount = new RepoForkChartVO();
		starCount = new RepoStarChartVO();
		repoContributors = new RepoContriChartVO();
		repoCollabrators = new RepoCollaChartVO();
		repoSubscribers = new RepoSubscChartVO();
		language = new RepoLanguChartVO();

		statUserType = new UserTypeChartVO();
		statUserCreateTime = new UserCreateTimeChartVO();
		statUserOwnRepo = new UserOwnRepoChartVO();
		statUserCollaborateRepo = new UserCollaRepoChartVO();
		statUserContributorRepo = new UserContriRepoChartVO();
		statUserSubscriberRepo = new UserSubscrRepoChartVO();
		statUserGist = new UserGistChartVO();
		statUserFolllowers = new UserFollowerChartVO();
		statUserEmail = new UserEmailChartVO();
		statCompanyUser = new UserCompanyChartVO();

		for (RepoPO repo : nameOrderRepoPOs) {
			repoCreateTime.increase(repo.getCreate_at().substring(0,4));//get the year of create time
			repoSize.increase(repo.getSize());
			forkCount.increase(repo.getNum_forks());
			starCount.increase(repo.getNum_stars());
			repoContributors.increase(repo.getNum_contrbutors());
			repoCollabrators.increase(repo.getNum_collaborators());
			repoSubscribers.increase(repo.getNum_subscribers());
			language.increase(repo.getLanguage());
		}

		for (UserPO user : allUserPOs) {
			statUserType.increase(user.getType());
			statUserCreateTime.increase(user.getCreate_at().substring(0,4));
			statUserOwnRepo.increase(user.getPublic_repos());

			statUserGist.increase(user.getPublic_gists());
			statUserFolllowers.increase(user.getFollowNum());
			statUserEmail.increase(user.getEmail());
			statCompanyUser.increase(user.getCompany());
		}

		for (UserPO user: nameOrderUsers) {
			statUserCollaborateRepo.increase(user.getNum_collaborate());
			statUserContributorRepo.increase(user.getNum_contribute());
			statUserSubscriberRepo.increase(user.getNum_subscribe());
		}

		//attention! the two method can not delete!
		statUserEmail.sortAndSetArray();
		statCompanyUser.sortAndSetArray();
	}

	/**
	 * this method set user value by traverse all user to his own and collaborate repos' value
	 */
	private void setUserValue() {
		// need traverse all user to his own and collaborate repos' value
		for (UserPO userPO : nameOrderUsers) {
			double value = 0;
			int repoCount = 0;
			List<String> userOwnRepos = userToOwnerRepo.get(userPO.getName());
			if (userOwnRepos != null) {
				for (String repoName : userOwnRepos) {

					RepoPO repo = nameToRepo.get(repoName);
					if (repo != null) {
						value += repo.getRepoValue();
						repoCount ++;
					}
					else {
						System.out.println("impossible!! no repo for "+repoName);
					}
				}
			}
			else {
				System.out.println("no own repos map for user "+userPO.getName());
			}

			List<String> userCollaRepos = userToCollabRepo.get(userPO.getName());
			if (userCollaRepos != null) {
				for (String repoName : userCollaRepos) {

					RepoPO repo = nameToRepo.get(repoName);
					if (repo != null) {
						value += repo.getRepoValue();
						repoCount ++;
					}
					else {
						System.out.println("impossible!! no repo for "+repoName);
					}
				}
			}
			else {
				System.out.println("no collaborate repos map for user "+userPO.getName());
			}

			//set the mean of all repo value to user value
			userPO.setUserValue(value/repoCount);
		}
	}

	/**
	 * this method set num_contributors, num_collaborators, num_commits, num_pulls, num_issues of <br>
	 *     a RepoPO. set num_subscribe, num_contribute, num_collaborate, userValue.
	 * <p>
	 *     all the above value is counted each time the server launches.
	 * </p>
	 */
	private void setCounts() {
		// count repo
		for (RepoPO repoPO : nameOrderRepoPOs) {
			String repoID = repoPO.getOwnerName()+"/"+repoPO.getName();

			List<String> repoToContriList = repoToContributor.get(repoID);
			if (repoToContriList !=null) {
				repoPO.setNum_contrbutors(repoToContriList.size());
			}
			else { 		//there is no such repo in the map
				System.out.println("no element in repo to contributor map for "+repoID);
			}

			List<String> repoToCollaList = repoToCollab.get(repoID);
			if (repoToCollaList != null) {
				repoPO.setNum_collaborators(repoToCollaList.size());
			}
			else {
				System.out.println("no element for in repo to colla map for "+repoID);
			}

			List<String> repoToCommitList = repoToCommit.get(repoID);
			if (repoToCommitList != null) {
				repoPO.setNum_commits(repoToCommitList.size());
			}
			else {
				System.out.println("no element for in repo to commit map for "+repoID);
			}

			List<String> repoToIssueList = repoToIssue.get(repoID);
			if (repoToIssueList != null) {
				repoPO.setNum_commits(repoToIssueList.size());
			}
			else {
				System.out.println("no element for in repo to issue map for "+repoID);
			}

			List<String> repoToPullList = repoToPull.get(repoID);
			if (repoToPullList != null) {
				repoPO.setNum_commits(repoToPullList.size());
			}
			else {
				System.out.println("no element for in repo to pull map for "+repoID);
			}
		}	//end count repo

		//count user
		for (UserPO userPO : nameOrderUsers) {
			String userID = userPO.getName();

			List<String> userToSubscrList = userToSubscribeRepo.get(userID);
			if (userToSubscrList != null) {
				userPO.setNum_subscribe(userToSubscrList.size());
			}
			else {
				System.out.println("no element for in user to subscri map for "+userID);
			}

			List<String> userToContriList = userToContribute.get(userID);
			if (userToContriList != null) {
				userPO.setNum_contribute(userToContriList.size());
			}
			else {
				System.out.println("no element for in user to contribute map for "+userID);
			}

			List<String> userToCollaList = userToCollabRepo.get(userID);
			if (userToCollaList != null) {
				userPO.setNum_collaborate(userToCollaList.size());
			}
			else {
				System.out.println("no element for in user to collab map for "+userID);
			}

		}	//end count user

	}

	/**
	 * sort all list with different comparators
	 */
	private void sortList() {
		//sort repo list
		this.starOrderRepoPOs = (ArrayList<RepoPO>)((ArrayList)nameOrderRepoPOs).clone();
		starOrderRepoPOs.sort(new RepoPOStarComparator());
		this.forkOrderRepoPOs = (ArrayList<RepoPO>)((ArrayList)nameOrderRepoPOs).clone();
		forkOrderRepoPOs.sort(new RepoPOForkComparator());
		this.subscrOrderRepoPOs = (ArrayList<RepoPO>)((ArrayList)nameOrderRepoPOs).clone();
		subscrOrderRepoPOs.sort(new RepoPOSubscrComparator());
		this.updateOrderRepoPOs = (ArrayList<RepoPO>)((ArrayList)nameOrderRepoPOs).clone();
		updateOrderRepoPOs.sort(new RepoPOUpdateComparator());
		nameOrderRepoPOs.sort(new RepoPONameComparator());

		//sort user list
		this.followerOrderUsers = (ArrayList<UserPO>)((ArrayList)nameOrderUsers).clone();
		followerOrderUsers.sort(new UserPOFollowerComparator());
		this.repoNumOrderUsers = (ArrayList<UserPO>)((ArrayList)nameOrderUsers).clone();
		repoNumOrderUsers.sort(new UserPORepoNumComparator());
		this.followingOrderUsers = (ArrayList<UserPO>)((ArrayList)nameOrderUsers).clone();
		followingOrderUsers.sort(new UserPOFollowingComparator());
		nameOrderUsers.sort(new UserPONameComparator());

	}

	/**
	 * this method generate map from existing list
	 */
	private void generateMap(){
		nameToRepo = new HashMap<>();
		nameToUser = new HashMap<>();

		StringBuilder builder = new StringBuilder();
		for (RepoPO repoPO: nameOrderRepoPOs) {
			builder.append(repoPO.getOwnerName());
			builder.append("/");
			builder.append(repoPO.getName());
			nameToRepo.put(builder.toString(), repoPO);
			builder.setLength(0);	//clear the string builder
		}

		for (UserPO userPO: nameOrderUsers) {
			nameToUser.put(userPO.getName(), userPO);
		}
	}

	public RepoPO getMostRepo(MostType type) {
		switch (type) {
			case REPO_SIZE:return mostSizeRepo;
			case REPO_ACTIVITY:return mostActivityRepo;
			case REPO_COMPLEXITY:return mostComplexRepo;
			case REPO_POPULARITY:return mostPopularRepo;
			case REPO_CONTRIBUTOR:return mostContrbuteRepo;
			case REPO_COLLABORATOR:return mostCollaborateRepo;
			default:return null;
		}
	}

	public UserPO getMostUser(MostType type) {
		switch (type) {
			case USER_VALUE:return mostValueUser;
			case USER_ACTIVITY:return mostActivityUser;
			case USER_GIST:return mostGistUser;
			default:return null;
		}
	}

	private void setMost() {
		if (nameOrderRepoPOs.size()>0){
			mostSizeRepo = mostActivityRepo = mostComplexRepo = mostPopularRepo
					= mostContrbuteRepo = mostCollaborateRepo = nameOrderRepoPOs.get(0);
			for (RepoPO po : nameOrderRepoPOs) {
				if (mostSizeRepo.getSize()<po.getSize())
					mostSizeRepo = po;
				if (mostActivityRepo.getRepoActivity()<po.getRepoActivity())
					mostActivityRepo = po;
				if (mostComplexRepo.getComplexity()<po.getComplexity())
					mostComplexRepo = po;
				if (mostPopularRepo.getPopular()<po.getPopular())
					mostPopularRepo = po;
				if (mostContrbuteRepo.getNum_contrbutors()<po.getNum_contrbutors())
					mostContrbuteRepo = po;
				if (mostCollaborateRepo.getNum_collaborators()<po.getNum_collaborators())
					mostCollaborateRepo = po;
			}
		}

		if (nameOrderUsers.size()>0) {
			mostValueUser = mostActivityUser = mostGistUser = nameOrderUsers.get(0);
			for (UserPO po : nameOrderUsers) {
				if (mostValueUser.getUserValue()<po.getUserValue())
					mostValueUser = po;
				if (mostActivityUser.getUserActivity()<po.getUserActivity())
					mostActivityUser = po;
				if (mostGistUser.getPublic_gists()<po.getPublic_gists())
					mostGistUser = po;
			}
		}

	}


	public List<RepoPO> getNameOrderRepoPOs() {
		return nameOrderRepoPOs;
	}
	public List<RepoPO> getStarOrderRepoPOs() {
		return starOrderRepoPOs;
	}
	public List<RepoPO> getForkOrderRepoPOs() {
		return forkOrderRepoPOs;
	}
	public List<RepoPO> getSubscrOrderRepoPOs() {
		return subscrOrderRepoPOs;
	}
	public List<RepoPO> getUpdateOrderRepoPOs() {
		return updateOrderRepoPOs;
	}
	public List<UserPO> getNameOrderUsers() {
		return nameOrderUsers;
	}
	public List<UserPO> getFollowerOrderUsers() {
		return followerOrderUsers;
	}
	public List<UserPO> getFollowingOrderUsers() {
		return followingOrderUsers;
	}
	public List<UserPO> getRepoNumOrderUsers() {
		return repoNumOrderUsers;
	}
	public Map<String, RepoPO> getNameToRepo() {
		return nameToRepo;
	}
	public Map<String, UserPO> getNameToUser() {
		return nameToUser;
	}
	public Map<String, List<String>> getUserToOwnerRepo() {
		return userToOwnerRepo;
	}
	public Map<String, List<String>> getUserToCollabRepo() {
		return userToCollabRepo;
	}
	public Map<String, List<String>> getUserToContribute() {
		return userToContribute;
	}
	public Map<String, List<String>> getUserToSubscribeRepo() {
		return userToSubscribeRepo;
	}
	public Map<String, List<String>> getRepoToContributor() {
		return repoToContributor;
	}
	public Map<String, List<String>> getRepoToCollab() {
		return repoToCollab;
	}
	public Map<String, List<String>> getRepoToSubscriber() {
		return repoToSubscriber;
	}
	public Map<String, List<String>> getRepoToCommit() {
		return repoToCommit;
	}
	public Map<String, List<String>> getRepoToIssue() {
		return repoToIssue;
	}
	public Map<String, List<String>> getRepoToPull() {
		return repoToPull;
	}
	public MyChartVO getRepoCreateTime() {
		return repoCreateTime;
	}

	public MyChartVO getRepoSubscribers() {
		return repoSubscribers;
	}

	public Map<String, Map<String, Integer>> getRepoLanguages() {
		return repoLanguages;
	}

	public MyChartVO getForkCount() {
		return forkCount;
	}
	public MyChartVO getStarCount() {
		return starCount;
	}
	public MyChartVO getLanguage() {
		return language;
	}
	public MyChartVO getStatUserType() {
		return statUserType;
	}
	public MyChartVO getStatUserCreateTime() {
		return statUserCreateTime;
	}
	public MyChartVO getStatUserCollaborateRepo() {
		return statUserCollaborateRepo;
	}
	public MyChartVO getStatUserContributorRepo() {
		return statUserContributorRepo;
	}
	public MyChartVO getStatUserSubscriberRepo() {
		return statUserSubscriberRepo;
	}
	public MyChartVO getStatUserOwnRepo() {
		return statUserOwnRepo;
	}
	public MyChartVO getStatCompanyUser() {
		return statCompanyUser;
	}
	public MyChartVO getRepoContributors() {
		return repoContributors;
	}
	public MyChartVO getRepoCollabrators() {
		return repoCollabrators;
	}
	public MyChartVO getRepoSize() {
		return repoSize;
	}
	public MyChartVO getStatUserEmail() {
		return statUserEmail;
	}
	public MyChartVO getStatUserGist() {
		return statUserGist;
	}
	public MyChartVO getStatUserFolllowers() {
		return statUserFolllowers;
	}
}
