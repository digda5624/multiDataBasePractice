package com.fisolution.multiDatabase.hyun.app.repository;

import com.fisolution.multiDatabase.hyun.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;

//@Transactional(transactionManager = "mainTransactionManager")
@Slf4j
@Component
public class UseTransactionAnnotation {

    private final EntityManager mainEntityManager;
    private final EntityManager secondEntityManager;
    private final DataSource mainDataSource;
    private final DataSource secondDataSource;

    public UseTransactionAnnotation(EntityManager mainEntityManager, @Qualifier("secondEntityManagerFactory") EntityManager secondEntityManager, DataSource mainDataSource, @Qualifier("secondDataSource") DataSource secondDataSource) {
        this.mainDataSource = mainDataSource;
        this.secondDataSource = secondDataSource;
        this.mainEntityManager = mainEntityManager;
        this.secondEntityManager = secondEntityManager;
    }

//    @Transactional(transactionManager = "mainTransactionManager")
    @Transactional
    public Member mainSave(Member member){
        Map<Object, Object> resourceMap = TransactionSynchronizationManager.getResourceMap();
        log.info("============ main ==============");
        log.info(TransactionSynchronizationManager.getCurrentTransactionName());
        log.info(String.valueOf(mainEntityManager.getClass()));
        mainEntityManager.persist(member);
        return member;
    }

//    @Transactional(transactionManager = "mainTransactionManager")
    @Transactional
    public Member mainFind(Long id){
        Member member = mainEntityManager.find(Member.class, id);
        return member;
    }

    @Transactional(transactionManager = "secondTransactionManager")
    public Member secondSave(Member member){
        Map<Object, Object> resourceMap = TransactionSynchronizationManager.getResourceMap();
        log.info("============ second ==============");
        log.info(TransactionSynchronizationManager.getCurrentTransactionName());
        log.info(String.valueOf(secondEntityManager.getClass()));
        secondEntityManager.persist(member);
        saveForTransactionTest(member);
//        mainSave(member);
        return member;
    }

    @Transactional(transactionManager = "secondTransactionManager")
    public Member saveForTransactionTest(Member member){
        Map<Object, Object> resourceMap = TransactionSynchronizationManager.getResourceMap();
        log.info(TransactionSynchronizationManager.getCurrentTransactionName());
        secondEntityManager.persist(member);
        return member;
    }

}
