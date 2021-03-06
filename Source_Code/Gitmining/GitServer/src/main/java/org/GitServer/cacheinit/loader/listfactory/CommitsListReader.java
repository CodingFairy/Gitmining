package org.GitServer.cacheinit.loader.listfactory;

public class CommitsListReader extends ListJsonReader {

	/**
	 * @param fullname "{owner}/{reponame}"
	 */
	public CommitsListReader(String fullname) {
		super("http://www.gitmining.net/api/repository/"+fullname+"/commits/shas");
	}
	
	public CommitsListReader(String owner,String name) {
		this(owner+"/"+name);
	}

}
