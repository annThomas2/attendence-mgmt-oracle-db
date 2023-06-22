package com.test.attendancemgmtsystem.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.attendancemgmtsystem.entity.Faculty;
import com.test.attendancemgmtsystem.exceptions.EnterValidLeaveException;
import com.test.attendancemgmtsystem.exceptions.NonExistentFacultyIdException;
import com.test.attendancemgmtsystem.service.FacultyService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class FacultyControllerTest {

	@Mock
	FacultyService facultyService;

	@InjectMocks
	FacultyController facultyController;

	private MockMvc mockMvc;

	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(facultyController).build();

	}

	@Test
	@DisplayName("Positive test for Availing Leave (Number of leaves less than available number of leaves)")
	public void availLeaveFacultyLeaveApplied_Test() throws Exception {

		int id = 1;
		int leaveNo = 7;
		when(facultyService.availLeaveFaculty(leaveNo, id)).thenReturn("Leave Applied");

		mockMvc.perform(MockMvcRequestBuilders.put("/faculty/availLeave/1/7").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}

	@Test
	@DisplayName("Positive test for Availing Leave (Number of leaves more than available number of leaves)")
	public void availLeaveFacultyLeaveCantApply_Test() throws Exception {

		int id = 1;
		int leaveNo = 20;
		when(facultyService.availLeaveFaculty(leaveNo, id))
				.thenReturn("Leaves cannot be granted due to insufficient leave balance");

		mockMvc.perform(MockMvcRequestBuilders.put("/faculty/availLeave/1/20").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$",
						is("Leaves cannot be granted due to insufficient leave balance")));

	}

	@Test
	@DisplayName("Test case for availing leave for a faculty record that doesn't exist")
	public void availLeaveNonExistentFaculty_Test() throws Exception {

		int id = 1;
		int leaveNo = 20;

		when(facultyService.availLeaveFaculty(leaveNo, id))
				.thenThrow(new NonExistentFacultyIdException("No Such Faculty exists"));

		mockMvc.perform(MockMvcRequestBuilders.put("/faculty/availLeave/1/20").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Test case for availing leave for a faculty record with negative value")
	public void availLeaveNegativeFaculty_Test() throws Exception {

		Faculty faculty = new Faculty();
		int id = 1;
		int leaveNo = -20;

		String payLoad = objectMapper.writeValueAsString(faculty);

		when(facultyService.availLeaveFaculty(leaveNo, id))
				.thenThrow(new EnterValidLeaveException("Enter a Number greater than 0"));

		mockMvc.perform(MockMvcRequestBuilders.put("/faculty/availLeave/1/-20").contentType(MediaType.APPLICATION_JSON)
				.content(payLoad)).andExpect(status().isBadRequest());
	}
}
