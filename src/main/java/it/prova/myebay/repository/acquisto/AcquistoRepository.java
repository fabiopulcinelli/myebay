package it.prova.myebay.repository.acquisto;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.prova.myebay.model.Acquisto;
import it.prova.myebay.repository.acquisto.CustomAcquistoRepository;

public interface AcquistoRepository extends CrudRepository<Acquisto, Long>, CustomAcquistoRepository {

	Optional<Acquisto> findById(Long id);
}
