package com.amaan.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * 佛祖保佑，永无BUG
 * 处理重复请求,同一用户不同请求参数不算重复，但类似请求时间等也算重复
 * 根据请求参数生成唯一ID，存入Redis并设置存活时间
 * 请求每次来都计算然后去redis中看有没有，有的话就不再执行
 * 接口幂等性的一种考虑
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-12-26 13:25
 */
@Component
public class ReqDedupHelper {

    private static Logger log = LoggerFactory.getLogger(ReqDedupHelper.class);

    /**
     * 需剔除的参数如请求时间，经纬度坐标等在重复请求中也会不同的部分
     * @param reqJSON 请求的参数，这里通常是JSON
     * @param excludeKeys 请求参数里面要去除哪些字段再求摘要
     * @return 去除参数的MD5摘要
     */
    public static String dedupParamMD5(final String reqJSON, String... excludeKeys){
        String decreptParam = reqJSON;
        TreeMap paramMap = JSON.parseObject(decreptParam,TreeMap.class);
        if (paramMap!=null){
            List<String> dedupExcludeKeys = Arrays.asList(excludeKeys);
            if (!dedupExcludeKeys.isEmpty()){
                for (String dedupExcludeKey : dedupExcludeKeys) {
                    paramMap.remove(dedupExcludeKey);
                }
            }
        }
        String paramTreeMapJSON = JSON.toJSONString(paramMap);
        String md5 = DigestUtils.md5DigestAsHex(paramTreeMapJSON.getBytes());
        log.debug("md5dedupParam = {}, excludeKeys = {}， paramTreeMapJSON = {}", md5, Arrays.deepToString(excludeKeys), paramTreeMapJSON);
        return md5;
    }
}
