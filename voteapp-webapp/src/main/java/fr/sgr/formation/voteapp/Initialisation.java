package fr.sgr.formation.voteapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.sgr.formation.voteapp.elections.modele.Election;
import fr.sgr.formation.voteapp.elections.services.ElectionInvalideException;
import fr.sgr.formation.voteapp.elections.services.ElectionsServices;
import fr.sgr.formation.voteapp.utilisateurs.modele.Adresse;
import fr.sgr.formation.voteapp.utilisateurs.modele.ProfilsUtilisateur;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import fr.sgr.formation.voteapp.utilisateurs.modele.Ville;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateursServices;
import fr.sgr.formation.voteapp.utilisateurs.services.VilleService;
import fr.sgr.formation.voteapp.vote.modele.ChoixVote;
import fr.sgr.formation.voteapp.vote.modele.Vote;
import fr.sgr.formation.voteapp.vote.services.VoteInvalideException;
import fr.sgr.formation.voteapp.vote.services.VoteServices;
import lombok.extern.slf4j.Slf4j;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class Initialisation {

	@Autowired
	private VilleService villeService;
	@Autowired
	private UtilisateursServices us;
	@Autowired
	private ElectionsServices electionServices;

	@Autowired
	private VoteServices voteServices;

	@PostConstruct
	@Transactional(propagation = Propagation.REQUIRED)
	public void init() throws VoteInvalideException {
		log.info("Initialisation des villes par défaut dans la base...");

		Ville rennes = new Ville();
		rennes.setCodePostal("35000");
		rennes.setNom("Rennes");

		// Initialisation de l'utilisateur jean
		log.info("Initialisation des utilisateurs par défaut dans la base...");
		Utilisateur admin = new Utilisateur();
		Adresse adminAdress = new Adresse();
		adminAdress.setRue("dummy adress");
		adminAdress.setVille(rennes);

		admin.setAdresse(adminAdress);
		admin.setEmail("admin@votteapp.com");
		admin.setLogin("admin");
		admin.setMotDePasse("admin");
		admin.setNom("admin");
		admin.setPrenom("admin");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date annivJean;
		try {
			annivJean = sdf.parse("01/01/20016");
			admin.setDateDeNaissance(annivJean);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<ProfilsUtilisateur> profilsJean = new ArrayList<ProfilsUtilisateur>();

		profilsJean.add(ProfilsUtilisateur.ADMINISTRATEUR);
		profilsJean.add(ProfilsUtilisateur.GERANT);
		admin.setProfils(profilsJean);

		villeService.creer(rennes);
		try {
			us.creer(admin,"admin");
		} catch (UtilisateurInvalideException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Vote> votes = new ArrayList<Vote>();
		Election elec = new Election("ElectionTest", "ceci est une élection test", true, admin);

		// Initialisation d'une election
		log.info("Creation d'une élection");
		try {
			electionServices.creer(elec, "admin");
		} catch (ElectionInvalideException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Vote vote1 = new Vote();
		vote1.setChoix(ChoixVote.OUI);
		vote1.setElection(elec);
		vote1.setUtilisateur(admin);
		votes.add(vote1);


		try {
			voteServices.creer(elec.getTitre(), "oui", admin.getLogin());
		} catch (UtilisateurInvalideException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		/*
		 * try { electionServices.creer(elec, "jean");
		 * electionServices.cloturer(elec.getTitre(), "jean");
		 * electionServices.creer(elec, "jean");
		 * System.out.println(elec.isActiveElection());
		 * 
		 * } catch (ElectionInvalideException e) {
		 *//** Then: Alors une exception est levée. *//*
		 * e.printStackTrace();
		 * }
		 */

	}
}
