package edu.nju.git.VO;

import edu.nju.git.type.OwnerType;

/**
 * view info of a user
 * @author cuihao
 *
 */
public class UserVO {
	private String login;
	private OwnerType type;
	private String name;
	private String company;
	private String blog;
	private String location;
	private String email;
	private String bio;
	private int followNum = 0;
	private int followingNum = 0;
	private String create_at;
	private String update_at;
	private int public_repos;
	private int public_gists;
	
	private String avatar_url;
	public String getAvatar_url() {
		return avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public OwnerType getType() {
		return type;
	}
	public void setType(OwnerType type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getBlog() {
		return blog;
	}
	public void setBlog(String blog) {
		this.blog = blog;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public int getFollowNum() {
		return followNum;
	}
	public void setFollowNum(int followNum) {
		this.followNum = followNum;
	}
	public int getFollowingNum() {
		return followingNum;
	}
	public void setFollowingNum(int followingNum) {
		this.followingNum = followingNum;
	}
	public String getCreate_at() {
		return create_at;
	}
	public void setCreate_at(String create_at) {
		this.create_at = create_at;
	}
	public String getUpdate_at() {
		return update_at;
	}
	public void setUpdate_at(String update_at) {
		this.update_at = update_at;
	}
	public int getPublic_gists() {
		return public_gists;
	}
	public void setPublic_gists(int public_gists) {
		this.public_gists = public_gists;
	}
	public int getPublic_repos() {
		return public_repos;
	}
	public void setPublic_repos(int public_repos) {
		this.public_repos = public_repos;
	}
	
	

}
