package com.lucipurr.tax.database.repository;

import com.lucipurr.tax.database.model.EmployeeInfoMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface EmployeeInfoMasterRepository extends JpaRepository<EmployeeInfoMaster, Number> {

    @Query(value = "SELECT E FROM EmployeeInfoMaster E WHERE UPPER(E.empId) = UPPER(?1)")
    EmployeeInfoMaster findByUserId(String userId);

}
