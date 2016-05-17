package edu.nju.common;

import edu.nju.dao.service.UserDaoService;
import edu.nju.entity.TblRepo;
import edu.nju.entity.TblUser;
import edu.nju.model.pojo.RepoVO;
import edu.nju.model.pojo.UserVO;
import org.hibernate.annotations.Source;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Harry on 2016/5/17.
 * this is a tool class to a <tt>TblRepo</tt> or <tt>TblUser</tt> to vo when transfer on the internet
 */
@Service
public class VOConvertor {

    @Resource
    private UserDaoService userDaoImpl;

    @Resource
    private TimeTranslator timeTranslator;

    public RepoVO convert(TblRepo tblRepo){
        TblUser tblUser = userDaoImpl.findUserByLoginName(tblRepo.getOwnerName());
        String avatar = "";
        if (tblUser != null){
            avatar = tblUser.getAvatarUrl();
        }
        String createAt = timeTranslator.transUnixTime(tblRepo.getCreateAt().getTime());
        String updateAt = timeTranslator.transUnixTime(tblRepo.getUpdateAt().getTime());
        RepoVO vo = new RepoVO(tblRepo.getOwnerName(),avatar,tblRepo.getName(),tblRepo.getSize(),tblRepo.getDescription(),
                tblRepo.getLanguage(),tblRepo.getUrl(),createAt,updateAt,tblRepo.getNumStar(),tblRepo.getNumFork(),
                tblRepo.getNumSubscriber());
        return vo;
    }

    public UserVO convert(TblUser tblUser){
        String createAt = timeTranslator.transUnixTime(tblUser.getCreateAt().getTime());
        String updateAt = timeTranslator.transUnixTime(tblUser.getUpdateAt().getTime());
        String type = (tblUser.getType()==0)?"User":"Organization";
        UserVO vo = new UserVO(tblUser.getLoginName(),tblUser.getName(),tblUser.getAvatarUrl(),tblUser.getBlog(),
                tblUser.getEmail(),tblUser.getLocation(),tblUser.getCompany(),tblUser.getBio(),type,tblUser.getPublicRepo(),
                tblUser.getPublicGist(),tblUser.getFollower(),tblUser.getFollowing(),createAt,updateAt);

        return vo;
    }
}
