package org.dynafilter.controller;

import static org.dynafilter.controller.Address.generateAddress;
import static org.dynafilter.controller.User.generateUser;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Car {

	public final String id = "Car.class";
	private boolean newVehicle;
	private User user;
	private Address address;

	public User getUser() {
		return user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(final Address address) {
		this.address = address;
	}

	public boolean isNew() {
		return newVehicle;
	}

	public void setIsNew(final boolean isNew) {
		this.newVehicle = isNew;
	}

	public static Car generateCar() {
		final Car car = new Car();
		car.newVehicle = true;
		car.user = generateUser();
		car.address = generateAddress();
		return car;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(15, 25).append(address).append(user)
				.append(newVehicle).toHashCode();
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
		final Car car = (Car) obj;
		return new EqualsBuilder().append(id, car.id)
				.append(address, car.address)
				.append(newVehicle, car.newVehicle).append(user, car.user)
				.build();
	}

}
