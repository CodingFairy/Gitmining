package edu.nju.git.data.init.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.nju.git.PO.UserBriefPO;
import edu.nju.git.data.api.centralization.RepoMapReader;
import edu.nju.git.data.api.centralization.UserMapReader;
import edu.nju.git.data.api.liststring.RepositoriesListReader;
import edu.nju.git.data.factory.impl.POfactory.UserBriefPOfactory;
import edu.nju.git.data.init.service.UserInitService;

public class UserInitImpl implements UserInitService {

	private RepositoriesListReader repositoriesReader;
	private List<UserBriefPO> pos ;
	
	public UserInitImpl(RepositoriesListReader reader) {
		this.repositoriesReader = 
				reader==null? new RepositoriesListReader(): reader;
	}
	
	@Override
	public void init() {
		//1. get all repositories' full names
		List<String> repos = repositoriesReader.getNames();
		//2. get details information,one by one
		
		pos = new ArrayList<UserBriefPO>();
		
		Set<String> stringSet = new HashSet<String>();
		
		UserMapReader getter = new UserMapReader();
		UserBriefPOfactory userBriefPOfactory = new UserBriefPOfactory(getter);
		int i = 0,y=0;;
		for (String string : repos) {
			String tempString = string.split("/")[0];
			if(!stringSet.contains(tempString))
			{
				stringSet.add(tempString);
				getter.setName(tempString);
				UserBriefPO po = userBriefPOfactory.getPO();
				if(po!=null){
					pos.add(po);
				}
				System.out.print(i++);
			}
			System.out.println("  "+(y++));
		}
		
		new MyObjectWiter(pos, "cache/userinfo.txt").excute();
	}
			

}
