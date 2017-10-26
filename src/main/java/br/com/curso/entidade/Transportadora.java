/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.entidade;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Miguel
 */
@Entity
@Table(name = "pes_transportadora")
@PrimaryKeyJoinColumn(name = "pes_id")
public class Transportadora extends Fornecedor implements Serializable {

    private static final long serialVersionUID = 1L;
    


    @Override
    public String getDocumentoFederal() {
        return cnpj;
    }

    @Override
    public String getDocumentoEstadual() {
        return ie;
    }

}
