package com.easy.lock.annotation;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.lang.Nullable;

/**
 * <p>  </p>
 *
 * @author zhouhongyin
 * @since 2024/1/15 11:41
 */
public class EasyLockConfigureSelector extends AdviceModeImportSelector<EnableEasyLock> {

    @Override
    @Nullable
    public String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
            case PROXY:
                return new String[]{AutoProxyRegistrar.class.getName(), LogRecordProxyAutoConfiguration.class.getName()};
            case ASPECTJ:
                return new String[] {LogRecordProxyAutoConfiguration.class.getName()};
            default:
                return null;
        }
    }
}
