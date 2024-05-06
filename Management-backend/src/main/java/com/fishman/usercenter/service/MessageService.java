package com.fishman.usercenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fishman.usercenter.model.domain.Message;
import com.fishman.usercenter.model.vo.BlogVO;
import com.fishman.usercenter.model.vo.MessageVO;


import java.util.List;

/**
 * 消息服务
 */
public interface MessageService extends IService<Message> {

    /**
     * 获取消息编号
     *
     * @param userId 用户id
     * @return long
     */
    long getMessageNum(Long userId);

    /**
     * 像num一样
     *
     * @param userId 用户id
     * @return long
     */
    long getLikeNum(Long userId);

    /**
     * 就像
     *
     * @param userId 用户id
     * @return {@link List}<{@link MessageVO}>
     */
    List<MessageVO> getLike(Long userId);

    /**
     * 收到用户博客
     *
     * @param userId 用户id
     * @return {@link List}<{@link BlogVO}>
     */
    List<BlogVO> getUserBlog(Long userId);

    /**
     * 有新消息
     *
     * @param userId 用户id
     * @return {@link Boolean}
     */
    Boolean hasNewMessage(Long userId);

    /**
     * 分页喜欢
     *
     * @param userId      用户id
     * @param currentPage 当前页码
     * @return {@link Page}<{@link MessageVO}>
     */
    Page<MessageVO> pageLike(Long userId, Long currentPage);
}
