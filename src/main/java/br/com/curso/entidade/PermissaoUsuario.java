/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.entidade;

/**
 *
 * @author Miguel
 */
public enum PermissaoUsuario {

    ADMIN("Administrador do sistema"),
    USER("Usuário comum do sistema");

    private String descricao;

    private PermissaoUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
