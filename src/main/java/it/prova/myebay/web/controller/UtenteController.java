package it.prova.myebay.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.prova.myebay.dto.RuoloDTO;
import it.prova.myebay.dto.UtenteDTO;
import it.prova.myebay.dto.UtenteDTOReset;
import it.prova.myebay.model.Utente;
import it.prova.myebay.service.RuoloService;
import it.prova.myebay.service.UtenteService;
import it.prova.myebay.validation.ValidationNoPassword;
import it.prova.myebay.validation.ValidationWithPassword;

@Controller
@RequestMapping(value = "/utente")
public class UtenteController {

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private RuoloService ruoloService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping
	public ModelAndView listAllUtenti() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("utente_list_attribute",
				UtenteDTO.createUtenteDTOListFromModelList(utenteService.listAllUtenti(), false, false, false));
		mv.setViewName("utente/list");
		return mv;
	}

	@GetMapping("/search")
	public String searchUtente() {
		return "utente/search";
	}

	@PostMapping("/list")
	public String listUtenti(Utente utenteExample, ModelMap model) {
		model.addAttribute("utente_list_attribute",
				UtenteDTO.createUtenteDTOListFromModelList(utenteService.findByExample(utenteExample), false, false, false));
		return "utente/list";
	}
	
	@GetMapping("/show/{idUtente}")
	public String show(@PathVariable(required = true) Long idUtente, Model model) {
		model.addAttribute("show_utente_attr", utenteService.caricaSingoloUtente(idUtente));
		return "utente/show";
	}

	@GetMapping("/insert")
	public String create(Model model) {
		model.addAttribute("ruoli_totali_attr", RuoloDTO.createRuoloDTOListFromModelList(ruoloService.listAll()));
		model.addAttribute("insert_utente_attr", new UtenteDTO());
		return "utente/insert";
	}

	// per la validazione devo usare i groups in quanto nella insert devo validare
	// la pwd, nella edit no
	@PostMapping("/save")
	public String save(
			@Validated({ ValidationWithPassword.class,
					ValidationNoPassword.class }) @ModelAttribute("insert_utente_attr") UtenteDTO utenteDTO,
			BindingResult result, Model model, RedirectAttributes redirectAttrs) {

		if (!result.hasFieldErrors("password") && !utenteDTO.getPassword().equals(utenteDTO.getConfermaPassword()))
			result.rejectValue("confermaPassword", "password.diverse");

		if (result.hasErrors()) {
			model.addAttribute("ruoli_totali_attr", RuoloDTO.createRuoloDTOListFromModelList(ruoloService.listAll()));
			return "utente/insert";
		}
		Utente temp = utenteDTO.buildUtenteModel(true, true, true);
		temp.setCreditoResiduo(100);
		utenteService.inserisciNuovo(temp);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/utente";
	}

	@GetMapping("/edit/{idUtente}")
	public String edit(@PathVariable(required = true) Long idUtente, Model model) {
		Utente utenteModel = utenteService.caricaSingoloUtenteConRuoli(idUtente);
		model.addAttribute("edit_utente_attr", UtenteDTO.buildUtenteDTOFromModel(utenteModel,true, true, true));
		model.addAttribute("ruoli_totali_attr", RuoloDTO.createRuoloDTOListFromModelList(ruoloService.listAll()));
		return "utente/edit";
	}

	@PostMapping("/update")
	public String update(@Validated(ValidationNoPassword.class) @ModelAttribute("edit_utente_attr") UtenteDTO utenteDTO,
			BindingResult result, Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {

		if (result.hasErrors()) {
			model.addAttribute("ruoli_totali_attr", RuoloDTO.createRuoloDTOListFromModelList(ruoloService.listAll()));
			return "utente/edit";
		}
		utenteService.aggiorna(utenteDTO.buildUtenteModel(true, true, true));

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/utente";
	}

	@PostMapping("/cambiaStato")
	public String cambiaStato(@RequestParam(name = "idUtenteForChangingStato", required = true) Long idUtente) {
		utenteService.changeUserAbilitation(idUtente);
		return "redirect:/utente";
	}
	
	@PostMapping("/defaultPassword")
	public String defaultPassword(@RequestParam(name = "idUtente", required = true) Long idUtente) {
		utenteService.setDefaultPassword(idUtente);
		return "redirect:/utente";
	}

	
	@GetMapping("/resetPassword")
	public String resetPassword(Model model) {
		model.addAttribute("utente_reset_psw", new UtenteDTOReset());
		return "utente/reset";
	}
	
	@PostMapping("/resetta")
	public String resetta(@ModelAttribute("utente_reset_psw") UtenteDTOReset utente, Model model) {
		
		UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Utente utenteInSessione = utenteService.findByUsername(principal.getUsername());

		if (utente.getPasswordNuova().isBlank() || utente.getConfermaPassword().isBlank()
				|| utente.getPasswordNuova().isBlank()) {
			model.addAttribute("errorMessage", "Sono presenti errori di validazione!");
			return "utente/reset";
		}
		
		if (passwordEncoder.matches(utente.getPasswordNuova(), utenteInSessione.getPassword())) {
			model.addAttribute("errorMessage", "La nuova password e' uguale alla vecchia!");
			return "utente/reset";
		}
		
		if (!passwordEncoder.matches(utente.getPasswordVecchia(), utenteInSessione.getPassword())) {
			model.addAttribute("errorMessage", "La vecchia password e' sbagliata!");
			return "utente/reset";
		}
		
		if (!utente.getPasswordNuova().equals(utente.getConfermaPassword())) {
			model.addAttribute("errorMessage", "La password di conferma non coincide con quella appena inserita!");
			return "utente/reset";
		}
		
		
		//RIPARTIRE DA QUI
		utenteService.cambiaPassword(utente, utenteInSessione);

		return "redirect:/logout";
	}
}
