
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="Ander Uraga">
    <title>Supermercado</title>

   <!-- Bootstrap core CSS -->
   <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

   <!-- nuestro css -->
   <link rel="stylesheet" href="css/custom.css">

  </head>
  <body id="top">
    <nav class="site-header sticky-top py-1">
        <div class="container d-flex flex-column flex-md-row justify-content-between">
            <a class="py-2" href="index.html">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" class="d-block mx-auto" role="img" viewBox="0 0 24 24" focusable="false"><title>Product</title><circle cx="12" cy="12" r="10"/><path d="M14.31 8l5.74 9.94M9.69 8h11.48M7.38 12l5.74-9.94M9.69 16L3.95 6.06M14.31 16H2.83m13.79-4l-5.74 9.94"/></svg>
            </a>
            <a class="py-2 d-none d-md-inline-block" href="login.html">Login</a>
            <a class="py-2 d-none d-md-inline-block" href="tabla.html">Tabla</a>
            <a class="py-2 d-none d-md-inline-block" href="formulario.html">Formulario</a>            
           
        </div>
    </nav>

    <main class="container pt-4">
        <div class="row justify-content-center">
            <div class="col-6">

                    <form action="URL_CONTROLADOR" method="post">

                        <div class="form-group">
                            <label for="nombre">Producto</label>
                            <input type="text" 
                                   class="form-control" 
                                   id="nombre" 
                                   required
                                   placeholder="Mínimo 2 Máximo 150"
                                   pattern=".{2,150}"
                                   aria-describedby="nombreHelp">
                            <small id="nombreHelp" class="form-text text-muted">Nombre y descripcion del producto</small>
                        </div>

                        
                        <div class="form-group">
                                <label for="precio">Precio</label>
                                <input type="number" 
                                       class="form-control" 
                                       id="precio" 
                                       required
                                       placeholder="0,00€"
                                       pattern=".{2,150}"
                                       aria-describedby="precioHelp">
                                <small id="precioHelp" class="form-text text-muted">Precio en euros sin Iva, ni descuento</small>
                            </div>
    

                        <button type="submit" class="btn btn-block btn-outline-primary">Crear</button>
                    </form>

            </div>
        </div>  
        <a id="btn-top" href="#top" class="btn btn-primary">top</a>
    </main>    

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

    </body>

</html>
