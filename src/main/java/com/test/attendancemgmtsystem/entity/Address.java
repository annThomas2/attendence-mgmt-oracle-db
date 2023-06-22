package com.test.attendancemgmtsystem.entity;

import java.util.Objects;
import java.util.stream.Stream;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Address {
	
	private String city;
	@Id
	private String addressLine1;
	private String addressLine2;
	private String country;
	private Integer pinCode;
	
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Integer getPinCode() {
		return pinCode;
	}
	public void setPinCode(Integer pinCode) {
		this.pinCode = pinCode;
	}
	public Address(String city, String addressLine1, String addressLine2, String country, Integer pinCode) {
		super();
		this.city = city;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.country = country;
		this.pinCode = pinCode;
	}
	public Address() {
		
	}
	@Override
	public String toString() {
		return "Address [city=" + city + ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2
				+ ", country=" + country + ", pinCode=" + pinCode + "]";
	}
	
	public boolean isAddressFieldNull() {
		return Stream.of(city, addressLine1, addressLine2, country, pinCode)
				.anyMatch(Objects::isNull);
	}
	
	

}
