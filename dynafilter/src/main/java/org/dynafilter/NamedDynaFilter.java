package org.dynafilter;

import java.lang.annotation.Annotation;

import javax.enterprise.util.AnnotationLiteral;

import org.dynafilter.annotation.DynaFilter;

public class NamedDynaFilter extends AnnotationLiteral<DynaFilter> implements DynaFilter {

	private static final long serialVersionUID = 4743462617169096720L;

	private final String name;
	private final Class<?> type;
	private boolean includeNulls = false;
	private final String[] fields;

	public NamedDynaFilter(final String name, final Class<?> type, final String... fields) {
		this.name = name;
		this.type = type;
		this.fields = fields;
	}

	public NamedDynaFilter(final String name, final Class<?> type, final boolean includeNulls, final String... fields) {
		this.name = name;
		this.type = type;
		this.includeNulls = includeNulls;
		this.fields = fields;
	}

	public String getName() {
		return name;
	}

	public Class<?> getType() {
		return type;
	}

	public boolean getIncludeNulls() {
		return includeNulls;
	}

	public String[] getFields() {
		return fields;
	}

	@Override
	public Class<?> value() {
		return type;
	}

	@Override
	public String[] fields() {
		return fields;
	}

	@Override
	public boolean includeNulls() {
		return includeNulls;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return DynaFilter.class;
	}

}
