package org.GitServer.cacheinit.loader.listfactory;

/**
 * unit testing pass
 * get all repositories full name.
 * @author daixinyan
 * @date 2016-03-06
 */
public class RepositoriesListReader extends ListJsonReader{

	public RepositoriesListReader()
	{
		super("http://www.gitmining.net/api/repository/names");
	}

}
