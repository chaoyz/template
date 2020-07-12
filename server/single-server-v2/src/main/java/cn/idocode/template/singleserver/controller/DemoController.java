package cn.idocode.template.singleserver.controller;

import cn.idocode.template.singleserver.model.ResultMsg;
import cn.idocode.template.singleserver.model.User;
import cn.idocode.template.singleserver.service.DemoService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 基本http接口demo
 */
@Api
@RestController
@RequestMapping("/demo")
public class DemoController {

    private DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/echo")
    public String echo(@RequestParam("param") String param) throws Exception {
        return this.demoService.echo(param);
    }

    @GetMapping("/user/{userName}")
    public ResultMsg<User> getUser(@PathVariable String userName) {
        ResultMsg<User> resultMsg = new ResultMsg<>();
        User user = this.demoService.getUser(userName);
        resultMsg.setData(user);
        return resultMsg;
    }

    @GetMapping("/user1/{userName}")
    public ResultMsg<User> getUserWithNativeQuery(@PathVariable String userName) {
        ResultMsg<User> resultMsg = new ResultMsg<>();
        User user = this.demoService.getUserWithNativeSqlQuery(userName);
        resultMsg.setData(user);
        return resultMsg;
    }

    @GetMapping("/user2/{userName}")
    public ResultMsg<User> getUserWithEntityManager(@PathVariable String userName) {
        ResultMsg<User> resultMsg = new ResultMsg<>();
        User user = this.demoService.getUserWithEntityManager(userName);
        resultMsg.setData(user);
        return resultMsg;
    }

    @GetMapping("/user3/{userName}")
    public ResultMsg<User> getUserWithPage(@PathVariable String userName,
                                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "1") int pageSize) {
        ResultMsg<User> resultMsg = new ResultMsg<>();
        User user = this.demoService.getUserWithPageQuery(userName, page, pageSize);
        resultMsg.setData(user);
        return resultMsg;
    }


    @PostMapping("/user")
    public ResultMsg<User> saveUser(@RequestBody User user) {
        ResultMsg<User> resultMsg = new ResultMsg<>();
        User result = this.demoService.saveUser(user);
        resultMsg.setData(result);
        return resultMsg;
    }

    @PostMapping("/user1")
    public ResultMsg<User> saveUserWithNativeQuery(@RequestBody User user) {
        ResultMsg<User> resultMsg = new ResultMsg<>();
        User result = this.demoService.saveUserWithNativeSqlInsert(user);
        resultMsg.setData(result);
        return resultMsg;
    }

}
