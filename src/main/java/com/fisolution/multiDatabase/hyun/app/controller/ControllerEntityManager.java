package com.fisolution.multiDatabase.hyun.app.controller;

import com.fisolution.multiDatabase.hyun.Member;
import com.fisolution.multiDatabase.hyun.app.repository.UseEntityManger;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ControllerEntityManager {

    private final UseEntityManger useEntityManger;

    @GetMapping("/v2/mainsave")
    public Long mainSave() {
        Member member = useEntityManger.mainSave(new Member());
        return member.getId();
    }

    @GetMapping("/v2/secondsave")
    public Long secondSave() {
        Member member = useEntityManger.secondSave(new Member());
        return member.getId();
    }
}
