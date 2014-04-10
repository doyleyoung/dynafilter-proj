package org.dynafilter.controller;

import static java.util.Arrays.asList;
import static org.dynafilter.controller.Address.generateAddress;
import static org.dynafilter.controller.User.generateUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Composite {

	public String id;

	public String nativeType;

	public User simpleType;

	public List<User> list;

	public List<Object> hybridList;

	public User array[];

	public Object hybridArray[];

	public Map<String, User> map;

	public Map<String, Object> hybridMap;

	public final static Composite generateComposite() {
		final Composite composite = new Composite();
		composite.id = "Composite.class";
		composite.nativeType = "Native Type";
		composite.simpleType = generateUser();
		composite.list = asList(generateUser(), generateUser());
		composite.hybridList = asList(generateUser(), generateAddress());
		composite.array = new User[] { generateUser(), generateUser() };
		composite.hybridArray = new Object[] { generateUser(), generateAddress() };
		composite.map = new HashMap<String, User>() {{
			put("user1", generateUser()); put("user2", generateUser());
		}};
		composite.hybridMap = new HashMap<String, Object>() {{
			put("user", generateUser()); put("address", generateAddress());
		}};
		return composite;
	}

}
