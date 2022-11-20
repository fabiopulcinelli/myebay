package it.prova.myebay.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.NumberFormat;

import it.prova.myebay.model.Acquisto;
import it.prova.myebay.model.Annuncio;
import it.prova.myebay.model.Categoria;
import it.prova.myebay.model.Ruolo;
import it.prova.myebay.model.Utente;

public class AnnuncioDTO {
	private Long id;
	
	@NotBlank(message = "{annuncio.testoannuncio.notblank}")
	@Size(min = 4, max = 40, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri")
	private String testoAnnuncio;
	
	@NotNull(message = "{annuncio.prezzo.notnull}")
	@Min(1)
	@NumberFormat
	private Integer prezzo;
	
	@NotNull(message = "{annuncio.data.notnull}")
	private Date data;
	
	public boolean aperto;
	
	@NotNull(message = "{annuncio.utente.notnull}")
	private UtenteDTO utenteInserimento;
	
	private Set<Categoria> categorie = new HashSet<>(0);
	
	private Long[] categorieIds;
	private Long utenteInserimentoId;
	
	public AnnuncioDTO() {
	}
	
	public AnnuncioDTO(Long id) {
		this.id = id;
	}

	public AnnuncioDTO(Long id, String testoAnnuncio, Integer prezzo, Date data, boolean aperto) {
		super();
		this.id = id;
		this.testoAnnuncio = testoAnnuncio;
		this.prezzo = prezzo;
		this.data = data;
		this.aperto = aperto;
	}
	
	public AnnuncioDTO(String testoAnnuncio, Integer prezzo, Date data, boolean aperto) {
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

	public Integer getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(Integer prezzo) {
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
	
	public Long[] getCategorieIds() {
		return categorieIds;
	}

	public void setCategorieIds(Long[] categorieIds) {
		this.categorieIds = categorieIds;
	}

	public Long getUtenteInserimentoId() {
		return utenteInserimentoId;
	}

	public void setUtenteInserimentoId(Long utenteInserimentoId) {
		this.utenteInserimentoId = utenteInserimentoId;
	}

	public Annuncio buildAnnuncioModel(boolean includeIdCategorie, boolean includeUtenteInserimento) {
		Annuncio result = new Annuncio(this.id, this.testoAnnuncio, this.prezzo, this.data, this.aperto);
		
		if (includeIdCategorie && categorieIds != null)
			result.setCategorie(Arrays.asList(categorieIds).stream().map(id -> new Categoria(id)).collect(Collectors.toSet()));
		if (includeUtenteInserimento && utenteInserimentoId != null)
			result.setUtenteInserimento(new Utente(utenteInserimentoId));

		return result;
	}

	public static AnnuncioDTO buildAnnuncioDTOFromModel(Annuncio annuncioModel, boolean includeIdCategorie, boolean includeUtenteInserimento) {
		AnnuncioDTO result = new AnnuncioDTO(annuncioModel.getId(), annuncioModel.getTestoAnnuncio(), annuncioModel.getPrezzo(), annuncioModel.getData(),
				annuncioModel.isAperto());
		
		if (includeIdCategorie && !annuncioModel.getCategorie().isEmpty())
			result.categorieIds = annuncioModel.getCategorie().stream().map(r -> r.getId()).collect(Collectors.toList())
					.toArray(new Long[] {});
		
		if (includeUtenteInserimento)
			result.utenteInserimentoId = annuncioModel.getUtenteInserimento().getId();

		return result;
	}

	public static List<AnnuncioDTO> createAnnuncioDTOListFromModelList(List<Annuncio> modelListInput, boolean includeIdCategorie, boolean includeUtenteInserimento) {
		return modelListInput.stream().map(annuncioEntity -> {
			return AnnuncioDTO.buildAnnuncioDTOFromModel(annuncioEntity, includeIdCategorie, includeUtenteInserimento);
		}).collect(Collectors.toList());
	}
}
