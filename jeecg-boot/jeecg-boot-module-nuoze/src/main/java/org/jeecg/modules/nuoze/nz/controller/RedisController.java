package org.jeecg.modules.nuoze.nz.controller;


import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author channing
 * @since 2020-12-27
 */
@RestController
@RequestMapping("/redis")
@Api(tags="缓存管理")
@Slf4j
public class RedisController {
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("缓存列表接口")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result<?> list(){
        Set list = new HashSet<>();
       // list = redisUtil.keys("*");
        return Result.OK(list);
    }

    /**
     *
     * @param type 默认1,dictTable  2,dict  3,除tocken以外的缓存
     * @return
     */
    @ApiOperation("清除缓存接口dictTable")
    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public Result<?> clear(@RequestParam(name="type",required=false) String type){
        String s ="sys:cache:dictTable::SimpleKey*";
        if("2".equals(type)){
            s = "sys:cache:dict*";
        }
        redisUtil.del(s);
        return Result.OK();
    }
}