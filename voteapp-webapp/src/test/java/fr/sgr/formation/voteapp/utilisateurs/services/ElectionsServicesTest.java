package fr.sgr.formation.voteapp.utilisateurs.services;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.sgr.formation.voteapp.elections.modele.Election;
import fr.sgr.formation.voteapp.elections.services.ElectionInvalideException;
import fr.sgr.formation.voteapp.elections.services.ElectionsServices;
import fr.sgr.formation.voteapp.elections.services.ValidationElectionServices;
import fr.sgr.formation.voteapp.notifications.services.NotificationsServices;
import fr.sgr.formation.voteapp.utilisateurs.modele.Proprietaire;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;

@RunWith(MockitoJUnitRunner.class)
public class ElectionsServicesTest {

	@InjectMocks
	private ElectionsServices services = new ElectionsServices();

	@Mock
	private Utilisateur user;
	@Mock
	private Proprietaire proprio;
	@InjectMocks
	private Election election2 = new Election(user, "super election", "ceci est une élection test");
	@InjectMocks
	private Election election3 = new Election(proprio, "super election", "ceci est une élection test");
	@Mock
	private EntityManager entityManager;
	@Mock
	private ValidationElectionServices validElectionServices;
	@Mock
	private NotificationsServices notificationsServices;

	@Test
	public void testCreer() {
		try {
			/** When: Lorsqu'on appel le service de création */
			services.creer(election2);
		} catch (ElectionInvalideException e) {
			/** Then: Alors une exception est levée. */
			Assert.assertEquals(ElectionInvalideException.ErreurElection.PROPRIETAIRE_NON_AUTORISE, e.getErreur());
		}
	}

	@Test
	public void testRechercherParTitre() {
		try {
			/** When: Lorsqu'on appel le service de création */
			services.creer(election3);
		} catch (ElectionInvalideException e) {
			/** Then: Alors une exception est levée. */
			Assert.assertEquals("ceci est une élection test",
					services.rechercherParTitre("super election").getDescription());
		}
	}

}
