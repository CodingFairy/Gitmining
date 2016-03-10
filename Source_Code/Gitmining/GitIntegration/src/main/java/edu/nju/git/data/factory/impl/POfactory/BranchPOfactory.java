package edu.nju.git.data.factory.impl.POfactory;

import edu.nju.git.VO.BranchVO;
import edu.nju.git.data.api.abstractservice.FieldsGetterService;
import edu.nju.git.data.factory.service.POfactory;

public class BranchPOfactory implements POfactory<BranchVO>{

	private FieldsGetterService itemHelper;
	

	public BranchPOfactory(FieldsGetterService itemHelper) {
		super();
		this.itemHelper = itemHelper;
	}


	@Override
	public BranchVO getPO() {
		BranchVO branch = new BranchVO();
		branch.setId(itemHelper.getString("name"));
		branch.setName(itemHelper.getString("fn"));
		branch.setSHA(itemHelper.getString("commit"));
		return branch;
	}

}
