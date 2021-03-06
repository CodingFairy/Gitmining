package org.GitServer.cacheinit;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.GitServer.cacheinit.writer.Saver;
import org.GitServer.dataread.Reader;

import edu.nju.git.PO.UserPO;

/**
 * a temperary class , for deleting some users which do not have 
 * enough contributots',conllabrators' or subscirbers' reposotories.
 * this class is used only before the administer start the server.
 * <br/><b>once you created the object, triming users started and ending after creating</b>
 * @author daixinyan
 * @date 2016-03-31
 * 
 */
public final class TrimUsers {

	
	private List<UserPO> userPOs;
	private DataEncapsulation dataEncapsulation;
	
	public TrimUsers(){
		dataEncapsulation = new Reader().excute();
		userPOs = dataEncapsulation.nameOrderUserPOs;
		
		trimByCollabrators();
		System.out.println("done with trim collabrators of users");
		trimByContributors();
		System.out.println("done with trim contributors of users");
		trimBySubscribers();
		System.out.println("done with trim subscribers of users");
		
		
		try {
			new Saver(dataEncapsulation, "cache").excute(dataEncapsulation.getClass().getField("nameOrderUserPOs"));
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	
	private final void  trimByContributors(){
		trim(dataEncapsulation.userToContribute);
	}
	
	private final void trimByCollabrators(){
		trim(dataEncapsulation.userToCollabRepo);
	}
	
	private final void trimBySubscribers(){
		trim(dataEncapsulation.userToSubscribeRepo);
	}
	
	private final void trim(Map<String, List<String>> map){
		int count = 0;
		System.out.println("current users size: "+userPOs.size());
		for (Iterator<UserPO> iterator = userPOs.iterator(); iterator.hasNext();) {
			UserPO userPO = (UserPO) iterator.next();
			if(map.get(userPO.getName())==null){
				count++;
				iterator.remove();
			}
		}
		System.out.println("after deleted :"+count);
		System.out.println("current users size: "+userPOs.size());
	}
	
	public static void main(String [] args){
		new TrimUsers();
	}
}
