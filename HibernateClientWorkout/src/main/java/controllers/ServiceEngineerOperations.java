package controllers;

import java.io.IOException;
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
 * Servlet implementation class ServiceEngineerOperations
 */
public class ServiceEngineerOperations extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServiceEngineerOperations() {
        super();
        // TODO Auto-generated constructor stub
    }

    final String apiURL = "http://localhost:8080/HibernateClientWorkout/webapi/res/";
    
    private List<Ticket> makeRestCallGetListOfTickets(String user_name){
    	/* Client request */
		String apiURL_InsertTicketAndAssignServiceEngineer = apiURL + "get-tickets";
		Client client = ClientBuilder.newBuilder().build();
		WebTarget target = client.target(apiURL_InsertTicketAndAssignServiceEngineer);
		Response restResponse = target.request().post(Entity.json(user_name));
		/* Insert the ticket into the Ticket table and
		 * update the ServiceEngineer table and modify the required columns */
		
		GenericType<List<Ticket>> genericTypeGetTickets = new GenericType<List<Ticket>>() {};
					
		List<Ticket> listOfTickets = restResponse.readEntity(genericTypeGetTickets);
		
		return listOfTickets;
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		
		String operation = request.getParameter("operation");

		HttpSession session = request.getSession(); 
		String user_name = (String) session.getAttribute("user_name");
			
		
		if(operation.equals("Show_All_Tickets")) {
			List<Ticket> listOfTickets = makeRestCallGetListOfTickets(user_name);

			session.setAttribute("listOfTickets", listOfTickets);
			
			System.out.println("listOfTickets has been added into session");

            /* Redirecting to ServiceEngineer.jsp page to display All the Tickets */
            response.sendRedirect("ServiceEngineer.jsp?operation=ShowAllTickets");
		}
		else if(operation.equals("CloseTicket")) {
			/* get the ticket id, which is to be closed  */
			Integer ticket_id = Integer.parseInt(request.getParameter("TicketId"));
			/*
			 * 1. 
			 */
			
			String apiURL_CloseTicket = apiURL + "close-ticket";
			Client client = ClientBuilder.newBuilder().build();
			WebTarget target = client.target(apiURL_CloseTicket);
			Response response_CloseTicket = target.request().post(Entity.json(ticket_id));
			/* 
			 * Close that ticket and update relevant columns in ServiceEngineer and Ticket table
			 */
			
			String status = response_CloseTicket.readEntity(String.class);
			if(status.equals("closed")) {
				
				/*
				 * Check for pending tickets of this ServiceEmployee
				 * If found any assign that ticket to him/her
				 */
				String apiURL_CheckPending = apiURL + "check-peding-tickets";
				Client client_CheckPending = ClientBuilder.newBuilder().build();
				WebTarget target_CheckPending = client_CheckPending.target(apiURL_CheckPending);
				Response response_CheckPending = target_CheckPending.request().post(Entity.json(ticket_id));
				
				
				if(response_CheckPending.readEntity(String.class).equals("found_pending")) {
					
					System.out.println("pending ticket re-Assigned");
				}else {
					System.out.println("No pending tickets found");
				}

				response.sendRedirect("ServiceEngineer.jsp?display=DispalyTicketClosedsuccessfully&operation=ShowAllTickets");
			}

			
			
			List<Ticket> listOfTickets = makeRestCallGetListOfTickets(user_name);

			session.setAttribute("listOfTickets", listOfTickets);
			
			System.out.println("listOfTickets has been added into session");
			
			System.out.println("In closedTicket part");
			
		}else if(operation.equals("Average_Time_Taken_Per_Engineer")) {
			
			String apiURL_getAverageTimeTakenPerEngineer = apiURL + "get-avg-time-per-eng";
			Client client_getAverageTimeTakenPerEngineer = ClientBuilder.newBuilder().build();
			WebTarget target_getAverageTimeTakenPerEngineer = client_getAverageTimeTakenPerEngineer.target(apiURL_getAverageTimeTakenPerEngineer);
			Response response_getAverageTimeTakenPerEngineer = target_getAverageTimeTakenPerEngineer.request().get();
			
			GenericType<ArrayList> genericTypeGetTickets = new GenericType<ArrayList>() {};
			ArrayList averageTimeTakenPerEngineer = (ArrayList)response_getAverageTimeTakenPerEngineer.readEntity(genericTypeGetTickets);

			System.out.println("In servlet = " + averageTimeTakenPerEngineer);
			/* setting the  averageTimeTakenPerEngineer  into session*/
			session.setAttribute("Average_Time_Taken_Per_Engineer", averageTimeTakenPerEngineer);
			
			response.sendRedirect("ServiceEngineer.jsp?operation=DisplayAverageTimeTakenPerEngineer");
			
			return;
			
		}else if(operation.equals("Average_Time_Taken_Per_Severity")) {
			
			String apiURL_getAverageTimeTakenPerServerity = apiURL + "get-avg-time-per-severity";
			Client client_getAverageTimeTakenPerServerity= ClientBuilder.newBuilder().build();
			WebTarget target_getAverageTimeTakenPerServerity = client_getAverageTimeTakenPerServerity.target(apiURL_getAverageTimeTakenPerServerity);
			Response response_getAverageTimeTakenPerServerity = target_getAverageTimeTakenPerServerity.request().get();
			
			GenericType<ArrayList> genericTypeAvgerageTimeTakenPerServerity = new GenericType<ArrayList>() {};
			ArrayList averageTimeTakenPerSeverity = (ArrayList)response_getAverageTimeTakenPerServerity.readEntity(genericTypeAvgerageTimeTakenPerServerity);

			System.out.println("In servlet = " + averageTimeTakenPerSeverity);
			
			System.out.println("setting averageTimeTakenPerSeverity into session");
			session.setAttribute("Avg_Time_Taken_Per_Severity", averageTimeTakenPerSeverity);
			
			response.sendRedirect("ServiceEngineer.jsp?operation=DisplayAverageTimeTakenPerSeverity");
			
			return;
			
		}else if(operation.equals("Aging_of_Open_Tickets")) {
			
			
			String apiURL_getAgingOfOpenTicket = apiURL + "get-age-open-tickets";
			Client client_getAgingOfOpenTicket= ClientBuilder.newBuilder().build();
			WebTarget target_getAgingOfOpenTicket = client_getAgingOfOpenTicket.target(apiURL_getAgingOfOpenTicket);
			Response response_getAgingOfOpenTicket= target_getAgingOfOpenTicket.request().post(Entity.json( user_name ));
			
			GenericType<List<Ticket>> genericTypeAgingOfOpenTickets = new GenericType<List<Ticket>>() {};
			
			List<Ticket> agingOfOpenTickets = (List<Ticket>)response_getAgingOfOpenTicket.readEntity(genericTypeAgingOfOpenTickets);

			
			System.out.println("In servlet = " + agingOfOpenTickets);
			
			System.out.println("Setting AgingOfOpenTickets into session");
			session.setAttribute("AgingOfOpenTickets", agingOfOpenTickets);
			
			response.sendRedirect("ServiceEngineer.jsp?operation=DisplayAgingOfOpenTickets");
			
			return;
			
			
		}else if(operation.equals("updateTicketPriroty")) {
			/*
			 * 1. get the ticket id
			 * 2. update the ServiceEngineer table
			 * 3. update the Ticket table 
			 */
			String ticket_id = request.getParameter("TicketId");
			String newPriortyValue = request.getParameter("newPriority");
			
			ArrayList<String> updateTicketValues = new ArrayList<>();
			updateTicketValues.add(ticket_id);
			updateTicketValues.add(newPriortyValue);
			
			
			String apiURL_getAverageTimeTakenPerServerity = apiURL + "updateTicketPriority";
			Client client_getAverageTimeTakenPerServerity= ClientBuilder.newBuilder().build();
			WebTarget target_getAverageTimeTakenPerServerity = client_getAverageTimeTakenPerServerity.target(apiURL_getAverageTimeTakenPerServerity);
			Response response_getAverageTimeTakenPerServerity = target_getAverageTimeTakenPerServerity.request().put( Entity.json(updateTicketValues) );

			String status = response_getAverageTimeTakenPerServerity.readEntity(String.class);
			if(status.equals("Updated")) {
				
				/* update the lists and display */
				List<Ticket> listOfTickets = makeRestCallGetListOfTickets(user_name);
				session.setAttribute("listOfTickets", listOfTickets);
				
				response.sendRedirect("ServiceEngineer.jsp?operation=ShowAllTickets");
				
			}else {
				System.out.println("ERROR UPDATING THE PRIRITY !!!");
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
