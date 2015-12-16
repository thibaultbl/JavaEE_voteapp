package fr.sgr.formation.voteapp.vote.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.sgr.formation.voteapp.vote.services.VoteServices;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("votes/{voteKey}")
@Slf4j
public class VoteRest {
	@Autowired
	private VoteServices votesServices;

	/*
	 * @RequestMapping(method = RequestMethod.PUT) public void
	 * creer(@PathVariable String titre, @RequestBody Election
	 * election, @RequestParam String idUser) throws VoteInvalideException {
	 * log.info("=====> Création du vote d'idVote {}.", idVote);
	 * vote.setTitre(titre); votesServices.creer(election, idUser); }
	 * 
	 * @RequestMapping(method = RequestMethod.DELETE) public void
	 * supprimer(@PathVariable String titre, @RequestParam String idUser) throws
	 * ElectionInvalideException {
	 * 
	 * log.info("=====> Suppression du vote d'idVote {}.", idVote);
	 * votesServices.supprimer(titre, idUser);
	 * 
	 * }
	 * 
	 * @RequestMapping(method = RequestMethod.GET) public Election
	 * lire(@PathVariable String titre) { log.info(
	 * "=====> Récupération du vote d'idVote {}.", idVote);
	 * 
	 * return votesServices.rechercherParIdVote(idVote); }
	 * 
	 * @ExceptionHandler({ VoteInvalideException.class })
	 * 
	 * @ResponseStatus(value = HttpStatus.BAD_REQUEST) public DescriptionErreur
	 * gestionErreur(ElectionInvalideException exception) { return new
	 * DescriptionErreur(exception.getErreur().name(),
	 * exception.getErreur().getMessage()); }
	 */
}
