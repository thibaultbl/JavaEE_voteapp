package fr.sgr.formation.voteapp.elections.modele;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = { "titre" })
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Election {
	@ManyToOne
	private Utilisateur proprietaire;
	@Id
	private String titre;
	private String description;

}
