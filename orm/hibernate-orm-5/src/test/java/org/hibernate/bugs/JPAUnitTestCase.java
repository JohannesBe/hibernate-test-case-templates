package org.hibernate.bugs;

import org.hibernate.SessionFactory;
import org.hibernate.entities.Country;
import org.hibernate.entities.Customer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.JoinColumn;
import javax.persistence.Persistence;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;
	private SessionFactory sessionFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );

		// Enable statistics
		sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
		sessionFactory.getStatistics().setStatisticsEnabled(true);
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	/**
	 * This method tests the retrieval of a {@link Customer}.
	 * The Customer contains a ManyToOne relation that is
	 * non-optional and LAZY with a JoinColumn that has a
	 * {@link JoinColumn#referencedColumnName()} defined.
	 *
	 * We expect that this only results in ONE query being sent to the database,
	 * namely: ~ `SELECT * FROM Customer`, because the relation is LAZY.
	 *
	 * but actually it sends two:
	 *    - First it runs `SELECT * FROM Customer`
	 *    - Then it runs: `SELECT * FROM Country WHERE isoCode=?`
	 */
	@Test
	public void hhh7526Test() throws Exception {
		Customer johnDoe = createDummyData();
		testForeignKeyReferenceRetrieval(johnDoe);
	}

	private Customer createDummyData() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		Country usa = new Country("US", "United States of America");
		entityManager.persist(usa);

		Customer johnDoe = new Customer("John Doe", usa);
		entityManager.persist(johnDoe);

		entityManager.getTransaction().commit();
		entityManager.close();

		return johnDoe;
	}

	private void testForeignKeyReferenceRetrieval(Customer johnDoe) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		// Clear the stats, such that we can be sure only the relevant stats are collected below
		sessionFactory.getStatistics().clear();

		entityManager.getTransaction().begin();

		entityManager.find(Customer.class, johnDoe.getId());

		entityManager.getTransaction().commit();

		// We expect only one query: a Customer with a !lazy-loaded! Country
		Assert.assertEquals(
				1,
				sessionFactory.getStatistics().getPrepareStatementCount()
		);

		entityManager.close();
	}
}
