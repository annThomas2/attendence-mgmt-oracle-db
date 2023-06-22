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

import com.test.attendancemgmtsystem.entity.Faculty;
import com.test.attendancemgmtsystem.exceptions.EnterValidLeaveException;
import com.test.attendancemgmtsystem.exceptions.InvalidFacultyIdException;
import com.test.attendancemgmtsystem.exceptions.NonExistentFacultyIdException;
import com.test.attendancemgmtsystem.exceptions.NullFacultyException;
import com.test.attendancemgmtsystem.repository.AddressRepository;
import com.test.attendancemgmtsystem.repository.FacultyRepository;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {

	@Mock
	FacultyRepository facultyRepo;

	@Mock
	AddressRepository addressRepo;

	@InjectMocks
	FacultyService facultyService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

	}

	@Nested
	public class AddFaculty {
		@Test
		@DisplayName("Positive Test case for Creation of FacultyRecord")
		public void addFaculty_SuccessTest() throws Exception {

			// Faculty Record Creation
			Faculty faculty = new Faculty(12, "Regal", 'M', 101, 10, 9876543211L, "C City", "CLine1", "CLine2",
					"Demo Country", 12345);
			// Stubbing
			when(facultyRepo.save(any(Faculty.class))).thenReturn(faculty);
			// Testing service class for addFaculty() with mock value
			Faculty actVal = facultyService.addFaculty(faculty);
			// Checking if the value returned from the call and provided values are equal
			Assertions.assertEquals(faculty.getGender(), actVal.getGender());
			assertEquals("Regal", actVal.getName());
			// Verifying if save() from facultyRepo is called exactly one time
			verify(facultyRepo, times(1)).save(faculty);
		}

		@Test
		@DisplayName("Test case for Creation of Faculty with an existing Id'")
		public void addFaculty_ExistingIdTest() {

			Faculty faculty = new Faculty(12, "Regal", 'M', 101, 10, 9876543211L, "C City", "CLine1", "CLine2",
					"Demo Country", 12345);
			when(facultyRepo.existsById(faculty.getFacultyId())).thenReturn(true);
			Throwable actVal = Assertions.assertThrows(Exception.class, () -> facultyService.addFaculty(faculty));
			String expected = "Faculty with this ID already exists";
			assertEquals(expected, actVal.getMessage());
		}

		@Test
		@DisplayName("Test case for Creation of FacultyRecord with any field null")
		public void addFaculty_NullFieldTest() {

			Faculty faculty = new Faculty(12, null, 'M', 101, 10, 9876543211L, null, "CLine1", "CLine2", "Demo Country",
					12345);

			Throwable actVal = Assertions.assertThrows(NullFacultyException.class,
					() -> facultyService.addFaculty(faculty));
			String expected = "Faculty fields or address fields must not be null";
			assertEquals(expected, actVal.getMessage());
		}
	}

	@Nested
	public class ReadFaculty {

		@Test
		@DisplayName("Positive Test case for Reading all FacultyRecords")
		public void getFacultyDetails_SuccessTest() {
			// Creating a list containing 2 Faculties record
			List<Faculty> facultyList = new ArrayList<>(Arrays.asList(
					new Faculty(12, "Regal", 'M', 101, 10, 9876543211L, "C City", "CLine1", "CLine2", "Demo Country",
							12345),
					new Faculty(12, "Melani", 'F', 102, 10, 6789342812L, "D City", "DLine1", "DLine2", "Demo Country",
							13444)));
			// Stubbing
			when(facultyRepo.findAll()).thenReturn(facultyList);
			// Testing service class for getFaculty() with mock value
			List<Faculty> value = facultyService.getFaculty();
			// Checking if the value returned from the call and provided values are equal
			assertEquals(facultyList.get(0), value.get(0));
			assertEquals(2, value.size());
			// Verifying if findAll() from facultyRepo is called exactly one time
			verify(facultyRepo, times(1)).findAll();
		}
	}

	@Nested
	public class UpdateFaculty {

		@Test
		@DisplayName("Positive Test case for updating FacultyRecord by Id")
		public void updateFacultyDetails_SuccessTest() throws Exception {
			Faculty faculty = new Faculty(12, "Regal", 'M', 101, 10, 9876543211L, "C City", "CLine1", "CLine2",
					"Demo Country", 12345);
			when(facultyRepo.save(any(Faculty.class))).thenReturn(faculty);
			Faculty val = facultyService.updateFaculty(faculty);
			assertEquals(faculty, val);
			assertTrue(val.getDepartmentId() == 101);
			verify(facultyRepo, times(1)).save(faculty);
		}

		@Test
		@DisplayName("Test case for updating id field as null")
		public void updateFacultyDetails_NullIdTest() {

			Faculty faculty = new Faculty(null, "Regal", 'M', 101, 10, 9876543211L, "C City", "CLine1", "CLine2",
					"Demo Country", 12345);
			Exception actVal = Assertions.assertThrows(InvalidFacultyIdException.class,
					() -> facultyService.updateFaculty(faculty));
			String expected = "Faculty ID cant be null";
			assertEquals(expected, actVal.getMessage());
		}

		@Test
		@DisplayName("Test Case for giving null fields in updation")
		public void updateFacultyDetails_NullFieldsTest() {
			Faculty faculty = new Faculty(12, null, 'M', 101, 10, 9876543211L, "C City", null, "CLine2", "Demo Country",
					12345);

			Exception actVal = Assertions.assertThrows(NullFacultyException.class,
					() -> facultyService.updateFaculty(faculty));
			String expected = "Faculty or address fields must not be null";
			assertEquals(expected, actVal.getMessage());
		}
	}

	@Nested
	public class DeleteFaculty {

		@Test
		@DisplayName("Positive Test case for deleting Faculty Record by Id")
		public void deleteFacultyRecordById_SuccessTest() {
			Faculty faculty = new Faculty(12, "Regal", 'M', 101, 10, 9876543211L, "C City", "CLine1", "CLine2",
					"Demo Country", 12345);
			int id = faculty.getFacultyId();
			facultyRepo.deleteById(id);
			verify(facultyRepo, times(1)).deleteById(id);
		}

		@Test
		@DisplayName("Positive Test case for deleting FacultyRecord of non existent Faculty by Id")
		public void deleteFacultyRecordById_NonExistentFacultyTest() {
			int id = 1;
			when(facultyRepo.existsById(id)).thenReturn(false);
			Assertions.assertThrows(NonExistentFacultyIdException.class, () -> facultyService.deleteByFacultyId(id));
			verify(facultyRepo, Mockito.never()).deleteById(id);
		}
	}

	@Nested
	public class AvailFacultyLeave {
		@Test
		@DisplayName("Positive test for Availing Leave (Number of leaves less than available number of leaves)")
		public void availLeaveFacultyLeaveApplied_Test() throws Exception {
			int id = 1, numLeave = 4;
			Faculty faculty = new Faculty();
			when(facultyRepo.existsById(id)).thenReturn(true);
			when(facultyRepo.findById(id)).thenReturn(Optional.of(faculty));
			String actString = facultyService.availLeaveFaculty(numLeave, id);
			assertEquals(actString, "Leave Applied");
			verify(facultyRepo, times(1)).findById(id);
		}

		@Test
		@DisplayName("Positive test for Availing Leave (Number of leaves more than available number of leaves)")
		public void availLeaveFacultyLeaveCantApply_Test() throws Exception {
			int id = 1, numLeave = 12;
			Faculty Faculty = new Faculty();
			when(facultyRepo.existsById(id)).thenReturn(true);
			when(facultyRepo.findById(id)).thenReturn(Optional.of(Faculty));
			String actString = facultyService.availLeaveFaculty(numLeave, id);
			assertEquals(actString, "Leaves cannot be granted due to insufficient leave balance");
			verify(facultyRepo, times(1)).findById(id);
		}

		@Test
		@DisplayName("Test case for applying negative value as leave")
		public void availLeaveFacultyInvalidFaculty_Test() throws Exception {
			int id = 1, numLeave = -2;
			when(facultyRepo.existsById(id)).thenReturn(true);
			Exception actVal = Assertions.assertThrows(EnterValidLeaveException.class,
					() -> facultyService.availLeaveFaculty(numLeave, id));
			assertEquals(actVal.getMessage(), "Enter a Number greater than 0");

		}

		@Test
		@DisplayName("Test case for applying negative value as leave")
		public void availLeaveFacultyInvalidCase_Test() throws Exception {
			int id = 1, numLeave = 2;
			when(facultyRepo.existsById(id)).thenReturn(false);
			Exception actVal = Assertions.assertThrows(NonExistentFacultyIdException.class,
					() -> facultyService.availLeaveFaculty(numLeave, id));
			assertEquals(actVal.getMessage(), "No Such Faculty exists");

		}
	}
}
