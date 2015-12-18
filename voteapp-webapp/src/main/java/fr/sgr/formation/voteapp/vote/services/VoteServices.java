package fr.sgr.formation.voteapp.vote.services;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.sgr.formation.voteapp.elections.modele.Election;
import fr.sgr.formation.voteapp.elections.services.ElectionsServices;
import fr.sgr.formation.voteapp.notifications.services.NotificationsServices;
import fr.sgr.formation.voteapp.traces.modele.TypesTraces;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateursServices;
import fr.sgr.formation.voteapp.vote.modele.ChoixVote;
import fr.sgr.formation.voteapp.vote.modele.Vote;
import fr.sgr.formation.voteapp.vote.services.VoteInvalideException.ErreurVote;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS)
public class VoteServices {
	/** Services de validation d'un vote. */
	@Autowired
	private ValidationVoteServices validationServices;
	/** Services de notification des événements. */
	@Autowired
	private NotificationsServices notificationsServices;
	@Autowired
	private UtilisateursServices utilisateursServices;
	@Autowired
	private ElectionsServices electionsServices;

	@Autowired
	private EntityManager entityManager;

	/**
	 * Crée un nouveau vote sur le système.
	 * 
	 * @param vote
	 *            Vote à créer.
	 * @return Vote créée.
	 * @throws VoteInvalideException
	 *             Levée si l'election est invalide.
	 */

	@Transactional(propagation = Propagation.REQUIRED)
	public Vote creer(String titre, String choix, String idUser) throws VoteInvalideException {

		log.info("=====> Création du vote pour l'élection : {}.", titre);
		Query query = entityManager.createNativeQuery("SELECT login FROM UTILISATEUR where login=?");
		query.setParameter(1, idUser);

		if (utilisateursServices.rechercherParLogin(query.getSingleResult().toString()) == null) {
			throw new VoteInvalideException(ErreurVote.UTILISATEUR_NON_ENREGISTRE);
		}

		Vote vote = new Vote();
		if (choix.toLowerCase().equals("oui")) {
			vote.setChoix(ChoixVote.OUI);
		} else if (choix.toLowerCase().equals("non")) {
			vote.setChoix(ChoixVote.NON);
		} else {
			throw new VoteInvalideException(ErreurVote.CHOIX_NON_AUTORISE);
		}

		vote.setUtilisateur(utilisateursServices.rechercherParLogin(idUser));

		Query query2 = entityManager.createNativeQuery("SELECT titre FROM ELECTION WHERE titre=?");
		query2.setParameter(1, titre);

		if (electionsServices.rechercherParTitre(query2.getSingleResult().toString()) == null) {
			throw new VoteInvalideException(ErreurVote.ELECTION_INEXISTANTE);
		}

		Query query3 = entityManager.createNativeQuery(
				"SELECT UTILISATEUR_LOGIN FROM VOTE WHERE (UTILISATEUR_LOGIN=:username) AND (ELECTION_TITRE=:titreE)");
		query3.setParameter("username", idUser);
		query3.setParameter("titreE", titre);

		if (query3.getResultList().isEmpty() == false) {
			throw new VoteInvalideException(ErreurVote.DEJA_VOTE);
		}

		Election elec = electionsServices.rechercherParTitre(titre);
		if (elec.isActiveElection() == false) {
			throw new VoteInvalideException(ErreurVote.ELECTION_CLOTURE);
		}

		vote.setElection(electionsServices.rechercherParTitre(titre));

		/**
		 * Validation du vote : lève une exception si le vote est invalide.
		 */
		validationServices.validerVote(vote);

		/** Persistance du vote. */
		entityManager.persist(vote);

		/** Notification de l'événement de création */
		notificationsServices.notifier("Création du vote: " + vote.toString(),
				"Création vote " + vote.getKey(), TypesTraces.CREATION, TypesTraces.SUCCES,
				vote.getUtilisateur().getLogin());

		return vote;
	}

	/**
	 * Retourne le vote identifié par l'idVote.
	 * 
	 * @param idVote
	 *            idVote identifiant le vote.
	 * @return Retourne le vote identifié par l'idVote.
	 */
	public Vote rechercherParVoteKey(long key2) {
		log.info("=====> Recherche du vote d'voteKey {}.", key2);
		return entityManager.find(Vote.class, key2);
	}

}
