/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Miguel
 */
@Entity
@Table(name = "impressao")
public class Impressao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aju_id")
    private Long id;
//    @ManyToOne
//    @JoinColumn(name = "usu_id", nullable = false)
//    private Usuario usuario;
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true,
            mappedBy = "impressao")
    private List<ItemImpressao> itensImpressaos = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true,
            mappedBy = "impressao")
    private List<ItemImpressaoAux> itensImpressaosAux = new ArrayList<>();

    public void addItem(ItemImpressao item) throws Exception {

        if (item.getPlantio() == null) {
            throw new Exception("É necessário selecionar um item");
        }
        if (item.getQuantidade() == null) {
            throw new Exception("É necessário informar uma quantidade");

        }
        item.setImpressao(this);
        itensImpressaos.add(item);

    }

    public void addItemAux(ItemImpressaoAux item) throws Exception {

        if (item.getPlantio() == null) {
            throw new Exception("É necessário selecionar um item");
        }
        if (item.getQuantidade() == null) {
            throw new Exception("É necessário informar uma quantidade");

        }

        item.setImpressao(this);
        itensImpressaosAux.add(item);

    }

    public void removeItem(ItemImpressao item) throws Exception {
        if (itensImpressaos.contains(item)) {
            itensImpressaos.remove(item);
//            item.estornaEstoque();
        } else {
            throw new Exception("O item selecionado não contem na lista de itens");
        }
    }

    public void removeItemAux(ItemImpressaoAux item) throws Exception {
        if (itensImpressaosAux.contains(item)) {
            itensImpressaosAux.remove(item);
//            item.estornaEstoque();
        } else {
            throw new Exception("O item selecionado não contem na lista de itens");
        }
    }

    public List<ItemImpressaoAux> getItensImpressaosAux() {
        return itensImpressaosAux;
    }

    public void setItensImpressaosAux(List<ItemImpressaoAux> itensImpressaosAux) {
        this.itensImpressaosAux = itensImpressaosAux;
    }

    public List<ItemImpressao> getItensImpressaos() {
        return itensImpressaos;
    }

    public void setItensImpressaos(List<ItemImpressao> itensImpressaos) {
        this.itensImpressaos = itensImpressaos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//
//    public Usuario getUsuario() {
//        return usuario;
//    }
//
//    public void setUsuario(Usuario usuario) {
//        this.usuario = usuario;
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Impressao)) {
            return false;
        }
        Impressao other = (Impressao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id.toString();
    }

}
