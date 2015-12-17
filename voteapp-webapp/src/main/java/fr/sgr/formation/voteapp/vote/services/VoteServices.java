package fr.sgr.formation.voteapp.vote.services;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.sgr.formation.voteapp.notifications.services.NotificationsServices;
import fr.sgr.formation.voteapp.traces.modele.TypesTraces;
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
	public Vote creer(Vote vote) throws VoteInvalideException {

		log.info("=====> Création du vote : {}.", vote);

		if (vote == null) {
			throw new VoteInvalideException(ErreurVote.VOTE_OBLIGATOIRE);
		}

		/** Validation de l'existance du vote. */
		if (rechercherParVoteKey(vote.getKey()) != null) {
			throw new VoteInvalideException(ErreurVote.VOTE_EXISTANT);
		}

		/**
		 * Validation du vote : lève une exception si le vote est invalide.
		 */
		validationServices.validerVote(vote);

		/** Notification de l'événement de création */
		notificationsServices.notifier("Création du vote: " + vote.toString(),
				"Création vote "+vote.toString(),TypesTraces.CREATION,TypesTraces.SUCCES,null);

		/** Persistance du vote. */
		entityManager.persist(vote);

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
