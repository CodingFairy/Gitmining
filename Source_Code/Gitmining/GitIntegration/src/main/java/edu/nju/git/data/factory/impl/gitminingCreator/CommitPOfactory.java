package edu.nju.git.data.factory.impl.gitminingCreator;

import edu.nju.git.VO.CommitVO;
import edu.nju.git.data.api.abstractservice.FieldsGetterService;
import edu.nju.git.data.factory.service.POfactory;

public class CommitPOfactory implements POfactory<CommitVO> {

	private FieldsGetterService itemHelper;
	private String sha;
	
	public CommitPOfactory(FieldsGetterService itemHelper) {
		this.itemHelper = itemHelper;
    }

	public CommitPOfactory(FieldsGetterService itemHelper,String sha) {
		super();
		this.itemHelper = itemHelper;
		this.sha = sha;
	}
    public void setFiledsGetter(FieldsGetterService itemHelper,String sha) {
		this.itemHelper = itemHelper;
		this.sha = sha;
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
		commit.setId(sha);
//		po.setContents(itemHelper.getString(""));
		commit.setDate(itemHelper.getString("Date"));
//		po.setId(itemHelper.getString(""));
		commit.setMessage(itemHelper.getString("message"));
		
//		commit.setNum_addition(itemHelper.getInteger("additions"));
//		commit.setNum_deletion(itemHelper.getInteger("deletions"));
//		commit.setNum_file(itemHelper.getInteger("filenumber"));
//		commit.setNum_total(itemHelper.getInteger("total"));
		
//		commit.setUserName(itemHelper.getString("committer"));
		commit.setUserName("committer");
		
		return commit;
	}

}