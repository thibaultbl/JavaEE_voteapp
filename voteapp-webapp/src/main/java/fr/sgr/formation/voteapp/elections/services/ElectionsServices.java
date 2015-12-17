package fr.sgr.formation.voteapp.elections.services;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.sgr.formation.voteapp.elections.modele.Election;
import fr.sgr.formation.voteapp.elections.services.ElectionInvalideException.ErreurElection;
import fr.sgr.formation.voteapp.notifications.services.NotificationsServices;
import fr.sgr.formation.voteapp.traces.modele.TypesTraces;
import fr.sgr.formation.voteapp.utilisateurs.services.AuthentificationUtilisateursServices;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS)
public class ElectionsServices {
	/** Services de validation d'une élection. */
	@Autowired
	private ValidationElectionServices validationServices;
	/** Services de notification des événements. */
	@Autowired
	private NotificationsServices notificationsServices;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private AuthentificationUtilisateursServices authentificationUtilisateursServices;
	@Autowired
	private VerificationCreateurService verificationCreateurService;

	/**
	 * Crée une nouvelle élection sur le système.
	 * 
	 * @param election
	 *            Election à créer.
	 * @return Election créée.
	 * @throws ElectionInvalideException
	 *             Levée si l'election est invalide.
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Election creer(Election election, String idUser) throws ElectionInvalideException {
		boolean gerant = authentificationUtilisateursServices.gerantVerif(idUser);
		if (gerant) {

			log.info("=====> Création de l'election : {}.", election);

			if (election == null) {
				throw new ElectionInvalideException(ErreurElection.ELECTION_OBLIGATOIRE);
			}

			/** Validation de l'existance de l'election. */
			if (rechercherParTitre(election.getTitre()) != null) {
				throw new ElectionInvalideException(ErreurElection.ELECTION_EXISTANTE);
			}

			/**
			 * Validation de l'election: lève une exception si l'election est
			 * invalide.
			 */
			validationServices.validerElection(election);

			/** Notification de l'événement de création */
			notificationsServices.notifier("Création de l'election: " + election.toString(),"Création éléction " + election.toString(),TypesTraces.CREATION,TypesTraces.SUCCES,idUser);

			/** Persistance de l'election. */
			entityManager.persist(election);
		} else {
			notificationsServices
					.notifier("Impossible de créer l'élection " + election.getTitre()+" car vous n'êtes pas gérant",
							"Création impossible / Utilisateur non gérant",TypesTraces.CREATION,TypesTraces.ECHEC,idUser);
		}
		return election;
	}

	/**
	 * Cloture uen élection
	 * 
	 * @param election
	 *            Election à cloturer.
	 * @return Election cloturée.
	 * @throws ElectionInvalideException
	 *             Levée si l'election est invalide.
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Election cloturer(String titre, String idUser) throws ElectionInvalideException {
		/** Validation de l'existance de l'election. */
		if (rechercherParTitre(titre) == null) {
			throw new ElectionInvalideException(ErreurElection.ELECTION_NON_EXISTANTE);
		}

		Election election = entityManager.find(Election.class, titre);

		if (election == null) {
			throw new ElectionInvalideException(ErreurElection.ELECTION_OBLIGATOIRE);
		}

		log.info("=====> Cloture de l'election : {}.", election);

		boolean gerant = authentificationUtilisateursServices.gerantVerif(idUser);
		boolean createur = verificationCreateurService.createurVerif(idUser, titre);

		if (gerant) {
			// Supprime l'utilisateur de la base si il existe
			if (createur) {
				/**
				 * Validation de l'election: lève une exception si l'election
				 * est invalide.
				 */
				validationServices.validerElection(election);

				/** Notification de l'événement de cloture */
				notificationsServices.notifier("Cloture de l'election: " + election.toString(),
						"Cloture éléction",TypesTraces.CLOTURE,TypesTraces.SUCCES,idUser);
				entityManager.remove(election);
				election.setActiveElection(false);
				creer(election, idUser);

			} else {
				notificationsServices
						.notifier("Impossible de cloturer l'élection " + titre + " car vous n'êtes pas le créateur",
								"Cloture impossible / Pas créateur",TypesTraces.CLOTURE,TypesTraces.ECHEC,idUser);
			}

		} else {
			notificationsServices
					.notifier("Impossible de cloturer l'élection " + titre + " car vous n'êtes pas gérant",
							"Cloture impossible / Pas gérant",TypesTraces.CLOTURE,TypesTraces.ECHEC,idUser);

		}

		/** Persistance de l'election. */
		// entityManager.persist(election);

		return election;
	}

	/**
	 * Modifier uen élection
	 * 
	 * @param election
	 *            Election à cloturer.
	 * @return Election cloturée.
	 * @throws ElectionInvalideException
	 *             Levée si l'election est invalide.
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Election modifier(String titre, String idUser) throws ElectionInvalideException {
		/** Validation de l'existance de l'election. */
		if (rechercherParTitre(titre) == null) {
			throw new ElectionInvalideException(ErreurElection.ELECTION_NON_EXISTANTE);
		}

		Election election = entityManager.find(Election.class, titre);

		if (election == null) {
			throw new ElectionInvalideException(ErreurElection.ELECTION_OBLIGATOIRE);
		}

		log.info("=====> Modification de l'election : {}.", election);

		boolean gerant = authentificationUtilisateursServices.gerantVerif(idUser);
		boolean createur = verificationCreateurService.createurVerif(idUser, titre);

		if (gerant) {
			// Supprime l'utilisateur de la base si il existe
			if (createur) {
				/**
				 * Validation de l'election: lève une exception si l'election
				 * est invalide.
				 */
				validationServices.validerElection(election);

				/** Notification de l'événement de cloture */
				notificationsServices.notifier("Modification de l'election: " + election.toString(),
						"Modification éléction",TypesTraces.MODIFICATION,TypesTraces.SUCCES,idUser);
				entityManager.remove(election);
				election.setActiveElection(false);

				creer(election, idUser);

			} else {
				notificationsServices
						.notifier("Impossible de modifier l'élection " + titre + " car vous n'êtes pas le créateur",
								"Modification impossible / Pas créateur",TypesTraces.MODIFICATION,TypesTraces.ECHEC,idUser);
			}

		} else {
			notificationsServices
					.notifier("Impossible de modifier l'élection " + titre + " car vous n'êtes pas gérant",
							"Modification impossible / Pas gérant",TypesTraces.MODIFICATION,TypesTraces.ECHEC,idUser);

		}

		/** Persistance de l'election. */
		// entityManager.persist(election);

		return election;
	}

	/**
	 * Supprimer uen élection
	 * 
	 * @param election
	 *            Election à cloturer.
	 * @return Election cloturée.
	 * @throws ElectionInvalideException
	 *             Levée si l'election est invalide.
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Election supprimer(String titre, String idUser) throws ElectionInvalideException {
		/** Validation de l'existance de l'election. */
		if (rechercherParTitre(titre) == null) {
			throw new ElectionInvalideException(ErreurElection.ELECTION_NON_EXISTANTE);
		}
		Election election = entityManager.find(Election.class, titre);
		log.info("=====> supression de l'election : {}.", election);

		if (election == null) {
			throw new ElectionInvalideException(ErreurElection.ELECTION_OBLIGATOIRE);
		}

		boolean gerant = authentificationUtilisateursServices.gerantVerif(idUser);
		boolean createur = verificationCreateurService.createurVerif(idUser, titre);

		if (gerant) {
			// Supprime l'utilisateur de la base si il existe
			if (createur) {
				if (election != null) {
					/**
					 * Validation de l'election: lève une exception si
					 * l'election est invalide.
					 */
					validationServices.validerElection(election);

					/** Notification de l'événement de création */
					notificationsServices.notifier("Suppression de l'election: " + election.toString(),
							"Suppression éléction",TypesTraces.SUPRESSION,TypesTraces.SUCCES,idUser);
					entityManager.remove(election);
				}
			} else {
				notificationsServices
						.notifier("Impossible de supprimer l'élection " + titre + " car vous n'êtes pas le créateur",
								"Suppression impossible / Pas créateur",TypesTraces.SUPRESSION,TypesTraces.ECHEC,idUser);
			}

		} else {
			notificationsServices
					.notifier("Impossible de supprimer l'élection " + titre + " car vous n'êtes pas gérant",
							"Suppression impossible / Pas gérant",TypesTraces.SUPRESSION,TypesTraces.ECHEC,idUser);

		}

		/** Persistance de l'election. */
		// entityManager.persist(election);

		return election;
	}

	/**
	 * Retourne l'election identifiée par le titre.
	 * 
	 * @param titre
	 *            titre identifiant l'election.
	 * @return Retourne l'election identifiée par le titre.
	 */
	public Election rechercherParTitre(String titre) {
		log.info("=====> Recherche de l'election de titre {}.", titre);

		if (StringUtils.isNotBlank(titre)) {
			return entityManager.find(Election.class, titre);
		}

		return null;
	}
}
