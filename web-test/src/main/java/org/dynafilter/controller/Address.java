package org.dynafilter.controller;

import static java.util.Arrays.asList;
import static org.dynafilter.controller.User.generateUser;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Address {
	public String id;
	public String address;
	public Integer postCode;
	public List<User> users;

	@Override
	public int hashCode() {
		return new HashCodeBuilder(11, 23).append(address)
				.append(postCode).append(users).toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		final Address adr = (Address) obj;
		return new EqualsBuilder().append(id, adr.id)
				.append(address, adr.address).append(postCode, adr.postCode)
				.append(users, adr.users).build();
	}

	public final static Address generateAddress() {
		final Address address = new Address();
		address.id = "Address.class";
		address.address = "Address to somewhere";
		address.postCode = 1234;
		address.users = asList(generateUser(), generateUser());
		return address;
	}
}
