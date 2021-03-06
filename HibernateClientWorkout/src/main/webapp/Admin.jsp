<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="entities.User" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Service Ticket Management System</title>
	<style type="text/css">
	*{
	text-align:center
	}

.alignColumns {
  /* Standard syntax */
  column-count: 1;
  column-gap: 40px;
}
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
	</style>
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

<h1>Welcome <%= session.getAttribute("user_name") %></h1>

<div class="alignColumns">
<p>Users</p>
<a href="AdminOperations?operation=Show_All_User">Show All Users</a><br>
<a href="Admin.jsp?operation=Add_user">Add a User</a><br>

</div>

<form action="Logout" method="POST">
	<input type="submit" class="button button2" value="Logout">
</form>



<%

if(request.getParameter("operation") != null){
	String operation = request.getParameter("operation");
	if(operation.equals("Show_All_User")){
%>

<h3>All Users:</h3><br>

	<table>
	<!-- Table heading -->
  <tr>
    <th>User Id</th>
    <th>Name</th>
    <th>User Name</th>
    <th>Password</th>
    <th>Role</th>
    <th>Action</th>
  </tr>
  <%
	   		ArrayList<User> listOfUsers = (ArrayList<User>) session.getAttribute("listOfUsers");
			System.out.println("--> all users : " + listOfUsers);
  			for(User user : listOfUsers){
  %>
   <tr>
    <td> <%= user.getId()  %> </td>
    <td> <%= user.getName() %> </td>
    <td> <%= user.getUser_name() %> </td>
    <td> <%= user.getPassword() %> </td>
    <td> <%= user.getRole().getName() %> </td>
    <td>
        <form action="AdminOperations?operation=DeleteUser&UserId=<%= user.getId() %>" method="POST">
    		<input type="submit" class="close_button" value="Delete User">
		</form>
    </td>
  </tr>
  
			<%
  			}
		}else if(operation.equals("Add_user")){
			%>
			<br>
			<br>
			<form action="AdminOperations?operation=Add_User" method="POST">
				Name: <input type="text" name="name" ><br>
				User Name: <input type="text" name="user_name" ><br>
				Password: <input type="password" name="password"><br>
				<input type="submit" value="submit">
			</form>
			
			<%
		}
	}
%>


</body>
</html>