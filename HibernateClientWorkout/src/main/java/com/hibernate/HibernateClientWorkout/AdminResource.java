package com.hibernate.HibernateClientWorkout;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import entities.User;
import strs_repository.AdminRepo;

/**
 * Root resource (exposed at "res" path)
 */
@Path("admin")
public class AdminResource {
	AdminRepo adminRepo = new AdminRepo();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("get-all-users")
	public ArrayList<User> getAllUsers() {
		
		ArrayList<User> listOfAllUsers = (ArrayList<User>) adminRepo.getlistOfAllUsers(); 
		
		return listOfAllUsers;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("add-user")
	public String addUser(User user) {	
		
		if(adminRepo.addUser(user)) {
			return "success";
		}
		
		return "Not success";
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("delete-user")
	public String deleteUser(Integer id) {	
		
		if(adminRepo.deleteUser(id)) {
			return "success";
		}
		
		return "Not success";
	}

}
