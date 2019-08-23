package controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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

/**
 * Servlet implementation class EndUserOperations
 */
public class EndUserOperations extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EndUserOperations() {
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

		/**/
		response.setHeader("Cache-Control", "no-cache,  no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setHeader("Expires", "0"); // Proxies.
		/**/

		String operation = request.getParameter("operation");

		if (operation.equals("RaiseTicket")) {

			HttpSession session = request.getSession();
			String requested_by_user_name = (String) session.getAttribute("user_name");

			/* datat from the form */
			String issue_category = request.getParameter("IssueCategory"); // Department Code
			String message = request.getParameter("message");
			String priority = request.getParameter("priority"); // Priorty Code
			String start_date = request.getParameter("start_date");
			String requested_end_date = request.getParameter("requested_end_date");

			ArrayList<String> formData = new ArrayList<>();

			formData.add(requested_by_user_name);// 0
			formData.add(issue_category);// 1
			formData.add(message);// 2
			formData.add(priority);// 3

			formData.add(start_date);// 4
			formData.add(requested_end_date);// 5

			/* Client request */
			String apiURL_InsertTicketAndAssignServiceEngineer = "http://localhost:8080/HibernateClientWorkout/webapi/res/insert-ticket";
			Client client = ClientBuilder.newBuilder().build();
			WebTarget target = client.target(apiURL_InsertTicketAndAssignServiceEngineer);
			Response restResponse = target.request().post(Entity.json(formData));
			/*
			 * Insert the ticket into the Ticket table and update the ServiceEngineer table
			 * and modify the required columns
			 */

			String status = restResponse.readEntity(String.class);
			restResponse.close(); // You should close connections!

			if (status.equals("Inserted")) {
				System.out.println("Inserted successfully and Assigned to Service Engineer!");

				response.sendRedirect("EndUser.jsp?operation=TicketGenerated");
			} else {
				System.out.println("FAILED TO INSERT");
			}

		} else if (operation.equals("ShowAllTickets")) {
			HttpSession session = request.getSession();
			String user_name = (String) session.getAttribute("user_name");

			// GET
			/* Client request */
			String apiURL_InsertTicketAndAssignServiceEngineer = "http://localhost:8080/HibernateClientWorkout/webapi/res/get-tickets";
			Client client = ClientBuilder.newBuilder().build();
			WebTarget target = client.target(apiURL_InsertTicketAndAssignServiceEngineer);
			Response restResponse = target.request().post(Entity.json(user_name));
			/*
			 * Insert the ticket into the Ticket table and update the ServiceEngineer table
			 * and modify the required columns
			 */

			GenericType<List<Ticket>> genericTypeGetTickets = new GenericType<List<Ticket>>() {
			};

			List<Ticket> listOfTickets = restResponse.readEntity(genericTypeGetTickets);

			/* setting */
			session.setAttribute("listOfTickets", listOfTickets);
			System.out.println("listOfTickets has been added into session");

			/* Redirecting to EndUser.jsp page to display All the Tickets */
			response.sendRedirect("EndUser.jsp?operation=ShowAllTickets");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/* */
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setHeader("Expires", "0"); // Proxies.
		/* */

		doGet(request, response);
	}

}
