package cn.jpush.commons.utils.beans;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;

/**
 * 自动命名 bean name
 * @author Roc
 * 	<context:component-scan base-package="cn.itcast.spring0401.jdbc.transaction.annotation"  name-generator="cn.itcast.spring0401.jdbc.transaction.annotation.SpringAnnotationBeanNameGenerator"></context:component-scan>
 *
 */
public class SpringAnnotationBeanNameGenerator extends AnnotationBeanNameGenerator {


	@Override
	protected String buildDefaultBeanName(BeanDefinition definition) {
		String className = definition.getBeanClassName();
		if(className.endsWith("ServiceImpl")){
			String shortName = ClassUtils.getShortName(className.replace("Impl", ""));
			return Introspector.decapitalize(shortName);
		}
		return super.buildDefaultBeanName(definition);
	}

//	@Override
//	protected String determineBeanNameFromAnnotation(
//			AnnotatedBeanDefinition arg0) {
//		return super.determineBeanNameFromAnnotation(arg0);
//	}
//
//	@Override
//	public String generateBeanName(BeanDefinition arg0,
//			BeanDefinitionRegistry arg1) {
//		return super.generateBeanName(arg0, arg1);
//	}
//
//	@Override
//	protected boolean isStereotypeWithNameValue(String annotationType,
//			Set<String> metaAnnotationTypes, Map<String, Object> attributes) {
//		return super.isStereotypeWithNameValue(annotationType, metaAnnotationTypes,
//				attributes);
//	}
//	

}
