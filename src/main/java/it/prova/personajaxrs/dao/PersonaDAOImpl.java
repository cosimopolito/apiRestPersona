package it.prova.personajaxrs.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import it.prova.personajaxrs.model.Persona;

public class PersonaDAOImpl implements PersonaDAO {
	private EntityManager entityManager;

	@Override
	public List<Persona> list() throws Exception {
		return entityManager.createQuery("from Persona", Persona.class).getResultList();
	}

	@Override
	public Optional<Persona> findOne(Long id) throws Exception {
		return Optional.ofNullable(entityManager.find(Persona.class, id));
	}

	@Override
	public void update(Persona input) throws Exception {
		input = entityManager.merge(input);

	}

	@Override
	public void insert(Persona input) throws Exception {
		entityManager.persist(input);

	}

	@Override
	public void delete(Persona input) throws Exception {
		entityManager.remove(entityManager.merge(input));

	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Persona> findByExample(Persona example) throws Exception {
		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereClauses = new ArrayList<String>();

		StringBuilder queryBuilder = new StringBuilder("select p from Persona p where p.id = p.id ");

		if (StringUtils.isNotEmpty(example.getNome())) {
			whereClauses.add(" p.nome  like :nome ");
			paramaterMap.put("nome", "%" + example.getNome() + "%");
		}
		if (StringUtils.isNotEmpty(example.getCognome())) {
			whereClauses.add(" p.cognome like :cognome ");
			paramaterMap.put("cognome", "%" + example.getCognome() + "%");
		}
		if (example.getDataNascita() != null) {
			whereClauses.add("p.dataDiNascita >= :dataDiNascita ");
			paramaterMap.put("dataDiNascita", example.getDataNascita());
		}

		queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<Persona> typedQuery = entityManager.createQuery(queryBuilder.toString(), Persona.class);

		for (String key : paramaterMap.keySet()) {
			typedQuery.setParameter(key, paramaterMap.get(key));
		}

		return typedQuery.getResultList();
	}

}
