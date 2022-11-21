package it.prova.myebay.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.prova.myebay.dto.AcquistoDTO;
import it.prova.myebay.dto.AnnuncioDTO;
import it.prova.myebay.dto.CategoriaDTO;
import it.prova.myebay.dto.RuoloDTO;
import it.prova.myebay.dto.UtenteDTO;
import it.prova.myebay.model.Annuncio;
import it.prova.myebay.model.Utente;
import it.prova.myebay.service.CategoriaService;
import it.prova.myebay.service.UtenteService;
import it.prova.myebay.validation.ValidationNoPassword;
import it.prova.myebay.validation.ValidationWithPassword;
import it.prova.myebay.service.AnnuncioService;

@Controller
public class AnnuncioController {

	@Autowired
	private AnnuncioService annuncioService;

	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private UtenteService utenteService;
	
	@GetMapping
	public ModelAndView listAllAnnunci() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("annuncio_list_attribute",
				AnnuncioDTO.createAnnuncioDTOListFromModelList(annuncioService.listAllAnnunci(), true, true));
		mv.setViewName("risultati");
		return mv;
	}

	@GetMapping("/annuncio/insert")
	public String create(Model model) {
		model.addAttribute("categorie_totali_attr", CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.listAllCategorie()));
		model.addAttribute("insert_annuncio_attr", new AnnuncioDTO());
		return "annuncio/insert";
	}
	
	@PostMapping("/annuncio/save/{idUtente}")
	public String save(
			@ModelAttribute("insert_annuncio_attr") Annuncio annuncio, @PathVariable(required = true) Long idUtente,
			BindingResult result, Model model, RedirectAttributes redirectAttrs) {

		if (result.hasErrors()) {
			model.addAttribute("categorie_totali_attr", CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.listAllCategorie()));
			return "annuncio/insert";
		}
		
		annuncio.setUtenteInserimento(utenteService.caricaSingoloUtente(idUtente));
		annuncioService.inserisciNuovo(annuncio);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/index";
	}
	
	@PostMapping("/risultati")
	public String listAnnunci(Annuncio annuncioExample, ModelMap model) {
		model.addAttribute("annuncio_list_attribute",
				AnnuncioDTO.createAnnuncioDTOListFromModelList(annuncioService.findByExample(annuncioExample), true, true));
		return "risultati";
	}
	
	@GetMapping("/show/{idAnnuncio}")
	public String show(@PathVariable(required = true) Long idAnnuncio, Model model) {
		model.addAttribute("show_annuncio_attr", annuncioService.caricaSingoloAnnuncio(idAnnuncio));
		return "show";
	}
	
	@GetMapping("/annuncio/list/{idUtente}")
	public String listAnnunci(@PathVariable(required = true) Long idUtente, Model model) {
		model.addAttribute("annuncio_list_attribute",
				AnnuncioDTO.createAnnuncioDTOListFromModelList(annuncioService.findByIdUtente(idUtente), true, true));
		return "annuncio/list";
	}
	
	@GetMapping("/annuncio/edit/{idAnnuncio}")
	public String edit(@PathVariable(required = true) Long idAnnuncio,  Model model) {
		Annuncio annuncioModel = annuncioService.caricaSingoloAnnuncioConCategorie(idAnnuncio);
		model.addAttribute("edit_annuncio_attr", AnnuncioDTO.buildAnnuncioDTOFromModel(annuncioModel,true, true));
		model.addAttribute("categorie_totali_attr", CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.listAllCategorie()));
		return "annuncio/edit";
	}

	@PostMapping("/annuncio/update/{idUtente}")
	public String update(@Validated(ValidationNoPassword.class) @ModelAttribute("edit_annuncio_attr") AnnuncioDTO annuncioDTO,
			@PathVariable(required = true) Long idUtente, BindingResult result, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {

		if (result.hasErrors()) {
			model.addAttribute("categorie_totali_attr", CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.listAllCategorie()));
			return "redirect:/annuncio/edit";
		}
		annuncioService.aggiorna(annuncioDTO.buildAnnuncioModel(true, true));

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/annuncio/list/" + idUtente;
	}
	
	@GetMapping("/annuncio/delete/{idAnnuncio}")
	public String delete(@PathVariable(required = true) Long idAnnuncio, Model model) {
		Annuncio annuncioModel = annuncioService.caricaSingoloAnnuncioConCategorie(idAnnuncio);
		model.addAttribute("delete_annuncio_attr", AnnuncioDTO.buildAnnuncioDTOFromModel(annuncioModel,true, true));
		model.addAttribute("categorie_totali_attr", CategoriaDTO.createCategoriaDTOListFromModelList(categoriaService.listAllCategorie()));
		return "annuncio/delete";
	}
	
	@PostMapping("/annuncio/elimina/{idUtente}")
	public String elimina(@Validated(ValidationNoPassword.class) @ModelAttribute("delete_annuncio_attr") AnnuncioDTO annuncioDTO,
			@PathVariable(required = true) Long idUtente, BindingResult result, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {
		
		annuncioService.rimuovi(annuncioDTO.buildAnnuncioModel(true, true).getId());

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/annuncio/list/" + idUtente;
	}
	
	@PostMapping("/annuncio/compra")
	public String compra(Annuncio annuncio, ModelMap model) {
		AnnuncioDTO annuncioTemp = AnnuncioDTO.buildAnnuncioDTOFromModel(annuncioService.caricaSingoloAnnuncio(annuncio.getId()), true, true);
		
		model.addAttribute("compra_annuncio_attr", annuncioTemp);
		
		//TODO controllare che sia lo stesso utente
/*	
		if(annuncioTemp.getPrezzo() > annuncioTemp.getUtenteInserimento().getCreditoResiduo()) {
			model.addAttribute("errorMessage", "Credito residuo insufficiente");
			return "index";
		}
*/	
		return "/annuncio/compra";
	}
}
