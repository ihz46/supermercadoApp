<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

 <%@include file="/includes/header.jsp" %>
 <%@include file="/includes/top-nav.jsp" %>

    
     <form action="/seguridad/productos?action=guardar" method="post">
     <div class="row">
     
     	<div class="col-6">
     	
	     	  <div class="form-group">
					    <label for="id">ID</label>
					    <input type="number" class="form-control"placeholder="Introduce el código">
	     	  </div>
     	  
     	</div>
     
     </div>
     <div class="row">
     
     	<div class="col-6">
     	
	     	  <div class="form-group">
					    <label for="nombre">NOMBRE</label>
					    <input type="text" class="form-control"placeholder="Introduce el nombre">
	     	  </div>
     	  
     	</div>
     
     </div>
     <div class="row">
     		<div class="col-6">
     	   		<button type="submit" class="btn btn-primary btn-block">Enviar</button>
     		</div>
     </div>
      
     
     
     </form>
	            

   
 <%@include file="/includes/footer.jsp" %>