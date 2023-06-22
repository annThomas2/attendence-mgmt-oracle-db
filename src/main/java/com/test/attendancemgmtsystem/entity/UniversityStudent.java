package com.test.attendancemgmtsystem.entity;

import java.util.Objects;
import java.util.stream.Stream;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "STUDENT")
public class UniversityStudent {
	@Id
	private Integer studentId;
	private String name;
	private Character gender;
	private Integer departmentId;
	private Integer availableNumberOfLeaves = 7;
	private Long phoneNumber;
	@OneToOne(cascade = CascadeType.ALL)
	Address address;

	// Getters
	public Integer getStudentId() {
		return studentId;
	}

	public String getName() {
		return name;
	}

	public Character getGender() {
		return gender;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public Integer getAvailableNumberOfLeaves() {
		return availableNumberOfLeaves;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public Address getAddress() {
		return address;
	}

	// Setters
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public void setAvailableNumberOfLeaves(Integer availableNumberOfLeaves) {
		this.availableNumberOfLeaves = availableNumberOfLeaves;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	// Default Constructor
	public UniversityStudent() {
		this.address = new Address();

	}

	public UniversityStudent(Integer studentId, String name, Character gender, Integer departmentId, Integer availableNumberOfLeaves,
			Long phonenumber, String city, String addressLine1, String addressLine2, String country, Integer pinCode) {
		super();
		this.studentId = studentId;
		this.name = name;
		this.gender = gender;
		this.departmentId = departmentId;
		this.availableNumberOfLeaves = availableNumberOfLeaves;
		this.phoneNumber = phonenumber;
		this.address = new Address(city, addressLine1, addressLine2, country, pinCode);
	}

	@Override
	public String toString() {
		return "UniversityStudent [studentId=" + studentId + ", name=" + name + ", gender=" + gender + ", departmentId="
				+ departmentId + ", availableNumberOfLeaves=" + availableNumberOfLeaves + ", phoneNumber=" + phoneNumber
				+ ", address=" + address + "]";
	}

	public boolean isFieldNull() {
		return Stream.of(studentId, name, gender, departmentId, availableNumberOfLeaves, phoneNumber)
				.anyMatch(Objects::isNull);
	}

}