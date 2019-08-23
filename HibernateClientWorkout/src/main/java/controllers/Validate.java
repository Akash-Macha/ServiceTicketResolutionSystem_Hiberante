package controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import entities.Department;
import entities.Priority;
import entities.Role;
import entities.Ticket;
import entities.User;

/**
 * Servlet implementation class Validate
 */
public class Validate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Validate() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());

		Ticket t;
		/**/
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setHeader("Expires", "0"); // Proxies.
		/**/

		String user_name = request.getParameter("user_name");
		String password = request.getParameter("password");

		HttpSession session = request.getSession();

		/* setting user_name into session */
		session.setAttribute("user_name", user_name);

		User user = new User();
		user.setUser_name(user_name);
		user.setPassword(password);

		/* Client request */
		String apiURL = "http://localhost:8080/HibernateClientWorkout/webapi/res/";
		
		String apiURL_get = apiURL + "validate";
		Client client = ClientBuilder.newBuilder().build();
		WebTarget target = client.target(apiURL_get);
		Response restResponse = target.request().post(Entity.json(user));// Entity.entity(user,
																			// MediaType.APPLICATION_JSON));
		/* validate the user and get the role if valid ! */
		Role role = restResponse.readEntity(Role.class);
		restResponse.close(); // You should close connections!

		System.out.println("response = user = " + role);

		if (role == null) {
			/* In valid user */
			response.sendRedirect("index.jsp?isvalid=false");
		} else if (role != null) {
			/*
			 * get the data required for the RaiseTicket forms and
			 * set them in the session
			 */
			/* getting listOfDepartments */
			String apiURL_getListOfDepartments = apiURL + "get-departments";
			Client client_getListOfDepartments = ClientBuilder.newBuilder().build();
			WebTarget target_getListOfDepartments = client_getListOfDepartments.target(apiURL_getListOfDepartments);
			Response response_getListOfDepartments = target_getListOfDepartments.request().get();
						
			List<Object[]> listOfDepartments = (List<Object[]>) response_getListOfDepartments.readEntity(List.class);
			response_getListOfDepartments.close(); // You should close connections!
			
			session.setAttribute("listOfDepartments", listOfDepartments);
			
			/* getting listOfPriorities */
			String apiURL_getAvailablePriorities = apiURL + "get-priorities";
			Client client_getAvailablePriorities = ClientBuilder.newBuilder().build();
			WebTarget target_getAvailablePriorities = client_getAvailablePriorities.target(apiURL_getAvailablePriorities);
			Response response_getAvailablePriorities = target_getAvailablePriorities.request().get();
			List<Object[]> listOfPriorities = (List<Object[]>) response_getAvailablePriorities.readEntity(List.class);
			response_getAvailablePriorities.close(); // You should close connections!
			
			session.setAttribute("listOfPriorities", listOfPriorities);
			
		
			if(role.getCode().equals("END_U")) {
			/* USE REQUESTDISPATCHER if everything works fine */
			System.out.println("Redirecting to End User.jsp file");

			response.sendRedirect("EndUser.jsp");

			} else if (role != null && role.getCode().equals("SER_ENGG")) {
	
				System.out.println("Service_Engineer");
	
				response.sendRedirect("ServiceEngineer.jsp");
	
			} else if (role != null && role.getCode().equals("ADMN")) {
				System.out.println("Validated : Admin");
				
				response.sendRedirect("Admin.jsp");
				
			} else {
				// this should not get executed !
				System.out.println("this should not get executed !");
	
				response.sendRedirect("index.jsp?isvalid=false");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setHeader("Expires", "0"); // Proxies.

		doGet(request, response);
	}

}
