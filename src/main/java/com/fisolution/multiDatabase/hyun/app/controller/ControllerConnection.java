package com.fisolution.multiDatabase.hyun.app.controller;

import com.fisolution.multiDatabase.hyun.Member;
import com.fisolution.multiDatabase.hyun.app.repository.UseConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
public class ControllerConnection {

    private final UseConnection useConnection;

    @GetMapping("/v1/mainsave")
    public Long mainSave() throws SQLException {
        Member member = useConnection.mainSave(new Member());
        return member.getId();
    }

    @GetMapping("/v1/secondsave")
    public Long secondSave() throws SQLException {
        Member member = useConnection.secondSave(new Member());
        return member.getId();
    }
}
