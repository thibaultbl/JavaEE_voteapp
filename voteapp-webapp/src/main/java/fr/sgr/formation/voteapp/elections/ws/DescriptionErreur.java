package fr.sgr.formation.voteapp.elections.ws;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DescriptionErreur {
	private String codeErreur;
	private String description;
}
