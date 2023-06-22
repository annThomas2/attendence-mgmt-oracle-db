package com.test.attendancemgmtsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.attendancemgmtsystem.entity.Address;
import com.test.attendancemgmtsystem.entity.UniversityStudent;
import com.test.attendancemgmtsystem.exceptions.EnterValidLeaveException;
import com.test.attendancemgmtsystem.exceptions.InvalidCreationStudentException;
import com.test.attendancemgmtsystem.exceptions.InvalidStudentIdException;
import com.test.attendancemgmtsystem.exceptions.NonExistentStudentIdException;
import com.test.attendancemgmtsystem.exceptions.NullStudentException;
import com.test.attendancemgmtsystem.repository.AddressRepository;
import com.test.attendancemgmtsystem.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StudentService {

	@Autowired
	StudentRepository studentRepo;
	@Autowired
	AddressRepository addressRepo;

	UniversityStudent student;

	// Create a Student Record
	public UniversityStudent addStudent(UniversityStudent student) throws NullStudentException,InvalidCreationStudentException {

		if (studentRepo.existsById(student.getStudentId())) {
					throw new InvalidCreationStudentException("Student with this ID already exists");
		} else if (student.isFieldNull() == true || student.getAddress().isAddressFieldNull() == true) {
			throw new NullStudentException("Student or address fields must not be null");
		}
		return studentRepo.save(student);

	}

	// Read Student Record
	public List<UniversityStudent> getStudent() {
		try {
			return (List<UniversityStudent>) studentRepo.findAll();
		} catch (Exception e) {
			System.out.println(" getStudent() " + e.getMessage());
		}
		return null;

	}

	// Update Student Record
	public UniversityStudent updateStudent(UniversityStudent student) throws NullStudentException,InvalidStudentIdException {
		if (student.getStudentId() == null) {
			throw new InvalidStudentIdException("Student ID cant be null");
		} else if (student.isFieldNull() == true || student.getAddress().isAddressFieldNull() == true) {
			throw new NullStudentException("Student or address fields must not be null");
		}
		return studentRepo.save(student);
	}

	// Delete Student Record
	public void deleteByStudId(Integer id) throws NonExistentStudentIdException {
		if (!(studentRepo.existsById(id))) {
			throw new NonExistentStudentIdException("No Such Student exists");
		} else {
			try {
				// System.out.println(studentRepo.findById(id));
				studentRepo.deleteById(id);
			} catch (Exception e) {
				System.out.println("deleteStudentById() " + e.getMessage());
			}
		}
	}

	// Update Student Address
	public Address updateStudentAddress(int id, Address studentAddress) throws Exception {

		if (studentAddress.isAddressFieldNull() == true) {
			throw new NullStudentException("Address fields must not be null");
		} else {
			UniversityStudent student = studentRepo.findById(id).get();
			student.setAddress(studentAddress);
			return studentAddress;
		}
	}

	// Avail Leave
	public String availLeaveStudent(int leaveNo, int id) throws Exception {
		if (studentRepo.existsById(id)) {
			System.out.println("1st if");
			if (leaveNo >= 0) {
				System.out.println("2nd if");
				student = studentRepo.findById(id).get();
				int a = student.getAvailableNumberOfLeaves();
				while (a != 0) {
					System.out.println("While");
					if (a - leaveNo >= 0) {
						System.out.println(student);
						System.out.println(a - leaveNo);
						student.setAvailableNumberOfLeaves(a - leaveNo);
						return "Leave Applied";
					} else
						return "Leaves cannot be granted due to insufficient leave balance";
				}
				return "Leaves cannot be granted due to insufficient leave balance";
			} else
				throw new EnterValidLeaveException("Enter a Number greater than 0");
		}
		else
			throw new NonExistentStudentIdException("No Such Student exists") ;

	}

}
