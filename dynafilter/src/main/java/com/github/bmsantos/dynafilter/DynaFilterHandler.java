package com.github.bmsantos.dynafilter;

import static org.springframework.util.ClassUtils.isPrimitiveOrWrapper;
import static org.springframework.util.ReflectionUtils.findMethod;
import static org.springframework.util.ReflectionUtils.getField;
import static org.springframework.util.ReflectionUtils.invokeMethod;
import static org.springframework.util.StringUtils.capitalize;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.github.bmsantos.dynafilter.annotation.DynaFilter;
import com.github.bmsantos.dynafilter.annotation.DynaFilters;
import com.github.bmsantos.dynafilter.annotation.NamedDynaFilters;

@Component
public class DynaFilterHandler implements
HandlerMethodReturnValueHandler {

	private final HandlerMethodReturnValueHandler delegate;
	private final Map<String, DynaFilter> namedFilters = new HashMap<String, DynaFilter>();

	public DynaFilterHandler(final HandlerMethodReturnValueHandler delegate, final List<NamedDynaFilter> namedFilters) {
		this.delegate = delegate;
		processNamedFilters(namedFilters);
	}

	private void processNamedFilters(final List<NamedDynaFilter> namedFilters) {
		for (final NamedDynaFilter namedFilter : namedFilters) {
			this.namedFilters.put(namedFilter.getName(), namedFilter);
		}
	}

	@Override
	public void handleReturnValue(final Object returnValue,
			final MethodParameter returnType,
			final ModelAndViewContainer mavContainer,
			final NativeWebRequest webRequest) throws Exception {

		final Map<Class<?>, DynaFilter> objectMapper = processAnnotations(returnType);
		final Class<?> filterType = returnType.getMethod().getReturnType();

		final Object newReturnValue = processValue(returnValue, objectMapper, filterType);

		delegate.handleReturnValue(newReturnValue, returnType, mavContainer,
				webRequest);
	}

	@SuppressWarnings("unchecked")
	private Object processValue(final Object returnValue,
			final Map<Class<?>, DynaFilter> objectMapper,
			final Class<?> filterType) {

		if (isCollection(returnValue)) {
			final List<Object> data = new ArrayList<Object>();
			for (final Object item : (Collection<?>) returnValue) {
				data.add(processValue(item, objectMapper, item.getClass()));
			}
			return data;
		} else if (isArray(returnValue)) {
			final List<Object> data = new ArrayList<Object>();
			for (final Object item : (Object[]) returnValue) {
				data.add(processValue(item, objectMapper, item.getClass()));
			}
			return data;
		} else if (isMap(returnValue)) {
			final Map<Object, Object> data = new HashMap<Object,Object>();
			final Map<Object, Object> values = (Map<Object,Object>) returnValue;
			for (final Entry<Object,Object> entry: values.entrySet()) {
				data.put(entry.getKey(),
						processValue(entry.getValue(), objectMapper, entry
								.getValue().getClass()));
			}
			return data;
		}

		return filterData(returnValue, filterType, objectMapper);
	}

	private Map<String, Object> filterData(final Object value,
			final Class<?> type,
			final Map<Class<?>, DynaFilter> objectMapper) {

		final Map<String, Object> data = new HashMap<String, Object>();
		if (objectMapper.containsKey(type)) {
			final String[] fields = objectMapper.get(type).fields();
			if (fields != null) {
				for (final String field : fields) {
					Object result = obtainFieldValue(value, field);
					if (result != null) {
						if (!(isPrimitiveOrWrapper(result.getClass()) ||
								String.class.isAssignableFrom(result.getClass()))) {
							result = processValue(result, objectMapper, result.getClass());
						}
					}
					if (result != null || objectMapper.get(type).includeNulls()) {
						data.put(field, result);
					}
				}
			}
		}
		return data;
	}

	private Map<Class<?>, DynaFilter> processAnnotations(
			final MethodParameter returnType) {

		final Map<Class<?>, DynaFilter> objectMapper = new HashMap<Class<?>, DynaFilter>();

		final NamedDynaFilters named = returnType.getMethodAnnotation(NamedDynaFilters.class);
		final DynaFilters dynamicFilters = returnType.getMethodAnnotation(DynaFilters.class);
		final DynaFilter dynamicFilter = returnType.getMethodAnnotation(DynaFilter.class);

		if (dynamicFilters != null) {
			for (final DynaFilter filter : dynamicFilters.value()) {
				objectMapper.put(filter.value(), filter);
			}
		} else if (dynamicFilter != null) {
			objectMapper.put(dynamicFilter.value(), dynamicFilter);
		}

		if (named != null) {
			for (final String name : named.value()) {
				if (namedFilters.containsKey(name)) {
					final DynaFilter filter  = namedFilters.get(name);
					objectMapper.put(filter.value(), filter);
				}
			}
		}

		return objectMapper;
	}

	private Object obtainFieldValue(final Object value, final String field) {
		Object result = null;
		try {
			result = getField(value.getClass().getField(field), value);
		} catch (final NoSuchFieldException e) {
			Method method = findMethod(value.getClass(), "get" + capitalize(field));
			if (method == null) {
				method = findMethod(value.getClass(), field);
			}
			if (method != null) {
				result = invokeMethod(method, value);
			}
		}
		return result;
	}

	private boolean isCollection(final Object obj) {
		return Collection.class.isAssignableFrom(obj.getClass());
	}

	private boolean isArray(final Object obj) {
		return obj.getClass().isArray();
	}

	private boolean isMap(final Object obj) {
		return Map.class.isAssignableFrom(obj.getClass());
	}

	@Override
	public boolean supportsReturnType(final MethodParameter returnType) {
		return delegate.supportsReturnType(returnType);
	}

}
