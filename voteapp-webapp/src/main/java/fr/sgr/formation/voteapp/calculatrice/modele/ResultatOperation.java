package fr.sgr.formation.voteapp.calculatrice.modele;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultatOperation {
	private double operande1;
	private double operande2;
	private double resultat;
	private String typeOperation;

	public String toHtml() {
		StringBuilder stringBuilder = new StringBuilder();

		/** Construction de la réponse au format demandé... */
		stringBuilder.append("<html>");
		stringBuilder.append("<body>");
		stringBuilder.append("<h1>Résultat de l'addition</H1>");
		stringBuilder.append(operande1 + typeOperation + operande2 + " = " + resultat);
		stringBuilder.append("</body>");
		stringBuilder.append("</html>");

		return stringBuilder.toString();
	}
}
