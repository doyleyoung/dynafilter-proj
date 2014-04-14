package com.github.bmsantos.dynafilter.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.Mapping;

@Target(value = { METHOD })
@Retention(value = RUNTIME)
@Inherited
@Mapping
public @interface DynaFilters {
	DynaFilter[] value();
}
