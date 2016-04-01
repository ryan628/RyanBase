package ryan.com.librarybase.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类描述
 * 创建人 Ryan
 * 创建时间 2015/6/16 18:08.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Column {
    /**
     * 设置字段名
     *
     * @return
     */
    String name() default "";

    /**
     * 字段默认值
     *
     * @return
     */
    public String defaultValue() default "";
}
