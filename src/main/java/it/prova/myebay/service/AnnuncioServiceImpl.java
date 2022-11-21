package it.prova.myebay.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.myebay.model.Acquisto;
import it.prova.myebay.model.Annuncio;
import it.prova.myebay.repository.annuncio.AnnuncioRepository;

@Service
public class AnnuncioServiceImpl implements AnnuncioService {
	
	@Autowired
	private AnnuncioRepository repository;

	@Transactional(readOnly = true)
	public List<Annuncio> listAllAnnunci() {
		return (List<Annuncio>) repository.findAll();
	}

	@Transactional(readOnly = true)
	public Annuncio caricaSingoloAnnuncio(Long id) {
		return repository.findById(id).orElse(null);
	}
	
	@Transactional(readOnly = true)
	public Annuncio caricaSingoloAnnuncioConCategorie(Long id) {
		return repository.findByIdConCategorie(id).orElse(null);
	}

	@Transactional
	public void aggiorna(Annuncio annuncioInstance) {
		//deve aggiornare solo nome, cognome, username, ruoli
		Annuncio annuncioReloaded = repository.findById(annuncioInstance.getId()).orElse(null);
		if(annuncioReloaded == null)
			throw new RuntimeException("Elemento non trovato");
		annuncioReloaded.setTestoAnnuncio(annuncioInstance.getTestoAnnuncio());
		annuncioReloaded.setPrezzo(annuncioInstance.getPrezzo());
		annuncioReloaded.setData(annuncioInstance.getData());
		annuncioReloaded.setAperto(annuncioInstance.isAperto());
		annuncioReloaded.setCategorie(annuncioInstance.getCategorie());
		repository.save(annuncioReloaded);
	}

	@Transactional
	public void inserisciNuovo(Annuncio annuncioInstance) {
		annuncioInstance.setAperto(true);
		repository.save(annuncioInstance);
	}

	@Transactional
	public void rimuovi(Long idToDelete) {
		repository.deleteById(idToDelete);
	}

	@Transactional(readOnly = true)
	public List<Annuncio> findByExample(Annuncio example) {
		return repository.findByExample(example);
	}

	@Override
	public List<Annuncio> findByIdUtente(Long idUtente) {
		return repository.findByIdUtente(idUtente);
	}
}
