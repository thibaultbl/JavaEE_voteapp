package fr.sgr.formation.voteapp.cli;

public class ReponseAddition {
	private double operande1;
	private double operande2;
	private double resultat;

	public ReponseAddition() {
		super();
	}

	public ReponseAddition(double operande1, double operande2, double resultat) {
		super();
		this.operande1 = operande1;
		this.operande2 = operande2;
		this.resultat = resultat;
	}

	public double getOperande1() {
		return operande1;
	}

	public void setOperande1(double operande1) {
		this.operande1 = operande1;
	}

	public double getOperande2() {
		return operande2;
	}

	public void setOperande2(double operande2) {
		this.operande2 = operande2;
	}

	public double getResultat() {
		return resultat;
	}

	public void setResultat(double resultat) {
		this.resultat = resultat;
	}

	@Override
	public String toString() {
		return "ReponseAddition [operande1=" + operande1 + ", operande2=" + operande2 + ", resultat=" + resultat + "]";
	}
}
