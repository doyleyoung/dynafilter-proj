package org.dynafilter.dsl;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSonDeserializer implements RootSelector {

	final ObjectMapper mapper = new ObjectMapper();
	private final String jsonDoc;

	public static RootSelector useJson(final String jsonDoc) {
		return new JSonDeserializer(jsonDoc);
	}

	private JSonDeserializer(final String jsonDoc) {
		this.jsonDoc = jsonDoc;
	}

	@Override
	public ObjectOp asObject() throws Exception {
		return new ObjectOpImpl(mapper.readValue(jsonDoc, Map.class));
	}

	@Override
	public ListOp asList() throws Exception {
		return new ListOpImpl(mapper.readValue(jsonDoc, List.class));
	}

	@Override
	public <T> T asObject(final Class<T> type) throws Exception {
		return mapper.<T>readValue(jsonDoc, mapper.getTypeFactory().constructType(type));
	}

	@Override
	public <T> List<T> asList(final Class<T> type) throws Exception {
		return mapper.<List<T>> readValue(jsonDoc, mapper.getTypeFactory()
				.constructCollectionType(List.class, type));
	}
}
