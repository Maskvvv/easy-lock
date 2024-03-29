package com.easy.lock.annotation;


import com.easy.lock.core.support.EasyLockEvaluationContextPostProcessor;
import com.easy.lock.core.support.LockProcessor;
import com.easy.lock.interceptor.BeanFactoryEasyLockAdvisor;
import com.easy.lock.interceptor.EasyLockInterceptor;
import com.easy.lock.support.DefaultLockProcessor;
import com.easy.lock.support.EasyLockValueParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

/**
 * <p>  </p>
 *
 * @author zhouhongyin
 * @since 2024/1/15 11:47
 */
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties({EasyLockProperties.class})
public class EasyLockProxyAutoConfiguration implements ImportAware {

    private final Log logger = LogFactory.getLog(getClass());

    private AnnotationAttributes enableEasyLock;

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public EasyLockOperationSource easyLockOperationSource() {
        return new EasyLockOperationSource();
    }


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanFactoryEasyLockAdvisor easyLockAdvisor() {
        BeanFactoryEasyLockAdvisor advisor = new BeanFactoryEasyLockAdvisor();
        advisor.setEasyLockOperationSource(easyLockOperationSource());
        advisor.setAdvice(easyLockInterceptor());
        advisor.setOrder(enableEasyLock.getNumber("order"));
        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public EasyLockInterceptor easyLockInterceptor() {
        EasyLockInterceptor interceptor = new EasyLockInterceptor();
        interceptor.setEasyLockOperationSource(easyLockOperationSource());
        return interceptor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public EasyLockValueParser easyLockValueParser(List<EasyLockEvaluationContextPostProcessor> evaluationContextPostProcessors) {
        EasyLockValueParser easyLockValueParser = new EasyLockValueParser();
        easyLockValueParser.setEvaluationContextPostProcessors(evaluationContextPostProcessors);
        return easyLockValueParser;
    }


    @Bean(DefaultLockProcessor.BEAN_NAME)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public LockProcessor lockProcessor() {
        return new DefaultLockProcessor();
    }


    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableEasyLock = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableEasyLock.class.getName(), false));
        if (this.enableEasyLock == null) {
            logger.info("EnableEasyLock is not present on importing class");
        }
    }
}
