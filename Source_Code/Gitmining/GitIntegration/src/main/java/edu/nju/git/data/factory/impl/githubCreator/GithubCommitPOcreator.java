package edu.nju.git.data.factory.impl.githubCreator;

import edu.nju.git.VO.CommitVO;
import edu.nju.git.data.api.abstractservice.FieldsGetterService;
import edu.nju.git.data.factory.service.POfactory;

public class GithubCommitPOcreator implements POfactory<CommitVO> {

	private FieldsGetterService itemHelper;
	
	public GithubCommitPOcreator(FieldsGetterService itemHelper) {
		this.itemHelper = itemHelper;
    }


    /**
	 * http://www.gitmining.net/api/repository/{owner}/{reponame}/commit/{sha}/item/{item}
     * 查询某个commit的某项信息
     * item可接受的参数有：
     * committer,committer_email,Date,message
     * filenumber 更改文件数,additions 添加代码行数,deletions 删除代码行数,total 总共影响行数
	 */
    
	@Override
	public CommitVO getPO() {
		CommitVO commit = new CommitVO();
		commit.setId(itemHelper.getString("sha"));
//		po.setContents(itemHelper.getString(""));
		commit.setDate(itemHelper.getString("date"));

		commit.setMessage(itemHelper.getString("message"));
		
//		commit.setNum_addition(itemHelper.getInteger("additions"));
//		commit.setNum_deletion(itemHelper.getInteger("deletions"));
//		commit.setNum_file(itemHelper.getInteger("filenumber"));
//		commit.setNum_file(0);
//		commit.setNum_total(itemHelper.getInteger("total"));
		
//		commit.setUserName(itemHelper.getString("committer"));
		commit.setUserName(itemHelper.getString("name"));
//		commit.setComments(comments);
		return commit;
	}

}

