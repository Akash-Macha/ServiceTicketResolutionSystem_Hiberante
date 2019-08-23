package strs_repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entities.Role;
import entities.User;

public class AdminRepo {

	EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DBConnection");
	
	public List<User> getlistOfAllUsers(){
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		Query getListOfAllUsers = entityManager.createQuery(""
				+ "SELECT user "
				+ "FROM User user "
				+ "WHERE user.role.code = 'END_U' ");
		
		return (List<User>) getListOfAllUsers.getResultList();
	}
	
	public boolean addUser(User user) {
		
		try {
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			
				Query getEndUserRole = entityManager.createQuery("SELECT role FROM Role role WHERE role.code = 'END_U'");
			
			user.setRole( (Role) getEndUserRole.getResultList().get(0) ); 
			
			entityManager.getTransaction().begin();
			entityManager.persist(user);
			entityManager.getTransaction().commit();
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	public boolean deleteUser(Integer id) {
		try {
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			
			entityManager.getTransaction().begin();
			
			User user = entityManager.find(User.class, id);
			entityManager.remove(user);
			
			entityManager.getTransaction().commit();
			
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
