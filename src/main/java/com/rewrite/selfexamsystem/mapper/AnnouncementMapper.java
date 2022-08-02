package com.rewrite.selfexamsystem.mapper;

import com.rewrite.selfexamsystem.domain.Announcement;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: Levi
 * @description: Announcement的数据库Mapper类
 * @since: 2022/7/27 23:16
 * @version: 3.1（Created By Spring Boot）
 */
@Repository
@Mapper
public interface AnnouncementMapper {
    /**
     * @param announcement: 需要加入到数据库中的公告类
     * @return
     * @description : 将Announcement加入到数据库中
     * @author Levi
     * @since 2022/7/27 23:18
     */
    void addAnnouncement(Announcement announcement);

    /**
     * @param up:   页码上界
     * @param down: 页码下届
     * @return 返回分页后的公告信息
     * @description : 根据分页返回指定页面的公告信息
     * @author Levi
     * @since 2022/7/28 22:57
     */
    List<Announcement> showSomeAnnouncement(int down, int up);

    /**
     * @param :
     * @return List：返回Announcement公告信息
     * @description : 查询并返回所有的公告信息
     * @author Levi
     * @since 2022/7/28 22:56
     */
    List<Announcement> showAllAnnouncement();

    /**
     * @param keyWords: 搜索的关键词
     * @param up:       返回的上界
     * @param down:     返回的下界
     * @return 返回搜索的结果
     * @description : 根据keyWords对title和body进行模糊查询，并且分页返回
     * @author Levi
     * @since 2022/7/29 22:31
     */
    List<Announcement> searchAnnouncement(String keyWords, int down, int up);

    /**
     * @param :
     * @return 返回公告的数量
     * @description : 返回数据库中公告的数量
     * @author Levi
     * @since 2022/7/29 16:58
     */
    int getAnnouncementNumber();

    /**
     * @param announcement: 获取title和time
     * @return 返回删除的公告数量
     * @description : 根据title和time删除公告，返回删除的公告数量
     * @author Levi
     * @since 2022/7/29 18:02
     */
    long deleteAnnouncement(Announcement announcement);

    /**
     * @param announcement: 获取title和time
     * @return 返回获取到的公告信息
     * @description : 根据title和time查询公告信息，并返回
     * @author Levi
     * @since 2022/7/29 18:01
     */
    Announcement getAnnouncement(Announcement announcement);

    /**
     * @param keyWords: 关键词
     * @return 返回查询到记录的数量
     * @description : 通过关键词进行模糊查询，返回查询到记录的数量
     * @author Levi
     * @since 2022/7/29 22:49
     */
    int searchAnnouncementNumber(String keyWords);

}
