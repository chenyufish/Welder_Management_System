package com.fishman.usercenter.contant;


/**
 * 布隆过滤器常量
 *

 */
public final class BloomFilterConstants {
    private BloomFilterConstants() {
    }

    /**
     * 用户布隆前缀
     */
    public static final String USER_BLOOM_PREFIX = "fishman:user:id:";
    /**
     * 队伍布隆前缀
     */
    public static final String TEAM_BLOOM_PREFIX = "fishman:team:id:";
    /**
     * 博客布隆前缀
     */
    public static final String BLOG_BLOOM_PREFIX = "fishman:blog:id:";

    /**
     * 预先打开的最大包含记录
     */
    public static final int PRE_OPENED_MAXIMUM_INCLUSION_RECORD = 2000;

    /**
     * 预期包含记录
     */
    public static final int EXPECTED_INCLUSION_RECORD = 1000;

    /**
     * 散列函数数
     */
    public static final int HASH_FUNCTION_NUMBER = 2;
}
