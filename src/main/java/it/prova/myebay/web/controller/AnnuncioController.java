package it.prova.myebay.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import it.prova.myebay.dto.AnnuncioDTO;
import it.prova.myebay.dto.CategoriaDTO;
import it.prova.myebay.dto.RuoloDTO;
import it.prova.myebay.dto.UtenteDTO;
import it.prova.myebay.model.Annuncio;
import it.prova.myebay.service.CategoriaService;
import it.prova.myebay.service.AnnuncioService;

@Controller
public class AnnuncioController {

	@Autowired
	private AnnuncioService annuncioService;

	@Autowired
	private CategoriaService categoriaService;
	
	@GetMapping
	public ModelAndView listAllAnnunci() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("annuncio_list_attribute",
				AnnuncioDTO.createAnnuncioDTOListFromModelList(annuncioService.listAllAnnunci(), false, false, false));
		mv.setViewName("risultati");
		return mv;
	}

	@PostMapping("/risultati")
	public String listAnnunci(Annuncio annuncioExample, ModelMap model) {
		model.addAttribute("annuncio_list_attribute",
				AnnuncioDTO.createAnnuncioDTOListFromModelList(annuncioService.findByExample(annuncioExample), false, false, false));
		return "risultati";
	}
	
	@GetMapping("/show/{idAnnuncio}")
	public String show(@PathVariable(required = true) Long idAnnuncio, Model model) {
		model.addAttribute("show_annuncio_attr", annuncioService.caricaSingoloAnnuncio(idAnnuncio));
		return "show";
	}
}
