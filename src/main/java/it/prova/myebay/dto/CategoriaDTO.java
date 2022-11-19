package it.prova.myebay.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import it.prova.myebay.model.Annuncio;
import it.prova.myebay.model.Categoria;

public class CategoriaDTO {
	private Long id;

	@NotBlank(message = "{categoria.descrizione.notblank}")
	@Size(min = 4, max = 40, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri")
	private String descrizione;
	
	@NotBlank(message = "{categoria.codice.notblank}")
	@Size(min = 4, max = 40, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri")
	private String codice;
	
    private Set<Annuncio> annunci = new HashSet<>(0);
    
    public CategoriaDTO() {
	}

	public CategoriaDTO(Long id, String descrizione, String codice, Set<Annuncio> annunci) {
		super();
		this.id = id;
		this.descrizione = descrizione;
		this.codice = codice;
		this.annunci = annunci;
	}
	
	public CategoriaDTO(String descrizione, String codice) {
		super();
		this.descrizione = descrizione;
		this.codice = codice;
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

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public Set<Annuncio> getAnnunci() {
		return annunci;
	}

	public void setAnnunci(Set<Annuncio> annunci) {
		this.annunci = annunci;
	}

	public Categoria buildCategoriaModel() {
		return new Categoria(this.id, this.descrizione, this.codice, this.annunci);
		
	}

	public static CategoriaDTO buildCategoriaDTOFromModel(Categoria categoriaModel) {
		CategoriaDTO result = new CategoriaDTO(categoriaModel.getId(), categoriaModel.getDescrizione(), categoriaModel.getCodice(), categoriaModel.getAnnunci());

		return result;
	}

	public static List<CategoriaDTO> createCategoriaDTOListFromModelList(List<Categoria> modelListInput) {
		return modelListInput.stream().map(categoriaEntity -> {
			return CategoriaDTO.buildCategoriaDTOFromModel(categoriaEntity);
		}).collect(Collectors.toList());
	}
}
