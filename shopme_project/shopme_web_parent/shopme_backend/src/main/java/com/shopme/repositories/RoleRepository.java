package com.shopme.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shopme.entities.Role;

// CrudRepository ใช้สำหรับการทำ CRUD กับ Entity ที่เรากำหนด
// JpaRepository ใช้สำหรับการทำ CRUD กับ Entity ที่เรากำหนด โดยสามารถทำการ sort หรือ paginate ได้

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

}
