//package com.fishman.usercenter.service;
//
//import com.fishman.usercenter.mapper.UserMapper;
//import com.fishman.usercenter.model.domain.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.util.StopWatch;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.*;
//
///**
// * @author: fishman
// * @Description:    用户插入单元测试，注意打包时要删掉或忽略，不然打一次包就插入一次
// */
//@SpringBootTest
//public class InsertUserTest {
//    @Resource
//    private UserService userService;
//    //线程设置
//    private ExecutorService executorService = new ThreadPoolExecutor(16, 1000, 10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));
//
//    @Test
//    public void doInsertUser() {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        final int INSERT_NUM = 1000;
//        List<User> userList = new ArrayList<>();
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
//           userList.add(user);
//        }
//        userService.saveBatch(userList,100);
//        stopWatch.stop();
//        System.out.println( stopWatch.getLastTaskTimeMillis());
//
//    }
//
//
//    @Test
//    public void doConcurrencyInsertUser() {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        final int INSERT_NUM = 100000;
//        // 分十组
//        int j = 0;
//        //批量插入数据的大小
//        int batchSize = 5000;
//        List<CompletableFuture<Void>> futureList = new ArrayList<>();
//        // i 要根据数据量和插入批量来计算需要循环的次数。
//        for (int i = 0; i < INSERT_NUM/batchSize; i++) {
//            List<User> userList = new ArrayList<>();
//            while (true){
//                j++;
//                User user = new User();
//                user.setUsername("小黑子没有树脂");
//                user.setUserAccount("114514");
//                user.setAvatarUrl("https://img0.baidu.com/it/u=1942571423,123300228&fm=253&fmt=auto&app=120&f=JPEG?w=504&h=500");
//                user.setProfile("小黑子露出鸡脚了吧");
//                user.setGender(1);
//                user.setUserPassword("12345678");
//                user.setPhone("123456789108");
//                user.setEmail("114514@qq.com");
//                user.setUserStatus(0);
//                user.setUserRole(0);
//                user.setPlanetCode("931");
//                user.setTags("[\"java\",\"python\",\"c++\",\"女\",\"大五\",\"大六\"]");
//                userList.add(user);
//                if (j % batchSize == 0 ){
//                    break;
//                }
//            }
//            //异步执行
//            CompletableFuture<Void> future = CompletableFuture.runAsync(() ->{
//                System.out.println("ThreadName：" + Thread.currentThread().getName());
//                userService.saveBatch(userList,batchSize);
//            },executorService);
//                futureList.add(future);
//        }
//        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
//
//        stopWatch.stop();
//        System.out.println( stopWatch.getLastTaskTimeMillis());
//
//    }
//}
