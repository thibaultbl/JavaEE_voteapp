package fr.sgr.formation.voteapp.calculatrice.ws;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.sgr.formation.voteapp.calculatrice.modele.ResultatOperation;

@RestController
@RequestMapping("/calculatrice")
public class CalculatriceRest {

	@RequestMapping("/add")
	// @RequestMapping(value = "/add", produces = {
	// MediaType.APPLICATION_JSON_VALUE})
	public ResultatOperation additionner(@RequestParam double operande1, @RequestParam double operande2) {
		return add(operande1, operande2);
	}

	/**
	 * Exécution de l'addition et production d'une réponse HTML.
	 * 
	 * @param operande1
	 * @param operande2
	 * @return réponse HTML
	 */
	@RequestMapping(value = "/add", produces = { MediaType.TEXT_HTML_VALUE })
	public String additionnerVersHtml(@RequestParam double operande1, @RequestParam double operande2) {
		return add(operande1, operande2).toHtml();
	}

	private ResultatOperation add(double operande1, double operande2) {
		// @formatter:off
		return ResultatOperation.builder()
				.typeOperation(" + ")
				.operande1(operande1)
				.operande2(operande2)
				.resultat(operande1 + operande2)
				.build();
		// @formatter:on
	}
}
