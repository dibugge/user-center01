package com.yupi.usercenter.service;
import java.util.Date;

import com.yupi.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceTest {
   @Resource
    private UserService userService;
   @Test
   void testAddUser(){
       User user = new User();
       user.setUsername("catyupi");
       user.setUserAccount("321");
       user.setAvatarUrl("https://img1.baidu.com/it/u=467212011,1034521901&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=500");
       user.setGender(0);
       user.setProfile("");
       user.setUserPassword("");
       user.setEmail("2445300725@qq.com");
       user.setPhone("13456789001");
       boolean result = userService.save(user);
       System.out.println(user.getId());
   }
    @Test
    void testRegister(){
        String userAccount = "catyupi1";
        String userPassword = "123456";
        String checkPassword = "123456";
        String planetCode="001";
        long result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);


    }


}