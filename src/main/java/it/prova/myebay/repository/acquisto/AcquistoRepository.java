package it.prova.myebay.repository.acquisto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.myebay.model.Acquisto;
import it.prova.myebay.repository.acquisto.CustomAcquistoRepository;

public interface AcquistoRepository extends CrudRepository<Acquisto, Long>, CustomAcquistoRepository {

	Optional<Acquisto> findById(Long id);
	
	@Query(value="select * from acquisto where id=id and utente_id = ?1", nativeQuery= true)
	List<Acquisto> findByIdUtente(Long idUtente);
}
