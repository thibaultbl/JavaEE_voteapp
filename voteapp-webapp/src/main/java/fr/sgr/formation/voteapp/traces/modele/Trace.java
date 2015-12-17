package fr.sgr.formation.voteapp.traces.modele;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(of = { "key" })
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Trace {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int key;
	
	@OneToOne
	private Utilisateur user;
	
	@Enumerated(EnumType.STRING)
	private TypesTraces typeAction;
	
	@Temporal(TemporalType.DATE)
	private Date dateDeb;
	
	@Temporal(TemporalType.DATE)
	private Date dateFin;
	
	@Enumerated(EnumType.STRING)
	private TypesTraces resAction;
	
	private String descAction;
	

}
