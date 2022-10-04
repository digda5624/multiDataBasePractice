package com.fisolution.multiDatabase.hyun.advisor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.Method;

@RequiredArgsConstructor
@Slf4j
public class TxAdvice implements MethodInterceptor {

    private final PlatformTransactionManager mainTxManager;
    private final PlatformTransactionManager secondTxManager;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("MethodInterceptor 활용");
        TransactionStatus status = null;
        PlatformTransactionManager txManager = null;
        try{

            Method method = invocation.getMethod();
            txManager = getCurTxManger(method);
            status = txManager.getTransaction(new DefaultTransactionDefinition());

            Object result = invocation.proceed();
            txManager.commit(status);
            return result;
        } catch (Throwable e) {
            txManager.rollback(status);
            throw new RuntimeException(e);
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
