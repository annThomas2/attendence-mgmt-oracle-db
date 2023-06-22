package com.test.attendancemgmtsystem.entity;

import java.util.Objects;
import java.util.stream.Stream;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Faculty {

	@Id
	private Integer facultyId;
	private String name;
	private char gender;
	private int departmentId;
	private int availableNumberOfLeaves = 10;
	private long phoneNumber;
	@OneToOne(cascade = CascadeType.ALL)
	Address address;

	public Faculty() {

		this.address = new Address();
	}

	public Integer getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(Integer facultyId) {
		this.facultyId = facultyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public int getAvailableNumberOfLeaves() {
		return availableNumberOfLeaves;
	}

	public void setAvailableNumberOfLeaves(int availableNumberOfLeaves) {
		this.availableNumberOfLeaves = availableNumberOfLeaves;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Faculty(Integer facultyId, String name, char gender, int departmentId, int availableNumberOfLeaves,
			long phoneNumber,String city, String addressLine1, String addressLine2, String country, int pinCode) {
		super();
		this.facultyId = facultyId;
		this.name = name;
		this.gender = gender;
		this.departmentId = departmentId;
		this.availableNumberOfLeaves = availableNumberOfLeaves;
		this.phoneNumber = phoneNumber;
		this.address = new Address(city, addressLine1, addressLine2, country, pinCode);
	}
	public boolean isFacultyFieldNull() {
		return Stream.of(facultyId, name, gender, departmentId, availableNumberOfLeaves, phoneNumber)
				.anyMatch(Objects::isNull);
	}

	

}
