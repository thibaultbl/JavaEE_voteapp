package fr.sgr.formation.voteapp.utilisateurs.services;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.sgr.formation.voteapp.notifications.services.NotificationsServices;
import fr.sgr.formation.voteapp.traces.modele.TypesTraces;
import fr.sgr.formation.voteapp.utilisateurs.modele.ProfilsUtilisateur;
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
	public Utilisateur creer(Utilisateur utilisateur, String idUser) throws UtilisateurInvalideException {

		if (utilisateur == null) {
			/** Notification de l'événement de création */
			notificationsServices.notifier("Création d'utilisateur null",
					"Création d'utilisateur null",TypesTraces.CREATION,TypesTraces.ECHEC,utilisateur.getLogin());
			throw new UtilisateurInvalideException(ErreurUtilisateur.UTILISATEUR_OBLIGATOIRE);
		}

		/** Validation de l'existance de l'utilisateur. */
		if (rechercherParLogin(utilisateur.getLogin(),"admin","admin") != null) {
			/** Notification de l'événement de création */
			notificationsServices.notifier("L'utilisateur "+utilisateur.getLogin()+ " existe déjà.",
					"L'utilisateur "+utilisateur.getLogin()+ " existe déjà.",TypesTraces.CREATION,TypesTraces.ECHEC,utilisateur.getLogin());
			throw new UtilisateurInvalideException(ErreurUtilisateur.UTILISATEUR_EXISTANT);
		}

		/**Gestion des logins interdits**/
		if (utilisateur.getLogin().equals("ADMINISTRATEUR") || utilisateur.getLogin().equals("UTILISATEUR") || utilisateur.getLogin().equals("GERANT") || utilisateur.getLogin().equals("all")) {
			/** Notification de l'événement de création */
			notificationsServices.notifier("Impossible de créer un utilisateur de login " +utilisateur.getLogin() ,
					"login"+utilisateur.getLogin()+" impossible",TypesTraces.CREATION,TypesTraces.ECHEC,utilisateur.getLogin());
			throw new UtilisateurInvalideException(ErreurUtilisateur.UTILISATEUR_IMPOSSIBLE);
		}
		
		boolean admin = AuthentificationServices.adminVerif(idUser);
		if(!admin){
				/** Notification de l'événement de création */
				notificationsServices.notifier("Impossible de créer un utilisateur si vous n'etes pas administrateur" ,
						"Création d'utilisateur impossible si pas admin",TypesTraces.CREATION,TypesTraces.ECHEC,utilisateur.getLogin());
				throw new UtilisateurInvalideException(ErreurUtilisateur.PAS_DROIT);
		}

		/**
		 * Validation de l'utilisateur: lève une exception si l'utilisateur est
		 * invalide.
		 */

		validationServices.validerUtilisateur(utilisateur);

		/** Notification de l'événement de création */
		notificationsServices.notifier("Création de l'utilisateur: " + utilisateur.getLogin(),
				"Création de l'utilisateur "+ utilisateur.getLogin(),TypesTraces.CREATION,TypesTraces.SUCCES,utilisateur.getLogin());

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
	 * @throws UtilisateurInvalideException 
	 */
	public Utilisateur rechercherParLogin(String login, String idUser, String mdp) throws UtilisateurInvalideException {

		if (StringUtils.isNotBlank(login)) {
			boolean admin = AuthentificationServices.adminVerif(idUser);
			boolean auth = login.equals(idUser);

			if(auth || admin){
				boolean mdpOK = AuthentificationServices.mdpVerif(idUser,mdp);
				if(mdpOK){
					/** Notification de l'événement de recherche */
					notificationsServices.notifier("Affichage des infos de : " + login,
							"Affichage des infos de : " + login,TypesTraces.RECHERCHE,TypesTraces.SUCCES,login);
					return entityManager.find(Utilisateur.class, login);
				}
				else{
					notificationsServices.notifier("Affichage des infos de " + login+ " impossible : mauvais mot de passe." ,
							"Affichage " + login+" impossible / mauvais mdp",TypesTraces.RECHERCHE,TypesTraces.ECHEC,login);
					throw new UtilisateurInvalideException(ErreurUtilisateur.MAUVAIS_MDP);
				}
			}
			else{
				notificationsServices.notifier("Affichage des infos de " + login+ " impossible : pas lui" ,
						"Affichage " + login+" impossible / pas lui ou pas admin",TypesTraces.RECHERCHE,TypesTraces.ECHEC,login);
				return null;
			}

		}else{
			notificationsServices.notifier("Affichage d'un utilisateur null impossible" ,
					"Affichage d'un utilisateur null impossible" ,TypesTraces.RECHERCHE,TypesTraces.ECHEC,login);
			return null;
		}
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
					notificationsServices.notifier("Suppression de l'utilisateur: " + temp.getLogin(),
							"Suppression de l'utilisateur "+ temp.getLogin(),TypesTraces.SUPRESSION,TypesTraces.SUCCES,idUser);
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

	/*public Utilisateur rechercherUserNom(String nom) {

		if (StringUtils.isNotBlank(nom)) {
			return entityManager.find(Utilisateur.class, nom);
		}
		return null;
	}

	public Utilisateur rechercherUserPrenom(String prenom) {

		if (StringUtils.isNotBlank(prenom)) {
			return entityManager.find(Utilisateur.class, prenom);
		}
		return null;
	}

	public Utilisateur rechercherUserVille(Ville ville) {

		if (StringUtils.isNotBlank(ville.getNom())) {
			return entityManager.find(Utilisateur.class, ville.getNom());
		}
		return null;
	}*/

	public String rechercherUserProfil(String login, String idUser) {

		boolean droit = AuthentificationServices.adminVerif(idUser);
		if (StringUtils.isNotBlank(login)) {

			if(droit){
				if(login.equals("all")){
					String queryString = "SELECT * FROM UTILISATEUR";
					Query query = entityManager.createNativeQuery(queryString, Utilisateur.class);


					String result="";
					for (int i=0;i<query.getResultList().size();i++){
						result += query.getResultList().get(i).toString();
						result+="</br></br>";
					}

					notificationsServices.notifier("Affichage de tous les utilisateurs",
							"Affichage de tous les utilisateurs",TypesTraces.RECHERCHE,TypesTraces.SUCCES,idUser);
					return result;
				}
				else{
					String queryString = "SELECT * FROM UTILISATEUR AS u, PROFILS_UTILISATEURS AS up WHERE up.PROFILS = ? AND u.LOGIN = up.LOGIN_UTILISATEUR";
					Query query = entityManager.createNativeQuery(queryString, Utilisateur.class);
					query.setParameter(1, login);


					String result="";
					for (int i=0;i<query.getResultList().size();i++){
						result += query.getResultList().get(i).toString();
						result+="</br></br>";
					}

					notificationsServices.notifier("Affichage des utilisateurs "+login,
							"Affichage des utilisateurs "+login,TypesTraces.RECHERCHE,TypesTraces.SUCCES,idUser);
					return result;
				}
			}
			else{
				notificationsServices.notifier("Impossible d'afficher les utilisateurs " + login + " car vous n'avez pas les droits.",
						"Affichage "+ login +" impossible / pas admin",TypesTraces.RECHERCHE,TypesTraces.ECHEC,idUser);
				return null;
			}
		}
		else{
			notificationsServices.notifier("Impossible d'afficher des utilisateurs vides",
					"Affichage utlisateur vide impossible",TypesTraces.RECHERCHE,TypesTraces.ECHEC,idUser);
			return null;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)	
	public void changePassword(String login, String idUser, String new_pswd){
		if(!login.equals("admin")){
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
		else{
			notificationsServices.notifier("Impossible de modifier le mot de passe de l'administrateur",
					"Modification mot de passe admin impossible",TypesTraces.MODIFICATION,TypesTraces.ECHEC,idUser);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)	
	public void changeInfos(String login,String idUser,Utilisateur user){
		user.setLogin(login);
		if(!login.equals("admin")){
			boolean auth = login.equals(idUser);
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
					if(admin){
						if(user.getProfils()!=null){
							temp.setProfils(user.getProfils());
						}
					}
				}
			}
			else{
				notificationsServices.notifier("Impossible de modifier un utilisateur qui n'est pas vous si vous n'êtes pas administrateur.",
						"Modification "+login+" impossible / pas admin ou pas lui",TypesTraces.MODIFICATION,TypesTraces.ECHEC,idUser);
			}
		}
		else{
			notificationsServices.notifier("Impossible de modifier les infos de l'administrateur",
					"Modification infos admin impossible",TypesTraces.MODIFICATION,TypesTraces.ECHEC,idUser);
		}
	}
}
