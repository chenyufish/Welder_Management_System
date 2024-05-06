package com.fishman.management_backend.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.fishman.management_backend.model.domain.Chat;
import com.fishman.management_backend.model.domain.User;
import com.fishman.management_backend.model.request.ChatRequest;
import com.fishman.management_backend.model.vo.ChatMessageVO;

import java.util.Date;
import java.util.List;

/**
 * 聊天服务
 *
 */
public interface ChatService extends IService<Chat> {
    /**
     * 获取私人聊天
     *
     * @param chatRequest 聊天请求
     * @param chatType    聊天类型
     * @param loginUser   登录用户
     */
    List<ChatMessageVO> getPrivateChat(ChatRequest chatRequest, int chatType, User loginUser);

    /**
     * 获取缓存
     *
     * @param redisKey redis键
     * @param id       id
     */
    List<ChatMessageVO> getCache(String redisKey, String id);

    /**
     * 保存缓存
     *
     * @param redisKey       redis键
     * @param id             id
     * @param chatMessageVos 聊天消息vos
     */
    void saveCache(String redisKey, String id, List<ChatMessageVO> chatMessageVos);

    /**
     * 聊天结果
     *
     * @param userId     用户id
     * @param toId       到id
     * @param text       文本
     * @param chatType   聊天类型
     * @param createTime 创建时间
     */
    ChatMessageVO chatResult(Long userId, Long toId, String text, Integer chatType, Date createTime);

    /**
     * 删除密钥
     *
     * @param key 钥匙
     * @param id  id
     */
    void deleteKey(String key, String id);

    /**
     * 获取团队聊天
     *
     * @param chatRequest 聊天请求
     * @param teamChat    团队聊天
     * @param loginUser   登录用户
     */
    List<ChatMessageVO> getTeamChat(ChatRequest chatRequest, int teamChat, User loginUser);

    /**
     * 获得大厅聊天
     *
     * @param chatType  聊天类型
     * @param loginUser 登录用户
     * @return {@link List}<{@link ChatMessageVO}>
     */
    List<ChatMessageVO> getHallChat(int chatType, User loginUser);
}
