package com.example.lapse.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lapse.domain.LeaveType;
import com.example.lapse.repo.LeaveTypeRepo;

@Service
public class LeaveTypeServiceImpl implements LeaveTypeService {

	@Autowired
	LeaveTypeRepo ltRepo;

	@Override
	public ArrayList<LeaveType> findAll() {
		ArrayList<LeaveType> ltlist = (ArrayList<LeaveType>) ltRepo.findAll();
		return ltlist;
	}
	
	@Override
	public ArrayList<String> findAllLeaveTypeNamesExCL() {
		return (ArrayList<String>) ltRepo.findAllLeaveTypeNamesExCL();
	}
	
	@Override
	public LeaveType findLeaveTypeByLeaveType(String name) {
		return ltRepo.findLeaveTypeByLeaveType(name);
	}

	@Override
	public boolean saveLeaveType(LeaveType leavetype) {
		if(ltRepo.save(leavetype)!=null) 
		    return true; 
		else 
		    return false;
	}

	@Override
	public void deleteLeaveType(LeaveType leavetype) {
		ltRepo.delete(leavetype);
		
	}
	
	@Override
	public LeaveType findLeaveTypeById(Integer id) {
		return ltRepo.findById(id).get();
	}
	
	@Override
	public List<LeaveType> findAllLeaveTypesEXCL() {
		List<LeaveType> lts = ltRepo.findAll();
		lts.removeIf(lt -> (lt.getLeaveType().equals("Compensation Leave")));
		System.out.print(lts);
		return lts;
	}
	
	@Override
	public float findEntitlementByLeaveType(String name) {
		return ltRepo.findEntitlementByLeaveType(name);
	}

	@Override
	public Integer findIdByLeaveType(String name) {
		return ltRepo.findIdByLeaveType(name);
	}
}
