package edu.nju.git.data.factory.impl.gitminingCreator;

import edu.nju.git.VO.RepoVO;
import edu.nju.git.data.api.abstractservice.FieldsGetterService;
import edu.nju.git.data.factory.service.POfactory;
import edu.nju.git.type.OwnerType;

public class RepoPOfactory implements POfactory<RepoVO> {

	private FieldsGetterService itemHelper;
	

	public RepoPOfactory(FieldsGetterService itemHelper) {
		super();
		this.itemHelper = itemHelper;
	}


	/**
	 * <p/>http://www.gitmining.net/api/api/repository/{owner}/{reponame}/item/{item}
	 * <br/>item可接受的参数有：
	 * <br/>owner_name:项目所有者登录名 
	 * <br/>owner_id:所有者的id 
	 * <br/>owner_type:所有者的用户类型（分为User和Organization）
	 * <br/>html_url:项目主页
	 * <br/>url description:项目描述 
	 * <br/>fork:是否是fork了他人项目所产生的项目 
     * <br/>created_at:创建时间 
     * <br/>updated_at:更新时间 
     * <br/>pushed_at:最后一次push的时间
     * <br/>size:项目大小 
     * <br/>stargazers_count:点赞人数 
     * <br/>language:项目主语言 
     * <br/>forks:被fork的次数 
     * <br/>open_issues:open的issue数 
     * <br/>subscribers_count:关注者数量
	 */
	@Override
	public RepoVO getPO() {
		RepoVO repo = new RepoVO();
		
		String fullname[] = itemHelper.getString("full_name").split("/");
		repo.setOwnerName(fullname[0]);
		repo.setName(fullname[1]);
		
		repo.setUrl(itemHelper.getString("url"));

		repo.setDescription(itemHelper.getString("description"));
		
		repo.setNum_subscribers(itemHelper.getInteger("subscribers_count"));
 		repo.setNum_forks(itemHelper.getInteger("forks_count"));
		repo.setNum_stars(itemHelper.getInteger("stargazers_count"));


		//repo.setType(OwnerType.getInstance( (itemHelper.getString("owner_type"))));
		repo.setLanguage(itemHelper.getString("language"));
		
		repo.setCreate_at(itemHelper.getString("created_at"));
		repo.setUpdate_at(itemHelper.getString("updated_at"));
		
		return repo;
	}

}