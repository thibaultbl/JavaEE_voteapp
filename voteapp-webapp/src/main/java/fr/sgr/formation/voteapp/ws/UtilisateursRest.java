package fr.sgr.formation.voteapp.ws;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("utilisateurs/{login}")
@Slf4j
public class UtilisateursRest {
	@RequestMapping(method = RequestMethod.PUT)
	public void creer(@PathVariable String login, @RequestBody Utilisateur utilisateur) {
		log.info("=====> Création ou modification de l'utilisateur de login {}: {}.", login, utilisateur);

	}

	@RequestMapping(method = RequestMethod.DELETE)
	public void supprimer(@PathVariable String login) {
		log.info("=====> Suppression de l'utilisateur de login {}.", login);

	}

	@RequestMapping(method = RequestMethod.GET)
	public Utilisateur lire(@PathVariable String login) {
		log.info("=====> Récupération de l'utilisateur de login {}.", login);

		// @formatter:off
		return Utilisateur.builder()
				.login(login)
				.nom("Martin")
				.prenom("Jean")
				.build();
		// @formatter:on
	}
}
