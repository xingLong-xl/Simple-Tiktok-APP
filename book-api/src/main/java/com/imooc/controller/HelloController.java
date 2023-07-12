package com.imooc.controller;

import com.imooc.RabbitMQConfig;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.model.Stu;
import com.imooc.utils.SMSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "Hello 测试的接口")
@RestController
public class HelloController {

    @ApiOperation(value = "这是一个hello的测试路由")
    @GetMapping("hello")
    public Object hello(){
        Stu stu = new Stu();
        stu.setAge("20");
        stu.setName("zhangsan");
        //System.out.println(stu.toString());
        log.info(stu.toString());
        return GraceJSONResult.ok(stu) ;
    }

    @Autowired
    private SMSUtils smsUtils;
    @GetMapping("sms")
    public Object sms() throws Exception {
        String code = "123456";
        smsUtils.sendSMS("19989827639",code);
        return GraceJSONResult.ok();
    }

    @Autowired
    public RabbitTemplate rabbitTemplate;
    @GetMapping("produce")
    public Object produce() throws Exception {
       rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_MSG,
                            "sys.msg.send",
                               "我发了一个消息~~~");
        return GraceJSONResult.ok();
    }
}
