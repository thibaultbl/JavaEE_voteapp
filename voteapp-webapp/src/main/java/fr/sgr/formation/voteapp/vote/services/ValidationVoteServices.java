package fr.sgr.formation.voteapp.vote.services;

import org.springframework.stereotype.Service;

import fr.sgr.formation.voteapp.vote.modele.Vote;
import fr.sgr.formation.voteapp.vote.services.VoteInvalideException.ErreurVote;

/**
 * Bean mettant à disposition les services permettant de valider les
 * informations d'un vote.
 */
@Service
public class ValidationVoteServices {
	/**
	 * Vérifie qu'un vote est valide.
	 * 
	 * @param vote
	 *            Vote à valider.
	 * @return true si le vote est valide, false si aucun vote n'est passé en
	 *         paramètre.
	 * @throws VoteInvalideException
	 *             Levée si le vote est invalide.
	 */
	public boolean validerVote(Vote vote) throws VoteInvalideException {
		if (vote == null) {
			return false;
		}

		// validerVoteKey(vote);
		validerChoix(vote);

		/** Validation des champs. */
		return true;
	}

	/*
	 * private void validerVoteKey(Vote vote) throws VoteInvalideException { if
	 * (vote.getVoteKey() == null) { throw new
	 * VoteInvalideException(ErreurVote.VOTEKEY_OBLIGATOIRE); } }
	 */

	private void validerChoix(Vote vote) throws VoteInvalideException {
		if (vote.getChoix() == null) {
			throw new VoteInvalideException(ErreurVote.CHOIX_OBLIGATOIRE);
		}
	}
}
