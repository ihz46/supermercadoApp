<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <%@include file="includes/header.jsp" %>
 <%@include file="includes/top-nav.jsp" %>
 
 
 <div class="row justify-content-center">
 
 	<div class="col-4 mt-5">
 	
 
	 <form action="login" method="post">
	 
	 	 <div class="form-group  text-center">
		    <label for="nombre">Nombre:</label>
		    <input type="text" class="form-control" name="nombre" id="nombre" value="admin" required autofocus>
    	  </div>
    	   <div class="form-group text-center">
		    <label for="password">Contraseña:</label>
		    <input type="password" class="form-control" name="password" value="123456" id="password" required>
    	  </div>
    	  <button type="submit" class="btn btn-primary btn-block">Entrar</button>
	 
	 
	 </form>
 	
 	</div>
 	
 </div>
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 <%@include file="includes/footer.jsp" %>