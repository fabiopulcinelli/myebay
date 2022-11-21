package it.prova.myebay.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import it.prova.myebay.dto.AcquistoDTO;
import it.prova.myebay.service.AcquistoService;

@Controller
@RequestMapping(value = "/acquisto")
public class AcquistoController {
	@Autowired
	private AcquistoService acquistoService;
	
	@GetMapping
	public ModelAndView listAllAcquisti() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("acquisto_list_attribute",
				AcquistoDTO.createAcquistoDTOListFromModelList(acquistoService.listAllAcquisti(), true));
		mv.setViewName("list");
		return mv;
	}

	@GetMapping("/list/{idUtente}")
	public String listAcquisti(@PathVariable(required = true) Long idUtente, Model model) {
		model.addAttribute("acquisto_list_attribute",
				AcquistoDTO.createAcquistoDTOListFromModelList(acquistoService.findByIdUtente(idUtente), true));
		return "acquisto/list";
	}
	
	@GetMapping("/show/{idAcquisto}")
	public String show(@PathVariable(required = true) Long idAcquisto, Model model) {
		model.addAttribute("show_acquisto_attr", acquistoService.caricaSingoloAcquisto(idAcquisto));
		return "acquisto/show";
	}
}
