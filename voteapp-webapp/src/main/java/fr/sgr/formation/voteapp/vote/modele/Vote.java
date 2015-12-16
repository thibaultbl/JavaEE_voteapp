package fr.sgr.formation.voteapp.vote.modele;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import fr.sgr.formation.voteapp.elections.modele.Election;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = { "voteKey" })
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Vote implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long key;

	@OneToOne
	private Utilisateur utilisateur;
	@OneToOne
	private Election election;
	private ChoixVote choix;
}
