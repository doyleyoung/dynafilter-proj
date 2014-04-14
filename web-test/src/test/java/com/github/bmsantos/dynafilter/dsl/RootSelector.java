package com.github.bmsantos.dynafilter.dsl;

import java.util.List;

public interface RootSelector {

	ObjectOp asObject() throws Exception;

	<T> T asObject(Class<T> type) throws Exception;

	ListOp asList() throws Exception;

	<T> List<T> asList(Class<T> type) throws Exception;
}
