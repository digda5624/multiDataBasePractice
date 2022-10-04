package com.fisolution.multiDatabase.hyun.app.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@AllArgsConstructor
@Slf4j
public class ProxyHandler implements InvocationHandler {

    private Object target;
    // 트랜잭션 기능에 필요한 것 추상화 해놓은 상황
    private PlatformTransactionManager mainTxManager;
    private PlatformTransactionManager secondTxManager;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        PlatformTransactionManager txManager = getCurTxManger(method);
        TransactionStatus transactionStatus = txManager.getTransaction(new DefaultTransactionDefinition());

        try {
            Object result = method.invoke(target, args);
            txManager.commit(transactionStatus);
            return result;
        } catch (Exception exception) {
            txManager.rollback(transactionStatus);
            throw exception;
        }
    }

    private PlatformTransactionManager getCurTxManger(Method method){
        if(method.getName().startsWith("main")){
            log.info("main TxManager 호출 {}", mainTxManager.getClass());
            return mainTxManager;
        } else {
            log.info("second TxManager 호출 {}", secondTxManager.getClass());
            return secondTxManager;
        }
    }
}
