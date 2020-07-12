package cn.idocode.template.singleserver.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpUtilsTest {
    // this is a get request demo test
    @Test
    void get() {
        String requestUrl = "http://www.baidu.com";
        HttpUtils.HttpResp<String> resp = HttpUtils.get(requestUrl, null, null);
        System.out.println(resp);
    }

}