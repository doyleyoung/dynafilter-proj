package com.github.bmsantos.dynafilter.controller;

import static com.github.bmsantos.dynafilter.controller.Address.generateAddress;
import static com.github.bmsantos.dynafilter.controller.Car.generateCar;
import static com.github.bmsantos.dynafilter.controller.Composite.generateComposite;
import static com.github.bmsantos.dynafilter.controller.User.generateUser;
import static java.util.Arrays.asList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import com.github.bmsantos.dynafilter.annotation.DynaFilter;
import com.github.bmsantos.dynafilter.annotation.DynaFilters;
import com.github.bmsantos.dynafilter.annotation.NamedDynaFilters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DynaFilterController {

	@RequestMapping(value = "simple", method = GET, produces = "application/json")
	@DynaFilter(value = User.class, fields = { "id", "name" })
	public @ResponseBody User returnSimple() {
		return generateUser();
	}

	@RequestMapping(value = "collection", method = GET, produces = "application/json")
	@DynaFilter(value = User.class, fields = { "id", "name" })
	public @ResponseBody List<User> returnCollection() {
		return asList(generateUser(), generateUser());
	}

	@RequestMapping(value = "hybrid", method = GET, produces = "application/json")
	@DynaFilters({
		@DynaFilter(value = User.class, fields = { "id", "name" } ),
		@DynaFilter(value = Address.class, fields = { "id", "users"} )
	})
	public @ResponseBody List<Object> returnHybrid() {
		return asList(generateUser(), generateAddress());
	}

	@RequestMapping(value = "composite", method = GET, produces = "application/json")
	@DynaFilters({
		@DynaFilter(value = Composite.class, fields = {
			"id", "nativeType", "simpleType", "list", "hybridList",
			"array", "hybridArray", "map", "hybridMap"
		}),
		@DynaFilter(value = User.class, fields = { "id", "name" }),
		@DynaFilter(value = Address.class, fields = { "id", "users" })
	})
	public @ResponseBody Composite returnComposite() {
		return generateComposite();
	}

	@RequestMapping(value = "empty", method = GET, produces = "application/json")
	@DynaFilter(value = String.class, fields = { "invalid" })
	public @ResponseBody Composite shouldBeEmpty() {
		return generateComposite();
	}

	@RequestMapping(value = "emptyarray", method = GET, produces = "application/json")
	@DynaFilter(value = String.class, fields = { "invalid" })
	public @ResponseBody List<Composite> shouldBeEmptyArray() {
		return asList(generateComposite());
	}

	@RequestMapping(value = "includenulls", method = GET, produces = "application/json")
	@DynaFilter(value = Composite.class, fields = "id", includeNulls = true)
	public @ResponseBody Composite shouldIncludeNulls() {
		return new Composite();
	}

	@RequestMapping(value = "named", method = GET, produces = "application/json")
	@NamedDynaFilters(value = { "userName", "addressUsers" })
	public @ResponseBody List<Object> shouldUseNamedFitlers() {
		return asList(generateUser(), generateAddress());
	}

	@RequestMapping(value = "accessors", method = GET, produces = "application/json")
	@DynaFilters({
		@DynaFilter(value = Car.class, fields = { "id", "isNew", "user", "address" }),
		@DynaFilter(value = User.class, fields = { "id", "name" } ),
		@DynaFilter(value = Address.class, fields = { "id", "users" } )
	})
	public @ResponseBody Car shouldSerializeAccessors() {
		return generateCar();
	}
}
