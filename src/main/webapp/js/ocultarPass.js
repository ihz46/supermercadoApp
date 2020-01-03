function verTexto() {
    console.trace('funciona el botón');
    document.getElementById("password").addEventListener("click", function(event){
    	event.preventDefault()
    });
    
    let iContrasena = document.getElementById('password');
    if (iContrasena.type == "text") {
        iContrasena.type = "password";
        event.target.innerHTML = 'Ver';
    } else {
        iContrasena.type = "text";
        event.target.innerHTML = 'Ocultar';
    }



}
