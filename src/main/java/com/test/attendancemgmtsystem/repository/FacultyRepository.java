package com.test.attendancemgmtsystem.repository;




import org.springframework.data.repository.CrudRepository;

import com.test.attendancemgmtsystem.entity.Faculty;



public interface FacultyRepository extends CrudRepository <Faculty, Integer> {
	
}
