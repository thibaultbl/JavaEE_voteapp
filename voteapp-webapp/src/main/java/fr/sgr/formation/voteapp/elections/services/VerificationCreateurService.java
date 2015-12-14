package fr.sgr.formation.voteapp.elections.services;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.sgr.formation.voteapp.elections.modele.Election;
import fr.sgr.formation.voteapp.notifications.services.NotificationsServices;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class VerificationCreateurService {
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private NotificationsServices notificationsServices;

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean createurVerif(String login, String titre) {

		boolean droit = false;

		if (StringUtils.isNotBlank(login)) {
			// Trouve l'utilisateur par le login
			Utilisateur user = entityManager.find(Utilisateur.class, login);
			Election election = entityManager.find(Election.class, titre);
			// Vérifie si l'utilisateur existe en base
			if (user != null) {
				notificationsServices.notifier("Utilisateur : " + user.getLogin());
				if (user.getLogin().equals(login)) { // Forcément vraie si
														// l'utilisateur existe
														// en base...
					if (election.getProprietaire().getLogin().equals(login)) {
						droit = true;
					}

				}
			} else {
				/** Notification de l'échec de l'authentification */
				notificationsServices
						.notifier("Impossible d'utiliser l'utilisateur " + login + " car il n'existe pas.");
			}
		}
		return droit;
	}
}
