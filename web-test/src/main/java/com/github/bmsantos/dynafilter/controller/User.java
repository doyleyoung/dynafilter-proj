package com.github.bmsantos.dynafilter.controller;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class User {
	public String id;
	public String name;
	public String surname;
	public Integer age;

	@Override
	public int hashCode() {
		return new HashCodeBuilder(19, 27).append(name).append(surname)
				.append(age).toHashCode();
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
		final User user = (User) obj;
		return new EqualsBuilder().append(id, user.id).append(name, user.name)
				.append(surname, user.surname).append(age, user.age).build();
	}

	public final static User generateUser() {
		final User user = new User();
		user.id = "User.class";
		user.name = "User Name";
		user.surname = "User Surname";
		user.age = 33;
		return user;
	}

}
