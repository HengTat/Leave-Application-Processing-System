package com.example.lapse.repo;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.lapse.domain.LeaveType;

public interface LeaveTypeRepo extends JpaRepository<LeaveType, Integer> {
	
	@Query("SELECT t.leaveType from LeaveType AS t where t.leaveType != 'Compensation Leave'")
	ArrayList<String> findAllLeaveTypeNamesExCL();
	
	LeaveType findLeaveTypeByLeaveType(String name);
	
	@Query("SELECT t.entitlement from LeaveType AS t where t.leaveType = :name")
	float findEntitlementByLeaveType(@Param("name") String name);
	
	@Query("SELECT t.id from LeaveType AS t where t.leaveType = :name")
	Integer findIdByLeaveType(@Param("name") String name);

}
