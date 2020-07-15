package com.example.lapse.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lapse.domain.LeaveApplication;
import com.example.lapse.domain.Staff;
import com.example.lapse.enums.LeaveStatus;
import com.example.lapse.repo.LeaveApplicationRepo;
import com.example.lapse.repo.StaffRepo;

@Service
public class LeaveApplicationServiceImpl implements LeaveApplicationService{
	
	@Autowired
	LeaveApplicationRepo laRepo;
	
	@Autowired
	StaffRepo staffRepo;
	
	@Transactional 
	public void addLeaveApplication(LeaveApplication leaveApplication) {
		laRepo.save(leaveApplication);
	}

	@Transactional
	public void cancelLeaveApplication(LeaveApplication leaveApplication) {
		leaveApplication.setLeaveStatus(LeaveStatus.CANCELLED);
		laRepo.save(leaveApplication);
	}
	
	@Override
	public boolean saveLeaveApplication(LeaveApplication leaveApplication) {
		if(laRepo.save(leaveApplication)!=null) 
		    return true; 
		else 
		    return false;
	}
	
	@Transactional
	public ArrayList<LeaveApplication> listAllLeaveApplications() {
		ArrayList<LeaveApplication> leaveApplicationList = (ArrayList<LeaveApplication>) laRepo.findAll();
		return leaveApplicationList;
	}

	@Override
	public LeaveApplication findApplicationById(Integer id) {
		
		return laRepo.findById(id).get();
	}

	@Override
	public ArrayList<LeaveApplication> findApplicationByStaffId(Integer id) {
		return (ArrayList<LeaveApplication>) laRepo.findByStaffId(id);
	}

	
	public float getSumOfLeavesAppliedByStaff(Integer staffId, Integer leaveTypeId) {
	return laRepo.getSumOfLeavesAppliedByStaff(staffId, leaveTypeId);
	}
	
	@Override	
    public List<LeaveApplication> findpendingleaveapproval(Integer mgrid){
	 	 
		 List<Staff>listofstaff=staffRepo.findByManagerId(mgrid);
		 	 
		 List<LeaveApplication> AllEmployeeLeave= new ArrayList();
		 for (Iterator iterator = listofstaff.iterator(); iterator.hasNext();) {
				Staff staff = (Staff) iterator.next();
				AllEmployeeLeave.addAll(laRepo.findByStaffId(staff.getId()));					
		}
		 
		 List<LeaveApplication> PendingLeave= new ArrayList();
			for (Iterator iterator = AllEmployeeLeave.iterator(); iterator.hasNext();) {
				LeaveApplication leave = (LeaveApplication) iterator.next();
				if(leave.getLeaveStatus()==LeaveStatus.APPLIED) {
					PendingLeave.add(leave);
				}
			}
		 return PendingLeave;
	 }
	
	
	 @Override
	 public void approveleaveapplication(Integer id) {
			LeaveApplication leaveapplication= laRepo.findById(id).get();
			leaveapplication.setLeaveStatus(LeaveStatus.APPROVED);
			laRepo.save(leaveapplication);
		 }
	 
	 @Override
     public void rejectleaveapplication(Integer id) {
			 LeaveApplication leaveapplication= laRepo.findById(id).get();
			 leaveapplication.setLeaveStatus(LeaveStatus.REJECTED); 
				laRepo.save(leaveapplication);
		 }
	 
	 @Override
		public void updateLeaveStatus(int LeaveId, LeaveStatus status, String mComment) {
			LeaveApplication currLeaveApp=laRepo.findById(LeaveId);
			if(status.toString().equals("APPROVED")) {
				currLeaveApp.setLeaveStatus(LeaveStatus.APPROVED);
			}
			if(status.toString().equals("REJECTED")) {
				currLeaveApp.setLeaveStatus(LeaveStatus.REJECTED);
			}
			currLeaveApp.setManagerComment(mComment);
			laRepo.save(currLeaveApp);
		}
	 
	 
	 @Transactional
	 public void deleteLeaveApplication(LeaveApplication leaveApplication) {
		 laRepo.delete(leaveApplication);
	 }
	 
	 @Transactional
	 public void deleteLeaveApplication(Integer id) {
		 LeaveApplication leaveApplication = laRepo.findById(id).get();
		 if(leaveApplication.getLeaveStatus()==LeaveStatus.APPLIED) {
			 leaveApplication.setLeaveStatus(LeaveStatus.DELETED);
			 laRepo.save(leaveApplication);
		 }
	 }

	 @Override
		public boolean isWithinDateRange(Date currStartDate, Date currEndDate, Date testStartDate, Date testEndDate) {
		
		 if(testStartDate.equals(currStartDate) && testEndDate.equals(currEndDate)){
			 return true;
		 }
		 
		 if(testStartDate.after(currStartDate) && testEndDate.before(currEndDate)) {
				return true;
			}
			return false;
		}

	 @Override
		public ArrayList<LeaveApplication> findApplicationByManagerId(int managerId) {
			List<Staff>listofstaff=staffRepo.findByManagerId(managerId);
		 	 
			 ArrayList<LeaveApplication> allEmployeeLeave= new ArrayList<LeaveApplication>();
			 for (Iterator<Staff> iterator = listofstaff.iterator(); iterator.hasNext();) {
					Staff staff = (Staff) iterator.next();
					allEmployeeLeave.addAll(laRepo.findByStaffId(staff.getId()));					
			 }
			 
			 ArrayList<LeaveApplication> LeaveListExceptCancelAndReject= new ArrayList<LeaveApplication>();
			 for (Iterator<LeaveApplication> iterator=allEmployeeLeave.iterator();iterator.hasNext();) {
				 LeaveApplication la=(LeaveApplication)iterator.next();
				 if(la.getLeaveStatus()!=LeaveStatus.CANCELLED && la.getLeaveStatus()!=LeaveStatus.REJECTED) {
					 LeaveListExceptCancelAndReject.add(la);
				 }
			 }
			return LeaveListExceptCancelAndReject;
		}

	@Override
	public ArrayList<LeaveApplication> findSubLeaveAppByManagerId(int managerId) {
		List<Staff> listofstaff=staffRepo.findByManagerId(managerId);
	 	 
		 ArrayList<LeaveApplication> allEmployeeLeave= new ArrayList<LeaveApplication>();
		 for (Iterator<Staff> iterator = listofstaff.iterator(); iterator.hasNext();) {
				Staff staff = (Staff) iterator.next();
				allEmployeeLeave.addAll(laRepo.findByStaffId(staff.getId()));					
		}
		
		return allEmployeeLeave;
	}
	
	@Override
	public boolean validateforCancel(LeaveApplication leaveApp) {
		if(leaveApp.getLeaveStatus()==LeaveStatus.APPROVED) {
			Calendar c = Calendar.getInstance();

			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			
			Date startDate=leaveApp.getStartDate();
			Date today = c.getTime();
			if(startDate.after(today) || startDate.compareTo(today)!=0) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public ArrayList<LeaveApplication> findApplicationsExCancelDeleteReject(Integer staffId) {
		return laRepo.findApplicationsExCancelDeleteReject(staffId);
	}
}
