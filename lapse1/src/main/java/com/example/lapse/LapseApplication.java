package com.example.lapse;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.example.lapse.domain.Admin;
import com.example.lapse.domain.LeaveApplication;
import com.example.lapse.domain.LeaveType;
import com.example.lapse.domain.Manager;
import com.example.lapse.domain.PublicHoliday;
import com.example.lapse.domain.Staff;
import com.example.lapse.enums.LeaveStatus;
import com.example.lapse.repo.AdminRepo;
import com.example.lapse.repo.LeaveApplicationRepo;
import com.example.lapse.repo.LeaveTypeRepo;
import com.example.lapse.repo.ManagerRepo;
import com.example.lapse.repo.PublicRepo;
import com.example.lapse.repo.StaffRepo;

@SpringBootApplication
public class LapseApplication {

	@Autowired
	StaffRepo staffRepo;

	@Autowired
	ManagerRepo mgrRepo;

	@Autowired
	AdminRepo adminRepo;
	
	@Autowired
	LeaveTypeRepo ltRepo;
	
	@Autowired
	LeaveApplicationRepo laRepo;

	@Autowired
	PublicRepo phRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(LapseApplication.class, args);

	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			Admin admin1 = new Admin("AUDREY", "AUDREYPASSWORD", "AUDREYEMAIL@gmail.com" );
			adminRepo.save(admin1);
			
			LeaveType lt1 = new LeaveType("Annual Leave", 14);
			LeaveType lt2 = new LeaveType("Medical Leave", 60);
			LeaveType lt3 = new LeaveType("Compensation Leave", 0);
			ltRepo.save(lt1);
			ltRepo.save(lt2);
			ltRepo.save(lt3);
			
			List<LeaveType> leaveList = ltRepo.findAll();
	
			Manager manager1 = new Manager("JAMES","JAMESPASSWORD","EMAIL@gmail.com",leaveList );
			Manager manager2 = new Manager("BOB","BOBPASSWORD","BOBEMAIL@gmail.com",leaveList);
			Manager manager3 = new Manager("MARK","MARKPASSWORD","team9manager@gmail.com",leaveList);

			staffRepo.save(manager1);
			staffRepo.save(manager2);
			staffRepo.save(manager3);

			Staff staff1 = new Staff("JOHN", "JOHNPASSWORD", "JOHNEMAIL@gmail.com", leaveList, manager3);
			Staff staff2 = new Staff("JAKE", "JAKEPASSWORD1", "team9employee@gmail.com", leaveList, manager3);
			Staff staff3 = new Staff("ELL", "ELLPASSWORD1", "ELLEMAIL@gmail.com", leaveList, manager3);
			Staff staff4 = new Staff("NATHAN", "NATHANPASSWORD1", "NATHANEMAIL@gmail.com", leaveList, manager3);
			
			staffRepo.save(staff1);
			staffRepo.save(staff2);
			staffRepo.save(staff3);
			staffRepo.save(staff4);
			
			//User applied start date and end date of leave
			LocalDate ApplicationDate = LocalDate.of(2020, 6, 1);

			LocalDate dateStart1 = LocalDate.of(2020, 6, 15);
			LocalDate dateEnd1 = LocalDate.of(2020, 6,17);
			
			LocalDate dateStart2 = LocalDate.of(2020, 6, 3);
			LocalDate dateEnd2 = LocalDate.of(2020, 6,18);

			//Retrieve number of days in between start and end date (inclusive of start and end)
			float noOfDays1 = ChronoUnit.DAYS.between(dateStart1,dateEnd1) + 1;
			float noOfDays2 = ChronoUnit.DAYS.between(dateStart2,dateEnd2) + 1;
			
			//converting localdate to date
			Date APPLICATIONDATE =java.sql.Date.valueOf(ApplicationDate);

			Date START1 =java.sql.Date.valueOf(dateStart1);
			Date END1 = java.sql.Date.valueOf(dateEnd1);
			
			Date START2 =java.sql.Date.valueOf(dateStart2);
			Date END2 = java.sql.Date.valueOf(dateEnd2);
			
	
			LeaveApplication apply1 = new LeaveApplication(APPLICATIONDATE, START1,/*TimeOfDay.PM,*/END1, /*TimeOfDay.AM,*/
					lt1, noOfDays1, LeaveStatus.APPLIED,"holiday","staff 2", true, false, "999",  "holiday", staff1);
			
			LeaveApplication apply2 = new LeaveApplication(APPLICATIONDATE, START2,/*TimeOfDay.AM,*/ END2, /*TimeOfDay.AM,*/
					lt2, noOfDays2, LeaveStatus.APPLIED, "sick", "wait for return", true, false, "888",  "medical", staff2);
			
			LeaveApplication apply3 = new LeaveApplication(APPLICATIONDATE, START2,/*TimeOfDay.AM,*/END2, /*TimeOfDay.AM,*/
					lt2, noOfDays2, LeaveStatus.APPROVED, "sick again", "return", true, false, "777",  "hospital", manager2);
			LeaveApplication apply4 = new LeaveApplication(APPLICATIONDATE, START2,/*TimeOfDay.AM,*/END2, /*TimeOfDay.AM,*/
					lt2, noOfDays2, LeaveStatus.APPROVED, "Sugery", "return", true, false, "777",  "hospital", staff3);
			LeaveApplication apply5 = new LeaveApplication(APPLICATIONDATE, START2,/*TimeOfDay.AM,*/END2, /*TimeOfDay.AM,*/
					lt2, noOfDays2, LeaveStatus.APPROVED, "cough", "return", true, false, "777",  "hospital", staff4);
			
			laRepo.save(apply5);
			laRepo.save(apply4);
			laRepo.save(apply1);
			laRepo.save(apply2);
			laRepo.save(apply3);

					
			      float totaldaysapplied = laRepo.getSumOfLeavesAppliedByStaff(6, 2);
			      System.out.println(totaldaysapplied);
			      
				  Date ph1Start =java.sql.Date.valueOf(LocalDate.of(2020, 1, 1));
				  Date ph1End =java.sql.Date.valueOf(LocalDate.of(2020, 1, 1));

				  Date ph2Start =java.sql.Date.valueOf(LocalDate.of(2020, 1, 25));
				  Date ph2End =java.sql.Date.valueOf(LocalDate.of(2020, 1, 26));

				  Date ph3Start =java.sql.Date.valueOf(LocalDate.of(2020, 4, 10));
				  Date ph3End =java.sql.Date.valueOf(LocalDate.of(2020, 4, 10));

				  Date ph4Start =java.sql.Date.valueOf(LocalDate.of(2020, 5, 1));
				  Date ph4End =java.sql.Date.valueOf(LocalDate.of(2020, 5, 1));

				  Date ph5Start =java.sql.Date.valueOf(LocalDate.of(2020, 5, 7));
				  Date ph5End =java.sql.Date.valueOf(LocalDate.of(2020, 5, 7));

				  Date ph6Start =java.sql.Date.valueOf(LocalDate.of(2020, 5, 24));
				  Date ph6End =java.sql.Date.valueOf(LocalDate.of(2020, 5, 24));

				  Date ph7Start =java.sql.Date.valueOf(LocalDate.of(2020, 7, 31));
				  Date ph7End =java.sql.Date.valueOf(LocalDate.of(2020, 7, 31));

				  Date ph8Start =java.sql.Date.valueOf(LocalDate.of(2020, 8, 9));
				  Date ph8End =java.sql.Date.valueOf(LocalDate.of(2020, 8, 9));

				  Date ph9Start =java.sql.Date.valueOf(LocalDate.of(2020, 11, 14));
				  Date ph9End =java.sql.Date.valueOf(LocalDate.of(2020, 11, 14));

				  Date ph10Start =java.sql.Date.valueOf(LocalDate.of(2020, 12, 25));
				  Date ph10End =java.sql.Date.valueOf(LocalDate.of(2020, 12, 25));
				  
				  PublicHoliday ph1 = new PublicHoliday("New Year’s Day", ph1Start, ph1End);
				  PublicHoliday ph2 = new PublicHoliday("Chinese New Year", ph2Start, ph2End);
				  PublicHoliday ph3 = new PublicHoliday("Good Friday", ph3Start, ph3End);
				  PublicHoliday ph4 = new PublicHoliday("Labour Day", ph4Start, ph4End);
				  PublicHoliday ph5 = new PublicHoliday("Vesak Day", ph5Start, ph5End);
				  PublicHoliday ph6 = new PublicHoliday("Hari Raya Puasa", ph6Start, ph6End);
				  PublicHoliday ph7 = new PublicHoliday("Hari Raya Haji", ph7Start, ph7End);
				  PublicHoliday ph8 = new PublicHoliday("National Day", ph8Start, ph8End);
				  PublicHoliday ph9 = new PublicHoliday("Deepavali", ph9Start, ph9End);
				  PublicHoliday ph10 = new PublicHoliday("Christmas Day", ph10Start, ph10End);
				  phRepo.save(ph1);
				  phRepo.save(ph2);
				  phRepo.save(ph3);
				  phRepo.save(ph4);
				  phRepo.save(ph5);
				  phRepo.save(ph6);
				  phRepo.save(ph7);
				  phRepo.save(ph8);
				  phRepo.save(ph9);
				  phRepo.save(ph10);

		};
	}
}
