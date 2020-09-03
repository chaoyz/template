package cn.idocode.template.singleserver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

@Slf4j
public class HttpUtil {
    private static final ObjectMapper om = new ObjectMapper();

    static {
        Unirest.setTimeouts(1000, 120000);
    }

    private HttpUtil() {
    }

    /**
     * 读取cookie中的rtx用户名
     *
     * @param request http请求对象
     * @return 用户名，当cookie中无用户数据时返回null
     */
    public static String getLoginName(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String userName = null;
        for (Cookie cookie : cookies) {
            if ("ITIL_OA_NICK_NAME".equals(cookie.getName())) {
                // 此cookie有登录系统设置
                userName = cookie.getValue();
                break;
            }
        }
        if (userName == null) {
            // 如果cookie中无数据尝试从session中获取
            Object tmp = request.getSession().getAttribute("username");
            if (tmp != null) {
                userName = (String) tmp;
            }
        }
        return userName;
    }

    public static <T> T get(String requestUrl, Map<String, String> headers, Map<String, Object> queryString, Class<T> clazz) {
        T bodyObj = null;
        String body = get(requestUrl, headers, queryString);
        if (body != null) {
            try {
                bodyObj = om.readValue(body, clazz);
            } catch (JsonProcessingException e) {
                log.error("HttpUtil get request error. requestUrl:" + requestUrl + " headers:" + headers, e);
            }
        }
        return bodyObj;
    }

    public static String get(String requestUrl, Map<String, String> headers, Map<String, Object> queryString) {
        String result = null;
        try {
            HttpResponse<String> resp = Unirest.get(requestUrl)
                    .header("Content-Type", "application/json")
                    .headers(headers)
                    .queryString(queryString)
                    .asString();
            int status = resp.getStatus();
            if (status == 200) {
                result = resp.getBody();
            } else {
                log.error("HttpUtil get request response not 200. status:" + status + " requestUrl:" + requestUrl);
            }
        } catch (UnirestException e) {
            log.error("HttpUtil get request error. requestUrl:" + requestUrl + " headers:" + headers, e);
        }
        return result;
    }

    public static <T> T get(String requestUrl, Map<String, String> headers, Map<String, Object> queryString, TypeReference<T> typeReference) {
        T bodyObj = null;
        try {
            HttpResponse<String> resp = Unirest.get(requestUrl)
                    .header("Content-Type", "application/json")
                    .headers(headers)
                    .queryString(queryString)
                    .asString();
            int status = resp.getStatus();
            if (status == 200) {
                String b = resp.getBody();
                ObjectMapper om = new ObjectMapper();
                om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                bodyObj = om.readValue(b, typeReference);
            } else {
                log.error("HttpUtil get request response not 200. status:" + status + " requestUrl:" + requestUrl);
            }
        } catch (UnirestException | JsonProcessingException e) {
            log.error("HttpUtil get request error. requestUrl:" + requestUrl + " headers:" + headers, e);
        }
        return bodyObj;
    }

    public static String getImgBase64(String imgUrl) {
        String result = "";
        if (StringUtils.isEmpty(imgUrl)) {
            return result;
        }

        try {
            HttpResponse<InputStream> resp = Unirest.get(imgUrl).asBinary();
            if (resp.getStatus() == 200) {
                InputStream is = resp.getBody();
                byte[] buff = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int len = 0;
                while ((len = is.read(buff)) != -1) {
                    baos.write(buff, 0, len);
                }
                result = Base64.encodeBase64String(baos.toByteArray());
            }
        } catch (Exception e) {
            log.error("request img content failed. imgUrl:" + imgUrl, e);
        }
        return result;
    }

    public static byte[] getImgBytes(String imgUrl) {
        byte[] result = null;
        if (StringUtils.isEmpty(imgUrl)) {
            return result;
        }

        try {
            HttpResponse<InputStream> resp = Unirest.get(imgUrl)
                    .header("Accept", "image/webp,image/apng,image/*,*/*;q=0.8")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Encoding", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7").asBinary();
            if (resp.getStatus() == 200) {
                InputStream is = resp.getBody();
                byte[] buff = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int len = 0;
                while ((len = is.read(buff)) != -1) {
                    baos.write(buff, 0, len);
                }
                result = baos.toByteArray();
            }
        } catch (Exception e) {
            log.error("getImgBytes request img content failed. imgUrl:" + imgUrl, e);
        }
        return result;
    }

    public static <T> T post(String requestUrl, Map<String, String> headers, Map<String, Object> queryString, String requestBody, TypeReference<T> typeReference) {
        T bodyObj = null;
        try {
            HttpResponse<String> resp = Unirest.post(requestUrl)
                    .headers(headers)
                    .queryString(queryString)
                    .body(requestBody.getBytes())
                    .asString();
            int status = resp.getStatus();
            if (status == 200) {
                ObjectMapper om = new ObjectMapper();
                bodyObj = om.readValue(resp.getBody(), typeReference);
            } else {
                log.error("HttpUtil post request response not 200. status:" + status + " requestUrl:" + requestUrl);
            }
        } catch (UnirestException | JsonProcessingException e) {
            log.error("HttpUtil post request error. requestUrl:" + requestUrl + " headers:" + headers, e);
        }
        return bodyObj;
    }
}
