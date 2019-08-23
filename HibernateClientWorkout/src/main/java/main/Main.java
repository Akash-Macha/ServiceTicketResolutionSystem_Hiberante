package main;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import entities.Department;
import entities.Priority;
import entities.Role;
import entities.ServiceEngineer;
import entities.Status;
import entities.Ticket;
import entities.User;

public class Main {

	public static void main(String[] args) {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DBConnection");
		
		/* getListOfTickets */
//		String user_name = "Akash";
//		String apiURL_InsertTicketAndAssignServiceEngineer = "http://localhost:8011/HibernateClientWorkout/webapi/res/get-tickets";
//		Client client = ClientBuilder.newBuilder().build();
//		WebTarget target = client.target(apiURL_InsertTicketAndAssignServiceEngineer);
//		Response restResponse = target.request().post(Entity.json(user_name));
//		
//		List<Object[]> listOfTickets = restResponse.readEntity(List.class);
//		System.out.println(listOfTickets);		
		/* */
		
		/* START: finding out unAssigned employee */
		
//		EntityManager entityManager = entityManagerFactory.createEntityManager();
//		Query findUnAssignedEmployee = entityManager.createQuery(""
//				+ "SELECT "
//				+ "		serviceEngineer.user, "
//				+ "		serviceEngineer.current_high_priority_ticket "
//				+ "FROM ServiceEngineer serviceEngineer "
//				+ "WHERE "
//				+ "	serviceEngineer.current_high_priority_ticket = null and "
//				+ "	serviceEngineer.department = 7 ");
//		List<Object[]> serviceEmployeeRecords = findUnAssignedEmployee.getResultList();
		
//		Query findUnAssignedEmployee = entityManager.createQuery(""
//				+ "select s "
//				+ "FROM ServiceEngineer s "
//				+ "WHERE (s.current_high_priority_ticket IS NULL and s.department.id = 7)");
		

//		ServiceEngineer s = entityManager.find(ServiceEngineer.class, 29);
//		System.out.println(s);
		
//		System.out.println(new ArrayList( findUnAssignedEmployee.getResultList() ));

		/* END: finding out unAssigned employee */
		
		/* START - retreiving differenct types of column data from Table */
//		String apiURL_get = "http://localhost:8081/HibernateClientWorkout/webapi/res/get-departments";
//		Client client = ClientBuilder.newBuilder().build();
//		WebTarget target = client.target(apiURL_get);
//		Response response = target.request().get();
//
//		
//		List<Object[]> listOfDept = (List<Object[]>) response.readEntity(List.class);
//		response.close(); // You should close connections!
//		
//		for(Object eachObject : listOfDept) {
//			ArrayList eachDepartment = (ArrayList)eachObject;
//			
//			System.out.println("eachDepartment-- : " + eachDepartment.get(0));
//			System.out.println("eachDepartment : " + eachDepartment.get(1));
//			
//		}
		/* END - retreiving differenct types of column data from Table */
		
//		String apiURL_get = "http://localhost:8081/HibernateClientWorkout/webapi/res/get-departments";
//		Client client = ClientBuilder.newBuilder().build();
//		WebTarget target = client.target(apiURL_get);
//		Response response = target.request().get();
//
//		
//		List<Object[]> listOfDept = (List<Object[]>) response.readEntity(List.class);
//		response.close(); // You should close connections!
//		
//		for(Object eachObject : listOfDept) {
//			ArrayList eachDepartment = (ArrayList)eachObject;
//			
//			System.out.println("eachDepartment : " + eachDepartment.get(0));
//			System.out.println("eachDepartment : " + eachDepartment.get(1));
//			
//		}
		
		
		/* ---------------------- https://stackoverflow.com/a/46094946 */
		
//		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DBConnection");
//		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		
//		Query getDepartment = entityManager.createQuery("SELECT ");
//		List<ServiceEngineer> list =  getDepartment.getResultList();
//		System.out.println(list);

//		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		/* START: initialize tables */
//		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
//		init_user_and_role(entityManager);
//		
//		init_department(entityManager);
//		
//		init_priority(entityManager);
//		init_status(entityManager);
		
		/* Don't call this method
		 * 
		 * Got MySql Workbench
		 * 	and make necessary changes in the constraints: CHECK THE SCREENSHOT IN STRS_Hibernate folder
		 *  */
		
//		init_serviceEngieer(entityManagerFactory);
		
//		init_admin(entityManagerFactory);
		/* END: initialize tables */
		
		System.out.println("--> DONE!");
		
		/* ---- START - validation */
//		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DBConnection");
//		EntityManager entityManager = entityManagerFactory.createEntityManager();
//		
//		Query query = entityManager.createQuery(""
//				+ "SELECT u "
//				+ "FROM User u "
//				+ "INNER JOIN "
//				+ "u.role r "
//				+ "WHERE u.user_name= :user_name and u.password= :password;");
//		query.setParameter("user_name", "Akash");
//		query.setParameter("password", "Ak123");
//		// "SELECT DISTINCT e FROM Employee e INNER JOIN e.tasks t where t.supervisor='Denise'"
//		/* https://www.logicbig.com/tutorials/java-ee-tutorial/jpa/jpql-inner-join.html */
//		
//		List<User> resultUser = query.getResultList();
//		
//		System.out.println("--> " + resultUser );
//		
//		if(resultUser.size() == 0) {
//			System.out.println("invalid user");
//		}else
//			System.out.println("Result = " + resultUser.get(0).getRole().getCode());
		/* ---- END - validation */

		
		
		
		/* working 100%  - GET request */
//		String apiURL_get = "http://localhost:8081/HibernateClientWorkout/webapi/res/checkget";
//		Client client = ClientBuilder.newBuilder().build();
//		WebTarget target = client.target(apiURL_get);
//		Response response = target.request().get();
//		User response_user = response.readEntity(User.class);
//		response.close(); // You should close connections!
		
//		System.out.println("response = user = " + response_user);
		/* ---------------------- https://stackoverflow.com/a/46094946 */
		
		
//		User mani = new User();
//		mani.setName("Mani");  mani.setPassword("Ma123");
		
//		String apiURL_post = "http://localhost:8081/HibernateClientWorkout/webapi/res/checkpost";
//		Client client = ClientBuilder.newBuilder().build();
//		WebTarget target = client.target(apiURL_post);
//		Response response = target.request().post(Entity.json(mani));
//		
//		Role role = response.readEntity(Role.class);
//		
//		response.close();
//		System.out.println("reponse = role = " + role);
		
		/* check PUT */
//		User mani = new User();
//		mani.setName("Mani");  mani.setPassword("Ma123");
//		String apiURL_put = "http://localhost:8081/HibernateClientWorkout/webapi/res/checkput";
//		Client client = ClientBuilder.newBuilder().build();
//		WebTarget target = client.target(apiURL_put);
//		Response response = target.request().put(Entity.json(mani));
//		
//		Role role = response.readEntity(Role.class);
//		
//		response.close();
//		System.out.println("reponse = role = " + role);
	}
	
	private static void init_admin(EntityManagerFactory entityManagerFactory) {
		
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		/*
		 * 0. create a Role =
		 * 			code = ADMN
		 * 			name = Admin
		 * 1. create a User = 
		 * 			name = Admin
		 * 			pass = admin
		 * 			role ->
		 * 			user_name = Admin
		 */
		Role adminRole = new Role();
		adminRole.setCode("ADMN");
		adminRole.setName("Admin");
		
		
		User adminUser = new User();
		adminUser.setName("Admin");
		adminUser.setPassword("admin");
		adminUser.setRole(adminRole);
		adminUser.setUser_name("Admin");
		
		entityManager.getTransaction().begin();
		entityManager.persist(adminRole);
		entityManager.persist(adminUser);
		entityManager.getTransaction().commit();
		
	}
	
	private static void init_user_and_role(EntityManager entityManager) {
		
		/* ----------------------- || creating End_User - Akash || */
//		Role akash_role = new Role();
//		akash_role.setName("End_User");		// End_User, Service_Engineer
//		akash_role.setCode("END_U");		// END_U, SER_ENGG
//		
//		User akash = new User();
//		akash.setName("Akash Macha");
//		akash.setUser_name("Akash");
//		akash.setPassword("Ak123");
//
//		akash.setRole(akash_role);
//
//		entityManager.getTransaction().begin();
//		try {
//			entityManager.persist(akash_role);
//			entityManager.persist(akash);			
//		}catch(Exception e) {
//			System.out.println("cannot insert akash again !");
//		}
//
//		entityManager.getTransaction().commit();
		
		/* ----------------------- || creating Service_Engineer - Sahit || */
//		Role sahit_role = new Role();
//		sahit_role.setName("Service_Engineer");		// End_User, Service_Engineer
//		sahit_role.setCode("SER_ENGG");			// END_U, SER_ENGG
//		
//		User sahit = new User();
//		sahit.setName("Sahit Katta");
//		sahit.setUser_name("Sahit");
//		sahit.setPassword("Sa123");
//
//		
//		sahit.setRole(sahit_role);
//
//		entityManager.getTransaction().begin();
//		
//		entityManager.persist(sahit_role);
//		entityManager.persist(sahit);
//		
//		entityManager.getTransaction().commit();
		
		/* ----------------------- || creating End_User - Mani || */
		/* ----------------------- getting the role from the already existing table */
//		Query getEndUserPrimaryKey = entityManager.createQuery(""
//				+ "SELECT r.id "
//				+ "FROM Role r "
//				+ "WHERE r.code='END_U'");
//		
//		List<Integer> end_user_primary_key = (List<Integer>) getEndUserPrimaryKey.getResultList();
//		
//		Role mani_role;
//		if(end_user_primary_key != null && end_user_primary_key.size() != 0) {
//			System.out.println("Role object from query = "+ end_user_primary_key.get(0));
//			mani_role = entityManager.find(Role.class, end_user_primary_key.get(0));
//		}
//		else {
//			System.out.println("Query is wrong!!");
//			System.out.println("Inserting role object manullay !");
//		
//			System.out.println("WE SHOULD GET AN ERROR !! coz we are trying to insert the same Role object ! which is alread in the Role table");
//			
//			mani_role = new Role();
//			mani_role.setName("End_User");		// End_User, Service_Engineer
//			mani_role.setCode("END_U");		// END_U, SER_ENGG
//		}
//		
//		User mani = new User();
//		mani.setName("Mahihar");
//		mani.setUser_name("Mani");
//		mani.setPassword("Ma123");
//
//		mani.setRole(mani_role);
//
//		System.out.println("user mani = " + mani);
//		System.out.println("role mani = " + mani_role);
//		entityManager.getTransaction().begin();
//		
//		entityManager.persist(mani_role);
//		entityManager.persist(mani);
//		
//		entityManager.getTransaction().commit();
		
		/* ----------------------- || creating Service_Engineer - Sandeep - ICT || */
		/* ----------------------- getting the role from the already existing table */
//		Query getServiceEngineerPrimaryKey = entityManager.createQuery(""
//							+ "SELECT r.id "
//							+ "FROM Role r "
//							+ "WHERE r.code='SER_ENGG'");
//
//		List<Integer> serviceEngineerPrimaryKey = (List<Integer>) getServiceEngineerPrimaryKey.getResultList();
//		
//		Role sandeepRole;
//		if(serviceEngineerPrimaryKey != null && serviceEngineerPrimaryKey.size() != 0) {
//			System.out.println("Role object from query = "+ serviceEngineerPrimaryKey.get(0));
//			sandeepRole = entityManager.find(Role.class, serviceEngineerPrimaryKey.get(0));
//		}
//		else {
//			System.out.println("Query is wrong!!");
//			System.out.println("Inserting role object manullay !");
//			
//			System.out.println("WE SHOULD GET AN ERROR !! coz we are trying to insert the same Role object ! which is alread in the Role table");
//			
//			sandeepRole = new Role();
//			sandeepRole.setName("Service_Engineer");		// End_User, Service_Engineer
//			sandeepRole.setCode("SER_ENGG");		// END_U, SER_ENGG
//		}
//		
//		User sandeep = new User();
//		sandeep.setName("Sandeep Vanga");
//		sandeep.setUser_name("Sandeep");
//		sandeep.setPassword("Sa123");
//		
//		sandeep.setRole(sandeepRole);
//
//		entityManager.getTransaction().begin();
//		entityManager.persist(sandeepRole);
//		entityManager.persist(sandeep);
//		entityManager.getTransaction().commit();
		
		/* ---------- New Insertion */
		/* ----------------------- || creating Service_Engineer - Sainath - FM || [ reusing the getServiceEngineerRole ]*/
		
		User sainath = new User();
		sainath.setName("Sainath Jonnala");
		sainath.setPassword("Sa123");
		sainath.setUser_name("Sainath");
		
			Query getServiceEngineerRole = entityManager.createQuery("SELECT role FROM Role role WHERE role.code = 'SER_ENGG'");
		Role serviceEngineerRole = (Role) getServiceEngineerRole.getResultList().get(0) ;

		sainath.setRole( serviceEngineerRole );
		entityManager.getTransaction().begin();
		entityManager.persist(sainath);
		entityManager.getTransaction().commit();
		
		/* ----------------------- || creating Service_Engineer - Shravya - FM || */
		User shravya = new User();
		shravya.setName("Shravya Dharmapuri");
		shravya.setPassword("Sh123");
		shravya.setUser_name("Shravya");
		
		/* create one getServiceEngineerRole and use it all over this function */
		shravya.setRole( serviceEngineerRole );
		
		entityManager.getTransaction().begin();
		entityManager.persist(shravya);
		entityManager.getTransaction().commit();
		
		/*  ---------------------- || creating Service_Engineer - Abjishek - FM || */
		User abhi = new User();
		abhi.setName("Ahishek Jaksani");
		abhi.setPassword("Ab123");
		abhi.setUser_name("Ahishek");
		
		abhi.setRole(serviceEngineerRole);
		
		entityManager.getTransaction().begin();
		entityManager.persist(abhi);
		entityManager.getTransaction().commit();
		
		
		/*  ---------------------- || creating Service_Engineer - Shreya - FM || */
		User john = new User();
		john.setName("John Doe");
		john.setPassword("Jo123");
		john.setUser_name("John");
		
		john.setRole(serviceEngineerRole);
		
		entityManager.getTransaction().begin();
		entityManager.persist(john);
		entityManager.getTransaction().commit();

	}
	
	private static void init_department(EntityManager entityManager) {
		/* Department - ICT */
		Department ict = new Department();
		ict.setName("ICT");
		ict.setCode("ICT");
		
		entityManager.getTransaction().begin();
		entityManager.persist(ict);
		entityManager.getTransaction().commit();
		
		/* Department - Facility Management */
		Department facilityManagement = new Department();
		facilityManagement.setName("Facility Management");
		facilityManagement.setCode("FCT_MGT");
		
		entityManager.getTransaction().begin();
		entityManager.persist(facilityManagement);
		entityManager.getTransaction().commit();
		
		/* Department - Travel and Hospitality */
		Department travelAndHospitality = new Department();
		travelAndHospitality.setName("Travel and Hospitality");
		travelAndHospitality.setCode("TR_HSPTL");
		
		entityManager.getTransaction().begin();
		entityManager.persist(travelAndHospitality);
		entityManager.getTransaction().commit();
		
	}
	private static void init_priority(EntityManager entityManager) {
		// LOW, MEDIUM, HIGH
		// LOW, MED, HIG
		
		/* Priority - Low */
		Priority lowPriority = new Priority();
		lowPriority.setValue("Low");
		lowPriority.setCode("LOW");
		
		entityManager.getTransaction().begin();
		entityManager.persist(lowPriority);
		entityManager.getTransaction().commit();
		
		/* Priority - Medium */
		Priority mediumPriority = new Priority();
		mediumPriority.setValue("Medium");
		mediumPriority.setCode("MED");
		
		entityManager.getTransaction().begin();
		entityManager.persist(mediumPriority);
		entityManager.getTransaction().commit();
		
		/* Priority - High */
		Priority highPriority = new Priority();
		highPriority.setValue("High");
		highPriority.setCode("HIG");
		
		entityManager.getTransaction().begin();
		entityManager.persist(highPriority);
		entityManager.getTransaction().commit();
	}
	private static void init_status(EntityManager entityManager) {
		/* Status - On going */
		Status onGoing = new Status();
		onGoing.setValue("On going");
		onGoing.setCode("ON_GO");
		
		entityManager.getTransaction().begin();
		entityManager.persist(onGoing);
		entityManager.getTransaction().commit();
		
		/* Status - Pending */
		Status pending = new Status();
		pending.setValue("Pending");
		pending.setCode("PEND");
		
		entityManager.getTransaction().begin();
		entityManager.persist(pending);
		entityManager.getTransaction().commit();
		
		/* Status - Closed */
		Status closed = new Status();
		closed.setValue("Closed");
		closed.setCode("CLSD");
		
		entityManager.getTransaction().begin();
		entityManager.persist(closed);
		entityManager.getTransaction().commit();
	}
	
	private static void init_serviceEngieer(EntityManagerFactory entityManagerFactory) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
	
		/* START: inserting Sahit Katta */
//		ServiceEngineer sahit = new ServiceEngineer();
//		/* set user_id -> User */
//		Query getSahitUser = entityManager.createQuery(""
//				+ "SELECT u "
//				+ "FROM User u "
//				+ "WHERE u.user_name='Sahit'");
//		User userSahit = (User) getSahitUser.getResultList().get(0);
//		
//		System.out.println("user object = "+ userSahit);
//		
//		sahit.setUser(userSahit);
//		
//		/* set area_of_expertise -> Department */
//		Query getSahitDepartment = entityManager.createQuery(""
//				+ "SELECT d "
//				+ "FROM Department d "
//				+ "WHERE d.name='ICT'");
//		Department sahitDepartment =  (Department) getSahitDepartment.getResultList().get(0);
//		
//		System.out.println("Depart obj = " + sahitDepartment);
//		
//		sahit.setDepartment(sahitDepartment); /* area_of_expertise */
//		
//		sahit.setTotal_tickets_worked_on(0);
//		sahit.setCurrent_ticket_start_date(null);
//		sahit.setCurrent_high_priority_ticket(null);
//		sahit.setPriority(null);
//
//		System.out.println("Sahit obj = " + sahit);
//		
//		System.out.println("PERSISTING");
//		entityManager.getTransaction().begin();
//		entityManager.persist(sahit);
//		entityManager.getTransaction().commit();
		/* END: inserting Sahit Katta */
		
		/* START: inserting Sandeep Vanga */
//		ServiceEngineer sandeep = new ServiceEngineer();
//		/* set user_id -> User */
//		Query getSandeepUser = entityManager.createQuery(""
//				+ "SELECT u "
//				+ "FROM User u "
//				+ "WHERE u.user_name='Sandeep'");
//		User userSandeep = (User) getSandeepUser.getResultList().get(0);
//		
//		System.out.println("user object = "+ userSandeep);
//		
//		sandeep.setUser(userSandeep);
//		
//		/* set area_of_expertise -> Department */
//		Query getDepartment = entityManager.createQuery(""
//				+ "SELECT d "
//				+ "FROM Department d "
//				+ "WHERE d.name='ICT'");
//		Department sandeepDepartment =  (Department) getDepartment.getResultList().get(0);
//		
//		System.out.println("Depart obj = " + sandeepDepartment);
//		
//		sandeep.setDepartment(sandeepDepartment); /* area_of_expertise */
//		
//		sandeep.setTotal_tickets_worked_on(0);
//		sandeep.setCurrent_ticket_start_date(null);
//		sandeep.setCurrent_high_priority_ticket(null);
//		sandeep.setPriority(null);
//
//		System.out.println("Sandeep obj = " + sandeep);
//		
//		System.out.println("PERSISTING");
//		entityManager.getTransaction().begin();
//		entityManager.persist(sandeep);
//		entityManager.getTransaction().commit();
		/* END: inserting Sandeep Vanga */
		
		/* START: inserting Sainath - Facility Management */
		ServiceEngineer sainath = new ServiceEngineer();
		/* set user_id -> User */
			Query getSandeepUser = entityManager.createQuery(""
					+ "SELECT u "
					+ "FROM User u "
					+ "WHERE u.user_name='Sainath'");
			User userSandeep = (User) getSandeepUser.getResultList().get(0);
		sainath.setUser(userSandeep);
		
			/* set area_of_expertise -> Department */
			Query getDepartmentFCT = entityManager.createQuery(""
					+ "SELECT d "
					+ "FROM Department d "
					+ "WHERE d.code= 'FCT_MGT'");
			Department fctDepartment =  (Department) getDepartmentFCT.getResultList().get(0);
		sainath.setDepartment(fctDepartment); /* area_of_expertise */
		
		sainath.setTotal_tickets_worked_on(0);
		sainath.setCurrent_ticket_start_date(null);
		sainath.setCurrent_high_priority_ticket(null);
		sainath.setPriority(null);

		System.out.println("Sainath obj = " + sainath);
		
		entityManager.getTransaction().begin();
		entityManager.persist(sainath);
		entityManager.getTransaction().commit();
		/* END:  inserting Sainath - Facility Management */
		
		/* START: inserting shravya - Facility Management */
		ServiceEngineer shravya = new ServiceEngineer();
		/* set user_id -> User */
			Query getShravyaUser = entityManager.createQuery(""
					+ "SELECT u "
					+ "FROM User u "
					+ "WHERE u.user_name= 'Shravya'");
			User userShravya = (User) getShravyaUser.getResultList().get(0);
			shravya.setUser(userShravya);

			shravya.setDepartment(fctDepartment ); /* area_of_expertise */
		
			shravya.setTotal_tickets_worked_on(0);
			shravya.setCurrent_ticket_start_date(null);
			shravya.setCurrent_high_priority_ticket(null);
			shravya.setPriority(null);

		System.out.println("shravya obj = " + shravya);
		
		entityManager.getTransaction().begin();
		entityManager.persist(shravya);
		entityManager.getTransaction().commit();
		/* END:  inserting shravya - Facility Management */
		
		
		/* START: updating a record */
//		ServiceEngineer sandeep = entityManager.find(ServiceEngineer.class, 25);
//		System.out.println(sandeep);
//		
//		entityManager.getTransaction().begin();
//		
//		sandeep.setTotal_tickets_worked_on(0);
//		
//		entityManager.getTransaction().commit();
//		
//		System.out.println("after : " + sandeep);
		/* END: updating a record */
		
	}
}
