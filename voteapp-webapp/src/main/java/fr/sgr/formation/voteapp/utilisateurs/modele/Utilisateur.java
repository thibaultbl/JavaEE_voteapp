package fr.sgr.formation.voteapp.utilisateurs.modele;

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
}
