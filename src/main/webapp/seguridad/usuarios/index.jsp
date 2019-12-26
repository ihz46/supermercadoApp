<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/includes/header.jsp"%>
<%@include file="/includes/top-nav.jsp"%>

<a href="seguridad/usuarios?accion=formulario">Nuevo Usuario</a>

<h2>Listado de usuarios</h2>

<table class="tabla table">
	<thead class="thead-dark">
		<tr>
			<th>ID</th>
			<th>Nombre</th>
			<th>Contraseña</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${registros}" var="u">
			<tr>
				<td>${u.id}</td>
				<td>${u.user_name}</td>
				<td>${u.password}</td>
				<td><a href="seguridad/usuarios?accion=formulario&id=${u.id}">Editar</a></td>
			
			</tr>
					
		</c:forEach>
	</tbody>

</table>

<%@include file="/includes/footer.jsp"%>