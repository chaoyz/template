package cn.idocode.template.singleserver.controller;

import cn.idocode.template.singleserver.model.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用controller
 *
 * @author levic
 * 2020/7/12 6:25 PM
 **/
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    /**
     * 服务检测接口
     */
    @GetMapping("/check.do")
    public ResultMsg check() {
        return new ResultMsg();
    }
}
