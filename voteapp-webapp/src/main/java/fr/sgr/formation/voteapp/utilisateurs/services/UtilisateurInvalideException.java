package fr.sgr.formation.voteapp.utilisateurs.services;

import lombok.Builder;
import lombok.Getter;

/**
 * Exception levée pour indiquer qu'un utilisateur est invalide.
 */
public class UtilisateurInvalideException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3959709365968239401L;
	/** Identifie l'erreur. */
	@Getter
	private ErreurUtilisateur erreur;

	@Builder
	public UtilisateurInvalideException(ErreurUtilisateur erreur, Throwable cause) {
		super(cause);

		this.erreur = erreur;
	}

	public UtilisateurInvalideException(ErreurUtilisateur erreur) {
		this.erreur = erreur;
	}

	public enum ErreurUtilisateur {
		UTILISATEUR_OBLIGATOIRE("L'utilisateur est obligatoire pour effectuer l'opération."),
		NOM_OBLIGATOIRE("Le nom de l'utilisateur est obligatoire."),
		PRENOM_OBLIGATOIRE("Le prénom de l'utilisateur est obligatoire."),
		LOGIN_OBLIGATOIRE("Le login est obligatoire."),
		MDP_OBLIGATOIRE("Le mot de passe est obligatoire."),
		UTILISATEUR_EXISTANT("Un utilisateur de même login existe déjà sur le système."),
		UTILISATEUR_INEXISTANT("L'utilisateur n'existe pas"),
		UTILISATEUR_IMPOSSIBLE("Un tel nom d'utilisateur n'est pas possible."),
		MAUVAIS_MDP("Vous avez entré un mauvais mot de passe"),
		PAS_DROIT("Vous n'avez pas les droits pour effectuer cette action.");

		@Getter
		public String message;

		private ErreurUtilisateur(String message) {
			this.message = message;
		}
	}
}
