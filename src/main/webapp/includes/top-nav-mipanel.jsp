
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<body id="top">
	<nav class="site-header sticky-top py-1">
		<div class="container d-flex flex-column flex-md-row justify-content-between">
			<a class="py-2" href="inicio"> 
				<i class="fas fa-home fa-2x"></i>
			</a>

			<c:if test="${empty usuarioLogueado }">
				<a class="py-2 d-none d-md-inline-block" href="login.jsp">Login</a>
			</c:if>

			<c:if test="${not empty usuarioLogueado }">
				<div class="dropdown show">
					<a class="btn btn-lg dropdown-toggle" href="#" role="button"
						id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false"><i class="fas fa-tag fa-sm"></i> Producto </a>

					<div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
						<a class="dropdown-item" href="mipanel/productos?accion=listar"><i class="fas fa-list fa-sm mr-2"></i>Todos</a> 
						<a	class="dropdown-item" href="mipanel/productos?accion=formulario"><i class="far fa-plus-square mr-2"></i>Nuevo</a> 
					</div>
				</div>
							
				<a class="py-2 d-none d-md-inline-block" href="logout">Cerrar
					Sesión</a>
				<a class="py-2 d-none d-md-inline-block ">Hola ${usuarioLogueado.nombre}</a>
			</c:if>
		</div>
	</nav>

	<main class="container"> <c:if
		test="${not empty mensajeAlerta }">

		<div
			class="alert alert-${mensajeAlerta.tipo } alert-dismissible fade show mt-3"
			role="alert">
			<strong>${mensajeAlerta.texto}</strong>
			<button type="button" class="close" data-dismiss="alert"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
		</div>

	</c:if>