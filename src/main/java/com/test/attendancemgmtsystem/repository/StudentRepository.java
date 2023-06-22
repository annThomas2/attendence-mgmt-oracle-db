package com.test.attendancemgmtsystem.repository;

import org.springframework.data.repository.CrudRepository;

import com.test.attendancemgmtsystem.entity.UniversityStudent;

public interface StudentRepository extends CrudRepository<UniversityStudent, Integer> {

}
