package com.test.attendancemgmtsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.test.attendancemgmtsystem.entity.Address;
import com.test.attendancemgmtsystem.entity.UniversityStudent;
import com.test.attendancemgmtsystem.exceptions.EnterValidLeaveException;
import com.test.attendancemgmtsystem.exceptions.InvalidStudentIdException;
import com.test.attendancemgmtsystem.exceptions.NonExistentStudentIdException;
import com.test.attendancemgmtsystem.exceptions.NullStudentException;
import com.test.attendancemgmtsystem.repository.AddressRepository;
import com.test.attendancemgmtsystem.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

	@Mock
	StudentRepository studentRepo;

	@Mock
	AddressRepository addressRepo;

	@InjectMocks
	StudentService studentService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

	}

	@Nested
	public class AddStudent {

		@Test
		@DisplayName("Positive Test case for Creation of StudentRecord")
		public void addStudent_SuccessTest() throws Exception {
			UniversityStudent student = new UniversityStudent(10, "Ann", 'F', 21, 7, 1234567890L, "City", "Line1",
					"Line2", "Country", 12345);
			when(studentRepo.save(any(UniversityStudent.class))).thenReturn(student);
			UniversityStudent actVal = studentService.addStudent(student);
			assertEquals(student.getDepartmentId(), actVal.getDepartmentId());
			assertEquals("Ann", actVal.getName());
			verify(studentRepo, times(1)).save(student);
		}

		@Test
		@DisplayName("Test case for Creation of StudentRecord with an existing Id'")
		public void addStudent_ExistingIdTest() {
			UniversityStudent student = new UniversityStudent(10, "Ann", 'F', 21, 7, 1234567890L, "City", "Line1",
					"Line2", "Country", 12345);
			when(studentRepo.existsById(student.getStudentId())).thenReturn(true);
			Throwable actVal = Assertions.assertThrows(Exception.class, () -> studentService.addStudent(student));
			String expected = "Student with this ID already exists";
			assertEquals(expected, actVal.getMessage());
		}

		@Test
		@DisplayName("Test case for Creation of StudentRecord with any field null")
		public void addStudent_NullFieldTest() {
			UniversityStudent student = new UniversityStudent(10, null, 'F', null, 7, 1234567890L, "City", "Line1",
					"Line2", "Country", 12345);
			// when(studentRepo.save(any(UniversityStudent.class))).thenReturn(student);
			Throwable actVal = Assertions.assertThrows(NullStudentException.class,
					() -> studentService.addStudent(student));
			String expected = "Student or address fields must not be null";
			assertEquals(expected, actVal.getMessage());
		}
	}

	@Nested
	public class ReadStudent {

		@Test
		@DisplayName("Positive Test case for Reading all StudentRecords")
		public void getStudentDetails_SuccessTest() {
			// Creating a list containing 2 students record
			List<UniversityStudent> studentList = new ArrayList<>(Arrays.asList(
					new UniversityStudent(10, "Ann", 'F', 21, 7, 1234567890L, "City", "Line1", "Line2", "Country",
							12345),
					new UniversityStudent(12, "Amy", 'F', 21, 7, 1234567890L, "City", "Line2", "Line3", "Country",
							12345)));
			when(studentRepo.findAll()).thenReturn(studentList);
			List<UniversityStudent> value = studentService.getStudent();
			System.out.println(value.get(1));
			assertEquals(2, value.size());
			verify(studentRepo, times(1)).findAll();
		}
	}

	@Nested
	public class UpdateStudent {
		@Test
		@DisplayName("Positive Test case for updating StudentRecord by Id")
		public void updateStudentDetails_SuccessTest() throws Exception {
			UniversityStudent student = new UniversityStudent(10, "Ann", 'F', 101, 7, 1234567890L, "City", "Line1",
					"Line2", "Country", 12345);
			when(studentRepo.save(any(UniversityStudent.class))).thenReturn(student);
			UniversityStudent val = studentService.updateStudent(student);
			assertEquals(student.getAvailableNumberOfLeaves(), val.getAvailableNumberOfLeaves());
			assertTrue(val.getDepartmentId() == 101);
			verify(studentRepo, times(1)).save(student);
		}

		@Test
		@DisplayName("Test case for updating id field as null")
		public void updateStudentDetails_NullIdTest() {
			UniversityStudent student = new UniversityStudent(null, "Ann", 'F', 21, 7, 1234567890L, "City", "Line1",
					"Line2", "Country", 12345);
			// when(studentRepo.save(any(UniversityStudent.class))).thenReturn(student);
			Exception actVal = Assertions.assertThrows(InvalidStudentIdException.class,
					() -> studentService.updateStudent(student));
			String expected = "Student ID cant be null";
			assertEquals(expected, actVal.getMessage());
		}

		@Test
		@DisplayName("Test Case for giving null fields in updation")
		public void updateStudentDetails_NullFieldsTest() {
			UniversityStudent student = new UniversityStudent(1, null, 'F', 21, 7, null, "City", "Line1", "Line2",
					"Country", 12345);
			// when(studentRepo.save(any(UniversityStudent.class))).thenReturn(student);
			Exception actVal = Assertions.assertThrows(NullStudentException.class,
					() -> studentService.updateStudent(student));
			String expected = "Student or address fields must not be null";
			assertEquals(expected, actVal.getMessage());
		}
	}

	@Nested
	public class DeleteStudent {

		@Test
		@DisplayName("Positive Test case for deleting StudentRecord by Id")
		public void deleteStudentRecordById_SuccessTest() {
			UniversityStudent student = new UniversityStudent(10, "Ann", 'F', 21, 7, 1234567890L, "City", "Line1",
					"Line2", "Country", 12345);
			int id = student.getStudentId();
			studentRepo.deleteById(id);
			verify(studentRepo, times(1)).deleteById(id);
		}

		@Test
		@DisplayName("Positive Test case for deleting StudentRecord of non existent Student by Id")
		public void deleteStudentRecordById_NonExistentStudentTest() {
			int id = 1;
			when(studentRepo.existsById(id)).thenReturn(false);
			Assertions.assertThrows(NonExistentStudentIdException.class, () -> studentService.deleteByStudId(id));
			verify(studentRepo, Mockito.never()).deleteById(id);
		}
	}

	@Nested
	public class UpdateStudentAddress {

		@Test
		@DisplayName("Positive Test case for updating Student address")
		public void updateStudentAddress_SuccessTest() throws Exception {
			int id = 1;
			UniversityStudent student = new UniversityStudent();
			Address address = new Address("Demo City", "Demo Line 1", "Demo Line 2", "Demo Country", 12346);
			when(studentRepo.findById(id)).thenReturn(Optional.of(student));
			Address newAddress = studentService.updateStudentAddress(id, address);
			assertEquals(newAddress.getAddressLine1(), student.getAddress().getAddressLine1());
			verify(studentRepo, times(1)).findById(id);
		}

		@Test
		@DisplayName("Test Case for leaving Address Field Blank")
		public void updateStudentAddress_NullFieldTest() {
			int id = 1;
			Address address = new Address("Demo City", null, "Demo Line 2", null, 12346);
			Exception actVal = Assertions.assertThrows(NullStudentException.class,
					() -> studentService.updateStudentAddress(id, address));
			String expected = "Address fields must not be null";
			assertEquals(expected, actVal.getMessage());
		}
	}

	@Nested
	public class AvailStudentLeave {
		@Test
		@DisplayName("Positive test for Availing Leave (Number of leaves less than available number of leaves)")
		public void availLeaveStudentLeaveApplied_Test() throws Exception {
			int id = 1, numLeave = 4;
			UniversityStudent student = new UniversityStudent();
			when(studentRepo.existsById(id)).thenReturn(true);
			when(studentRepo.findById(id)).thenReturn(Optional.of(student));
			String actString = studentService.availLeaveStudent(numLeave, id);
			assertEquals(actString, "Leave Applied");
			verify(studentRepo, times(1)).findById(id);
		}

		@Test
		@DisplayName("Positive test for Availing Leave (Number of leaves more than available number of leaves)")
		public void availLeaveStudentLeaveCantApply_Test() throws Exception {
			int id = 1, numLeave = 8;
			UniversityStudent student = new UniversityStudent();
			when(studentRepo.existsById(id)).thenReturn(true);
			when(studentRepo.findById(id)).thenReturn(Optional.of(student));
			String actString = studentService.availLeaveStudent(numLeave, id);
			assertEquals(actString, "Leaves cannot be granted due to insufficient leave balance");
			verify(studentRepo, times(1)).findById(id);
		}

		@Test
		@DisplayName("Test case for applying negative value as leave")
		public void availLeaveStudentInvalidStudent_Test() throws Exception {
			int id = 1, numLeave = -2;
			when(studentRepo.existsById(id)).thenReturn(true);
			Exception actVal = Assertions.assertThrows(EnterValidLeaveException.class,
					() -> studentService.availLeaveStudent(numLeave, id));
			assertEquals(actVal.getMessage(), "Enter a Number greater than 0");

		}

		@Test
		@DisplayName("Test case for applying negative value as leave")
		public void availLeaveStudentInvalidCase_Test() throws Exception {
			int id = 1, numLeave = 2;
			when(studentRepo.existsById(id)).thenReturn(false);
			Exception actVal = Assertions.assertThrows(NonExistentStudentIdException.class,
					() -> studentService.availLeaveStudent(numLeave, id));
			assertEquals(actVal.getMessage(), "No Such Student exists");

		}
	}
}
