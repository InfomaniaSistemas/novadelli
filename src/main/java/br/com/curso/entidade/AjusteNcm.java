/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Miguel
 */
@Entity
@Table(name = "ajuste_ncm")
public class AjusteNcm implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aju_id")
    private Long id;
    @Column(name = "aju_data")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataAjuste = new Date();

    private String ncm;
    private String descricao;

//    @ManyToOne
//    @JoinColumn(name = "usu_id", nullable = false)
//    private Usuario usuario;
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true,
            mappedBy = "ajusteNcm")
    private List<Produto> itensNcms = new ArrayList<>();

    public void addItem(Produto item) throws Exception {
//        BigDecimal estoqueProd = item.getPlantio().getEstoque();
//        if (item.getQuantidade().compareTo(estoqueProd) <= 0) {
        if (!itensNcms.contains(item)) {
            itensNcms.add(item);
//            item.baixaEstoque();
        } else {
            throw new Exception("O Item já foi adicionado");
        }

//        } else {
//            throw new Exception("Estoque insuficiente");
//        }
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void removeItem(Produto item) throws Exception {
        if (itensNcms.contains(item)) {
            itensNcms.remove(item);
//            item.estornaEstoque();
        } else {
            throw new Exception("O item selecionado não contem na lista de itens");
        }
    }

    public List<Produto> getItensNcms() {
        return itensNcms;
    }

    public void setItensNcms(List<Produto> itensNcms) {
        this.itensNcms = itensNcms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataAjuste() {
        return dataAjuste;
    }

    public void setDataAjuste(Date dataAjuste) {
        this.dataAjuste = dataAjuste;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
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
        if (!(object instanceof AjusteNcm)) {
            return false;
        }
        AjusteNcm other = (AjusteNcm) object;
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
