package com.example.lapse.repo;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.lapse.domain.LeaveApplication;

public interface LeaveApplicationRepo extends JpaRepository<LeaveApplication, Integer> {
	
	ArrayList<LeaveApplication> findByStaffId(Integer id);
	LeaveApplication findById(int id);

	@Query
	(value = "SELECT IFNULL(SUM(no_of_days),0) FROM leave_application WHERE YEAR(start_date) = YEAR(CURDATE()) AND staff_id = ?1 AND leave_type_id = ?2 AND leave_status NOT IN ('REJECTED','CANCELLED','DELETED')", 
	nativeQuery = true)
	float getSumOfLeavesAppliedByStaff(Integer staffId, Integer leaveTypeId);
	
	@Query
	(value = "SELECT * FROM leave_application WHERE YEAR(start_date) = YEAR(CURDATE()) AND staff_id = ?1 AND leave_status NOT IN ('REJECTED','CANCELLED','DELETED')",
	nativeQuery = true)
	ArrayList<LeaveApplication> findApplicationsExCancelDeleteReject(Integer staffId);
	
}
