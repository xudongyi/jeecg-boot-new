package org.jeecg.modules.business.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {

    /**
     * 数据表名称注解，默认值为类名称
     * @return
     */
    public boolean value() default true;
    public String name() default "update";
}
