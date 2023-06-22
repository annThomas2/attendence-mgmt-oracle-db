package com.test.attendancemgmtsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.attendancemgmtsystem.entity.Faculty;
import com.test.attendancemgmtsystem.exceptions.EnterValidLeaveException;
import com.test.attendancemgmtsystem.exceptions.InvalidCreationFacultytException;
import com.test.attendancemgmtsystem.exceptions.InvalidFacultyIdException;
import com.test.attendancemgmtsystem.exceptions.NonExistentFacultyIdException;
import com.test.attendancemgmtsystem.exceptions.NullFacultyException;
import com.test.attendancemgmtsystem.repository.AddressRepository;
import com.test.attendancemgmtsystem.repository.FacultyRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FacultyService {

	@Autowired
	FacultyRepository facultyRepo;
	@Autowired
	AddressRepository addressRepo;

	Faculty faculty;

	// Create a Faculty Record
	public Faculty addFaculty(Faculty faculty) throws Exception {

		if (facultyRepo.existsById(faculty.getFacultyId())) {
			throw new InvalidCreationFacultytException("Faculty with this ID already exists");
		} 
		else if (faculty.isFacultyFieldNull() == true || faculty.getAddress().isAddressFieldNull() == true) {
			throw new NullFacultyException("Faculty fields or address fields must not be null");
		}
		return facultyRepo.save(faculty);
	}

	// Read Faculty Record
	public List<Faculty> getFaculty() {
		try {
			return (List<Faculty>) facultyRepo.findAll();
		} catch (Exception e) {
			System.out.println(" getFaculty() " + e.getMessage());
		}
		return null;
	}

	// Update Faculty Record
	public Faculty updateFaculty(Faculty faculty) throws Exception {

		if (faculty.getFacultyId() == null) {
			throw new InvalidFacultyIdException("Faculty ID cant be null");
		} else if (faculty.isFacultyFieldNull() == true || faculty.getAddress().isAddressFieldNull() == true) {
			throw new NullFacultyException("Faculty or address fields must not be null");
		}
		return facultyRepo.save(faculty);
	}

	// Delete Student Record
	public void deleteByFacultyId(Integer id) throws Exception {

		if (!(facultyRepo.existsById(id))) {
			throw new NonExistentFacultyIdException("No Such Faculty exists");
		} else {
			try {
				// System.out.println(studentRepo.findById(id));
				facultyRepo.deleteById(id);
			} catch (Exception e) {
				System.out.println(" deleteByFacultyId() " + e.getMessage());
			}
		}
	}

	// Avail Leave
	public String availLeaveFaculty(int leaveNo, int id) throws Exception {

		if (facultyRepo.existsById(id)) {
			System.out.println("1st if");
			if (leaveNo >= 0) {
				System.out.println("2nd if");
				faculty = facultyRepo.findById(id).get();
				int a = faculty.getAvailableNumberOfLeaves();
				while (a != 0) {
					System.out.println("While");
					if (a - leaveNo >= 0) {
						System.out.println(faculty);
						System.out.println(a - leaveNo);
						faculty.setAvailableNumberOfLeaves(a - leaveNo);
						return "Leave Applied";
					} else
						return "Leaves cannot be granted due to insufficient leave balance";
				}
				return "Leaves cannot be granted due to insufficient leave balance";
			} else
				throw new EnterValidLeaveException("Enter a Number greater than 0");
		} else
			throw new NonExistentFacultyIdException("No Such Faculty exists");

	}
}
