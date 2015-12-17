package fr.sgr.formation.voteapp.traces.services;

import java.util.Date;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.sgr.formation.voteapp.traces.modele.Trace;
import fr.sgr.formation.voteapp.traces.modele.TypesTraces;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class TraceServices {
	
	/** Services de notification des événements. */

	@Autowired
	private EntityManager entityManager;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void creerTrace(String desc, TypesTraces type, TypesTraces result, String idUser) {
		
		
		Trace trace = new Trace();

		Date dateDeb = new Date();
		trace.setDateDeb(dateDeb);
		
		trace.setResAction(result);
		trace.setDescAction(desc);
		trace.setTypeAction(type);


		Date dateFin = new Date();
		trace.setDateFin(dateFin);
		
		Utilisateur temp = entityManager.find(Utilisateur.class, idUser);
		trace.setUser(temp);
		
		entityManager.persist(trace);
	}

}
