package fr.sgr.formation.voteapp.notifications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.sgr.formation.voteapp.traces.modele.TypesTraces;
import fr.sgr.formation.voteapp.traces.services.TraceServices;
import lombok.extern.slf4j.Slf4j;

/**
 * Services de notification d'événements.
 */
@Service
@Slf4j
public class NotificationsServices {
	
	/** Services de gestion des traces */
	@Autowired
	private TraceServices traceServices;
	
	/**
	 * Service de notification d'un événement.
	 * 
	 * @param message
	 *            Message représentant l'événement.
	 */
	public void notifier(String message,String desc, TypesTraces type, TypesTraces result, String idUser) {
		log.info("[EVT]: " + message);
		traceServices.creerTrace("", null, null, idUser);
	}
}
