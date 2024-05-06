package com.fishman.welder_management_backend.job;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.fishman.welder_management_backend.model.domain.User;
import com.fishman.welder_management_backend.model.vo.UserVO;
import com.fishman.welder_management_backend.service.UserService;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;
import reactor.util.annotation.NonNull;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.fishman.welder_management_backend.contant.RedisConstants.USER_RECOMMEND_KEY;
import static com.fishman.welder_management_backend.contant.RedissonConstant.*;
import static com.fishman.welder_management_backend.contant.SystemConstants.DEFAULT_CACHE_PAGE;


/**
 * 用户推荐缓存
 *

 */
@Log4j2
public class UserRecommendationCache extends QuartzJobBean {
    /**
     * redisson客户
     */
    @Resource
    private RedissonClient redissonClient;

    /**
     * 用户服务
     */
    @Resource
    private UserService userService;

    /**
     * 字符串复述,模板
     */
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 执行内部
     *
     * @param context 上下文
     */
    @Override
    protected void executeInternal(@NonNull JobExecutionContext context) {
        RLock lock = redissonClient.getLock(USER_RECOMMEND_LOCK);
        try {
            if (lock.tryLock(DEFAULT_WAIT_TIME, DEFAULT_LEASE_TIME, TimeUnit.MICROSECONDS)) {
                log.info("开始用户缓存");
                long begin = System.currentTimeMillis();
                /**
                 * 用户列表
                 */
                List<User> userList = userService.list();
                for (User user : userList) {
                    for (int i = 1; i <= DEFAULT_CACHE_PAGE; i++) {
                        Page<UserVO> userVoPage = userService.matchUser(i, user);
                        Gson gson = new Gson();
                        String userVoPageStr = gson.toJson(userVoPage);
                        String key = USER_RECOMMEND_KEY + user.getId() + ":" + i;
                        stringRedisTemplate.opsForValue().set(key, userVoPageStr);
                    }
                }
                long end = System.currentTimeMillis();
                log.info("用户缓存结束，耗时" + (end - begin));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                log.info("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }

    }
}
