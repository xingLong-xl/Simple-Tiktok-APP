/*
package com.imooc.controller;

import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseInfoPropertise {
        @Autowired
        public RedisOperator redis;

        public static final Integer COMMON_START_PAGE = 1;
        public static final Integer COMMON_PAGE_SIZE = 10;
        public static final String MOBILE_SMSCODE = "mobile:smscode";
        public static final String REDIS_USER_TOKEN = "redis_user_token";
        public static final String REDIS_USER_INFO = "redis_user_info";
        // 我的关注总数
        public static final String REDIS_MY_FOLLOWS_COUNTS = "redis_my_follows_counts";
        // 我的粉丝总数
        public static final String REDIS_MY_FANS_COUNTS = "redis_my_fans_counts";

        // 视频和发布者获赞数
        public static final String REDIS_VLOG_BE_LIKED_COUNTS = "redis_vlog_be_liked_counts";
        public static final String REDIS_VLOGER_BE_LIKED_COUNTS = "redis_vloger_be_liked_counts";



public Map<String, String> getErrors(BindingResult result){
                Map<String,String> map = new HashMap<>();
                List<FieldError> errors = result.getFieldErrors();
                for (FieldError ff : errors){
                        // 错误所对应的属性字段名
                        String field = ff.getField();
                        String msg = ff.getDefaultMessage();
                        map.put(field,msg);
                }
                return map;
        }

}
*/
