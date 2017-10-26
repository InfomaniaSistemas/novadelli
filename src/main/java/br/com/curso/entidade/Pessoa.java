/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Miguel
 */
@Entity
@Table(name = "pessoa")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pes_id")
    private Long id;
    @Column
    private String nome = "".toUpperCase();
    @Column
    private Integer contC = 0;
    @Column
    private Integer contF = 0;

    @Column
    private String fone;
    @Column
    private Boolean validaFuncionario = Boolean.FALSE;
    @Column
    private String foneComercial;
    @Column
    private String celular;
    @Column
    private String cep;
    @Column
    private Boolean completo = Boolean.FALSE;
    @Column
    private BigDecimal limite = BigDecimal.ZERO;
    @Column
    private String endereco = "".toUpperCase();
    @Column
    private String bairro = "".toUpperCase();
    @Column(length = 600)
    private String obs = "".toUpperCase();
    @Column
    private String numero;
    @Column
    private Boolean ativo = true;
    @Column
    private Boolean restricao = false;
    @Column
    private String email;
    @ManyToOne
    @JoinColumn
    private Cidade cidade;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dtCadasrto;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true,
            mappedBy = "pessoa")
    private List<Restricao> restricaos = new ArrayList<>();

    public abstract String getDocumentoFederal();

    public abstract String getRazao();

    public abstract String getDocumentoEstadual();

    public void addItem(Restricao item) throws Exception {

        item.setPessoa(this);
        if (!restricaos.contains(item)) {
            restricaos.add(item);
            restricao = true;
        } else {
            throw new Exception("A restrição já foi adicionada");
        }

    }

    public Boolean getValidaFuncionario() {
        return validaFuncionario;
    }

    public void setValidaFuncionario(Boolean validaFuncionario) {
        this.validaFuncionario = validaFuncionario;
    }

    public String getFoneComercial() {
        return foneComercial;
    }

    public void setFoneComercial(String foneComercial) {
        this.foneComercial = foneComercial;
    }

    public void removeItem(Restricao item) throws Exception {
        if (restricaos.contains(item)) {
            restricaos.remove(item);
        } else {
            throw new Exception("O item selecionado não contem na lista de itens");
        }
    }

    public void maiusculo() {

        nome = nome.toUpperCase();
        bairro = bairro.toUpperCase();
        endereco = endereco.toUpperCase();

    }

    public Boolean getCompleto() {
        return completo;
    }

    public void setCompleto(Boolean completo) {
        this.completo = completo;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getRestricao() {
        return restricao;
    }

    public void setRestricao(Boolean restricao) {
        this.restricao = restricao;
    }

    public List<Restricao> getRestricaos() {
        return restricaos;
    }

    public void setRestricaos(List<Restricao> restricaos) {
        this.restricaos = restricaos;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Date getDtCadasrto() {
        return dtCadasrto;
    }

    public Integer getContC() {
        return contC;
    }

    public void setContC(Integer contC) {
        this.contC = contC;
    }

    public Integer getContF() {
        return contF;
    }

    public void setContF(Integer contF) {
        this.contF = contF;
    }

    public void setDtCadasrto(Date dtCadasrto) {
        this.dtCadasrto = dtCadasrto;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pessoa)) {
            return false;
        }
        Pessoa other = (Pessoa) object;
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
