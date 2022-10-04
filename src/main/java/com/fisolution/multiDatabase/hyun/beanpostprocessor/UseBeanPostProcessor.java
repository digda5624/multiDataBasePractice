package com.fisolution.multiDatabase.hyun.beanpostprocessor;

import com.fisolution.multiDatabase.hyun.advisor.TxAdvice;
import com.fisolution.multiDatabase.hyun.app.repository.UseDynamicProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
public class UseBeanPostProcessor implements BeanPostProcessor {

    private final PlatformTransactionManager mainTxManager;
    private final PlatformTransactionManager secondTxManager;

    /**
     * 객체 생성 이후에 @PostConstruct 같은 초기화가 발생하기 전에 호출되는 포스트 프로세서
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        log.info("모든 bean에 적용이 된다 {}", beanName);
        return bean;
    }

    /**
     * 객체 생성 이후에 @PostConstruct 같은 초기화가 발생한 다음에 호출되는 포스트 프로세서
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 주의!! instanceof 를 사용해야 한다!
        if(bean instanceof UseDynamicProxy){
            log.info("UseDynamicProxy.class 에만 적용 된다.");
            ProxyFactory factory = new ProxyFactory(bean);
            factory.addAdvice(new TxAdvice(mainTxManager, secondTxManager));
            return factory.getProxy();
        }
        return bean;
    }
}
