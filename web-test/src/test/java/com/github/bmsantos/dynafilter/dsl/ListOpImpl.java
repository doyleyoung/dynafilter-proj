package com.github.bmsantos.dynafilter.dsl;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("rawtypes")
public class ListOpImpl implements ListOp {

	private final ObjectMapper mapper = new ObjectMapper();
	private final List list;

	public ListOpImpl(final List list) {
		this.list = list;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public ObjectOp get(final int index) {
		final Object obj = list.get(index);
		if (Map.class.isAssignableFrom(obj.getClass())) {
			return new ObjectOpImpl((Map) obj);
		}
		return null;
	}

	@Override
	public ListOp getList(final int index) {
		final Object obj = list.get(index);
		if (List.class.isAssignableFrom(obj.getClass())) {
			return new ListOpImpl((List) obj);
		}
		return null;
	}

	@Override
	public boolean isList(final int index) {
		final Object obj = list.get(index);
		return List.class.isAssignableFrom(obj.getClass());
	}

	@Override
	public <T> T get(final int index, final Class<T> type) throws Exception {
		final Object obj = list.get(index);
		if (Map.class.isAssignableFrom(obj.getClass())) {
			final String jsonDoc = mapper.writeValueAsString(obj);
			return mapper.<T> readValue(jsonDoc, mapper.getTypeFactory().constructType(type));
		}
		return null;
	}

}
