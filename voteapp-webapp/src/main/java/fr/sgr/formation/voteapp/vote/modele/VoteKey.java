package fr.sgr.formation.voteapp.vote.modele;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VoteKey {
	@Column(name = "Utilisateur", nullable = false)
	private int utilisateur;

	@Column(name = "Election", nullable = false)
	private int election;
}
