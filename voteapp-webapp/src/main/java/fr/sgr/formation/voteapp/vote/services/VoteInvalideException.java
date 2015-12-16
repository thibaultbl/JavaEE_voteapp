package fr.sgr.formation.voteapp.vote.services;

import lombok.Builder;
import lombok.Getter;

public class VoteInvalideException extends Exception {
	@Getter
	private ErreurVote erreur;

	@Builder
	public VoteInvalideException(ErreurVote erreur, Throwable cause) {
		super(cause);

		this.erreur = erreur;
	}

	public VoteInvalideException(ErreurVote erreur) {
		this.erreur = erreur;
	}

	public enum ErreurVote {
		VOTEKEY_OBLIGATOIRE("L'utilisateur et l'élection sont obligatoires."),
		CHOIX_OBLIGATOIRE("Le choix est obligatoire."),
		VOTE_OBLIGATOIRE("Le vote est obligatoire pour effectuer l'opération."),
		VOTE_EXISTANT("Ce vote existe déjà."),
		VOTEKEY_NON_EXISTANT("Ce couple utilisateur/élection n'existe pas");

		@Getter
		public String message;

		private ErreurVote(String message) {
			this.message = message;
		}
	}
}