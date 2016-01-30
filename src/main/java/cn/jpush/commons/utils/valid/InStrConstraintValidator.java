package cn.jpush.commons.utils.valid;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author yuanzp@jpush.cn
 * @date 2015-6-24
 * @desc   自定义 验证器的实现
 *    <bean id="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean" />
 *    <mvc:annotation-driven  conversion-service="conversionService"  validator="validator"/>
 */
public class InStrConstraintValidator implements ConstraintValidator<InStr, String> {

    String [] valuesIn ;
    @Override
    public void initialize(InStr constraintAnnotation) {
        valuesIn = constraintAnnotation.value() ;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if ( valuesIn !=null ) {
            for ( String v : valuesIn ) {
                if ( v.equals(value) ) {
                    return true ;
                }
            }
        }
        return false;
    }

}
