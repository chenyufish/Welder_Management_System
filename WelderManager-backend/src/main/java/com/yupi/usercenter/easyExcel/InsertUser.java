//package com.yupi.usercenter.easyExcel;
//import java.util.Date;
//import com.yupi.usercenter.mapper.UserMapper;
//import com.yupi.usercenter.model.domain.User;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StopWatch;
//import javax.annotation.Resource;
//
///**
// * 用于模拟数据插入的
// */
//@Component
//public class InsertUser {
//
//    @Resource
//    private UserMapper userMapper;
//
//    /**
//     * 循环插入用户
//     */
////    @Scheduled(initialDelay = 5000,fixedRate = Long.MAX_VALUE )
//    public void doInsertUser() {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        final int INSERT_NUM = 1000;
//        for (int i = 0; i < INSERT_NUM; i++) {
//            User user = new User();
//            user.setUsername("基泥肽酶");
//            user.setUserAccount("114514");
//            user.setAvatarUrl("https://img0.baidu.com/it/u=256816879,771155532&fm=253&app=120&size=w931&n=0&f=JPEG&fmt=auto?sec=1711472400&t=3d85e716088ec306fe87b0113d803b0b");
//            user.setProfile("一条咸鱼");
//            user.setGender(0);
//            user.setUserPassword("12345678");
//            user.setPhone("123456789108");
//            user.setEmail("114514@qq.com");
//            user.setUserStatus(0);
//            user.setUserRole(0);
//            user.setPlanetCode("931");
//            user.setTags("[]");
//            userMapper.insert(user);
//        }
//        stopWatch.stop();
//        System.out.println( stopWatch.getLastTaskTimeMillis());
//
//    }
//}
