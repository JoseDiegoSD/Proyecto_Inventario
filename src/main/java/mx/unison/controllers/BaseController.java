package mx.unison.controllers;

import mx.unison.models.Usuario;

public abstract class BaseController {
    protected Usuario usuarioActual;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        onUsuarioSet();
    }

    protected void onUsuarioSet() {
        // Para ser sobrescrito por clases hijas si necesitan hacer algo al recibir el usuario
    }
}
