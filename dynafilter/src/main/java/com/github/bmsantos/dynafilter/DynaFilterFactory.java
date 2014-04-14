package com.github.bmsantos.dynafilter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

public class DynaFilterFactory implements InitializingBean {

	@Autowired
	private RequestMappingHandlerAdapter adapter;

	private List<NamedDynaFilter> namedFilters;

	@Override
	public void afterPropertiesSet() throws Exception {
		final List<HandlerMethodReturnValueHandler> handlers = new ArrayList<HandlerMethodReturnValueHandler>();

		decorateHandlers(handlers);

		adapter.setReturnValueHandlers(handlers);
	}

	public void setNamedFilters(final List<NamedDynaFilter> namedFilters) {
		this.namedFilters = namedFilters;
	}

	private void decorateHandlers(final List<HandlerMethodReturnValueHandler> handlers) {
		for (final HandlerMethodReturnValueHandler handler : adapter.getReturnValueHandlers()) {
			if (handler instanceof RequestResponseBodyMethodProcessor) {
				handlers.add(new DynaFilterHandler(handler, namedFilters));
			} else {
				handlers.add(handler);
			}
		}
	}

}
