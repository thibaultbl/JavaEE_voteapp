package fr.sgr.formation.voteapp.utilisateurs.services;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.sgr.formation.voteapp.notifications.services.NotificationsServices;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AuthentificationUtilisateurService {
	@Autowired
	private NotificationsServices notificationsServices;

	@Autowired
	private EntityManager entityManager;
	
	
	public boolean authVerif(String login) {
		
		boolean droit = false;

		if (StringUtils.isNotBlank(login)) {
	        //Trouve l'utilisateur par le login
	        Utilisateur temp = entityManager.find(Utilisateur.class, login);
	         
	        //Vérifie si l'utilisateur existe en base
	        if(temp != null){
	        	if(temp.getLogin() == login) { //Forcément vraie si l'utilisateur existe en base...
	        		/** Notification authentification */
		    		notificationsServices.notifier("Aut " + temp.toString());
		    		droit = true;
	        	} else {
	        		notificationsServices.notifier("Le login spécifié n'est pas valide.");
	        	}
	        }
	        else{
	        	/** Notification de l'échec de l'authentification */
	    		notificationsServices.notifier("Impossible de vous connecter avec le login "+login+" car il n'existe pas.");
	        }
		}
		return droit;
	}

}
