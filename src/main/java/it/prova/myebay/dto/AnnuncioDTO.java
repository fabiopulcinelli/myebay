package it.prova.myebay.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.prova.myebay.model.Annuncio;
import it.prova.myebay.model.Categoria;

public class AnnuncioDTO {
	private Long id;
	
	@NotBlank(message = "{annuncio.testoannuncio.notblank}")
	@Size(min = 4, max = 40, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri")
	private String testoAnnuncio;
	
	@NotNull(message = "{annuncio.prezzo.notnull}")
	@Min(1)
	private int prezzo;
	
	@NotNull(message = "{annuncio.data.notnull}")
	private Date data;
	
	public boolean aperto;
	
	@NotNull(message = "{annuncio.utente.notnull}")
	private UtenteDTO utenteInserimento;
	
	private Set<Categoria> categorie = new HashSet<>(0);
	
	public AnnuncioDTO() {
	}
	
	public AnnuncioDTO(Long id) {
		this.id = id;
	}

	public AnnuncioDTO(Long id, String testoAnnuncio, int prezzo, Date data, boolean aperto,
			Set<Categoria> categorie) {
		super();
		this.id = id;
		this.testoAnnuncio = testoAnnuncio;
		this.prezzo = prezzo;
		this.data = data;
		this.aperto = aperto;
		this.categorie = categorie;
	}
	
	public AnnuncioDTO(String testoAnnuncio, int prezzo, Date data, boolean aperto) {
		super();
		this.testoAnnuncio = testoAnnuncio;
		this.prezzo = prezzo;
		this.data = data;
		this.aperto = aperto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTestoAnnuncio() {
		return testoAnnuncio;
	}

	public void setTestoAnnuncio(String testoAnnuncio) {
		this.testoAnnuncio = testoAnnuncio;
	}

	public int getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(int prezzo) {
		this.prezzo = prezzo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public boolean isAperto() {
		return aperto;
	}

	public void setAperto(boolean aperto) {
		this.aperto = aperto;
	}

	public UtenteDTO getUtenteInserimento() {
		return utenteInserimento;
	}

	public void setUtenteInserimento(UtenteDTO utenteInserimento) {
		this.utenteInserimento = utenteInserimento;
	}

	public Set<Categoria> getCategorie() {
		return categorie;
	}

	public void setCategorie(Set<Categoria> categorie) {
		this.categorie = categorie;
	}
	
	public Annuncio buildAnnuncioModel(boolean includeIdRoles, boolean includeAcquisti, boolean includeAnnunci) {
		return new Annuncio(this.id, this.testoAnnuncio, this.prezzo, this.data, this.aperto, this.categorie, 
				this.utenteInserimento.buildUtenteModel(includeAnnunci, includeAnnunci, includeAnnunci));
		
	}

	public static AnnuncioDTO buildAnnuncioDTOFromModel(Annuncio annuncioModel, boolean includeIdRoles, boolean includeAcquisti, boolean includeAnnunci) {
		AnnuncioDTO result = new AnnuncioDTO(annuncioModel.getId(), annuncioModel.getTestoAnnuncio(), annuncioModel.getPrezzo(), annuncioModel.getData(),
				annuncioModel.isAperto(), annuncioModel.getCategorie());

		result.setUtenteInserimento(UtenteDTO.buildUtenteDTOFromModel(annuncioModel.getUtenteInserimento(), includeIdRoles, includeAcquisti, includeAnnunci));

		return result;
	}

	public static List<AnnuncioDTO> createAnnuncioDTOListFromModelList(List<Annuncio> modelListInput, boolean includeIdRoles, boolean includeAcquisti, boolean includeAnnunci) {
		return modelListInput.stream().map(annuncioEntity -> {
			return AnnuncioDTO.buildAnnuncioDTOFromModel(annuncioEntity, includeIdRoles, includeAcquisti, includeAnnunci);
		}).collect(Collectors.toList());
	}
}
