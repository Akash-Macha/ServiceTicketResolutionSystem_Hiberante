<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.Period" %>

<%@ page import="entities.Department" %>
<%@ page import="entities.Priority" %>
<%@ page import="entities.Ticket" %>



<!DOCTYPE html>
<html>
<head>

<meta charset="ISO-8859-1">

<!-- START: styling for Table [ tickets ] displaying -->
<style>
/* ----START: Table------ */
table {
  font-family: arial, sans-serif;
  border-collapse: collapse;
  width: 90%;
}

td, th {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 8px;
}

tr:nth-child(even) {
  background-color: #dddddd;
}
/*----END: Table --------*/

/*
* Basic styles
*/
body {
  margin: 0;
  font-family: Cambria, Cochin, Georgia, Times, 'Times New Roman', serif;
  font-size: 1.15em;
  line-height: 1.5;
}

a {
  color: var(--c-interactive);
}

h1 {
  margin-top: 0;
}

code {
  font-family: 'Fira Mono', Consolas, 'Liberation Mono', Menlo, Courier, monospace;
}

.wrapper {
  max-width: 600px;
}

/* to remove underline from achor tags */
a, u {
    text-decoration: none;;
}


/*-- START: styling for Button-- */
.button {
  background-color: #4CAF50; /* Green */
  border: none;
  color: white;
  padding: 10px 20px; /* padding: 15px 32px; */
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 2px 1px;
  cursor: pointer;
  -webkit-transition-duration: 0.4s; /* Safari */
  transition-duration: 0.4s;
}

.button2:hover {
  box-shadow: 0 12px 16px 0 rgba(0,0,0,0.24),0 17px 50px 0 rgba(0,0,0,0.19);
}
/*-- END: styling for Button-- */

/* -- START : styling Priorty Drop down */
body {
  font-family: Arial, Helvetica, sans-serif;
}

.navbar {
  overflow: hidden;
  background-color: #333;
}

.navbar a {
  float: left;
  font-size: 16px;
  color: white;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
}

.dropdown {
  float: left;
  overflow: hidden;
}

.dropdown .dropbtn {
  font-size: 14px;  
  border: none;
  outline: none;
  color: white;
  padding: 14px 16px;
  background-color: inherit;
  font-family: inherit;
  margin: 0;
}

.navbar a:hover, .dropdown:hover .dropbtn {
  background-color: red;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: #f9f9f9;
  min-width: 160px;
  box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
  z-index: 1;
}

.dropdown-content a {
  float: none;
  color: black;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
  text-align: left;
}

.dropdown-content a:hover {
  background-color: #ddd;
}

.dropdown:hover .dropdown-content {
  display: block;
}
/* -- END : styling Priorty Drop down */
</style>


<title>Service Ticket Resolution System</title>
</head>
<body>

<%
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
	response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
	response.setHeader("Expires", "0"); // Proxies.
%>
<!-- START:  If person tries to Come back after loggin OUT : redirect him/her to login page -->
<%
	if(session.getAttribute("user_name") == null){
		System.out.println("session.getAttribute(user_name) == null");
		
		response.sendRedirect("index.jsp?warning=UnAuthorizedLogin");
		return;
	}
%>
<!-- END: If person tries to Come back after loggin OUT -->

<center>

<!-- START: DispalyTicketClosedsuccessfully -->
<%
if(request.getParameter("display") != null){
	String operation = request.getParameter("display");
	if(operation.equals("DispalyTicketClosedsuccessfully")){
%>
<h3>Ticket has been closed successfully!</h3>
<%
	}
}
%>
<!-- END: DispalyTicketClosedsuccessfully -->

<h1>Welcome <%= session.getAttribute("user_name") %></h1>

<a href="ServiceEngineerOperations?operation=Show_All_Tickets">Show All Tickets</a><br>


<!-- Statistics :  -->
<a href="ServiceEngineerOperations?operation=Average_Time_Taken_Per_Engineer">Average Time Taken Per Engineer</a> | 
<a href="ServiceEngineerOperations?operation=Average_Time_Taken_Per_Severity">Average Time Taken Per Severity</a><br><br>
<a href="ServiceEngineerOperations?operation=Aging_of_Open_Tickets">Aging of Open Tickets</a><br><br>

<form action="Logout" method="POST">
	<input type="submit" class="button button2" value="Logout">
</form>


<%

if(request.getParameter("operation") != null){
	String operation = request.getParameter("operation");
	
	if(operation.equals("ShowAllTickets")){
%>

<!-- START: Show All Tickets -->

<h2>All Tickets</h2>

<table>
    <!-- Table heading -->
  <tr>
    <th>Ticket Id</th>
    <th>Requested by</th>
    
    <th>Message</th>
    
    <th>Start Date</th>
    <th>Requested End Date</th>
    
    <th>Priority</th>
    <th>Status</th>
    
    <th>Action</th>
  </tr>

<%
		ArrayList<Ticket> listOfTickets = (ArrayList<Ticket>)session.getAttribute("listOfTickets");
        
		/* this condition may not get a chance to execute, since it will be caught in the above user_name == null case itself! */
        if(listOfTickets == null){
            System.out.println("listOfTickets == NULL in ServiceEngineer.jsp -> ShowAllTickets");
            System.out.println("Redirecting to index page");
            
            response.sendRedirect("index.jsp?warning=UnAuthorizedLogin");
            
            return;
        }

        for(int i=0 ; i < listOfTickets.size() ; ++i){
%>

<!-- Table rows -->

    <tr>
    <td> <%= listOfTickets.get(i).getId()  %> </td>
    <td> <%= listOfTickets.get(i).getRequested_by().getName() %> </td>
    
    <td> <%= listOfTickets.get(i).getMessage()  %> </td>
    
    <td> <%= listOfTickets.get(i).getStart_date()  %> </td>
    <td> <%= listOfTickets.get(i).getRequested_end_date()  %> </td>
    
	<td> 
		<%
		if(listOfTickets.get(i).getStatus().getCode().equals("ON_GO")){
			
			%>
			<div class="navbar">
			  <div class="dropdown">
			    <button class="dropbtn"> <%= listOfTickets.get(i).getPriority().getValue() %>
			      <i class="fa fa-caret-down"></i>
			    </button>
			    <div class="dropdown-content">
			    <%
			    List<Object[]> listOfPriorities = (List<Object[]>) session.getAttribute("listOfPriorities");
				for(Object eachObject : listOfPriorities) {
					ArrayList priority = (ArrayList)eachObject;
					if(!priority.get(1).equals( listOfTickets.get(i).getPriority().getValue()) ){
			    		%>
							<a href="ServiceEngineerOperations?operation=updateTicketPriroty&TicketId= <%= listOfTickets.get(i).getId() %>&newPriority=<%= priority.get(1) %>  "> <%= priority.get(1) %> </a>
			    		<%
			    	}else{ 
			    		%>
			    			
			    		<%
			    		
			    	}
			    }
			    %>
			    </div>
			  </div> 
			</div>
			<%
		}else if(listOfTickets.get(i).getStatus().getCode().equals("PEND")){
			%>
			<%= listOfTickets.get(i).getPriority().getValue() %>
			<%
		}
			  
		%> 
	</td>
    <td> <%= listOfTickets.get(i).getStatus().getValue()  %> </td>
    
    <td> <% if(listOfTickets.get(i).getStatus().getCode().equals("ON_GO")){   %>
    			<form action="ServiceEngineerOperations?operation=CloseTicket&TicketId=<%= listOfTickets.get(i).getId() %>" method="POST">
    				  <input type="submit" class="close_button" value="Close Ticket">
    			</form>
    		<% } %>
	</td>
    
  </tr>

<%
		}
%>
</table>

<%
	}
%>
<!-- END: Show All Tickets -->

<!-- START: AverageTimeTakenPerSeverity -->

<%
	if(operation.equals("DisplayAverageTimeTakenPerSeverity")){
		
%>
<h2>Avergae time taken per Severity</h2>
<table>
    <!-- Table heading -->
  <tr>
    <th>Severity</th>
    <th>Average Time Taken</th>
  </tr>
<%
		
		ArrayList averageTimeTakenPerSeverity = (ArrayList)session.getAttribute("Avg_Time_Taken_Per_Severity");
		for(Object listOfserverityAndTime : averageTimeTakenPerSeverity){
			ArrayList severityAndTime = (ArrayList) listOfserverityAndTime;
%>

<!-- Table rows -->

    <tr>
    	<td> <%= severityAndTime.get(0) %></td>
	    <td> <%= severityAndTime.get(1)  %></td>
	</tr>
<%
		}
%>
</table>

<%
	}
%>
<!-- END: AverageTimeTakenPerSeverity -->

<!-- START: AverageTimeTakenPerEngineer -->
<!--  operation=DisplayAverageTimeTakenPerEngineer  -->
<%
	if(operation.equals("DisplayAverageTimeTakenPerEngineer")){
		
		ArrayList<ArrayList> averageTimeTakenPerEngineer = (ArrayList<ArrayList>)session.getAttribute("Average_Time_Taken_Per_Engineer");
		
%>
<h2>Avergae time taken per Engineer</h2>
<table>
    <!-- Table heading -->
  <tr>
    <th>Employee Name</th>
    <th>Average Time Taken</th>
  </tr>

<%
		for(ArrayList userNameAndHisStats : averageTimeTakenPerEngineer){
%>

<!-- Table rows -->
    <tr>
    	<td> <%= userNameAndHisStats.get(0)  %> </td>
	    <td> <%= userNameAndHisStats.get(1)  %> </td>
	</tr>
<%
		}
%>

</table>

<%
	}
%>
<!-- END: AverageTimeTakenPerEngineer -->

<!--  START : Aging of open tickets -->
<%
// operation=DisplayAgingOfOpenTickets
	if(operation.equals("DisplayAgingOfOpenTickets")){
		List<Ticket> agingOfOpenTickets = (List<Ticket>) session.getAttribute("AgingOfOpenTickets");
%>

<h2>Avergae time taken per Engineer</h2>
<table>
    <!-- Table heading -->
  <tr>
    <th>Ticket Id</th>
    <th>Priority</th>
    <th>Start Date</th>
    <th>AGE</th>
    <th>Status</th>
    <th>Message</th>
    <th>Requested by</th>
    <th>Requested End Date</th>
  </tr>

<%
		for(Ticket eachOpenTicket : agingOfOpenTickets){
%>
<%
	/* calculate the AGE  */
	LocalDate dateFrom = eachOpenTicket.getStart_date();  
    LocalDate dateTo = LocalDate.now();

    Period intervalPeriod = Period.between(dateFrom, dateTo);

    System.out.println("Difference of days: " + intervalPeriod.getDays());
    Integer age = intervalPeriod.getDays();
%>
<!-- Table rows -->
    <tr>
    	<td> <%= eachOpenTicket.getId()  %> </td>
    	<td> <%= eachOpenTicket.getPriority().getValue()  %> </td>
    	<td> <%= eachOpenTicket.getStart_date() %> </td>
    	<td> <%= age  %> </td>
    	<td> <%= eachOpenTicket.getStatus().getValue()  %> </td>
    	<td> <%= eachOpenTicket.getMessage()  %> </td>
    	<td> <%= eachOpenTicket.getRequested_by().getUser_name()  %> </td>
    	<td> <%= eachOpenTicket.getRequested_end_date()  %> </td>
	</tr>
<%
		}
%>

</table>

<%
	}
%>
<!--  END : Aging of open tickets -->

<%
}	//  close brase of NULL check
%>

</center>
</body>
</html>