package com.fisolution.multiDatabase.hyun.app.repository;

import com.fisolution.multiDatabase.hyun.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
public class DynamicProxyImpl implements UseDynamicProxy {

    private final EntityManager mainEntityManager;
    private final EntityManager secondaryEntityManager;

    @Override
    public Member mainSave(Member member) {
        mainEntityManager.persist(member);
        return member;
    }

    @Override
    public Member secondSave(Member member) {
        secondaryEntityManager.persist(member);
        return member;
    }
}
