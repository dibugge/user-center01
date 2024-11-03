package com.yupi.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.usercenter.common.BaseResponse;
import com.yupi.usercenter.common.ErrorCode;
import com.yupi.usercenter.common.ResultUtils;
import com.yupi.usercenter.exception.BusinessException;
import com.yupi.usercenter.model.domain.User;
import com.yupi.usercenter.model.domain.request.UserLoginRequest;
import com.yupi.usercenter.model.domain.request.UserRegisterRequest;
import com.yupi.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yupi.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.yupi.usercenter.constant.UserConstant.USER_LOGIN_STATE;


@RestController
@RequestMapping("/user")
public class userController {
    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/rigister")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount= userRegisterRequest.getUserAccount();
        String userPassword=userRegisterRequest.getUserPassword();
        String checkPassword= userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest ==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount= userLoginRequest.getUserAccount();
        String userPassword=userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 查询用户
     * @param username
     * @return
     */
   @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request){
       //仅仅是管理员才能查询
       if (!isAdmin(request)){
           throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
       }
       QueryWrapper<User> queryWrapper = new QueryWrapper<>();
       if (StringUtils.isNotBlank(username)){
           queryWrapper.like("username",username);
       }
       List<User> userList = userService.list(queryWrapper);
       List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
       return ResultUtils.success(list);
   }

    /**
     * 删除用户
     * @return
     */
   @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id,HttpServletRequest request){
       if (!isAdmin(request)){
           throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
       }
       if (id<=0){
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
       }
       boolean result = userService.removeById(id);
       return ResultUtils.success(result);
   }

    /**
     * 用户注销
     * @param request
     * @return
     */
   @PostMapping("/logout")
   public BaseResponse<Boolean> userLogout(@RequestBody HttpServletRequest request){
       if (request==null){
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
       }
       boolean result = userService.userLogout(request);
       return  ResultUtils.success(result);
   }

   private boolean isAdmin(HttpServletRequest request){
       Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
       User user=(User)userObj;
       return user != null && user.getUserRole() == ADMIN_ROLE;
   }
}
