package com.fisolution.multiDatabase.hyun.config;

import com.fisolution.multiDatabase.hyun.advisor.TxAdvice;
import com.fisolution.multiDatabase.hyun.beanpostprocessor.UseBeanPostProcessor;
import com.fisolution.multiDatabase.hyun.app.repository.DynamicProxyImpl;
import com.fisolution.multiDatabase.hyun.app.repository.ProxyHandler;
import com.fisolution.multiDatabase.hyun.app.repository.UseDynamicProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@RequiredArgsConstructor
@PropertySource(value = {"classpath:application.yml"})
public class MainDB {

    private final Environment env;


    @Bean
    @Primary
    public PlatformTransactionManager mainTransactionManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(mainEntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public PlatformTransactionManager secondTransactionManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(secondEntityManagerFactory().getObject());
        return transactionManager;
    }

    /**
     * ?????? jpaVendorAdaptor - JPA ????????? ???????????? ????????? ???????????? ??????????????? ?????????,
     * JpaVendorAdapter??? ???????????? ???????????? ????????? ?????? ??????????????? ????????? ????????? ?????????.
     * @see JpaVendorAdapter
     */

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean mainEntityManagerFactory(){
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(mainDataSource());
        // EntityScan ?????? package ??????
        entityManagerFactory.setPackagesToScan(new String[]{"com.fisolution.multiDatabase"});
        // Hibernate ?????? ??????
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        entityManagerFactory.setJpaPropertyMap(properties);
        // getObject ??? ????????? entityMangerFactory??? ?????? ?????? ?????? ?????? LoccalContainerEntityMangerFactoryBean ??? ????????? ??? ??? ??????.
        // ???????????? ????????? ????????? ??????
        return entityManagerFactory;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(){
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(secondDataSource());
        // EntityScan ?????? package ??????
        entityManagerFactory.setPackagesToScan(new String[]{"com.fisolution.multiDatabase"});
        // Hibernate ?????? ??????
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        entityManagerFactory.setJpaPropertyMap(properties);
        return entityManagerFactory;
    }

    /**
     * main dataBase resource ??????
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.main")
    public DataSource mainDataSource() {
        return DataSourceBuilder
                .create()
                .build()
                ;
    }


    /**
     * second dataBase resource ??????
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.second")
    public DataSource secondDataSource(){
        return DataSourceBuilder
                .create()
                .build()
                ;
    }


    /**
     * ???????????? ????????? ????????? ?????? ????????? ????????? ?????? ????????????
     */
//    @Bean
//    @Primary
    public UseDynamicProxy useDynamicProxy(EntityManager mainEntityManager,
                                           @Qualifier("secondEntityManagerFactory") EntityManager secondEntityManager){
        PlatformTransactionManager mainTxManager = mainTransactionManager();
        PlatformTransactionManager secondTxManager = secondTransactionManager();

        UseDynamicProxy useDynamicProxy = new DynamicProxyImpl(mainEntityManager, secondEntityManager);
        ProxyHandler handler = new ProxyHandler(useDynamicProxy, mainTxManager, secondTxManager);

        return (UseDynamicProxy) Proxy.newProxyInstance(
                UseDynamicProxy.class.getClassLoader(),
                new Class[]{UseDynamicProxy.class},
                handler
        );
    }

//    @Bean
    public UseDynamicProxy UseAdvisor(EntityManager mainEntityManager,
                                      @Qualifier("secondEntityManagerFactory") EntityManager secondEntityManager){
        PlatformTransactionManager mainTxManager = mainTransactionManager();
        PlatformTransactionManager secondTxManager = secondTransactionManager();

        UseDynamicProxy useDynamicProxy = new DynamicProxyImpl(mainEntityManager, secondEntityManager);

        ProxyFactory factory = new ProxyFactory(useDynamicProxy);
        factory.addAdvice(new TxAdvice(mainTxManager, secondTxManager));
        return (UseDynamicProxy) factory.getProxy();
    }

//    @Bean
    public UseBeanPostProcessor useBeanPostProcessor(){
        return new UseBeanPostProcessor(mainTransactionManager(), secondTransactionManager());
    }

//    @Bean
    public UseDynamicProxy useBeanPost(EntityManager mainEntityManager,
                                           @Qualifier("secondEntityManagerFactory") EntityManager secondEntityManager){
        return new DynamicProxyImpl(mainEntityManager, secondEntityManager);
    }

//    @Bean
    public Advisor advisor(){
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("main*", "second*");
        return new DefaultPointcutAdvisor(pointcut, new TxAdvice(mainTransactionManager(), secondTransactionManager()));
    }
}
