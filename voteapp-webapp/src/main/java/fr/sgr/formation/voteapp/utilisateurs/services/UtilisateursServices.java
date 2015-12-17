package fr.sgr.formation.voteapp.utilisateurs.services;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.sgr.formation.voteapp.notifications.services.NotificationsServices;
import fr.sgr.formation.voteapp.traces.modele.TypesTraces;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException.ErreurUtilisateur;
import lombok.extern.slf4j.Slf4j;

/**
 * Sur la création et modification d'un utilisateur : - Vérification des champs
 * obligatoires - Vérification de la validité (longueur) des champs - Appeler un
 * service de notification inscrivant dans la log création ou modification de
 * l'utilisateur Sur la récupération d'un utilisateur Vérification de
 * l'existance de l'utilisateur Retourner l'utilisateur Sur la suppression d'un
 * utilisateur Vérification de l'existance de l'utilisateur Retourner
 * l'utilisateur Appeler un service de notification inscrivant dans la log la
 * suppression de l'utilisateur
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS)
public class UtilisateursServices {

	/** Services d'authentification d'un utilisateur. */
	@Autowired
	private AuthentificationUtilisateursServices AuthentificationServices;

	/** Services de validation d'un utilisateur. */
	@Autowired
	private ValidationUtilisateurServices validationServices;
	/** Services de notification des événements. */
	@Autowired
	private NotificationsServices notificationsServices;

	@Autowired
	private EntityManager entityManager;

	/**
	 * Crée un nouvel utilisateur sur le système.
	 * 
	 * @param utilisateur
	 *            Utilisateur à créer.
	 * @return Utilisateur créé.
	 * @throws UtilisateurInvalideException
	 *             Levée si l'utilisateur est invalide.
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Utilisateur creer(Utilisateur utilisateur) throws UtilisateurInvalideException {

		log.info("=====> Création de l'utilisateur : {}.", utilisateur);

		if (utilisateur == null) {
			throw new UtilisateurInvalideException(ErreurUtilisateur.UTILISATEUR_OBLIGATOIRE);
		}

		/** Validation de l'existance de l'utilisateur. */
		if (rechercherParLogin(utilisateur.getLogin()) != null) {
			throw new UtilisateurInvalideException(ErreurUtilisateur.UTILISATEUR_EXISTANT);
		}


		/**
		 * Validation de l'utilisateur: lève une exception si l'utilisateur est
		 * invalide.
		 */
		validationServices.validerUtilisateur(utilisateur);

		/** Notification de l'événement de création */
		notificationsServices.notifier("Création de l'utilisateur: " + utilisateur.getLogin(),
				"Création de l'utilisateur "+ utilisateur.getLogin(),TypesTraces.CREATION,TypesTraces.SUCCES,utilisateur.toString());

		/** Persistance de l'utilisateur. */
		entityManager.persist(utilisateur);

		return utilisateur;
	}

	/**
	 * Retourne l'utilisateur identifié par le login.
	 * 
	 * @param login
	 *            Login identifiant l'utilisateur.
	 * @return Retourne l'utilisateur identifié par le login.
	 */
	public Utilisateur rechercherParLogin(String login) {

		log.info("=====> Recherche de l'utilisateur de login {}.", login);

		if (StringUtils.isNotBlank(login)) {
			return entityManager.find(Utilisateur.class, login);
		}
		return null;
	}

	/**
	 * Supprime de la base l'utilisateur identifié par le login.
	 * 
	 * @param login
	 *            Login identifiant l'utilisateur.
	 */

	@Transactional(propagation = Propagation.REQUIRED)
	public Utilisateur supprimer(String login, String idUser) {

		Utilisateur res = entityManager.find(Utilisateur.class, login);
		if (StringUtils.isNotBlank(login)) {
			boolean droit = AuthentificationServices.adminVerif(idUser);
			if(droit){

				Utilisateur temp = entityManager.find(Utilisateur.class, login);

				// Supprime l'utilisateur de la base si il existe
				if (temp != null) {
					entityManager.remove(temp);
					/** Notification de l'événement de création */
					notificationsServices.notifier("Suppression de l'utilisateur: " + temp.toString(),
							"Suppression de l'utilisateur "+ temp.toString(),TypesTraces.SUPRESSION,TypesTraces.SUCCES,idUser);
				} else {
					/** Notification de l'événement de création */
					notificationsServices.notifier("Impossible de supprimer l'utilisateur " + login + " car il n'existe pas.",
							"Supression de "+ login + "impossible / existe pas",TypesTraces.SUPRESSION,TypesTraces.ECHEC,idUser);
					return null;
				}
			}
			else{
				notificationsServices.notifier("Impossible de supprimer l'utilisateur " + login + " car vous n'avez pas les droits.",
						"Supression "+ login +" impossible / pas admin",TypesTraces.SUPRESSION,TypesTraces.ECHEC,idUser);
				return null;
			}
		}
		return res;
	}

	@Transactional(propagation = Propagation.REQUIRED)	
	public void changePassword(String login, String idUser, String new_pswd){
		boolean droit = AuthentificationServices.utilVerif(idUser);
		if(droit){
			notificationsServices.notifier("Modification du mot de passe de l'utilisateur "+login+".",
					"Modification mot de passe "+login,TypesTraces.MODIFICATION,TypesTraces.SUCCES,idUser);

			if (StringUtils.isNotBlank(login)) {
				Utilisateur temp = entityManager.find(Utilisateur.class, login);
				temp.setMotDePasse(new_pswd);
			}
		}
		else{
			notificationsServices.notifier("Impossible de modifier votre mot de passe car vous n'êtes pas dans la base.",
					"Modification mot de passe impossible / pas dans la base",TypesTraces.MODIFICATION,TypesTraces.ECHEC,idUser);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)	
	public void changeInfos(String login,String idUser,Utilisateur user){
		boolean auth = AuthentificationServices.utilVerif(idUser);
		boolean admin = AuthentificationServices.adminVerif(idUser);

		if(auth || admin){
			log.info("=====> Modification de l'utilisateur de login {}.", login);
			if (StringUtils.isNotBlank(login)) {
				Utilisateur temp = entityManager.find(Utilisateur.class, login);
				if(user.getAdresse()!=null){
					temp.setAdresse(user.getAdresse());
				}
				if(user.getDateDeNaissance()!=null){
					temp.setDateDeNaissance(user.getDateDeNaissance());
				}				
				if(user.getEmail()!=null){
					temp.setEmail(user.getEmail());		
				}		
				if(user.getPrenom()!=null){
					temp.setNom(user.getPrenom());
				}			
				if(user.getPrenom()!=null){
					temp.setPrenom(user.getPrenom());
				}
				if(user.getProfils()!=null){
					temp.setProfils(user.getProfils());
				}
			}
		}
		else{
			notificationsServices.notifier("Impossible de modifier un utilisateur qui n'est pas vous si vous n'êtes pas administrateur.",
					"Modification "+login+" impossible / pas admin ou pas lui",TypesTraces.MODIFICATION,TypesTraces.ECHEC,idUser);
		}
	}
}
