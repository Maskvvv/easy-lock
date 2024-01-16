package com.example.lock;

import com.easy.lock.support.KeyConvert;
import com.example.domain.DemoParam;
import org.springframework.stereotype.Component;

/**
 * <p> </p>
 *
 * @author zhouhongyin
 * @since 2024/1/16 11:32
 */
@Component
public class DemoConvert implements KeyConvert {

    @Override
    public String getKey(Object... params) {
        DemoParam param = (DemoParam) params[0];

        return param.getId();
    }
}
