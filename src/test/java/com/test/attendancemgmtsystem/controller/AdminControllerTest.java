package com.test.attendancemgmtsystem.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.attendancemgmtsystem.entity.Faculty;
import com.test.attendancemgmtsystem.entity.UniversityStudent;
import com.test.attendancemgmtsystem.exceptions.InvalidCreationFacultytException;
import com.test.attendancemgmtsystem.exceptions.InvalidCreationStudentException;
import com.test.attendancemgmtsystem.exceptions.InvalidFacultyIdException;
import com.test.attendancemgmtsystem.exceptions.InvalidStudentIdException;
import com.test.attendancemgmtsystem.exceptions.NonExistentFacultyIdException;
import com.test.attendancemgmtsystem.exceptions.NonExistentStudentIdException;
import com.test.attendancemgmtsystem.exceptions.NullFacultyException;
import com.test.attendancemgmtsystem.exceptions.NullStudentException;
import com.test.attendancemgmtsystem.service.FacultyService;
import com.test.attendancemgmtsystem.service.StudentService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AdminControllerTest {

	@Mock
	StudentService studentService;

	@Mock
	FacultyService facultyService;

	@InjectMocks
	AdminController adminController;

	private MockMvc mockMvc;

	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach()
	public void setup() {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();

	}

	@Nested
	public class Student_Test {

		@Nested
		public class AddStudent {

			@Test
			@DisplayName("Positive Test case for adding a student")
			public void addStudent_Test() throws Exception {

				// Data
				UniversityStudent student = new UniversityStudent(1, "Ann", 'F', 101, 7, 1234567898L, "chenganur",
						"101 Street", "11 Road", "India", 689521);

				// Mock Behavior
				// when(studentService.addStudent(any(UniversityStudent.class))).thenReturn(student);

				// execution and verification
				mockMvc.perform(MockMvcRequestBuilders.post("/admin/students").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(student)))
						.andExpect(content().string("Student created Successfully"));
			}

			@Test
			@DisplayName("Test case for adding an existing student")
			public void addExistingStudent_Test() throws Exception {

				UniversityStudent student = new UniversityStudent(1, "Ann", 'F', 101, 7, 1234567898L, "chenganur",
						"101 Street", "11 Road", "India", 689521);

				String payLoad = objectMapper.writeValueAsString(student);

				when(studentService.addStudent(any(UniversityStudent.class)))
						.thenThrow(new InvalidCreationStudentException("Student with this id already exists"));

				mockMvc.perform(MockMvcRequestBuilders.post("/admin/students").contentType(MediaType.APPLICATION_JSON)
						.content(payLoad)).andExpect(status().isBadRequest());
			}

			@Test
			@DisplayName("Test case for adding a student with some null fields")
			public void addStudentNullField_Test() throws Exception {

				UniversityStudent student = new UniversityStudent(1, null, 'F', 101, 7, 1234567898L, "chenganur", null,
						null, "India", 689521);

				String payLoad = objectMapper.writeValueAsString(student);

				when(studentService.addStudent(any(UniversityStudent.class)))
						.thenThrow(new NullStudentException("Student or address fields must not be null"));

				mockMvc.perform(MockMvcRequestBuilders.post("/admin/students").contentType(MediaType.APPLICATION_JSON)
						.content(payLoad)).andExpect(status().isBadRequest());
			}
		}

		@Nested
		public class GetStudent {

			@Test
			@DisplayName("Positive Test case for Reading all the student Records")
			public void getStudent() throws Exception {
				List<UniversityStudent> studList = new ArrayList<>(Arrays.asList(
						new UniversityStudent(1, "Ann", 'F', 101, 7, 1234567898L, "chenganur", "101 Street", "11 Road",
								"India", 689521),
						new UniversityStudent(1, "Amy", 'F', 101, 7, 8594998452L, "Kochi", "55 Street", "55 Road",
								"India", 689521)));

				when(studentService.getStudent()).thenReturn(studList);

				mockMvc.perform(MockMvcRequestBuilders.get("/admin/students").contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
						.andExpect(MockMvcResultMatchers.jsonPath("$[1].name", is("Amy")))
						.andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("Ann")))
						.andExpect(MockMvcResultMatchers.jsonPath("$[1].gender", is("F")));
			}
		}

		@Nested
		public class UpdateStudent {

			@Test
			@DisplayName("Positive Test case for Updating all the student Records")
			public void updateStudent_Test() throws Exception {
				UniversityStudent student = new UniversityStudent(1, "Ann_Updated", 'F', 101, 7, 1234567898L,
						"chenganur", "101 Street", "11 Road", "India", 689521);

				when(studentService.updateStudent(any(UniversityStudent.class))).thenReturn(student);

				mockMvc.perform(MockMvcRequestBuilders.put("/admin/students/1").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(student))).andExpect(status().isOk())
						.andExpect(content().string("Student updated Successfully"));
			}

			@Test
			@DisplayName("Test case for updating a student record that doesn't exist")
			public void updateNullStudentId_Test() throws Exception {

				UniversityStudent student = new UniversityStudent(null, "Ann_updated", 'F', 101, 7, 1234567898L,
						"chenganur", "101 Street", "11 Road", "India", 689521);

				String payLoad = objectMapper.writeValueAsString(student);

				when(studentService.updateStudent(any(UniversityStudent.class)))
						.thenThrow(new InvalidStudentIdException("Student ID cant be null"));

				mockMvc.perform(MockMvcRequestBuilders.put("/admin/students/1").contentType(MediaType.APPLICATION_JSON)
						.content(payLoad)).andExpect(status().isBadRequest());
			}

			@Test
			@DisplayName("Test case for updating a student with some null fields")
			public void updateStudentNullField_Test() throws Exception {

				UniversityStudent student = new UniversityStudent(1, null, 'F', 101, 7, 1234567898L, "chenganur", null,
						null, "India", 689521);

				String payLoad = objectMapper.writeValueAsString(student);

				when(studentService.updateStudent(any(UniversityStudent.class)))
						.thenThrow(new NullStudentException("Student or address fields must not be null"));

				mockMvc.perform(MockMvcRequestBuilders.put("/admin/students/1").contentType(MediaType.APPLICATION_JSON)
						.content(payLoad)).andExpect(status().isBadRequest());
			}
		}

		@Nested
		public class DeleteStudent {

			@Test
			@DisplayName("Positive Test case for Deleting all the student Records")
			public void deleteStudent_Test() throws Exception {
				UniversityStudent student = new UniversityStudent(1, "Ann", 'F', 101, 7, 1234567898L, "chenganur",
						"101 Street", "11 Road", "India", 689521);
				int id = student.getStudentId();
				mockMvc.perform(
						MockMvcRequestBuilders.delete("/admin/students/1").contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk());
				verify(studentService, times(1)).deleteByStudId(id);
			}

			@Test
			@DisplayName("Test case for deleting a student record that doesn't exist")
			public void deleteNonExistentStudent_Test() throws Exception {

				UniversityStudent student = new UniversityStudent();
				student.setStudentId(12);
				doThrow(new NonExistentStudentIdException("No Such Student exists")).when(studentService)
						.deleteByStudId(Mockito.anyInt());

				String payLoad = objectMapper.writeValueAsString(student);
				mockMvc.perform(MockMvcRequestBuilders.delete("/admin/students/1")
						.contentType(MediaType.APPLICATION_JSON).content(payLoad)).andExpect(status().isBadRequest());
			}

		}
	}

	@Nested
	public class Faculty_Test {

		@Nested
		public class AddFaculty {
			@Test
			@DisplayName("Positive Test case for adding a Faculty")
			public void addFaculty_Test() throws Exception {
				Faculty faculty = new Faculty(12, "Regal", 'M', 101, 10, 9876543211L, "C City", "CLine1", "CLine2",
						"Demo Country", 12345);

				when(facultyService.addFaculty(any(Faculty.class))).thenReturn(faculty);

				mockMvc.perform(MockMvcRequestBuilders.post("/admin/faculties").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(faculty))).andExpect(status().isOk())
						.andExpect(status().isOk()).andExpect(content().string("Faculty created Successfully"));
			}

			@Test
			@DisplayName("Test case for adding an existing faculty")
			public void addExistingFaculty_Test() throws Exception {

				Faculty faculty = new Faculty(12, "Regal", 'M', 101, 10, 9876543211L, "C City", "CLine1", "CLine2",
						"Demo Country", 12345);

				String payLoad = objectMapper.writeValueAsString(faculty);

				when(facultyService.addFaculty(any(Faculty.class)))
						.thenThrow(new InvalidCreationFacultytException("Faculty with this ID already exists"));

				mockMvc.perform(MockMvcRequestBuilders.post("/admin/faculties").contentType(MediaType.APPLICATION_JSON)
						.content(payLoad)).andExpect(status().isBadRequest());
			}

			@Test
			@DisplayName("Test case for adding a faculty with some null fields")
			public void addFacultyNullField_Test() throws Exception {

				Faculty faculty = new Faculty(12, null, 'M', 101, 10, 9876543211L, null, "CLine1", "CLine2",
						"Demo Country", 12345);

				String payLoad = objectMapper.writeValueAsString(faculty);

				when(facultyService.addFaculty(any(Faculty.class)))
						.thenThrow(new NullFacultyException("Faculty fields or address fields must not be null"));

				mockMvc.perform(MockMvcRequestBuilders.post("/admin/faculties").contentType(MediaType.APPLICATION_JSON)
						.content(payLoad)).andExpect(status().isBadRequest());
			}
		}

		@Nested
		public class ReadFaculty {
			@Test
			@DisplayName("Positive Test case for Reading all the Faculty Records")
			public void getFaculty() throws Exception {
				List<Faculty> facultyList = new ArrayList<>(Arrays.asList(
						new Faculty(12, "Regal", 'M', 101, 10, 9876543211L, "C City", "CLine1", "CLine2",
								"Demo Country", 12345),
						new Faculty(12, "Regina", 'F', 102, 10, 786545632L, "D City", "DLine1", "DLine2",
								"Demo1 Country", 23456)));

				when(facultyService.getFaculty()).thenReturn(facultyList);

				mockMvc.perform(MockMvcRequestBuilders.get("/admin/faculties").contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
						.andExpect(MockMvcResultMatchers.jsonPath("$[1].name", is("Regina")))
						.andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("Regal")))
						.andExpect(MockMvcResultMatchers.jsonPath("$[1].gender", is("F")));
			}
		}

		@Nested
		public class UpdateFaculty {
			@Test
			@DisplayName("Positive Test case for Updating all the student Records")
			public void updateFaculty_Test() throws Exception {
				Faculty faculty = new Faculty(12, "Regal_Updated", 'M', 101, 10, 9876543211L, "C City", "CLine1",
						"CLine2", "Demo Country", 12345);

				when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(faculty);

				mockMvc.perform(MockMvcRequestBuilders.put("/admin/faculties/12")
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(faculty)))
						.andExpect(status().isOk()).andExpect(status().isOk())
						.andExpect(content().string("Faculty updated Successfully"));
			}

			@Test
			@DisplayName("Test case for updating a faculty record that doesn't exist")
			public void updateNullFacultyId_Test() throws Exception {

				Faculty faculty = new Faculty(null, "Regal", 'M', 101, 10, 9876543211L, "C City", "CLine1", "CLine2",
						"Demo Country", 12345);

				String payLoad = objectMapper.writeValueAsString(faculty);

				when(facultyService.updateFaculty(any(Faculty.class)))
						.thenThrow(new InvalidFacultyIdException("Faculty ID cant be null"));

				mockMvc.perform(MockMvcRequestBuilders.put("/admin/faculties/12")
						.contentType(MediaType.APPLICATION_JSON).content(payLoad)).andExpect(status().isBadRequest());
			}

			@Test
			@DisplayName("Test case for updating a faculty with some null fields")
			public void updateFacultyNullField_Test() throws Exception {

				Faculty faculty = new Faculty();

				String payLoad = objectMapper.writeValueAsString(faculty);

				when(facultyService.updateFaculty(any(Faculty.class)))
						.thenThrow(new NullFacultyException("Faculty or address fields must not be null"));

				mockMvc.perform(MockMvcRequestBuilders.put("/admin/faculties/12")
						.contentType(MediaType.APPLICATION_JSON).content(payLoad)).andExpect(status().isBadRequest());
			}
		}

		@Nested
		public class DeleteFaculty {
			@Test
			@DisplayName("Positive Test case for Deleting all the student Records")
			public void deleteFaculty_Test() throws Exception {
				Faculty faculty = new Faculty(12, "Regal", 'M', 101, 10, 9876543211L, "C City", "CLine1", "CLine2",
						"Demo Country", 12345);
				int id = faculty.getFacultyId();
				mockMvc.perform(
						MockMvcRequestBuilders.delete("/admin/faculties/12").contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk());
				verify(facultyService, times(1)).deleteByFacultyId(id);
			}

			@Test
			@DisplayName("Test case for deleting a faculty record that doesn't exist")
			public void deleteNonFacultyStudent_Test() throws Exception {

				Faculty faculty = new Faculty();
				doThrow(new NonExistentFacultyIdException("No Such Faculty exists")).when(facultyService)
						.deleteByFacultyId(Mockito.anyInt());

				String payLoad = objectMapper.writeValueAsString(faculty);
				mockMvc.perform(MockMvcRequestBuilders.delete("/admin/faculties/12")
						.contentType(MediaType.APPLICATION_JSON).content(payLoad)).andExpect(status().isBadRequest());

			}
		}
	}
}
