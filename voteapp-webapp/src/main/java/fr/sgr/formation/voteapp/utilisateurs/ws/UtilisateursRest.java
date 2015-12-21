package fr.sgr.formation.voteapp.utilisateurs.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateursServices;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("utilisateurs/{login}")
@Slf4j
public class UtilisateursRest {
	@Autowired
	private UtilisateursServices utilisateursServices;

	@RequestMapping(method = RequestMethod.PUT)
	public void creer(@PathVariable String login, @RequestBody Utilisateur utilisateur)
			throws UtilisateurInvalideException {
		log.info("=====> Création ou modification de l'utilisateur de login {}: {}.", login, utilisateur);
		utilisateur.setLogin(login);
		utilisateursServices.creer(utilisateur);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public void supprimer(@PathVariable String login, @RequestParam String idUser) {
		log.info("=====> Suppression de l'utilisateur de login {}.", login);
		utilisateursServices.supprimer(login, idUser);
	}

	@RequestMapping(method = RequestMethod.GET)
	public Utilisateur lire(@PathVariable String login, @RequestParam String idUser,@RequestParam String mdp) throws UtilisateurInvalideException {

		return utilisateursServices.rechercherParLogin(login,idUser,mdp);
	}
	
	@RequestMapping(value="/list/",method = RequestMethod.GET)
	public String lireUserProfil(@PathVariable String login, @RequestParam String idUser) {

		return utilisateursServices.rechercherUserProfil(login,idUser);
	}
	
	@RequestMapping(value="/mdp",method = RequestMethod.PUT)
	public void changePassword(@PathVariable String login, @RequestParam String idUser,@RequestBody String new_pswd) throws UtilisateurInvalideException {

		utilisateursServices.changePassword(login,idUser,new_pswd);
	}
	
	@RequestMapping(value="/modif",method = RequestMethod.PUT)
	public void changeInfos(@PathVariable String login, @RequestParam String idUser,@RequestBody Utilisateur user) throws UtilisateurInvalideException {

		utilisateursServices.changeInfos(login,idUser,user);
	}
	
	@ExceptionHandler({ UtilisateurInvalideException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public DescriptionErreur gestionErreur(UtilisateurInvalideException exception) {
		return new DescriptionErreur(exception.getErreur().name(), exception.getErreur().getMessage());
	}
}
