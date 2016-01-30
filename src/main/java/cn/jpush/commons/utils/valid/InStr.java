package cn.jpush.commons.utils.valid;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author yuanzp@jpush.cn
 * @date 2015-6-24
 * @desc   :  自定义验证注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InStrConstraintValidator.class)
public @interface InStr {
    String[] value();
    String message() default "不在正确的取值范围之中";
    Class<?>[] groups() default { };
    //负载
    Class<? extends Payload>[] payload() default { };
}
