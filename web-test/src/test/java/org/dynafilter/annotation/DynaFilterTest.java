package org.dynafilter.annotation;

import static java.util.Arrays.asList;
import static org.dynafilter.controller.Address.generateAddress;
import static org.dynafilter.controller.Composite.generateComposite;
import static org.dynafilter.controller.User.generateUser;
import static org.dynafilter.dsl.JSonDeserializer.useJson;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.dynafilter.controller.Address;
import org.dynafilter.controller.Car;
import org.dynafilter.controller.Composite;
import org.dynafilter.controller.User;
import org.dynafilter.dsl.ListOp;
import org.dynafilter.dsl.ObjectOp;
import org.junit.Test;

public class DynaFilterTest {

	private final static User defaultUser = generateUser();
	private final static Address defaultAddress = generateAddress();
	private final static Composite defaultComposite = generateComposite();

	private final static String BASE_URL = "http://localhost:8080/jsonfilter/";

	@Test
	public void shouldFilterSimpleInstances() throws Exception {
		// When
		final String json = getJsonForEndpoint("simple");

		// Then
		final User user = useJson(json).asObject(User.class);

		assertThat("Should return user", user, equalTo(testUser()));
	}

	@Test
	public void shouldFilterListInstances() throws Exception {
		// When
		final String json = getJsonForEndpoint("collection");

		// Then
		final List<User> userList = useJson(json).asList(User.class);

		assertThat("Should return user list", userList, notNullValue());
		assertThat("Should have users", userList.size(), is(2));

		final User user = userList.get(0);
		assertThat("Should return user", user, equalTo(testUser()));
	}

	@Test
	public void shouldFilterMultipleSimpleInstances() throws Exception {
		// When
		final String json = getJsonForEndpoint("hybrid");

		// Then
		assertThat("Should return json", json, notNullValue());

		final ListOp list = useJson(json).asList();

		final User user = list.get(0, User.class);
		assertThat("Should return user", user, equalTo(testUser()));

		final Address address = list.get(1, Address.class);
		assertThat("Should return address", address, equalTo(testAddress()));
	}

	@Test
	public void shouldFilterCompositeInstances() throws Exception {
		// When
		final Composite testComposite = testComposite();

		final String json = getJsonForEndpoint("composite");

		// Then
		assertThat("Should return data", json, notNullValue());

		final Composite composite = useJson(json).asObject(Composite.class);

		assertThat("Should have same id", composite.id, equalTo(testComposite.id));
		assertThat("Should have same simple type", composite.simpleType, equalTo(testComposite.simpleType));
		assertThat("Should have same list", composite.list, equalTo(testComposite.list));
		assertThat("Should have same array", composite.array, equalTo(testComposite.array));
		assertThat("Should have same map", composite.map, equalTo(testComposite.map));

		final ListOp hybridList = useJson(json).asObject().getList("hybridList");
		assertThat("Should have same user in hybrid list", hybridList.get(0, User.class), equalTo(testUser()));
		assertThat("Should have same address in hybrid list", hybridList.get(1, Address.class), equalTo(testAddress()));

		final ListOp hybridArray = useJson(json).asObject().getList("hybridArray");
		assertThat("Should have same user in hybrid array", hybridArray.get(0, User.class), equalTo(testUser()));
		assertThat("Should have same address in hybrid array", hybridArray.get(1, Address.class), equalTo(testAddress()));

		final ObjectOp hybridMap = useJson(json).asObject().get("hybridMap");
		assertThat("Should have same user in hybrid map", hybridMap.get("user", User.class), equalTo(testUser()));
		assertThat("Should have same address in hybrid map", hybridMap.get("address", Address.class), equalTo(testAddress()));
	}

	@Test
	public void shouldSerializeEmptyObject() throws Exception {
		// When
		final String json = getJsonForEndpoint("empty");

		// Then
		assertThat("Should return empty object", json, equalTo("{}"));
	}

	@Test
	public void shouldSerializeEmptyArray() throws Exception {
		// When
		final String json = getJsonForEndpoint("emptyarray");

		// Then
		assertThat("Should return empty array", json, equalTo("[{}]"));
	}

	@Test
	public void shouldIncludeNulls() throws Exception {
		// When
		final String json = getJsonForEndpoint("includenulls");

		// Then
		assertThat("Should return null values", json, containsString("null"));
	}

	@Test
	public void shouldSerializeNamedAnnotations() throws Exception {
		// When
		final String json = getJsonForEndpoint("named");

		// Then
		assertThat("Should return json", json, notNullValue());

		final ListOp list = useJson(json).asList();

		final User user = list.get(0, User.class);
		assertThat("Should return user", user, equalTo(testUser()));

		final Address address = list.get(1, Address.class);
		assertThat("Should return address", address, equalTo(testAddress()));
	}

	@Test
	public void shouldSerializeAccessors() throws Exception {
		// When
		final String json = getJsonForEndpoint("accessors");

		// Then
		final Car car = useJson(json).asObject(Car.class);

		assertThat("Should filter car", car, equalTo(testCar()));
	}

	private String getJsonForEndpoint(final String endpoint)
			throws IOException, MalformedURLException {
		return IOUtils.toString(new URL(BASE_URL + endpoint));
	}

	private User testUser() {
		final User user = new User();
		user.id = defaultUser.id;
		user.name = defaultUser.name;
		return user;
	}

	private Address testAddress() {
		final Address address = new Address();
		address.id = defaultAddress.id;
		address.users = asList(testUser(), testUser());
		return address;
	}

	@SuppressWarnings("serial")
	private Composite testComposite() {
		final Composite composite = new Composite();
		composite.id = defaultComposite.id;
		composite.nativeType = defaultComposite.nativeType;
		composite.simpleType = testUser();
		composite.list = asList(testUser(), testUser());
		composite.hybridList = asList(testUser(), testAddress());
		composite.array = new User[] { testUser(), testUser() };
		composite.hybridArray = new Object[] { testUser(), testAddress() };
		composite.map = new HashMap<String, User>() {{
			put("user1", testUser()); put("user2", testUser());
		}};
		composite.hybridMap = new HashMap<String, Object>() {{
			put("user", testUser()); put("address", testAddress());
		}};
		return composite;
	}

	private Car testCar() {
		final Car car = Car.generateCar();
		car.setUser(testUser());
		car.setAddress(testAddress());
		return car;
	}
}
