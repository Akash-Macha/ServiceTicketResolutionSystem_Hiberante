package controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import entities.Ticket;
import entities.User;

/**
 * Servlet implementation class AdminOperations
 */
public class AdminOperations extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminOperations() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void getAllUsersAndSetThemInSession(HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	final String apiURL = "http://localhost:8080/HibernateClientWorkout/webapi/admin/";
    	
    	String apiURL_InsertTicketAndAssignServiceEngineer = apiURL + "get-all-users";
		Client client = ClientBuilder.newBuilder().build();
		WebTarget target = client.target(apiURL_InsertTicketAndAssignServiceEngineer);
		Response restResponse = target.request().get();
		/* Insert the ticket into the Ticket table and
		 * update the ServiceEngineer table and modify the required columns */
		
		GenericType<List<User>> genericTypeGetTickets = new GenericType<List<User>>() {};
					
		List<User> listOfUsers = restResponse.readEntity(genericTypeGetTickets);

		session.setAttribute("listOfUsers", listOfUsers);
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("HITT IN ADMINOPERATIONS");
		
		final String apiURL = "http://localhost:8080/HibernateClientWorkout/webapi/admin/";
		
		String operation = request.getParameter("operation");
		
		HttpSession session = request.getSession();
//		String user_name = (String) session.getAttribute("user_name");
		
		if(operation.equals("Show_All_User")) {
			System.out.println("Inside: if(operation.equals(\"Show_All_User\")) {");
			/*
			 * 1. make client request
			 * 2. store the object in the session and display it 
			 */
			
			getAllUsersAndSetThemInSession(request);
			
			response.sendRedirect("Admin.jsp?operation=Show_All_User");
			return;
		}else if(operation.equals("Add_User")) {
			
			User user = new User();
			user.setName( request.getParameter("name") );
			user.setPassword(  request.getParameter("password") );
			user.setUser_name( request.getParameter("user_name") );
			
			/* validate the user_name and password */
			
			String apiURL_AddUser = apiURL + "add-user";
			Client client_AddUser = ClientBuilder.newBuilder().build();
			WebTarget target_AddUser = client_AddUser.target(apiURL_AddUser);
			Response response_AddUser = target_AddUser.request().post(Entity.json(user));
			
			String status = response_AddUser.readEntity(String.class);
			if(status.equals("success")) {
				
				getAllUsersAndSetThemInSession(request);
				
				response.sendRedirect("Admin.jsp?operation=Show_All_User");
			}else {
				System.out.println("ERROR unable to add a new user from adim");
			}
		}else if(operation.equals("DeleteUser")) {
			System.out.println("Inside: }else if(operation.equals(\"DeleteUser\")) {");
			Integer id = Integer.parseInt( request.getParameter("UserId".trim()) );
			
			String apiURL_AddUser = apiURL + "delete-user";
			Client client_AddUser = ClientBuilder.newBuilder().build();
			WebTarget target_AddUser = client_AddUser.target(apiURL_AddUser);
			Response response_AddUser = target_AddUser.request().put(Entity.json(id));
			
			String status = response_AddUser.readEntity(String.class);
			
			if(status.equals("success")) {
				
				getAllUsersAndSetThemInSession(request);
				
				response.sendRedirect("Admin.jsp?operation=Show_All_User");
			}else {
				System.out.println("ERROR unable to DELETE a user from Admin");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
