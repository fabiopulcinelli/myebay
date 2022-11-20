package it.prova.myebay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.myebay.model.Acquisto;
import it.prova.myebay.model.Utente;
import it.prova.myebay.repository.acquisto.AcquistoRepository;

@Service
public class AcquistoServiceImpl implements AcquistoService {
	@Autowired
	private AcquistoRepository repository;

	@Transactional(readOnly = true)
	public List<Acquisto> listAllAcquisti() {
		return (List<Acquisto>) repository.findAll();
	}

	@Transactional(readOnly = true)
	public Acquisto caricaSingoloAcquisto(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Transactional
	public void aggiorna(Acquisto acquistoInstance) {
		//deve aggiornare solo nome, cognome, username, ruoli
		Acquisto acquistoReloaded = repository.findById(acquistoInstance.getId()).orElse(null);
		if(acquistoReloaded == null)
			throw new RuntimeException("Elemento non trovato");
		acquistoReloaded.setDescrizione(acquistoInstance.getDescrizione());
		acquistoReloaded.setPrezzo(acquistoInstance.getPrezzo());
		acquistoReloaded.setData(acquistoInstance.getData());
		repository.save(acquistoReloaded);
	}

	@Transactional
	public void inserisciNuovo(Acquisto acquistoInstance) {
		repository.save(acquistoInstance);
	}

	@Transactional
	public void rimuovi(Long idToDelete) {
		repository.deleteById(idToDelete);
	}

	@Transactional(readOnly = true)
	public List<Acquisto> findByExample(Acquisto example) {
		return repository.findByExample(example);
	}

	@Override
	public List<Acquisto> findByIdUtente(Long idUtente) {
		return repository.findByIdUtente(idUtente);
	}
}
