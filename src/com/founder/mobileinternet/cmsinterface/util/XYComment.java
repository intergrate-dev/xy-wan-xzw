package com.founder.mobileinternet.cmsinterface.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 外网api描述注解
 * <ul>
 * <li>{@code name} 名称
 * <li>{@code comment} 描述信息
 * </ul>
 * @author han.xf
 *
 */
@Target({ElementType.TYPE,ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD}) 
@Retention(RetentionPolicy.RUNTIME) 
@Documented 
public @interface XYComment {
	String name() ;
	String comment() default "" ;
}
