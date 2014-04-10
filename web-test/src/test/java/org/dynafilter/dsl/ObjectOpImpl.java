package org.dynafilter.dsl;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("rawtypes")
public class ObjectOpImpl implements ObjectOp {

	private final ObjectMapper mapper = new ObjectMapper();
	private final Map obj;

	public ObjectOpImpl(final Map obj) {
		this.obj = obj;
	}

	@Override
	public ObjectOp get(final String fieldName) {
		if (obj.containsKey(fieldName)) {
			final Object field = obj.get(fieldName);
			if (Map.class.isAssignableFrom(field.getClass())) {
				return new ObjectOpImpl((Map) field);
			}
		}
		return null;
	}

	@Override
	public <T> T get(final String fieldName, final Class<T> type)
			throws Exception {
		if (obj.containsKey(fieldName)) {
			final Object field = obj.get(fieldName);
			if (Map.class.isAssignableFrom(field.getClass())) {
				final String jsonDoc = mapper.writeValueAsString(field);
				return mapper.<T>readValue(jsonDoc, mapper.getTypeFactory().constructType(type));
			}
		}
		return null;
	}

	@Override
	public ListOp getList(final String fieldName) {
		if (obj.containsKey(fieldName)) {
			final Object field = obj.get(fieldName);
			if (List.class.isAssignableFrom(field.getClass())) {
				return new ListOpImpl((List) field);
			}
		}
		return null;
	}

	@Override
	public <T> List<T> getList(final String fieldName, final Class<T> type)
			throws Exception {
		if (obj.containsKey(fieldName)) {
			final Object field = obj.get(fieldName);
			if (List.class.isAssignableFrom(field.getClass())) {
				final String jsonDoc = mapper.writeValueAsString(field);
				return mapper.<List<T>>readValue(jsonDoc, mapper.getTypeFactory().constructCollectionType(List.class, type));
			}
		}
		return null;
	}
}
