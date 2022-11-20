package it.prova.myebay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.myebay.model.Categoria;
import it.prova.myebay.repository.categoria.CategoriaRepository;

@Service
public class CategoriaServiceImpl implements CategoriaService{
	@Autowired
	private CategoriaRepository repository;

	@Transactional(readOnly = true)
	public List<Categoria> listAllCategorie() {
		return (List<Categoria>) repository.findAll();
	}

	@Transactional(readOnly = true)
	public Categoria caricaSingoloCategoria(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Transactional
	public void aggiorna(Categoria categoriaInstance) {
		//deve aggiornare solo nome, cognome, username, ruoli
		Categoria categoriaReloaded = repository.findById(categoriaInstance.getId()).orElse(null);
		if(categoriaReloaded == null)
			throw new RuntimeException("Elemento non trovato");
		categoriaReloaded.setDescrizione(categoriaInstance.getDescrizione());
		categoriaReloaded.setCodice(categoriaInstance.getCodice());
		categoriaReloaded.setAnnunci(categoriaInstance.getAnnunci());
		repository.save(categoriaReloaded);
	}

	@Transactional
	public void inserisciNuovo(Categoria categoriaInstance) {
		repository.save(categoriaInstance);
	}

	@Transactional
	public void rimuovi(Long idToDelete) {
		repository.deleteById(idToDelete);
	}
}
