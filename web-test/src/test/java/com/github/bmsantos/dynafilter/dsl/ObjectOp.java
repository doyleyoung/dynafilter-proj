package com.github.bmsantos.dynafilter.dsl;

import java.util.List;

public interface ObjectOp {

	ObjectOp get(final String fieldName);

	<T> T get(final String fieldName, Class<T> type) throws Exception;

	ListOp getList(final String fieldName);

	<T> List<T> getList(final String fieldName, final Class<T> type) throws Exception;

}
