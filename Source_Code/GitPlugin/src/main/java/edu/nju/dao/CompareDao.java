package edu.nju.dao;

/**
 * Created by cuihao on 2016/8/3.
 */
public interface CompareDao {
    long rangeOfWatch(long num);
    long rangeOfStar(long num);
    long rangeOfFork(long num);
    long rangeOfFollwer(double num);
    double peopleFollower(String owner, String name);
    long sumRepo();
}
