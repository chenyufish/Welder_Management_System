//package com.fishman.management_backend.service;
//
//
//import com.fishman.management_backend.model.domain.User;
//import org.junit.Assert;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * 用户服务测试
// *
// */
//@SpringBootTest
//public class UserServiceTest {
//
//    @Resource
//    private UserService userService;
//
//    /**
//     * 测试添加用户
//     */
//    @Test
//    public void testAddUser() {
//        User user = new User();
//        user.setUsername("fishman");
//        user.setUserAccount("123");
//        user.setAvatarUrl("https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png");
//        user.setGender(0);
//        user.setUserPassword("xxx");
//        user.setPhone("123");
//        user.setEmail("456");
//        boolean result = userService.save(user);
//        System.out.println(user.getId());
//        Assertions.assertTrue(result);
//    }
//
//
//    /**
//     * 测试更新用户
//     */
//    @Test
//    public void testUpdateUser() {
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("dogfishman");
//        user.setUserAccount("123");
//        user.setAvatarUrl("https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png");
//        user.setGender(0);
//        user.setUserPassword("12345678");
//        user.setPhone("123");
//        user.setEmail("456");
//        boolean result = userService.updateById(user);
//        Assertions.assertTrue(result);
//    }
//
//    /**
//     * 测试删除用户
//     */
//    @Test
//    public void testDeleteUser() {
//        boolean result = userService.removeById(1L);
//        Assertions.assertTrue(result);
//    }
//
//
//    /**
//     * 测试获取用户
//     */
//    @Test
//    public void testGetUser() {
//        User user = userService.getById(1L);
//        Assertions.assertNotNull(user);
//    }
//
//    /**
//     * 测试用户注册
//     */
//    @Test
//    void userRegister() {
//        String userAccount = "fishman";
//        String userPassword = "";
//        String checkPassword = "123456";
//        String planetCode = "1";
//        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        userAccount = "yu";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        userAccount = "fishman";
//        userPassword = "123456";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        userAccount = "yu pi";
//        userPassword = "12345678";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        checkPassword = "123456789";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        userAccount = "dogfishman";
//        checkPassword = "12345678";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        userAccount = "fishman";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//    }
//
//    @Test
//    void testSearchUsersByTags(){
//        List<String> tagNameList= Arrays.asList("java","python");
//        List<User> userList=userService.searchUsersByTags(tagNameList);
//        Assert.assertNotNull(userList);
//    }
//}