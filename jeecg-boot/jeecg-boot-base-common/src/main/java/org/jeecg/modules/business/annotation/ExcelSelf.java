package org.jeecg.modules.business.annotation;

import javax.validation.groups.Default;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  类描述:  字典注解
 * 作    者： MYP
 * 日    期： 2020年07月23日-下午13:20
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelSelf {

    /**
     * 方法描述:  排序
     * 作    者： MYP
     * 日    期： 2020年07月23日-下午13:20
     *
     * @return 返回类型： int
     */
    int orderNum();
    /**
     * 方法描述:  列名
     * 作    者： MYP
     * 日    期： 2020年07月23日-下午13:20
     *
     * @return 返回类型： int
     */
    String name();
    /**
     * 方法描述:  父层  大的在前
     * 作    者： MYP
     * 日    期： 2020年07月23日-下午13:20
     *
     * @return 返回类型： int
     */
    String[] father() default {};

    int width() default 15;


    String format() default "";
    /**
     * 方法描述:  字典翻译
     * 作    者： MYP
     * 日    期： 2020年07月23日-下午13:20
     *
     * @return 返回类型： int
     */
    String dictType() default ""; //table  查询数据库  redis查询redis缓存  dict数据字典
    String dicCode() default "";//table的表名  redis的第一个键  dict的code
    String[] dicText() default {};//table的列 0-value的列 ，1-key的列 ---redis  和 dict 用不上
    //颜色
    boolean bgColor() default false;//颜色  特殊处理
}
