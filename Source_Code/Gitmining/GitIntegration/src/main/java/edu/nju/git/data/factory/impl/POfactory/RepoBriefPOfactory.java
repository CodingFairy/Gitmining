package edu.nju.git.data.factory.impl.POfactory;

import edu.nju.git.VO.RepoBriefVO;
import edu.nju.git.data.factory.service.POfactory;

public class RepoBriefPOfactory implements POfactory<RepoBriefVO> {

	private AbstractFieldsGetter itemHelper;
	public RepoBriefPOfactory(){}
	public RepoBriefPOfactory(AbstractFieldsGetter itemHelper) {
		super();
		this.itemHelper = itemHelper;
	}

	/**
	 * <br/><b>precondition</b>：this.owner this.repo (map)must be set
	 * <br/><b>postcondition</b>：return a PO
	 * @return 
	 * @date 2016-03-06
	 */
	@Override
	public RepoBriefVO getPO() {

		RepoBriefVO repoBriefPO = new RepoBriefVO();
		
		String fullname[] = itemHelper.getString("full_name").split("/");
		repoBriefPO.setOwner(fullname[0]);
		repoBriefPO.setName(fullname[1]);
		
		Object description = itemHelper.getString("description");
		repoBriefPO.setDescription(description==null? "" : description.toString());
		
		int num_subcribers = itemHelper.getInteger("subscribers_count");
		repoBriefPO.setNum_subscribers(num_subcribers);
		
 		repoBriefPO.setNum_forks(itemHelper.getInteger("forks_count"));
		repoBriefPO.setNum_stars(itemHelper.getInteger("stargazers_count"));
		
		repoBriefPO.setLastUpdate(itemHelper.getString("updated_at"));

		return repoBriefPO;
	}

}
