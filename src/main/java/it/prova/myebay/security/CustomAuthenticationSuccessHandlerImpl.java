package it.prova.myebay.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import it.prova.myebay.dto.UtenteDTO;
import it.prova.myebay.model.Utente;
import it.prova.myebay.repository.utente.UtenteRepository;

@Component
public class CustomAuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

	@Autowired
	private UtenteRepository utenteRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		//voglio mettere in sessione uno userInfo perché spring security mette solo un principal da cui attingere username
		Utente utenteFromDb = utenteRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("Username " + authentication.getName() + " not found"));
		UtenteDTO utenteParziale = new UtenteDTO();
		utenteParziale.setNome(utenteFromDb.getNome());
		utenteParziale.setCognome(utenteFromDb.getCognome());
		utenteParziale.setUsername(utenteFromDb.getUsername());
		utenteParziale.setId(utenteFromDb.getId());
		request.getSession().setAttribute("userInfo", utenteParziale);


		 StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
		 String queryString = request.getQueryString();
		 String doveViene ="";

		 if (queryString == null) {
			 doveViene = requestURL.toString();
		 } else {
			 doveViene = requestURL.append('?').append(queryString).toString();
		 }
		 
		 if(doveViene.equals("http://localhost:8080/login/compra")) {
			 response.sendRedirect("show/" + utenteParziale.getId());
		 }
		 else {

			 response.sendRedirect("home");
		}
		

	}

}
