package it.prova.myebay.repository.acquisto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import it.prova.myebay.model.Acquisto;
import it.prova.myebay.model.Utente;
public class CustomAcquistoRepositoryImpl implements CustomAcquistoRepository{
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Acquisto> findByExample(Acquisto example) {
		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereClauses = new ArrayList<String>();

		StringBuilder queryBuilder = new StringBuilder("select a from Acquisto a where a.id=a.id ");

		if (StringUtils.isNotEmpty(example.getDescrizione())) {
			whereClauses.add(" a.descrizione like :descrizione ");
			paramaterMap.put("descrizione", "%" + example.getDescrizione() + "%");
		}
		if (example.getData() != null) {
			whereClauses.add("a.data >= :data ");
			paramaterMap.put("data", example.getData());
		}
		if (example.getPrezzo() != null) {
			whereClauses.add("a.prezzo >= :prezzo ");
			paramaterMap.put("prezzo", example.getPrezzo());
		}
		
		queryBuilder.append(!whereClauses.isEmpty()?" and ":"");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<Acquisto> typedQuery = entityManager.createQuery(queryBuilder.toString(), Acquisto.class);

		for (String key : paramaterMap.keySet()) {
			typedQuery.setParameter(key, paramaterMap.get(key));
		}

		return typedQuery.getResultList();
	}

}
