package fr.sgr.formation.voteapp.utilisateurs.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.sgr.formation.voteapp.utilisateurs.modele.Ville;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException;
import fr.sgr.formation.voteapp.utilisateurs.services.VillesServices;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("villes/{nom_ville}")
@Slf4j
public class VillesRest {
	@Autowired
	private VillesServices villesServices;

	@RequestMapping(method = RequestMethod.PUT)
	public void creer(@PathVariable String nom_ville, @RequestBody Ville ville, String idUser)
			throws UtilisateurInvalideException {
		log.info("=====> Création de la ville {} : {}.", ville.getNom(), ville);
		ville.setNom(nom_ville);
		villesServices.creer(ville,idUser);
	}

}
