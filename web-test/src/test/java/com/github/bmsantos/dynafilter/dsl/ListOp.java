package com.github.bmsantos.dynafilter.dsl;

public interface ListOp {

	int size();

	ObjectOp get(final int index);

	<T> T get(final int index, Class<T> type) throws Exception;

	ListOp getList(final int index);

	boolean isList(final int index);
}
