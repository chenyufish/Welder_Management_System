package com.yupi.usercenter.listener;

import cn.hutool.bloomfilter.BitSetBloomFilter;
import cn.hutool.bloomfilter.BloomFilter;
import cn.hutool.bloomfilter.BloomFilterUtil;
import com.yupi.usercenter.model.domain.Blog;
import com.yupi.usercenter.model.domain.Team;
import com.yupi.usercenter.model.domain.User;
import com.yupi.usercenter.properties.SuperProperties;
import com.yupi.usercenter.service.BlogService;
import com.yupi.usercenter.service.TeamService;
import com.yupi.usercenter.service.UserService;
import lombok.extern.log4j.Log4j2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

import static com.yupi.usercenter.contant.BloomFilterConstants.*;


/**
 * 启动侦听器
 *
 */
@Configuration
@Log4j2
public class StartupListener implements CommandLineRunner {

    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;

    @Resource
    private BlogService blogService;

    @Resource
    private SuperProperties superProperties;

    /**
     * 启动
     *
     * @param args args
     */
    @Override
    public void run(String... args) {
        if (superProperties.isEnableBloomFilter()) {
            long begin = System.currentTimeMillis();
            log.info("Starting init BloomFilter......");
            this.initBloomFilter();
            long end = System.currentTimeMillis();
            String cost = end - begin + " ms";
            log.info("BloomFilter initialed in " + cost);
        }
    }

    /**
     * 初始化布隆过滤器
     *
     * @return {@link BloomFilter}
     */
    @Bean
    public BloomFilter initBloomFilter() {
        BitSetBloomFilter bloomFilter = BloomFilterUtil.createBitSet(
                PRE_OPENED_MAXIMUM_INCLUSION_RECORD,
                EXPECTED_INCLUSION_RECORD,
                HASH_FUNCTION_NUMBER
        );
        List<User> userList = userService.list(null);
        for (User user : userList) {
            bloomFilter.add(USER_BLOOM_PREFIX + user.getId());
        }
        List<Team> teamList = teamService.list(null);
        for (Team team : teamList) {
            bloomFilter.add(TEAM_BLOOM_PREFIX + team.getId());
        }

        List<Blog> blogList = blogService.list(null);
        for (Blog blog : blogList) {
            bloomFilter.add(BLOG_BLOOM_PREFIX + blog.getId());
        }
        return bloomFilter;
    }
}
