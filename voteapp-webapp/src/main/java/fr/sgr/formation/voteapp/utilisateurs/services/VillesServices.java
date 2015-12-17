package fr.sgr.formation.voteapp.utilisateurs.services;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.sgr.formation.voteapp.notifications.services.NotificationsServices;
import fr.sgr.formation.voteapp.traces.modele.TypesTraces;
import fr.sgr.formation.voteapp.utilisateurs.modele.Ville;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS)
public class VillesServices {

	/** Services d'authentification d'un utilisateur. */
	@Autowired
	private AuthentificationUtilisateursServices AuthentificationServices;

	/** Services de notification des événements. */
	@Autowired
	private NotificationsServices notificationsServices;

	@Autowired
	private EntityManager entityManager;

	/**
	 * Crée une nouvelle ville sur le système.
	 * 
	 * @param ville
	 *            Ville à créer.
	 * @return Ville créée.
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Ville creer(Ville ville, String idUser) {

		log.info("=====> Création de la ville : {}.", ville);
			boolean droit = AuthentificationServices.adminVerif(idUser);
			if(droit){
				if (ville == null) {
					notificationsServices.notifier("Impossible de crée une ville nulle",
							"Impossible de créer une ville nulle",TypesTraces.CREATION,TypesTraces.ECHEC,idUser);
					return null;
				}
				else{
					log.info("=====> TEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEST");
					notificationsServices.notifier("Création de la ville "+ville,
							"La ville "+ville.getNom()+" a été créée",TypesTraces.CREATION,TypesTraces.SUCCES,idUser);
					entityManager.persist(ville);
					return ville;	
				}
			}
			else{
				notificationsServices.notifier("Impossible de crée une ville si vous n'êtes pas administrateur",
						"Création de ville impossible / Pas admin",TypesTraces.CREATION,TypesTraces.ECHEC,idUser);
				return null;
			}
	}

}
