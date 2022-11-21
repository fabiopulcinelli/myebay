package it.prova.myebay.service;

import java.util.List;

import it.prova.myebay.model.Acquisto;
import it.prova.myebay.model.Annuncio;

public interface AnnuncioService {
	public List<Annuncio> listAllAnnunci() ;

	public Annuncio caricaSingoloAnnuncio(Long id);
	
	public Annuncio caricaSingoloAnnuncioConCategorie(Long id);

	public void aggiorna(Annuncio annuncioInstance);

	public void inserisciNuovo(Annuncio annuncioInstance);

	public void rimuovi(Long idToDelete);

	public List<Annuncio> findByExample(Annuncio example);
	
	public List<Annuncio> findByIdUtente(Long idUtente);
}
