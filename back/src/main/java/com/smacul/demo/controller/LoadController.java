package com.smacul.demo.controller;

import com.smacul.demo.bean.Customer;
import com.smacul.demo.model.ArtFullMod;
import com.smacul.demo.service.LoadService;
import com.smacul.demo.service.SelfService;
import com.smacul.demo.service.ShapeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/load")
public class LoadController {

    @Autowired
    LoadService loadService;
    @Autowired
    ShapeService shapeService;
    @Autowired
    SelfService selfService;
    @Autowired
    HttpSession session;

    /**
     * 获取新闻类别(英文)
     * 20-04-19 创建方法 TODO 老用户的处理逻辑还没完成
     * @return
     */
    @RequestMapping("/type")
    public List<String> getArtTypes() {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return null;
        }
        if (selfService.checkIsNewUser(customer.getCusId())) {
            return loadService.getArtTypesForNew();
        } else {
            return loadService.getArtTypesForOld(customer.getCusId());
        }
    }

    /**
     * 按照类别获取一页文章
     * 20-04-19 创建方法 TODO 老用户的处理逻辑需要更换.
     * @param artType
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/tiny")
    public List<ArtFullMod> getTinyArtOnePageByType(String artType, Integer page, Integer pageSize) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return null;
        }
        if (selfService.checkIsNewUser(customer.getCusId())) {
            return loadService.getTinyArtOnePageByTypeForNew(artType, page, pageSize);
        } else {
            return loadService.getTinyArtOnePageByTypeForOld(artType, page, pageSize);
        }
    }

    /**
     * 提供一页的热点新闻
     * 20-04-19 创建方法
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/hot")
    public List<ArtFullMod> getHotArtOnePage(Integer page, Integer pageSize) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return null;
        }
        return loadService.getHotArtOnePage(page, pageSize);
    }

    /**
     * 获取文章的主体内容, 包括文章内容, 文章作者, 文章的特征信息, 当前用户与文章的关系.
     *
     * @param artId
     * @return
     */
    @RequestMapping("/main")
    public ArtFullMod getFullArt(Integer artId) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return null;
        }
        ArtFullMod artFullMod = loadService.getFullArt(customer.getCusId(), artId);
        //shapeService.updateCusArtTable()
        return artFullMod;
    }

    /**
     * 文章点赞/点踩控制, 包括取消
     *
     * @param artId
     * @param type  1: 点赞, 2: 点踩, -1: 取消点赞, -2: 取消点踩
     * @return
     */
    @RequestMapping("/prefer")
    public String setArtPreference(Integer artId, Integer type) {
        return null;
    }

}
