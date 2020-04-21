package com.smacul.demo.service.impl;

import com.smacul.demo.bean.CusBehaviorRecord;
import com.smacul.demo.bean.Customer;
import com.smacul.demo.dao.CusBehaviorRecordDao;
import com.smacul.demo.dao.CusDao;
import com.smacul.demo.dao.CusFeatureCountDao;
import com.smacul.demo.model.CusDynamicMod;
import com.smacul.demo.model.CusFeatureFullMod;
import com.smacul.demo.service.SelfService;
import com.smacul.demo.util.MD5;
import com.smacul.demo.util.PageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class SelfServiceImpl implements SelfService {

    @Autowired
    CusDao cusDao;
    @Autowired
    CusFeatureCountDao cusFeatureCountDao;
    @Autowired
    CusBehaviorRecordDao cusBehaviorRecordDao;

    @Override
    public Boolean checkIsNewUser(Integer cusId) {
        Integer behaviorNum = cusFeatureCountDao.countBehaviorNum(cusId);
        if (behaviorNum > 20) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Customer checkCusForLogin(String cusName, String cusPass) throws NoSuchAlgorithmException {
        String cryptString = MD5.MD5Creator(cusPass);
        Customer customer = cusDao.getCusByName(cusName);
        if (customer != null && cryptString.equals(customer.getCusPass())) {
            return customer;
        } else {
            return null;
        }
    }

    @Override
    public String setNewCus(String cusName, String cusPass) throws NoSuchAlgorithmException {
        String cryptString = MD5.MD5Creator(cusPass);
        if (cusDao.countCusByName(cusName) != 0) {
            return "当前用户名已被注册";
        }
        if (cusDao.insertCusForRegister(cusName, cryptString) == 1) {
            return "注册成功";
        } else {
            return "注册失败";
        }
    }

    @Override
    public Customer getCusBasicInfo(Integer cusId) {
        Customer customer = cusDao.getCusById(cusId);
        customer.setCusPass(null);
        return customer;
    }

    @Override
    public Boolean setCusBasicInfo(Customer customer) throws NoSuchAlgorithmException {
        if (customer.getCusPass() == null) {
            if (cusDao.updateCusBasicInfoWithPass(customer) == 1) {
                return true;
            }
        } else {
            customer.setCusPass(MD5.MD5Creator(customer.getCusPass()));
            if (cusDao.updateCusBasicInfo(customer) == 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean setCusFollow(Integer cusIdFrom, Integer cusIdTo) {
        if (cusBehaviorRecordDao.countTargetCusFollowBehavior(cusIdFrom, cusIdTo) == 1) {
            if (cusBehaviorRecordDao.deleteTargetCusFollowBehavior(cusIdFrom, cusIdTo) == 1) {
                return true;
            }
        } else {
            if (cusBehaviorRecordDao.addTargetCusFollowBehavior(cusIdFrom, cusIdTo) == 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CusFeatureFullMod getCusFeatureInfo(Integer cusId) {
        CusFeatureFullMod cusFeatureFullMod = new CusFeatureFullMod();
        cusFeatureFullMod.setCusId(cusId);
        cusFeatureFullMod.setReadNum(cusBehaviorRecordDao.countCusBehaviorFrom(cusId, 2));
        cusFeatureFullMod.setFanNum(cusBehaviorRecordDao.countCusBehaviorTo(cusId, 11));
        cusFeatureFullMod.setFollowNum(cusBehaviorRecordDao.countCusBehaviorFrom(cusId, 11));
        cusFeatureFullMod.setArtEditNum(cusBehaviorRecordDao.countCusBehaviorFrom(cusId, 1));
        cusFeatureFullMod.setComEditNum(cusBehaviorRecordDao.countCusBehaviorFrom(cusId, 5));
        cusFeatureFullMod.setRepEditNum(cusBehaviorRecordDao.countCusBehaviorFrom(cusId, 8));
        cusFeatureFullMod.setFeatureCount(cusFeatureCountDao.getCusFeatureCountByCusId(cusId));
        return cusFeatureFullMod;
    }

    @Override
    public List<CusDynamicMod> getCusDynamic(Integer cusId, Integer page, Integer pageSize) {
        Integer start = PageHandler.calcuStartNO(page, pageSize);
        //CusBehaviorRecord cusBehaviorRecord = cusBehaviorRecordDao.
        return null;
    }
}
