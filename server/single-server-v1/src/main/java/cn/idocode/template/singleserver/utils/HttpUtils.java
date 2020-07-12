package cn.idocode.template.singleserver.utils;

import cn.idocode.template.singleserver.model.ResultCode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author levic
 * 2020-07-12 18:20:59
 */
@Slf4j
public class HttpUtils {
    private HttpUtils() {
    }

    /**
     * demo for http get
     */
    public static HttpResp<String> get(String requestUrl, Map<String, String> headers, Map<String, Object> requesttParams) {
        HttpResp<String> result = new HttpResp<>();
        HttpResponse<String> resp = null;
        try {
            resp = Unirest.get(requestUrl)
                    .queryString(requesttParams)
                    .headers(headers)
                    .asString();
        } catch (UnirestException e) {
            log.error("HttpUtils get unirest error.requestUrl:" + requestUrl + " headers:" + headers + " requestParams:" + requesttParams, e);
            return result;
        }
        if (resp.getStatus() != ResultCode.OK.getCode()) {
            log.error("HttpUtils get unirest error.requestUrl:{}, headers:{}, requestParams:{}, response status is not 200. response:{}"
                    , requesttParams, headers, requesttParams, resp);
            result.setCode(resp.getStatus());
            result.setMsg(resp.getStatusText());
            return result;
        }
        result.setCode(ResultCode.OK.getCode());
        result.setBody(resp.getBody());
        return result;
    }

    @Data
    public static class HttpResp<T> {
        private int code = -1;
        private T body;
        private String msg;
    }
}
