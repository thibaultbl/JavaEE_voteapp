package fr.sgr.formation.voteapp.vote.ws;

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

import fr.sgr.formation.voteapp.elections.services.ElectionInvalideException;
import fr.sgr.formation.voteapp.elections.services.ElectionsServices;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateursServices;
import fr.sgr.formation.voteapp.vote.modele.Vote;
import fr.sgr.formation.voteapp.vote.services.VoteInvalideException;
import fr.sgr.formation.voteapp.vote.services.VoteServices;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("votes/{titre}")
@Slf4j
public class VoteRest {
	@Autowired
	private VoteServices votesServices;

	@Autowired
	private ElectionsServices electionsServices;

	@Autowired
	private UtilisateursServices utilisateursServices;

	@RequestMapping(method = RequestMethod.PUT)
	public void creer(@PathVariable String titre, @RequestBody String choix, @RequestParam String idUser)
			throws VoteInvalideException {

		log.info("=====> Création du vote pour l'élection {}.", titre);
		votesServices.creer(titre, choix, idUser);
	}

	/*
	 * @RequestMapping(method = RequestMethod.DELETE) public void
	 * supprimer(@PathVariable String titre, @RequestParam String idUser) throws
	 * ElectionInvalideException { log.info(
	 * "=====> Tentative de Suppression du vote d'idVote {}.", idVote); //
	 * votesServices.supprimer(titre, idUser);
	 * 
	 * }
	 */

	@RequestMapping(method = RequestMethod.GET)
	public Vote lire(@PathVariable long key) {
		log.info(
				"=====> Récupération du vote d'idVote {}.", key);

		return votesServices.rechercherParVoteKey(key);
	}

	@ExceptionHandler({ VoteInvalideException.class })

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public DescriptionErreur gestionErreur(ElectionInvalideException exception) {
		return new DescriptionErreur(exception.getErreur().name(),
				exception.getErreur().getMessage());
	}

}
