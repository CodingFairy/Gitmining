package edu.nju.git.datavisitors.uservisitors;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import edu.nju.git.PO.UserBriefPO;
import edu.nju.git.PO.UserPO;
import edu.nju.git.VO.UserBriefVO;
import edu.nju.git.constant.Consts;
import edu.nju.git.data.service.UserDataService;
import edu.nju.git.tools.POVOConverter;
import edu.nju.git.type.SortType;

/**
 * Created by harry on 16-3-8.
 */
public abstract class SimpleUserVisitor implements UserVisitor {
    /**
     * the page number you want to get
     */
    protected int page;
    /**
     * true if the name order is form high to low
     */
    protected boolean reverse;

    protected SimpleUserVisitor(int page, boolean reverse){
        this.page = page;
        this.reverse = reverse;
    }

    public int getPage(){
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean getReverse(){
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public abstract List<UserBriefVO> visit(UserDataService userDataService);

    protected List<UserBriefVO> visit(UserDataService userDataService, SortType sortType) {
        List<UserPO> POList = null;
		try {
			POList = userDataService.getUserPOs(sortType);
		} catch (Exception e) {
			e.printStackTrace();
		}
        List<UserBriefVO> resultList = new ArrayList<>();
        int totalItem = 0;
		try {
			totalItem = userDataService.getTotalCount();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if ((page-1)* Consts.PAGE_CAPACITY < totalItem) {
            if (reverse) {
                ListIterator<UserPO> itr = POList.listIterator(totalItem - (page-1)*Consts.PAGE_CAPACITY);
                for (int i=0; i<Consts.PAGE_CAPACITY; i++) {
                    if (itr.hasPrevious()){
                        resultList.add(POVOConverter.convertToBriefVO(itr.previous()));
                    }
                    else {
                        break;
                    }
                }
            }
            else {
                ListIterator<UserPO> itr = POList.listIterator((page-1)*Consts.PAGE_CAPACITY);
                for (int i=0;i<Consts.PAGE_CAPACITY; i++) {
                    if (itr.hasNext()) {
                        resultList.add(POVOConverter.convertToBriefVO(itr.next()));
                    }
                    else {
                        break;
                    }
                }
            }
        }
        return resultList;
    }
}
