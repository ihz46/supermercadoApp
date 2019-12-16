<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <%@include file="/includes/header.jsp" %>
 <%@include file="/includes/top-nav.jsp" %>
 
<base href="${request.contextPath}" />
 
 	<h1>TABLA</h1>
	
	<a href="/seguridad/productos?accion=formulario">Nuevo Producto</a>
	
	
	
	<ol>
		<c:forEach items="${productos}" var="p">
			<li>
				${p} <br>
				<a href="?accion=formulario&id=${p.id}">Editar</a>
				<a href="?accion=eliminar&id=${p.id}">Eliminar</a>
			</li>
		</c:forEach>
	</ol>
	
	${productos}
 
 <%@include file="/includes/footer.jsp" %>