package it.prova.myebay.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.prova.myebay.model.Acquisto;

public class AcquistoDTO {

	private Long id;
	
	@NotBlank(message = "{acquisto.descrizione.notblank}")
	@Size(min = 4, max = 40, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri")
	private String descrizione;
	
	@NotNull(message = "{acquisto.data.notnull}")
	private Date data;

	@NotNull(message = "{acquisto.prezzo.notnull}")
	@Min(1)
	private int prezzo;
	
	@NotNull(message = "{acquisto.utente.notnull}")
	private UtenteDTO utenteAcquirente;

	public AcquistoDTO() {
	}
	
	public AcquistoDTO(Long id) {
		this.id = id;
	}
	
	public AcquistoDTO(Long id, String descrizione, Date data, int prezzo) {
		super();
		this.id = id;
		this.descrizione = descrizione;
		this.data = data;
		this.prezzo = prezzo;
	}
	
	public AcquistoDTO(String descrizione, Date data, int prezzo) {
		super();
		this.descrizione = descrizione;
		this.data = data;
		this.prezzo = prezzo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(int prezzo) {
		this.prezzo = prezzo;
	}

	public UtenteDTO getUtenteAcquirente() {
		return utenteAcquirente;
	}

	public void setUtenteAcquirente(UtenteDTO utenteAcquirente) {
		this.utenteAcquirente = utenteAcquirente;
	}
	
	public Acquisto buildAcquistoModel(boolean includeIdRoles, boolean includeAcquisti, boolean includeAnnunci) {
		return new Acquisto(this.id, this.descrizione, this.data, this.prezzo, 
				this.utenteAcquirente.buildUtenteModel(includeAnnunci, includeAnnunci, includeAnnunci));
		
	}

	public static AcquistoDTO buildAcquistoDTOFromModel(Acquisto acquistoModel, boolean includeIdRoles, boolean includeAcquisti, boolean includeAnnunci) {
		AcquistoDTO result = new AcquistoDTO(acquistoModel.getId(), acquistoModel.getDescrizione(), acquistoModel.getData(),
				acquistoModel.getPrezzo());

		result.setUtenteAcquirente(UtenteDTO.buildUtenteDTOFromModel(acquistoModel.getUtenteAcquirente(), includeIdRoles, includeAcquisti, includeAnnunci));

		return result;
	}

	public static List<AcquistoDTO> createAcquistoDTOListFromModelList(List<Acquisto> modelListInput, boolean includeIdRoles, boolean includeAcquisti, boolean includeAnnunci) {
		return modelListInput.stream().map(acquistoEntity -> {
			return AcquistoDTO.buildAcquistoDTOFromModel(acquistoEntity, includeIdRoles, includeAcquisti, includeAnnunci);
		}).collect(Collectors.toList());
	}
}
