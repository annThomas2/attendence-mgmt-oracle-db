package com.test.attendancemgmtsystem.repository;

import org.springframework.data.repository.CrudRepository;

import com.test.attendancemgmtsystem.entity.Address;



public interface AddressRepository extends CrudRepository<Address, String> {

}
