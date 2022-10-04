package com.fisolution.multiDatabase.hyun.app.controller;

import com.fisolution.multiDatabase.hyun.Member;
import com.fisolution.multiDatabase.hyun.app.repository.UseTransactionAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ControllerTransaction {

    private final UseTransactionAnnotation useTransactionAnnotation;

    public ControllerTransaction(UseTransactionAnnotation useTransactionAnnotation) {
        this.useTransactionAnnotation = useTransactionAnnotation;
    }

    @GetMapping("/v3/mainsave")
    public Long mainSave() {
        Member member = useTransactionAnnotation.mainSave(new Member());
//        log.info("");
        Member member1 = useTransactionAnnotation.mainFind(1L);
        Member member2 = useTransactionAnnotation.mainSave(new Member());
        return member.getId();
    }

    @GetMapping("/v3/secondsave")
    public Long secondSave() {
        Member member = useTransactionAnnotation.secondSave(new Member());
        return member.getId();
    }
}
