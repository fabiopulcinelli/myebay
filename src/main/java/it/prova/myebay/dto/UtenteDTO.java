package it.prova.myebay.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.prova.myebay.model.Acquisto;
import it.prova.myebay.model.Annuncio;
import it.prova.myebay.model.Ruolo;
import it.prova.myebay.model.StatoUtente;
import it.prova.myebay.model.Utente;
import it.prova.myebay.validation.ValidationNoPassword;
import it.prova.myebay.validation.ValidationWithPassword;

public class UtenteDTO {

	private Long id;

	@NotBlank(message = "{username.notblank}", groups = { ValidationWithPassword.class, ValidationNoPassword.class })
	@Size(min = 3, max = 15, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri")
	private String username;

	@NotBlank(message = "{password.notblank}", groups = ValidationWithPassword.class)
	@Size(min = 8, max = 15, message = "Il valore inserito deve essere lungo tra {min} e {max} caratteri")
	private String password;

	private String confermaPassword;

	@NotBlank(message = "{nome.notblank}", groups = { ValidationWithPassword.class, ValidationNoPassword.class })
	private String nome;

	@NotBlank(message = "{cognome.notblank}", groups = { ValidationWithPassword.class, ValidationNoPassword.class })
	private String cognome;

	private Date dateCreated;
	
	@NotNull(message = "{creditoResiduo.notnull}")
	@Min(1)
	private int creditoResiduo;

	private StatoUtente stato;

	private Long[] ruoliIds;
	
	private Long[] acquistiIds;
	private Long[] annunciIds;

	public UtenteDTO() {
	}

	public UtenteDTO(Long id, String username, String nome, String cognome, int creditoResiduo, StatoUtente stato) {
		super();
		this.id = id;
		this.username = username;
		this.nome = nome;
		this.cognome = cognome;
		this.creditoResiduo = creditoResiduo;
		this.stato = stato;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public int getCreditoResiduo() {
		return creditoResiduo;
	}

	public void setCreditoResiduo(int creditoResiduo) {
		this.creditoResiduo = creditoResiduo;
	}

	public StatoUtente getStato() {
		return stato;
	}

	public void setStato(StatoUtente stato) {
		this.stato = stato;
	}

	public String getConfermaPassword() {
		return confermaPassword;
	}

	public void setConfermaPassword(String confermaPassword) {
		this.confermaPassword = confermaPassword;
	}

	public Long[] getRuoliIds() {
		return ruoliIds;
	}

	public void setRuoliIds(Long[] ruoliIds) {
		this.ruoliIds = ruoliIds;
	}
	
	public Long[] getAcquistiIds() {
		return acquistiIds;
	}

	public void setAcquistiIds(Long[] acquistiIds) {
		this.acquistiIds = acquistiIds;
	}

	public Long[] getAnnunciIds() {
		return annunciIds;
	}

	public void setAnnunciIds(Long[] annunciIds) {
		this.annunciIds = annunciIds;
	}

	public boolean isAttivo() {
		return this.stato != null && this.stato.equals(StatoUtente.ATTIVO);
	}

	public Utente buildUtenteModel(boolean includeIdRoles, boolean includeAcquisti, boolean includeAnnunci) {
		Utente result = new Utente(this.id, this.username, this.password, this.nome, this.cognome, this.dateCreated, this.creditoResiduo,
				this.stato);
		if (includeIdRoles && ruoliIds != null)
			result.setRuoli(Arrays.asList(ruoliIds).stream().map(id -> new Ruolo(id)).collect(Collectors.toSet()));
		if (includeAcquisti && acquistiIds != null)
			result.setAcquisti(Arrays.asList(acquistiIds).stream().map(id -> new Acquisto(id)).collect(Collectors.toSet()));
		if (includeAnnunci && annunciIds != null)
			result.setAnnunci(Arrays.asList(annunciIds).stream().map(id -> new Annuncio(id)).collect(Collectors.toSet()));

		return result;
	}

	// niente password...
	public static UtenteDTO buildUtenteDTOFromModel(Utente utenteModel, boolean includeRoles, boolean includeAcquisti, boolean includeAnnunci) {
		UtenteDTO result = new UtenteDTO(utenteModel.getId(), utenteModel.getUsername(), utenteModel.getNome(),
				utenteModel.getCognome(), utenteModel.getCreditoResiduo(), utenteModel.getStato());

		if (includeRoles && !utenteModel.getRuoli().isEmpty())
			result.ruoliIds = utenteModel.getRuoli().stream().map(r -> r.getId()).collect(Collectors.toList())
					.toArray(new Long[] {});
		
		if (includeAcquisti && !utenteModel.getAcquisti().isEmpty())
			result.acquistiIds = utenteModel.getAcquisti().stream().map(r -> r.getId()).collect(Collectors.toList())
					.toArray(new Long[] {});
		
		if (includeAnnunci && !utenteModel.getAnnunci().isEmpty())
			result.annunciIds = utenteModel.getAnnunci().stream().map(r -> r.getId()).collect(Collectors.toList())
					.toArray(new Long[] {});

		return result;
	}

	public static List<UtenteDTO> createUtenteDTOListFromModelList(List<Utente> modelListInput, boolean includeRoles, boolean includeAcquisti, boolean includeAnnunci) {
		return modelListInput.stream().map(utenteEntity -> {
			return UtenteDTO.buildUtenteDTOFromModel(utenteEntity, includeRoles, includeAcquisti, includeAnnunci);
		}).collect(Collectors.toList());
	}

}
