<%-- 
    Document   : login
    Created on : 6 Feb, 2018, 8:35:30 PM
    Author     : caoyu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>
        <h1>Hello please log in!</h1>
        form action="loginServlet" method="post">
		Enter username : <input type="text" name="userId" required=""> <br />
		Enter password : <input type="password" name="password" required=""> <br />
		<input type="submit" />
	</form> <br />
    </body>
</html>
