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

	@PostConstruct
	@Transactional(propagation = Propagation.REQUIRED)
	public void init() {
		log.info("Initialisation des villes par défaut dans la base...");

		Ville rennes = new Ville();
		rennes.setCodePostal("35000");
		rennes.setNom("Rennes");

		// Initialisation de l'utilisateur jean
		log.info("Initialisation des utilisateurs par défaut dans la base...");
		Utilisateur jean = new Utilisateur();
		Adresse chezJean = new Adresse();
		chezJean.setRue("Contour Antoine de St Exupéry");
		chezJean.setVille(rennes);

		jean.setAdresse(chezJean);
		jean.setEmail("jean@Queyrie.com");
		jean.setLogin("jean");
		jean.setMotDePasse("talb");
		jean.setNom("Queyrie");
		jean.setPrenom("Jean");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date annivJean;
		try {
			annivJean = sdf.parse("28/12/1992");
			jean.setDateDeNaissance(annivJean);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<ProfilsUtilisateur> profilsJean = new ArrayList<ProfilsUtilisateur>();

		profilsJean.add(ProfilsUtilisateur.ADMINISTRATEUR);
		profilsJean.add(ProfilsUtilisateur.UTILISATEUR);
		jean.setProfils(profilsJean);

		villeService.creer(rennes);
		try {
			us.creer(jean);
		} catch (UtilisateurInvalideException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Initialisation d'une election
		log.info("Creation d'une élection");
		Election elec = new Election(jean, "super election", "ceci est une élection test", true);
		try {
			electionServices.creer(elec);
			electionServices.cloturer(elec);
			electionServices.creer(elec);
			System.out.println(elec.isActiveElection());

		} catch (ElectionInvalideException e) {
			/** Then: Alors une exception est levée. */
			e.printStackTrace();
		}

	}
}
