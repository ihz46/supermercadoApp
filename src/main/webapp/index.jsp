<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <%@include file="includes/header.jsp" %>
 <%@include file="includes/top-nav.jsp" %>
    
        <div class="row contenedor-productos">
			<c:forEach items="${productos}" var="producto">
            
	            <div class="col">
	
	                <!-- producto -->
	                <div class="producto">
	                    <span class="descuento">${producto.descuento } %</span>
	                    <img  src="${producto.imagen}" alt="imagen de ${producto.nombre }">
	
	                    <div class="body">
	                        <p>
	                            <span class="precio-descuento">
	                             <fmt:formatNumber type="currency" minFractionDigits="2" maxFractionDigits="2" currencySymbol="€" value="${producto.precio }"></fmt:formatNumber>
	                                                       
	                            </span>
	                            <span class="precio-original"> 
	                            <fmt:formatNumber type="currency" minFractionDigits="2" maxFractionDigits="2" currencySymbol="€" value="${producto.precioDescuento }"></fmt:formatNumber>
	                                             
	                            </span>
	                        </p>
	                        <p class="text-muted precio-unidad">(18,50€ / litro)</p>
	                        <p class="nombre">${producto.nombre }
	                        <p class="descripcion">${producto.descripcion}</p>
	                    </div>
	
	                    <div class="botones">
	                        <button class="minus">-</button>
	                        <input type="text" value="1">
	                        <button class="plus">+</button>
	                    </div>
	
	                    <button class="carro">Añadir al carro</button>
	
	                </div>
	                <!-- /.producto -->

            

       		 	</div>
       			 <!--  /.div class="col">-->
	
        	</c:forEach>
      	</div>

   
 <%@include file="includes/footer.jsp" %>