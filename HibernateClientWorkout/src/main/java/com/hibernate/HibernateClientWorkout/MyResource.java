package com.hibernate.HibernateClientWorkout;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import entities.Role;
import entities.Ticket;
import entities.User;
import strs_repository.Repository;

/**
 * Root resource (exposed at "res" path)
 */
@Path("res")
public class MyResource {
	
	/* create object for Repository Class
	 * and modularize all the functionalities
	 * and call their respective methods from this Rest
	 * */
	Repository repository = new Repository();
	
	@POST
	@Path("validate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Role validateTheUser(User user) {
		
		return repository.validate(user);
	}
	
	@GET
	@Path("get-departments")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object[]> getDepartments() {
		return repository.getDepartments();
	}
	
	@GET
	@Path("get-priorities")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object[]> getAvailablePriorities() {		
		return repository.getPriorities();
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("insert-ticket")
	public String insertTicketAndUpdateServiceEngineerTable(ArrayList<String> formData) {
	
		if(repository.insertTicket(formData)) {
			return "Inserted";
		}

		return "Not Inserted";
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("get-tickets")
	public List<Ticket> getListOfTickets(String user_name){
		
		return (List<Ticket>) repository.getListOfTickets(user_name);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("close-ticket")
	public String closeTheTicket(Integer TicketId) {
		
		if(repository.closeTheTicket(TicketId)) {
			return "closed";
		}
		return "not closed";
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("check-peding-tickets")
	public String checkPendingTicket(Integer TicketId) {
		
		if(repository.checkPendingTicket(TicketId)) {
			return "found_pending";
		}
		return "not_found_pending";
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("get-avg-time-per-eng")
	public ArrayList getAverageTimeTakenPerEngineer() {
		
		ArrayList statsOfServiceEngineers = repository.getAverageTimeTakenPerEngineer();
	
		return statsOfServiceEngineers;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("get-avg-time-per-severity")
	public ArrayList getAverageTimeTakenPerSeverity() {
		
		ArrayList statsOfServiceEngineers = repository.getAverageTimeTakenPerSeverity();
	
		return statsOfServiceEngineers;
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("updateTicketPriority")
	public String getAverageTimeTakenPerSeverity(ArrayList<String> updateTicketValues) {
		
		if(repository.updateTicketPriority(updateTicketValues)) {
			return "Updated";
		}
		
		return "Not updated";
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("get-age-open-tickets")
	public ArrayList<Ticket> getAgingOfTickets(String user_name) {
		
		ArrayList<Ticket> listOfAllOpenTickets = repository.getAgingOfTickets( user_name ); 
		
		return listOfAllOpenTickets;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String checkGET() {
		return "In MyResource";
	}
}
