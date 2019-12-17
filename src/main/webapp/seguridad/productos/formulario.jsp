<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@include file="/includes/header.jsp"%>
<%@include file="/includes/top-nav.jsp"%>


<form action="seguridad/productos" method="post">

	<input type="hidden" name="accion" value="guardar">
	<div class="row">

		<div class="col-6">

			<div class="form-group">
				<label for="id">ID</label> <input type="number" name="id"
					value="${producto.id}" class="form-control" readonly required>
			</div>

		</div>

	</div>
	<div class="row">
		<div class="col-6">
			<div class="form-group">
				<label for="nombre">NOMBRE</label> <input type="text" name="nombre"
					value="${producto.nombre }" class="form-control"
					placeholder="Introduce el nombre" required autofocus>
			</div>
		</div>

	</div>
	<div class="row">
		<div class="col-6">
			<div class="form-group">
				<label for="imagen">IMAGEN</label> <input type="text" name="imagen"
					value="${producto.imagen }" class="form-control"
					placeholder="Introduce la URL de la imagen" required>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-6">
			<label for="precio">PRECIO</label>
			<div class="input-group ">

				<input type="text" name="precio" value="${producto.precio}"class="form-control"
					placeholder="Precio en €" aria-describedby="basic-addon2">
				<div class="input-group-append">
					<span class="input-group-text" id="basic-addon2">€</span>
				</div>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-6">
			<div class="form-group">
				<label for="descripcion">DESCRIPCIÓN</label> <input type="text"
					name="descripcion" value="${producto.descripcion } "
					class="form-control" placeholder="Introduce la descripción"
					required>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-6">
			<div class="form-group">
				<label for="descuento">DESCUENTO</label> <input type="number"
					name="descuento" min="0" max="100" value="${producto.descuento}"
					class="form-control" placeholder="Introduce el descuento (0-100)"
					required>
			</div>
		</div>
	</div>
	<div class="row mb-2">
		<div class="col-6">
			<button type="submit" class="btn btn-primary btn-block">Enviar</button>
		</div>
	</div>
	<c:if test="${producto.id !=0 }">
	<div class="row mt-2 mb-5">
		<div class="col-6">
			<a href="seguridad/productos?accion=eliminar&id=${producto.id}" class="btn btn-danger btn-block">Eliminar</a>
		</div>
	</div>
	</c:if>
</form>



<%@include file="/includes/footer.jsp"%>