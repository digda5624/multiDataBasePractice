package com.fisolution.multiDatabase.hyun.app.repository;

import com.fisolution.multiDatabase.hyun.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@Component
@Slf4j
public class UseEntityManger {

    /**
     * @apiNote spring 의 추상화 통해서 spring 의 DI 주입 Qualifier 를 사용하여 주입
     * @see LocalContainerEntityManagerFactoryBean
     */
    private final EntityManagerFactory mainEntityManagerFactory;
    private final EntityManagerFactory secondEntityManagerFactory;

    public UseEntityManger(EntityManagerFactory mainentityManagerFactory, @Qualifier("secondEntityManagerFactory") EntityManagerFactory secondentityManagerFactory) {
        this.mainEntityManagerFactory = mainentityManagerFactory;
        this.secondEntityManagerFactory = secondentityManagerFactory;
    }

    public Member mainSave(Member member){
        EntityManager em = mainEntityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        log.info(em.toString());
        try {
            tx.begin();
            em.persist(member);
            tx.commit();
        } catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        }
        return member;
    }

    public Member secondSave(Member member){
        EntityManager em = secondEntityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        log.info(em.toString());
        try {
            tx.begin();
            em.persist(member);
            tx.commit();
        } catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        }
        return member;
    }

    public Member mainSaveException(Member member){
        EntityManager em = mainEntityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(member);
            log.info("exception 발 생 시킴");
            throw new RuntimeException("mainException");
        } catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        }
        return member;
    }

    public Member secondSaveException(Member member){
        EntityManager em = secondEntityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(member);
            log.info("exception 발 생 시킴");
            throw new RuntimeException("secondException");
        } catch (Exception e){
            tx.rollback();
        }
        return member;
    }

}
