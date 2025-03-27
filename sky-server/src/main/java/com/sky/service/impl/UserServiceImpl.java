package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    WeChatProperties weChatProperties;

    @Autowired
    UserMapper userMapper;
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxlogin(UserLoginDTO userLoginDTO) {
        String openid = getOpenId(userLoginDTO.getCode());

        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        User user = userMapper.getUserByOpenId(openid);

        if(user == null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        return user;
    }

    private String getOpenId(String code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", weChatProperties.getAppid());
        params.put("secret", weChatProperties.getSecret());
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, params);

        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
