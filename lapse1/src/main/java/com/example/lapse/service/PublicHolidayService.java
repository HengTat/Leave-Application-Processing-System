package com.example.lapse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.lapse.domain.PublicHoliday;

@Service
public interface PublicHolidayService {

	public List<PublicHoliday> findAll();
	public PublicHoliday findPublicHolidayById(Integer id);
	public void deletePublicHoliday(PublicHoliday publicHoliday);
	public boolean savePublicHoliday(PublicHoliday publicHoliday);

	
}
