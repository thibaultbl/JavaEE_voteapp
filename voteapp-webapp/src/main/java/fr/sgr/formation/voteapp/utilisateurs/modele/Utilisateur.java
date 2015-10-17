package fr.sgr.formation.voteapp.utilisateurs.modele;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = { "login" })
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {
	private String login;
	private String nom;
	private String prenom;
	private String motDePasse;
	private Date dateDeNaissance;
	private String email;
}
