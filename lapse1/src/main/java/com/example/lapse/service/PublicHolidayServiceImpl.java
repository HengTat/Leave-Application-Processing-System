package com.example.lapse.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lapse.domain.LeaveType;
import com.example.lapse.domain.PublicHoliday;
import com.example.lapse.repo.PublicRepo;


@Service
public class PublicHolidayServiceImpl implements PublicHolidayService {
	
	@Autowired
	PublicRepo pubRepo;
	
	@Override
	public List<PublicHoliday> findAll(){
		List<PublicHoliday> phlist = (List<PublicHoliday>)pubRepo.findAll();
		return phlist;
	};
	
	@Override
	public PublicHoliday findPublicHolidayById(Integer id) {
		return pubRepo.findById(id).get();
	}
	public void deletePublicHoliday(PublicHoliday publicHoliday) {
		pubRepo.delete(publicHoliday);

	}
	@Override
	public boolean savePublicHoliday(PublicHoliday publicHoliday) {
		if(pubRepo.save(publicHoliday)!=null)
			return true;
		else
			return false;
	}


}
