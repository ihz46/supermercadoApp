<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/includes/header.jsp"%>
<%@include file="/includes/top-nav.jsp"%>

<a href="seguridad/categorias?accion=formulario">Nueva categoría</a>

<h2>Listado de categorías</h2>
${listaCategorias }
<table class="tabla table">
	<thead class="thead-dark">
		<tr>
			<th>ID</th>
			<th>Nombre</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${listaCategorias}" var="c">
			<tr>
				<td>${c.id}</td>
				<td>${c.nombre}</td>
				<td><a href="seguridad/categorias?accion=formulario&id=${c.id}">Editar</a></td>

			</tr>

		</c:forEach>
	</tbody>

</table>

<%@include file="/includes/footer.jsp"%>