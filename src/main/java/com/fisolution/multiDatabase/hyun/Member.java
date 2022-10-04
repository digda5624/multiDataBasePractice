package com.fisolution.multiDatabase.hyun;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id @GeneratedValue
    Long id;

    public Member(String name) {
        this.name = name;
    }

    String name;
}
