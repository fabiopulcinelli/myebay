package it.prova.myebay.repository.annuncio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.myebay.model.Acquisto;
import it.prova.myebay.model.Annuncio;

public interface AnnuncioRepository extends CrudRepository<Annuncio, Long>, CustomAnnuncioRepository {
	@Query("from Annuncio a left join fetch a.categorie where a.id = ?1")
	Optional<Annuncio> findByIdConCategorie(Long id);
	
	@Query(value="select * from annuncio where id=id and utente_id = ?1", nativeQuery= true)
	List<Annuncio> findByIdUtente(Long idUtente);
}
