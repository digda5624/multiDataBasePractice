package com.fisolution.multiDatabase.hyun.app.controller;

import com.fisolution.multiDatabase.hyun.Member;
import com.fisolution.multiDatabase.hyun.app.repository.UseDynamicProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

//@RestController
@Slf4j
public class UseAdvisorController {

    public UseAdvisorController(UseDynamicProxy useDynamicProxy) {
        this.useDynamicProxy = useDynamicProxy;
    }

    private final UseDynamicProxy useDynamicProxy;

    @GetMapping("/v5/mainsave")
    public Long mainSave() {
        log.info("확인");
        Member member = useDynamicProxy.mainSave(new Member());
        return member.getId();
    }

    @GetMapping("/v5/secondsave")
    public Long secondSave() {
        Member member = useDynamicProxy.secondSave(new Member());
        return member.getId();
    }
}
