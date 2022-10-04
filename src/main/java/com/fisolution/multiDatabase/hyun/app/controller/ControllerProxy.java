package com.fisolution.multiDatabase.hyun.app.controller;

import com.fisolution.multiDatabase.hyun.Member;
import com.fisolution.multiDatabase.hyun.app.repository.UseDynamicProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

//@RestController
@RequiredArgsConstructor
public class ControllerProxy {

    private final UseDynamicProxy useDynamicProxy;

    @GetMapping("/v4/mainsave")
    public Long mainSave() {
        Member member = useDynamicProxy.mainSave(new Member());
        return member.getId();
    }

    @GetMapping("/v4/secondsave")
    public Long secondSave() {
        Member member = useDynamicProxy.secondSave(new Member());
        return member.getId();
    }
}
