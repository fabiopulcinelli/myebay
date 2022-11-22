package it.prova.myebay.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import it.prova.myebay.validation.ValidationWithPassword;

public class UtenteDTOReset {
	private Long id;


	@NotBlank(message = "{utente.password.notblank}", groups = ValidationWithPassword.class)
	@Size(min = 8, max = 15, message = "Il valore inserito deve essere lungo tra {min} e {max} caratteri")
	private String passwordNuova;

	private String passwordVecchia;
	
	private String confermaPassword;
	
	public UtenteDTOReset() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPasswordNuova() {
		return passwordNuova;
	}

	public void setPasswordNuova(String passwordNuova) {
		this.passwordNuova = passwordNuova;
	}

	public String getPasswordVecchia() {
		return passwordVecchia;
	}
	
	public void setPasswordVecchia(String passwordVecchia) {
		this.passwordVecchia = passwordVecchia;
	}

	public String getConfermaPassword() {
		return confermaPassword;
	}

	public void setConfermaPassword(String confermaPassword) {
		this.confermaPassword = confermaPassword;
	}

}
