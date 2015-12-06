package fr.sgr.formation.voteapp.utilisateurs.modele;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fr.sgr.formation.voteapp.elections.modele.Election;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@EqualsAndHashCode(of = { "login" })
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Utilisateur {
	@Id
	private String login;
	private String nom;
	private String prenom;
	private String motDePasse;
	@Temporal(TemporalType.DATE)
	private Date dateDeNaissance;
	private String email;

	@OneToMany
	private Collection<Election> elections;

	@ElementCollection(targetClass = ProfilsUtilisateur.class)
	@CollectionTable(name = "profilsUtilisateurs", joinColumns = @JoinColumn(name = "loginUtilisateur") )
	@Enumerated(EnumType.STRING)
	@Singular
	private List<ProfilsUtilisateur> profils;
	// test
	@Embedded
	private Adresse adresse;

}
