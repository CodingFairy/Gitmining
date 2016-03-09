package edu.nju.git.data.factory.impl.POfactory;

import edu.nju.git.VO.IssueVO;
import edu.nju.git.data.factory.service.POfactory;

public class IssuePOfactory implements POfactory<IssueVO> {

	private AbstractFieldsGetter itemHelper;
	private String sha;
	

	public IssuePOfactory(AbstractFieldsGetter itemHelper, String sha) {
		super();
		this.itemHelper = itemHelper;
		this.sha = sha;
	}

	public IssuePOfactory(){}
	
	public void setItemHelper(AbstractFieldsGetter itemHelper) {
		this.itemHelper = itemHelper;
	}

	public void setSHA(String sha) {
		this.sha = sha;
	}

	/**
	 *  <p>http://www.gitmining.net/api/repository/{owner}/{reponame}/issue/{number}/item/{item}
     * 查询某个Issue的某项信息
     * item可接受的参数有：
     * state,locked,title,body
     * user,user_id,user_type
     * created_at,updates_at,closed_at,merged_at
     * </p>
	 */
	@Override
	public IssueVO getPO() {
		IssueVO issue = new IssueVO();
		issue.setId(sha);
		issue.setState(itemHelper.getString("state"));
		issue.setLocked(itemHelper.getBoolean("locked"));
		issue.setTitle(itemHelper.getString("title"));
		issue.setBody(itemHelper.getString("body"));
		issue.setUserName(itemHelper.getString("user"));
		issue.setCreate_at(itemHelper.getString("created_at"));
		issue.setUpdate_at(itemHelper.getString("updates_at"));
		issue.setMerged_at(itemHelper.getString("merged_at"));
		issue.setClosed_at(itemHelper.getString("closed_at"));
		return issue;
	}

}
