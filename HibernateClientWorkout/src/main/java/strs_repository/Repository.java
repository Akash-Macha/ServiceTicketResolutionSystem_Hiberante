package strs_repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entities.Department;
import entities.Priority;
import entities.Role;
import entities.ServiceEngineer;
import entities.Status;
import entities.Ticket;
import entities.User;

public class Repository {
	EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DBConnection");
	
	public Role validate(User user) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query query = entityManager.createQuery(""
				+ "SELECT u "
				+ "FROM User u "
				+ "INNER JOIN "
				+ "u.role r "
				+ "WHERE u.user_name= :user_name and u.password= :password ");
		query.setParameter("user_name", user.getUser_name());
		query.setParameter("password", user.getPassword());
		// "SELECT DISTINCT e FROM Employee e INNER JOIN e.tasks t where t.supervisor='Denise'"
		/* https://www.logicbig.com/tutorials/java-ee-tutorial/jpa/jpql-inner-join.html */
		
		List<User> resultUser = (List<User>) query.getResultList();
		
		System.out.println("--> " + resultUser );
		
		if(resultUser.size() == 0) {
			System.out.println("invalid user");
			
			return null;
		}else {
			System.out.println("VALID user");
			System.out.println("Result = " + resultUser.get(0).getRole().getCode());
			
			return resultUser.get(0).getRole();
		}
	}
	
	public List<Object[]> getDepartments() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		Query getAllDepartments = entityManager.createQuery(""
				+ "SELECT department.code, department.name "
				+ "FROM Department department ");

		return (List<Object[]>) getAllDepartments.getResultList();
	}
	
	public List<Object[]> getPriorities() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		Query getAllDepartments = entityManager.createQuery(""
				+ "SELECT priority.code, priority.value "
				+ "FROM Priority priority");
		
		return (List<Object[]>) getAllDepartments.getResultList();
	}

	public boolean checkAndAssignTicketForUnAssignedServiceEmployee(ArrayList<String> formData) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

			Query findUnAssignedEmployee = entityManager.createQuery(""
					+ "SELECT serviceEngineer "
					+ "FROM ServiceEngineer serviceEngineer "
					+ "WHERE (serviceEngineer.current_high_priority_ticket IS NULL and serviceEngineer.department.code = :departmentCode) ");
	
			findUnAssignedEmployee.setParameter("departmentCode", formData.get(1));

		List<ServiceEngineer> serviceEmployeeRecords = findUnAssignedEmployee.getResultList();
		System.out.println("1 --> " + serviceEmployeeRecords);

		if (serviceEmployeeRecords != null && serviceEmployeeRecords.size() != 0) {
			/*
			 * Got an Employee who isn't assigned with any Ticket yet!
			 * 
			 * Assign this ticket to this person and modify the ServiceEngineer and Ticket
			 * Table columns !
			 */
			/* updating: ServiceEmployee */
			System.out.println("Inside : if (serviceEmployeeRecords != null && serviceEmployeeRecords.size() != 0) {");
			
			ServiceEngineer serviceEngineer_case_1 = serviceEmployeeRecords.get(0);

			/* inserting Priority */
				Query getPriority = entityManager.createQuery(""
						+ "SELECT priority " 
						+ "FROM Priority priority " 
						+ "WHERE priority.code = :code ");
				getPriority.setParameter("code", formData.get(3));
			serviceEngineer_case_1.setPriority( (Priority) getPriority.getResultList().get(0) );

			serviceEngineer_case_1.setCurrent_ticket_start_date(LocalDate.parse(formData.get(4)));

			
			/* updating: Ticket */
			Ticket ticket = new Ticket();
			/* inserting ""category"" -> department */
				Query getDepartment = entityManager.createQuery( ""
						+ "SELECT department " 
						+ "FROM Department department " 
						+ "WHERE department.code = :code ");
				getDepartment.setParameter("code", formData.get(1));
			ticket.setCategory((Department) getDepartment.getResultList().get(0));

			/* inserting requested by -> User */
				Query getRequestedByUser = entityManager.createQuery(""
						+ "SELECT user " 
						+ "FROM User user " 
						+ "WHERE user.user_name = :user_name ");
				getRequestedByUser.setParameter("user_name", formData.get(0));
			ticket.setRequested_by((User) getRequestedByUser.getResultList().get(0));

			/* inserting Priority: retreived at ServiceEngineer Table */
			ticket.setPriority( serviceEngineer_case_1.getPriority() );

			/* inserting Status */
				Query getStatus = entityManager
						.createQuery(""
								+ "SELECT status " 
								+ "FROM Status status " 
								+ "WHERE status.code = :code ");
				getStatus.setParameter("code", "ON_GO");
			ticket.setStatus((Status) getStatus.getResultList().get(0));
			
			ticket.setAssigned_to(serviceEngineer_case_1.getUser());

			LocalDate start_date = LocalDate.parse((String) formData.get(4));
			LocalDate requested_end_date = LocalDate.parse((String) formData.get(5));

			ticket.setStart_date(start_date);
			ticket.setRequested_end_date(requested_end_date);

			ticket.setMessage(formData.get(2));

			/* set the ""current_high_priority_ticket_id"" of ServiceEngineer */
			serviceEngineer_case_1.setCurrent_high_priority_ticket(ticket);

			entityManager.getTransaction().begin();
			entityManager.persist(serviceEngineer_case_1);
			entityManager.persist(ticket);
			entityManager.getTransaction().commit();

			return true;
		}
		return false;
	}
	public boolean checkForLowPriorityTicketServiceEmployee(ArrayList<String> formData) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			Query query_getLowPriorityTicketServiceEngineers = entityManager.createQuery(""
					+ "SELECT serviceEngineer "
					+ "FROM ServiceEngineer serviceEngineer "
					+ "WHERE 	serviceEngineer.department.code = :department and "
					+ "			serviceEngineer.priority.code = :priorty_code and "
					+ "			serviceEngineer.current_high_priority_ticket.status.code = 'ON_GO'");
			query_getLowPriorityTicketServiceEngineers.setParameter("department", formData.get(1));
			query_getLowPriorityTicketServiceEngineers.setParameter("priorty_code", "LOW");
			
			List<ServiceEngineer> serviceEmployeeRecords_case2 = query_getLowPriorityTicketServiceEngineers.getResultList();
			if(serviceEmployeeRecords_case2 != null && serviceEmployeeRecords_case2.size() >= 1) {
				/*
				 * We got an employee working with a Ticket with Priority = LOW
				 */
				System.out.println("Inside: List<ServiceEngineer> serviceEmployeeRecords_case2 = query_getLowPriorityTicketServiceEngineers.getResultList();\r\n" + 
						"			if(serviceEmployeeRecords_case2 != null && serviceEmployeeRecords_case2.size() >= 1) {");
				
				ServiceEngineer lowPriorytTicketServiceEngineer = serviceEmployeeRecords_case2.get(0);
					Query getPendingStatus = entityManager.createQuery(""
							+ "SELECT status "
							+ "FROM Status status "
							+ "WHERE status.code = 'PEND' ");
					System.out.println("PENDING STATUS IN REPO : " + (Status) getPendingStatus.getResultList().get(0));
					
				System.out.println(lowPriorytTicketServiceEngineer.getCurrent_high_priority_ticket());				
				lowPriorytTicketServiceEngineer.getCurrent_high_priority_ticket().setStatus( (Status) getPendingStatus.getResultList().get(0) );

				/*updating ServiceEngineer table */
				lowPriorytTicketServiceEngineer.setCurrent_ticket_start_date( LocalDate.parse( formData.get(4) ) );
				
				/* inserting Priority */
					Query getPriority_case2 = entityManager.createQuery(""
							+ "SELECT priority " 
							+ "FROM Priority priority " 
							+ "WHERE priority.code = :code ");
					getPriority_case2.setParameter("code", formData.get(3));
				lowPriorytTicketServiceEngineer.setPriority( (Priority) getPriority_case2.getResultList().get(0) );


				/*updating Ticket table */
				Ticket ticket = new Ticket();
				ticket.setAssigned_to(lowPriorytTicketServiceEngineer.getUser());
				ticket.setCategory(lowPriorytTicketServiceEngineer.getDepartment());
				ticket.setMessage( formData.get(2) );
				ticket.setPriority(lowPriorytTicketServiceEngineer.getPriority());
				
				/* inserting requested by -> User */
					Query getRequestedByUser_case_2 = entityManager.createQuery(""
							+ "SELECT user " 
							+ "FROM User user " 
							+ "WHERE user.user_name = :user_name ");
					getRequestedByUser_case_2.setParameter("user_name", formData.get(0));
				ticket.setRequested_by((User) getRequestedByUser_case_2.getResultList().get(0));
				
				ticket.setRequested_end_date( LocalDate.parse( formData.get(5) ) );
				ticket.setStart_date( LocalDate.parse( formData.get(4) ) );
				
					/* inserting Status */
					Query getStatus_case_2 = entityManager
							.createQuery(""
									+ "SELECT status " 
									+ "FROM Status status " 
									+ "WHERE status.code = :code ");
					getStatus_case_2.setParameter("code", "ON_GO");
				ticket.setStatus((Status) getStatus_case_2.getResultList().get(0));
				
				/* setting this newly generated ticket to this ServiceEmployee */
				lowPriorytTicketServiceEngineer.setCurrent_high_priority_ticket(ticket);
				
				entityManager.getTransaction().begin();
				entityManager.persist(lowPriorytTicketServiceEngineer);
				entityManager.persist(ticket);
				entityManager.getTransaction().commit();
				
				System.out.println( "Current " + ticket.getPriority().getValue() + " Priroyt Ticket has been assigned to "
						+  ticket.getAssigned_to().getName() +"");
				
				return true;
			}
		}catch (Exception e) {
			System.out.println("IN catch block");
			   e.printStackTrace();
			   
			   return false;
			}
		
		return false;
	}
	public boolean checkForMediumPriorityTicketServiceEmployee(ArrayList<String> formData) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			Query query_getMediumPriorityTicketServiceEngineers = entityManager.createQuery(""
					+ "SELECT serviceEngineer "
					+ "FROM ServiceEngineer serviceEngineer "
					+ "WHERE 	serviceEngineer.department.code = :department and "
					+ "			serviceEngineer.priority.code = :priorty_code and "
					+ "			serviceEngineer.current_high_priority_ticket.status.code = 'ON_GO'");
			query_getMediumPriorityTicketServiceEngineers.setParameter("department", formData.get(1));
			query_getMediumPriorityTicketServiceEngineers.setParameter("priorty_code", "MED");
			
			List<ServiceEngineer> serviceEmployeeRecords = query_getMediumPriorityTicketServiceEngineers.getResultList();
			
			if(serviceEmployeeRecords != null && serviceEmployeeRecords.size() >= 1) {
				/*
				 * Got a ServiceEmployee who is working on a MEDIUM priority Ticket
				 */
				ServiceEngineer mediumPriorityTicketServiceEngineer = serviceEmployeeRecords.get(0);
				Query getPendingStatus = entityManager.createQuery(""
						+ "SELECT status "
						+ "FROM Status status "
						+ "WHERE status.code = 'PEND' ");
				mediumPriorityTicketServiceEngineer.getCurrent_high_priority_ticket().setStatus( (Status) getPendingStatus.getResultList().get(0) );
				mediumPriorityTicketServiceEngineer.setCurrent_ticket_start_date(  LocalDate.parse( formData.get(4) ) );
				
				Query getPriority = entityManager.createQuery(""
						+ "SELECT priority " 
						+ "FROM Priority priority " 
						+ "WHERE priority.code = :code ");
				getPriority.setParameter("code", formData.get(3));
				mediumPriorityTicketServiceEngineer.setPriority( (Priority) getPriority.getResultList().get(0) );
				
				/*updating Ticket table */
				Ticket ticket = new Ticket();
				ticket.setAssigned_to(mediumPriorityTicketServiceEngineer.getUser());
				ticket.setCategory(mediumPriorityTicketServiceEngineer.getDepartment());
				ticket.setMessage( formData.get(2) );
				ticket.setPriority(mediumPriorityTicketServiceEngineer.getPriority());
				
				/* inserting requested by -> User */
				Query getRequestedByUser_case_2 = entityManager.createQuery(""
						+ "SELECT user " 
						+ "FROM User user " 
						+ "WHERE user.user_name = :user_name ");
				getRequestedByUser_case_2.setParameter("user_name", formData.get(0));
			ticket.setRequested_by((User) getRequestedByUser_case_2.getResultList().get(0));
			
			ticket.setRequested_end_date( LocalDate.parse( formData.get(5) ) );
			ticket.setStart_date( LocalDate.parse( formData.get(4) ) );
			
				/* inserting Status */
				Query getStatus_case_2 = entityManager
						.createQuery(""
								+ "SELECT status " 
								+ "FROM Status status " 
								+ "WHERE status.code = :code ");
				getStatus_case_2.setParameter("code", "ON_GO");
			ticket.setStatus((Status) getStatus_case_2.getResultList().get(0));
			
			/* setting this newly generated ticket to this ServiceEmployee */
			mediumPriorityTicketServiceEngineer.setCurrent_high_priority_ticket(ticket);
				
			entityManager.getTransaction().begin();
			entityManager.persist(mediumPriorityTicketServiceEngineer);
			entityManager.persist(ticket);
			entityManager.getTransaction().commit();
			
			System.out.println( "Current " + ticket.getPriority().getValue() + " Priroyt Ticket has been assigned to "
					+  ticket.getAssigned_to().getName() +"");
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
	public boolean checkForServiceEngineerWithLessWorkedTicketsAndAssignPutInPending(ArrayList<String> formData) {
		try {
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			
			Ticket ticket = new Ticket();
			/*
			 * Find an Employee who has worked with less number of tickets
			 */
			Query getEmployeeWithLessWorkedTicket = entityManager.createQuery(""
					+ "SELECT serviceEngineer "
					+ "FROM ServiceEngineer serviceEngineer "
					+ "WHERE serviceEngineer.department.code = :department "
					+ "ORDER BY serviceEngineer.total_tickets_worked_on , serviceEngineer.current_high_priority_ticket.id ");
			getEmployeeWithLessWorkedTicket.setParameter("department", formData.get(1));
			List<ServiceEngineer> listOfServiceEngineers = getEmployeeWithLessWorkedTicket.getResultList();
			
			if(listOfServiceEngineers != null && listOfServiceEngineers.size() >= 1) {
				System.out.println("Inside : getEmployeeWithLessWorkedTicket if(listOfServiceEngineers != null && listOfServiceEngineers.size() >= 1) {");
				
				ServiceEngineer serviceEngineer = (ServiceEngineer) getEmployeeWithLessWorkedTicket.getResultList().get(0);
				
//				serviceEngineer.getCurrent_high_priority_ticket().setStatus(  );
				
				ticket.setAssigned_to( (User) serviceEngineer.getUser() );

				/* inserting ""category"" -> department */
					Query getDepartment = entityManager.createQuery( ""
							+ "SELECT department " 
							+ "FROM Department department " 
							+ "WHERE department.code = :code ");
					getDepartment.setParameter("code", formData.get(1));
				ticket.setCategory( (Department) getDepartment.getResultList().get(0) );
				
				ticket.setMessage( formData.get(2) );
				
				/* inserting Priority */
					Query getPriority = entityManager.createQuery(""
							+ "SELECT priority " 
							+ "FROM Priority priority " 
							+ "WHERE priority.code = :code ");
					getPriority.setParameter("code", formData.get(3));
				ticket.setPriority( (Priority) getPriority.getResultList().get(0) );
				
				/* inserting requested by -> User */
					Query getRequestedByUser = entityManager.createQuery(""
							+ "SELECT user " 
							+ "FROM User user " 
							+ "WHERE user.user_name = :user_name ");
					getRequestedByUser.setParameter("user_name", formData.get(0));
				ticket.setRequested_by((User) getRequestedByUser.getResultList().get(0));
				
				ticket.setRequested_end_date( LocalDate.parse( formData.get(5) ) );
				ticket.setStart_date( LocalDate.parse( formData.get(4) ) );
				
				Query getPendingStatus = entityManager.createQuery(""
						+ "SELECT status "
						+ "FROM Status status "
						+ "WHERE status.code = 'PEND' ");
				ticket.setStatus( (Status) getPendingStatus.getResultList().get(0) );
				
				System.out.println("ticket.setStatus( (Status) getPendingStatus.getResultList().get(0) ); = " +
						(Status) getPendingStatus.getResultList().get(0) );
				
				entityManager.getTransaction().begin();
				entityManager.persist(ticket);
				entityManager.getTransaction().commit();
				
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("THIS SHOULD NOT GET EXECUTED !!");
		return false;
	}
	public boolean insertTicket(ArrayList<String> formData) {
		/*
		 * 1. Find a ServiceEmployee who is NOT working with any ticket 
		 * 	   [ current_high_priority_ticke_id should be null ] 
		 * 			1. if FOUND: 1. Assign this ticket to him/her and 
		 * 				update ServiceEngineer & Ticket Table 
		 * 
		 * 			2. if NOT FOUND:
		 * 				1.  search for the employees who are working with LESSER priority 
		 * 					than the current Ticket 
		 * 
		 * 					1. if FOUND: Assign this ticket to him/her and DONOT modify
		 * 						his/her PREVIOUS TICKET ""Assigned_To"" Just update the current Ticket row
		 * 						and previous ticket columns other than ""Assigned_To""
		 * **"" For assigning the same ticket to the same person #PendingCase ""**
		 * 
		 * If NOT FOUND 
		 * 		Find the ServiceEngineer who have worked less number of tickets and
		 * 		Assign this ticket to him/her
		 * 		Put the current Ticket in PENDING MODE
		 * 	SORT : 	total_tickets_worked_on ASC,
		 * 			current_high_priority_ticket_id [ max ticket_id = latest ticket ]
		 */
		/*
		 * data: 
		 * 
		 * 0: formData.add(requested_by_user_name); // user_name = Akash 
		 * 1: formData.add(issue_category); // area_of_expertise_id = 7 | -> Department: code: ICT 
		 * 2: formData.add(message); // message = 
		 * 3: formData.add(priority); // priority_id = 11 | MED 
		 * 4: formData.add(start_date); // 5:
		 * 5: formData.add(requested_end_date); //

		 */
		if(checkAndAssignTicketForUnAssignedServiceEmployee(formData)) {
			
			System.out.println("In: checkAndAssignTicketForUnAssignedServiceEmployee");
			return true;
		}
		
		if(formData.get(3).equals("HIG")){
			/* Priority = HIGH
			 * 
			 * 1. check whether any employee working with """LOW""" priority ticket
			 * 		if found:
			 * 			Assign this ticket to him/her and update Ticket and ServiceEngineer table
			 * 		if NOT found:
			 * 			2. check whether any employee working with """MEDIUM""" priority ticket
			 * 				if found:
			 * 					Assign this ticket to him/her and update Ticket and ServiceEngineer table
			 * 				if NOT found:
			 * 					put the ticket in pending mode !
			 */
			System.out.println("In: if(formData.get(3).equals(\"HIG\")){");
			
			if(checkForLowPriorityTicketServiceEmployee(formData)) {
				
				System.out.println("In: if(checkForLowPriorityTicketServiceEmployee(formData)) {");
				
				return true;
					
			}else if(checkForMediumPriorityTicketServiceEmployee(formData)) {
				
				System.out.println("In: }else if(checkForMediumPriorityTicketServiceEmployee(formData)) {");
				
				return true;
			}else if(checkForServiceEngineerWithLessWorkedTicketsAndAssignPutInPending(formData)) {		
				System.out.println("}else if(checkForServiceEngineerWithLessWorkedTicketsAndAssignPutInPending(formData)) {");
				
				return true;
			}
		}
		else if(formData.get(3).equals("MED")) {
			
			
			if(checkForLowPriorityTicketServiceEmployee(formData)) {
			
				System.out.println("		else if(formData.get(3).equals(\"MED\") && \r\n" + 
					"				checkForLowPriorityTicketServiceEmployee(formData)) {");
				
				return true;
			}else if(checkForServiceEngineerWithLessWorkedTicketsAndAssignPutInPending(formData)) {		
				System.out.println("}else if(checkForServiceEngineerWithLessWorkedTicketsAndAssignPutInPending(formData)) {");
				
				return true;
			}
			return false;
		}
		/*
		 * So, the ticket priority is LOW
		 * Else Ticket should be in pending mode
		 * create a plane Ticket Entry
		 * And Assign this ticket to an Employee who is working with 
		 */
		else if(checkForServiceEngineerWithLessWorkedTicketsAndAssignPutInPending(formData)) {		
			System.out.println("}else if(checkForServiceEngineerWithLessWorkedTicketsAndAssignPutInPending(formData)) {");
				
			return true;
		}
		
		System.out.println("returning false");
		return false;
	}

	public List<Ticket> getListOfTickets(String user_name) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query query_getListOfTickets = entityManager.createQuery("" 
				+ "SELECT ticket " 
				+ "FROM Ticket ticket "
				+ "WHERE ticket.requested_by.user_name = :user_name or ticket.assigned_to.user_name = :user_name "
				+ "ORDER BY ticket.status.id, ticket.id DESC");
		query_getListOfTickets.setParameter("user_name", user_name);

		// listOfTickets
		return (List<Ticket>) query_getListOfTickets.getResultList();
	}
	
	public boolean closeTheTicket(Integer TicketId) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
			
		try {
			/* updating Ticket table */
			Ticket ticket = entityManager.find(Ticket.class, TicketId);
			ticket.setClosed_date( LocalDate.now() );
			
				Query getClosedStatus = entityManager.createQuery(""
						+ "SELECT status "
						+ "FROM Status status "
						+ "WHERE status.code = 'CLSD'");
			ticket.setStatus( (Status) getClosedStatus.getResultList().get(0) ); // closed
			
			/* updating ServiceEngineer table */
			Query getServiceEngineer = entityManager.createQuery(""
					+ "SELECT serviceEngineer  "
					+ "FROM ServiceEngineer serviceEngineer "
					+ "WHERE serviceEngineer.user.id = :user_id");
			
				getServiceEngineer.setParameter("user_id", ticket.getAssigned_to().getId());
			ServiceEngineer serviceEngineer = (ServiceEngineer) getServiceEngineer.getResultList().get(0);
			
			serviceEngineer.setCurrent_high_priority_ticket(null);
			serviceEngineer.setCurrent_ticket_start_date(null);
			serviceEngineer.setPriority(null);
			serviceEngineer.setTotal_tickets_worked_on( serviceEngineer.getTotal_tickets_worked_on() + 1 );

			entityManager.getTransaction().commit();
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	public boolean checkPendingTicket(Integer ticketId) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Ticket ticket = entityManager .find(Ticket.class, ticketId);
		
		Integer id = ticket.getAssigned_to().getId();
		Query getPendingTickets = entityManager.createQuery(""
				+ "SELECT ticket "
				+ "FROM Ticket ticket "
				+ "WHERE ticket.assigned_to.id = :id and "
				+ "		 ticket.status.code = 'PEND' "
				+ "ORDER BY ticket.priority.id DESC, start_date ");
		getPendingTickets.setParameter("id", id);
		
		List<Ticket> listOfPendingTickets = getPendingTickets.getResultList();
		if(listOfPendingTickets != null && listOfPendingTickets.size() >= 1) {
			entityManager.getTransaction().begin();
			
			Ticket pendingTicket = listOfPendingTickets.get(0);
				Query getOnGoingStatus = entityManager.createQuery(""
						+ "SELECT status "
						+ "FROM Status status "
						+ "WHERE status.code = 'ON_GO'");
			pendingTicket.setStatus( (Status) getOnGoingStatus.getResultList().get(0) );
			
			entityManager.getTransaction().commit();
			
			return true;
		}
		return false;
	}
	
	public ArrayList getAverageTimeTakenPerEngineer() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		Query query_getAverageTimeTakenPerEngineer = entityManager.createQuery(""
				+ "SELECT serviceEngineer.user "
				+ "FROM ServiceEngineer serviceEngineer ");
		List<User> listOfServiceEngineer = query_getAverageTimeTakenPerEngineer.getResultList();
		
		ArrayList userNameAndStats = null;
		ArrayList statsOfServiceEngineers = new ArrayList();
		
		for(User user: listOfServiceEngineer) {
			Query getStatsPerEngineer = entityManager.createQuery(""
					+ "SELECT AVG(ticket.closed_date - ticket.start_date) "
					+ "FROM Ticket ticket "
					+ "WHERE ticket.assigned_to.id = :service_engineer_id and"
					+ "		ticket.status.code = 'CLSD'");
			getStatsPerEngineer.setParameter("service_engineer_id", user.getId());
			
			System.out.println( (Double) getStatsPerEngineer.getResultList().get(0) );
			
			userNameAndStats = new ArrayList();
			userNameAndStats.add(user.getName());
			
			Double value = (Double) getStatsPerEngineer.getResultList().get(0);
			if(value != null)
				userNameAndStats.add( value );
			else
				userNameAndStats.add( new String("Not yet resolved single ticket!") );
			
			statsOfServiceEngineers.add( userNameAndStats );
		}
	
		return statsOfServiceEngineers;
	}
	public ArrayList getAverageTimeTakenPerSeverity() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		Query getPriorities = entityManager.createQuery(""
				+ "SELECT priority "
				+ "FROM Priority priority ");
		List<Priority> listOfPriorities = getPriorities.getResultList();
		
		ArrayList AverageTimeTakenPerSeverity = new ArrayList();
		for(Priority priority : listOfPriorities ) {
			Query getStatsPerSeverity = entityManager.createQuery(""
					+ "SELECT AVG(ticket.closed_date - ticket.start_date) "
					+ "FROM Ticket ticket "
					+ "WHERE ticket.priority.id = :priority_id and"
					+ "		ticket.status.code = 'CLSD'");
			
			getStatsPerSeverity.setParameter("priority_id", priority.getId());
			
			ArrayList priorityAndAvg = new ArrayList();
			priorityAndAvg.add(priority.getValue());
			
			List<Double> average = getStatsPerSeverity.getResultList();
			System.out.println("CHECK average -> " + average);
			
			if(average != null && average.get(0) == null){
				priorityAndAvg.add( "No stats to display!" );
			}
			else if(average != null && average.size() != 0)
				priorityAndAvg.add( average.get(0) );
			
			AverageTimeTakenPerSeverity.add(priorityAndAvg);
		}
		
		return AverageTimeTakenPerSeverity;
	}
	public boolean updateTicketPriority(ArrayList<String> updateTicketValues) {
		/* 0 - ticketId, 
		 * 1 - newPriortyValue
					2. update the ServiceEngineer table
					3. update the Ticket table 
		 */
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Ticket ticket = entityManager.find(Ticket.class, Integer.parseInt( updateTicketValues.get(0).strip() ));
		
		System.out.println("--> updateTicketPriority = " + ticket);
		
		String newPriorityValue = (String) updateTicketValues.get(1);
		
		if( (ticket.getPriority().getValue().equals("High") && 
				(newPriorityValue.equals("Medium") || newPriorityValue.equals("Low")) ) ||
				
			(ticket.getPriority().getValue().equals("Medium") &&
					newPriorityValue.equals("Low")) ){
			
			/* update Priority to current Priority */
			Query getPriority = entityManager.createQuery(""
					+ "SELECT priority "
					+ "FROM Priority priority "
					+ "WHERE priority.value = :value");
			getPriority.setParameter("value", newPriorityValue);
		ticket.setPriority( (Priority) getPriority.getResultList().get(0) ); 
		
		entityManager.getTransaction().begin();
		entityManager.getTransaction().commit();

			/*
			 * First Find other high priority ticket and 
			 * if found:
				 assign to this ServiceEngineer
				 then assign this ticket to him/her
			 * 
			 * then, update current Ticket : status = pending  
			 */

			Query getHighPriorityTicket = entityManager.createQuery(""
					+ "SELECT ticket "
					+ "FROM Ticket ticket "
					+ "WHERE ticket.status.value = 'Pending' and "
					+ "		ticket.category.name = :category and "
					+ "		ticket.assigned_to.name = :currentServiceEmployee "
					+ "ORDER BY ticket.priority.id DESC ");
			getHighPriorityTicket.setParameter("category", ticket.getCategory().getName());
			getHighPriorityTicket.setParameter("currentServiceEmployee", ticket.getAssigned_to().getName());
			
			List<Ticket> otherHighPriortyTickets = getHighPriorityTicket.getResultList();
			
			if(otherHighPriortyTickets != null && otherHighPriortyTickets.size() >= 1) {
				System.out.println("We have other HIHG Priority Ticekt !!! ");
				
				Ticket otherHighPriortyTicket = otherHighPriortyTickets.get(0);
				
				/* update current Ticket : status = pending */
				Query getOngingStatus = entityManager.createQuery(""
						+ "SELECT status "
						+ "FROM Status status "
						+ "WHERE status.code = 'ON_GO'");
				otherHighPriortyTicket.setStatus( (Status) getOngingStatus.getResultList().get(0));
				
				
					/* NOW, we're setting the status of curretnTicket as PENDING  */
					Query getStatus = entityManager.createQuery(""
							+ "SELECT status "
							+ "FROM Status status "
							+ "WHERE status.code = 'PEND'");
				ticket.setStatus( (Status) getStatus.getResultList().get(0));
				
				/* updating ServiceEngineer table with otherHighPriorityTicket  */
					User user = ticket.getAssigned_to();
					Query getServiceEngineer = entityManager.createQuery(""
							+ "SELECT serviceEngineer "
							+ "FROM ServiceEngineer serviceEngineer "
							+ "WHERE serviceEngineer.user.id = :user_id");
					getServiceEngineer.setParameter("user_id", user.getId());
				ServiceEngineer serviceEngineer = (ServiceEngineer) getServiceEngineer.getResultList().get(0);
				
				serviceEngineer.setPriority( (Priority) otherHighPriortyTicket.getPriority() );
				serviceEngineer.setCurrent_high_priority_ticket(otherHighPriortyTicket);
				serviceEngineer.setCurrent_ticket_start_date(otherHighPriortyTicket.getStart_date());
				
				entityManager.getTransaction().begin();
				entityManager.getTransaction().commit();
				
				return true;
				
			}else {
				System.out.println("ELSE BLOCK after if(otherHighPriortyTickets != null && otherHighPriortyTickets.size() >= 1) {");
				
				/* update Priority to current Priority */
//				Query getPriority = entityManager.createQuery(""
//						+ "SELECT priority "
//						+ "FROM Priority priority "
//						+ "WHERE priority.value = :value");
//				getPriority.setParameter("value", newPriorityValue);
			ticket.setPriority( (Priority) getPriority.getResultList().get(0) );
			
				/* updating ServiceEngineer table with otherHighPriorityTicket  */
				User user = ticket.getAssigned_to();
				Query getServiceEngineer = entityManager.createQuery(""
						+ "SELECT serviceEngineer "
						+ "FROM ServiceEngineer serviceEngineer "
						+ "WHERE serviceEngineer.user.id = :user_id");
				getServiceEngineer.setParameter("user_id", user.getId());
			ServiceEngineer serviceEngineer = (ServiceEngineer) getServiceEngineer.getResultList().get(0);
			
			serviceEngineer.setPriority( (Priority) ticket.getPriority() );
		
			System.out.println("CHECK");
			
			entityManager.getTransaction().begin();
			entityManager.getTransaction().commit();
				
				return true;
			}
			/* if we did not find any other ticket with the higher priorities
			 * then we may have only one ticket
			 * 
			 *  In that case we will only modify the Priority of the current ticket and 
			 *  he/she should be working on that particular ticket itself */
			
		}
		else {
			/*
			 *  Low -> High | Low -> Medium | Medium -> High 
			 * 
			 *  Just change the Priority of the ticket and
			 *  this respective serviceEngineer
			 */
			
				/* update Priority to current Priority */
				Query getPriority = entityManager.createQuery(""
						+ "SELECT priority "
						+ "FROM Priority priority "
						+ "WHERE priority.value = :value");
				getPriority.setParameter("value", newPriorityValue);
			ticket.setPriority( (Priority) getPriority.getResultList().get(0) );
			
				/* updating ServiceEngineer table with otherHighPriorityTicket  */
				User user = ticket.getAssigned_to();
				Query getServiceEngineer = entityManager.createQuery(""
						+ "SELECT serviceEngineer "
						+ "FROM ServiceEngineer serviceEngineer "
						+ "WHERE serviceEngineer.user.id = :user_id");
				getServiceEngineer.setParameter("user_id", user.getId());
			ServiceEngineer serviceEngineer = (ServiceEngineer) getServiceEngineer.getResultList().get(0);
			
			serviceEngineer.setPriority( (Priority) ticket.getPriority() );
		
			System.out.println("CHECK");
			
			entityManager.getTransaction().begin();
			entityManager.getTransaction().commit();
			
			return true;
		}
	}
	public ArrayList<Ticket> getAgingOfTickets( String user_name ) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		Query getAgingOfTicketsOfAServiceEngineer = entityManager.createQuery(""
				+ "SELECT ticket "
				+ "FROM Ticket ticket "
				+ "WHERE ticket.assigned_to.user_name = :user_name and "
				+ "			ticket.status.code = 'ON_GO' or "
				+ "			ticket.status.code = 'PEND' ");
		getAgingOfTicketsOfAServiceEngineer.setParameter("user_name", user_name);
		
//		ArrayList<ArrayList> agingOfOpenTickets = new ArrayList<ArrayList>();
		
		ArrayList<Ticket> listOfAllOpenTickets = (ArrayList<Ticket>) getAgingOfTicketsOfAServiceEngineer.getResultList();
		
		return listOfAllOpenTickets;

	}
}
