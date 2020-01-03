<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@include file="/includes/header.jsp"%>
<%@include file="/includes/top-nav.jsp"%>

<form action="seguridad/usuarios" method="post">

	<input type="hidden" name="accion" value="guardar">
	<div class="row">

		<div class="col-6">

			<div class="form-group">
				<label for="id">ID</label> <input type="number" name="id"
					value="${usuario.id}" class="form-control" readonly >
			</div>

		</div>

	</div>
	<div class="row">
		<div class="col-6">
			<div class="form-group">
				<label for="nombre">NOMBRE</label> <input type="text" name="nombre"
					value="${usuario.nombre }" class="form-control"
					placeholder="Introduce el nombre"  autofocus>
			</div>
		</div>

	</div>
	<div class="row">
			<div class="col-4">
			<div class="form-group">
				<label for="password">CONTRASEÑA</label> <input id="password" type="password" name="password"
					value="${usuario.password}" class="form-control"
					placeholder="Introduce la contraseña" >
			</div>
		
		</div>
		<div class="col-2">
			<div class="form-group">
				<label for="nada"></label>
				<button class="btn btn-primary btn-block" onclick="verTexto()">Ver</button>
			</div>
			
		</div>
		
		
	</div>
	<div class="row mb-2">
		<div class="col-6">
			<button type="submit" class="btn btn-primary btn-block">Enviar</button>
		</div>
	</div>

	<c:if test="${usuario.id > 0}">




		<!-- Button trigger modal -->
		<div class="row">
			<div class="col-6">
				<button type="button" class="btn btn-outline-danger btn-block"
					data-toggle="modal" data-target="#exampleModal">Eliminar</button>
			</div>
		</div>


		<!-- Modal -->
		<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog"
			aria-labelledby="exampleModalLabel" aria-hidden="true">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="exampleModalLabel">¿Estás seguro?</h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">Se va a eliminar el usuario
						${usuario.nombre}.</div>
					<div class="row mt-2"></div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-dismiss="modal">Close</button>
						<a class="btn btn-danger btn-block"
							href="seguridad/usuarios?id=${usuario.id}&accion=eliminar">Eliminar</a>
					</div>
				</div>
			</div>
		</div>


	</c:if>


</form>



<%@include file="/includes/footer.jsp"%>