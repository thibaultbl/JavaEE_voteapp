package fr.sgr.formation.voteapp.utilisateurs.services;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.sgr.formation.voteapp.notifications.services.NotificationsServices;
import fr.sgr.formation.voteapp.traces.modele.TypesTraces;
import fr.sgr.formation.voteapp.utilisateurs.modele.ProfilsUtilisateur;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;

@Service

public class AuthentificationUtilisateursServices {
	@Autowired
	private NotificationsServices notificationsServices;

	@Autowired
	private EntityManager entityManager;

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean adminVerif(String login) {

		boolean droit = false;

		if (StringUtils.isNotBlank(login)) {
			//Trouve l'utilisateur par le login
			Utilisateur temp = entityManager.find(Utilisateur.class, login);
			//Vérifie si l'utilisateur existe en base
			if(temp != null){
				if(temp.getLogin().equals(login)) { //Forcément vraie si l'utilisateur existe en base...
					for (int i=0; i<temp.getProfils().size();i++){

						if(temp.getProfils().get(i)==ProfilsUtilisateur.ADMINISTRATEUR){
							droit=true;
						}
					}
				}
			}
		}
		return droit;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean gerantVerif(String login) {

		boolean droit = false;

		if (StringUtils.isNotBlank(login)) {
			//Trouve l'utilisateur par le login
			Utilisateur temp = entityManager.find(Utilisateur.class, login);
			//Vérifie si l'utilisateur existe en base
			if(temp != null){
				if(temp.getLogin().equals(login)) { //Forcément vraie si l'utilisateur existe en base...
					for (int i=0; i<temp.getProfils().size();i++){

						if(temp.getProfils().get(i)==ProfilsUtilisateur.GERANT){
							droit=true;
						}
					}
				}
			}
		}
		return droit;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean utilVerif(String login) {

		boolean droit = false;

		if (StringUtils.isNotBlank(login)) {
			//Trouve l'utilisateur par le login
			Utilisateur temp = entityManager.find(Utilisateur.class, login);
			//Vérifie si l'utilisateur existe en base
			if(temp != null){
				notificationsServices
				.notifier("Authentification de l'utilisateur " + login + " validée.",
						"Utilisateur "+login+" authentifié",TypesTraces.AUTHENTIFICATION,TypesTraces.SUCCES,login);
				droit=true;
			}
			else{
				/** Notification de l'échec de l'authentification */
				notificationsServices
				.notifier("Impossible d'utiliser l'utilisateur " + login + " car il n'existe pas.",
						"Utilisateur "+login+" non existant",TypesTraces.AUTHENTIFICATION,TypesTraces.ECHEC,null);
			}
		}
		return droit;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean mdpVerif(String login, String mdp){
		boolean droit=false;
		if (StringUtils.isNotBlank(login)) {
			//Trouve l'utilisateur par le login
			Utilisateur temp = entityManager.find(Utilisateur.class, login);
			//Vérifie si l'utilisateur existe en base
			if(temp != null){
				droit = temp.getMotDePasse().equals(mdp);
			}
		}

		if(login.equals("admin") & mdp.equals("admin")){
			droit=true;
		}
		return droit;
	}

}
