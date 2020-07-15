package com.example.lapse.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.lapse.domain.PublicHoliday;
import com.example.lapse.domain.Staff;
import com.example.lapse.service.PublicHolidayService;
import com.example.lapse.service.PublicHolidayServiceImpl;

@Controller
@RequestMapping("/ph")
public class PublicHolidayController {

	@Autowired
	private PublicHolidayService pubservice;

	@Autowired
	public void setPublicService(PublicHolidayServiceImpl publicServiceImpl) {
		this.pubservice = publicServiceImpl;
	}

	@RequestMapping(value = "/add")
	public String addPublicHoliday(Model model) {
		model.addAttribute("publicholiday", new PublicHoliday());
		return "ph-form";
	}
	
	@RequestMapping(value = "/list")
	 public String list(Model model,HttpSession session) {
	  model.addAttribute("phlist", pubservice.findAll());
	  Object error = session.getAttribute("error");
	  if (error != null) {
	  	 model.addAttribute("error",String.valueOf(error));
	  	 session.setAttribute("error", null);
	  }
	  return "ph-list";
	 }
	
	@RequestMapping(value = "/delete/{id}")
	public String deletePublicHoliday(@PathVariable("id") Integer id) {
		pubservice.deletePublicHoliday(pubservice.findPublicHolidayById(id));
	    return "forward:/ph/list";
	  }
	
	@RequestMapping(value="/save")
	public String savePublicHoliday(@ModelAttribute("publicholiday") @Valid PublicHoliday publicHoliday, BindingResult result, Model model) {
		  if (result.hasErrors()) {
		      model.addAttribute("publicholiday", publicHoliday); 
		      return "ph-form";
		    }
		  pubservice.savePublicHoliday(publicHoliday);
		  return "forward:/ph/list";
	}
	  @RequestMapping(value = "/edit/{id}")
	  public String editPublicHoliday(@PathVariable("id") Integer id, Model model) {
		  PublicHoliday publicHoliday = pubservice.findPublicHolidayById(id);
	    model.addAttribute("publicholiday", publicHoliday);
	    return "ph-form";
	  }

}
