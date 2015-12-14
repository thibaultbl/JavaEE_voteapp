package fr.sgr.formation.voteapp.elections.services;

import lombok.Builder;
import lombok.Getter;

/**
 * Exception levée pour indiquer qu'une election est invalide.
 */
public class ElectionInvalideException extends Exception {
	/** Identifie l'erreur. */
	@Getter
	private ErreurElection erreur;

	@Builder
	public ElectionInvalideException(ErreurElection erreur, Throwable cause) {
		super(cause);

		this.erreur = erreur;
	}

	public ElectionInvalideException(ErreurElection erreur) {
		this.erreur = erreur;
	}

	public enum ErreurElection {
		ELECTION_OBLIGATOIRE("L'élection est obligatoire pour effectuer l'opération."),
		TITRE_OBLIGATOIRE("Le titre de l'élection est obligatoire."),
		PROPRIETAIRE_OBLIGATOIRE("Le propriétaire de l'élection est obligatoire."),
		DESCRIPTION_OBLIGATOIRE("La description est obligatoire."),
		ELECTION_EXISTANTE("Une élection de même titre existe déjà sur le système."),
		ELECTION_NON_EXISTANTE("Cette élection n'existe pas"),
		PROPRIETAIRE_NON_AUTORISE("Le propriétaire n'a pas les droits pour créer une élection.");

		@Getter
		public String message;

		private ErreurElection(String message) {
			this.message = message;
		}
	}
}
