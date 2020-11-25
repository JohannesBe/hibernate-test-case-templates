package org.hibernate.bugs;

import org.hibernate.SessionFactory;
import org.hibernate.bugs.entities.Address;
import org.hibernate.bugs.entities.Country;
import org.hibernate.bugs.entities.Customer;
import org.hibernate.graph.GraphSemantic;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory("templatePU");
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}


	@Test
	public void hhh14349Test() throws Exception {
		Customer persistedCustomer = generateDummyCustomer();

		EntityManager em = entityManagerFactory.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		em.getTransaction().begin();

		// build easy "FROM Customer WHERE id=?" query
		CriteriaQuery<Customer> cQuery = cb.createQuery(Customer.class);
		Root<Customer> root = cQuery.from(Customer.class);
		cQuery.select(root);
		cQuery.where(cb.equal(root.get("id"), persistedCustomer.getId()));

		TypedQuery<Customer> tQuery = em.createQuery(cQuery);
		// Entity graph specifies we need customer.address.country
		tQuery.setHint(GraphSemantic.FETCH.getJpaHintName(), em.getEntityGraph(Customer.EG_WITH_COUNTRY));
		Customer fetchedCustomer = tQuery.getSingleResult();

		em.getTransaction().commit();
		em.close();

		// should not throw LazyInitializationException, because EG overrides the LAZY but does...
		String testLazyFetch = fetchedCustomer.getAddress().getCountry().getName();
	}


	public Customer generateDummyCustomer() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		Country swiss = new Country("Switzerland");
		entityManager.persist(swiss);

		Address companyAddr = new Address("Müllerstraße 14", "3423 Obenwald", "Swiss Mitelland", swiss);
		Customer customer = new Customer("Obenwald Chemikaliën", companyAddr);
		entityManager.persist(customer);

		entityManager.getTransaction().commit();
		entityManager.close();

		return customer;
	}
}
