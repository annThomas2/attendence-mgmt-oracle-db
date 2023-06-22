package com.test.attendancemgmtsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.attendancemgmtsystem.entity.Address;
import com.test.attendancemgmtsystem.entity.UniversityStudent;
import com.test.attendancemgmtsystem.exceptions.EnterValidLeaveException;
import com.test.attendancemgmtsystem.exceptions.NonExistentStudentIdException;
import com.test.attendancemgmtsystem.exceptions.NullStudentException;
import com.test.attendancemgmtsystem.service.StudentService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class StudentControllerTest {

	@Mock
	StudentService studentService;

	@InjectMocks
	StudentController studentController;

	private MockMvc mockMvc;

	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();

	}

	@Nested
	public class UpdateStudentAddress {
		@Test
		@DisplayName("Positive Test case for updating Student address")
		public void updateStudentAddress_SuccessTest() throws Exception {
			UniversityStudent student = new UniversityStudent(1, "Ann", 'F', 101, 7, 1234567898L, "chenganur",
					"101 Street", "11 Road", "India", 689521);
			Address address = student.getAddress();

			when(studentService.updateStudentAddress(any(Integer.class), any(Address.class))).thenReturn(address);

			mockMvc.perform(MockMvcRequestBuilders.put("/student/1").contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(address))).andExpect(status().isOk());
		}

		@Test
		@DisplayName("Test case for updating a student address record with some null fields")
		public void updateStudentAddressNullField_Test() throws Exception {

			UniversityStudent student = new UniversityStudent(1, "Ann", 'F', 101, 7, 1234567898L, "chenganur",
					"101 Street", "11 Road", "India", 689521);
			Address address = student.getAddress();

			String payLoad = objectMapper.writeValueAsString(address);

			when(studentService.updateStudentAddress(Mockito.anyInt(), any(Address.class)))
					.thenThrow(new NullStudentException("Address fields must not be null"));

			mockMvc.perform(
					MockMvcRequestBuilders.put("/student/1").contentType(MediaType.APPLICATION_JSON).content(payLoad))
					.andExpect(status().isBadRequest());
		}
	}

	@Nested
	public class AvailStudentLeave

	{
		@Test
		@DisplayName("Positive test for Availing Leave (Number of leaves less than available number of leaves)")
		public void availLeaveStudentLeaveApplied_Test() throws Exception {

			int id = 1;
			int leaveNo = 5;
			when(studentService.availLeaveStudent(leaveNo, id)).thenReturn("Leave Applied");

			mockMvc.perform(
					MockMvcRequestBuilders.put("/student/availLeave/1/5").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk());

		}

		@Test
		@DisplayName("Positive test for Availing Leave (Number of leaves more than available number of leaves)")
		public void availLeaveStudentLeaveCantApply_Test() throws Exception {

			int id = 1;
			int leaveNo = 18;
			when(studentService.availLeaveStudent(leaveNo, id))
					.thenReturn("Leaves cannot be granted due to insufficient leave balance");

			mockMvc.perform(
					MockMvcRequestBuilders.put("/student/availLeave/1/18").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk());

		}

		@Test
		@DisplayName("Test case for availing leave for a student record that doesn't exist")
		public void availLeaveNonExistentStudent_Test() throws Exception {

			int id = 1;
			int leaveNo = 4;

			when(studentService.availLeaveStudent(leaveNo, id))
					.thenThrow(new NonExistentStudentIdException("No Such Student exists"));

			mockMvc.perform(
					MockMvcRequestBuilders.put("/student/availLeave/1/4").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("Test case for availing leave for a faculty record with negative value")
		public void availLeaveNegativeFaculty_Test() throws Exception {

			int id = 1;
			int leaveNo = -20;

			when(studentService.availLeaveStudent(leaveNo, id))
					.thenThrow(new EnterValidLeaveException("Enter a Number greater than 0"));

			mockMvc.perform(
					MockMvcRequestBuilders.put("/student/availLeave/1/-20").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}
	}
}
