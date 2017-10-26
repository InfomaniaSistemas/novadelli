/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.ImpressaoFacade;
import br.com.curso.ejb.ItemImpressaoFacade;
import br.com.curso.entidade.Impressao;
import br.com.curso.entidade.ItemImpressao;
import br.com.curso.entidade.ItemImpressaoAux;
import br.com.curso.entidade.Produto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Miguel
 */
@ManagedBean
@SessionScoped
public class ImpressaoControle implements Serializable {

    private Impressao impressao;
    private ItemImpressao itemImpressao;
    private ItemImpressaoAux itemImpressaoAux;
    private RelatorioControle relatorioControle;
    @EJB
    private ImpressaoFacade impressaoFacade;
    @EJB
    private ItemImpressaoFacade itemImpressaoFacade;
    private GenericConverter converter;
    private Long id2 = Long.MAX_VALUE;
    private String teste = "nao";
    private String auxImpressao;
    private Integer cont = 0;
//    @Inject
//    private LoginControle loginControle;

    public Integer getCont() {
        return cont;
    }

    public void setCont(Integer cont) {
        this.cont = cont;
    }

    public String getTeste() {
        return teste;
    }

    public ItemImpressaoFacade getItemImpressaoFacade() {
        return itemImpressaoFacade;
    }

    public void setItemImpressaoFacade(ItemImpressaoFacade itemImpressaoFacade) {
        this.itemImpressaoFacade = itemImpressaoFacade;
    }

    public String getAuxImpressao() {
        return auxImpressao;
    }

    public void setAuxImpressao(String auxImpressao) {
        this.auxImpressao = auxImpressao;
    }

    public void setTeste(String teste) {
        this.teste = teste;
    }

    public RelatorioControle getRelatorioControle() {
        return relatorioControle;
    }

    public void setRelatorioControle(RelatorioControle relatorioControle) {
        this.relatorioControle = relatorioControle;
    }

    public ImpressaoFacade getImpressaoFacade() {
        return impressaoFacade;
    }

    public void setImpressaoFacade(ImpressaoFacade impressaoFacade) {
        this.impressaoFacade = impressaoFacade;
    }

    public GenericConverter getConverter() {
        return converter;
    }

    public void setConverter(GenericConverter converter) {
        this.converter = converter;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public ImpressaoControle() {
        impressao = new Impressao();
        itemImpressao = new ItemImpressao();
        itemImpressaoAux = new ItemImpressaoAux();
    }

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(impressaoFacade);
        }
        return converter;
    }

    public List<Impressao> autoComplete(String query) {
        return impressaoFacade.autoComplete("nome", query);
    }

    public String voltar() {
        cont = 0;
        teste = "nao";
        impressao = new Impressao();
        itemImpressao = new ItemImpressao();
        itemImpressaoAux = new ItemImpressaoAux();

        return "/impressao/form?faces-redirect=true";
    }
    private Integer qtd;

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    private Long idAux;

    public Long getIdAux() {
        return idAux;
    }

    public void setIdAux(Long idAux) {
        this.idAux = idAux;
    }

    private Produto produto;
    private Integer qtd5;

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQtd5() {
        return qtd5;
    }

    public void setQtd5(Integer qtd5) {
        this.qtd5 = qtd5;
    }

    public void addItem() {

        try {

            if (produto.getId() == 0) {
                cont = qtd5 + cont;
                itemImpressaoAux.setImpressao(impressao);
                itemImpressaoAux.setPlantio(produto);
                itemImpressaoAux.getPlantio().setNomeCodBarras("");
                itemImpressaoAux.setQuantidade(qtd5);
                itemImpressaoAux.setDescricao("");
                impressao.addItemAux(itemImpressaoAux);
                itemImpressaoAux = new ItemImpressaoAux();

                for (int i = 0; i < qtd5; i++) {
                    itemImpressao.setImpressao(impressao);
                    itemImpressao.setPlantio(produto);
                    itemImpressao.setPlantio(produto);
                    itemImpressao.getPlantio().setNomeCodBarras("");
                    itemImpressao.setQuantidade(qtd5);
                    itemImpressao.setDescricao("");
                    impressao.addItem(itemImpressao);
                    itemImpressao = new ItemImpressao();
                }

                produto = null;
                qtd5 = 1;
                System.out.println("Tamanho->" + impressao.getItensImpressaos().size());

            } else {
                cont = qtd5 + cont;
                itemImpressaoAux.setImpressao(impressao);
                itemImpressaoAux.setPlantio(produto);
                itemImpressaoAux.setPlantio(produto);
                itemImpressaoAux.getPlantio().setNomeCodBarras(produto.getNome());
                itemImpressaoAux.setDescricao("Moreira");
                itemImpressaoAux.setQuantidade(qtd5);
                impressao.addItemAux(itemImpressaoAux);
                itemImpressaoAux = new ItemImpressaoAux();

                for (int i = 0; i < qtd5; i++) {
                    itemImpressao.setImpressao(impressao);
                    itemImpressao.setPlantio(produto);
                    itemImpressao.setPlantio(produto);
                    itemImpressao.getPlantio().setNomeCodBarras(produto.getNome());
                    itemImpressao.setDescricao("Moreira");
                    itemImpressao.setQuantidade(qtd5);
                    impressao.addItem(itemImpressao);
                    itemImpressao = new ItemImpressao();
                }
                produto = null;
                qtd5 = 1;
                System.out.println("Tamanho->" + impressao.getItensImpressaos().size());
            }

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "Não foi possível adicionar!", ""));
        }
    }

    public ItemImpressaoAux getItemImpressaoAux() {
        return itemImpressaoAux;
    }

    public void setItemImpressaoAux(ItemImpressaoAux itemImpressaoAux) {
        this.itemImpressaoAux = itemImpressaoAux;
    }

    public ItemImpressao getItemImpressao() {
        return itemImpressao;
    }

    public void setItemImpressao(ItemImpressao itemImpressao) {
        this.itemImpressao = itemImpressao;
    }

    public void removeItem() {
        try {
            cont = 0;
            impressao.getItensImpressaos().clear();
            impressao.getItensImpressaosAux().clear();
            itemImpressao = new ItemImpressao();
            itemImpressaoAux = new ItemImpressaoAux();

        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    private String aux() {

        return "list?faces-redirect=true";
    }

    public String cancelar() {

        return "produto/list?faces-redirect=true";
    }

    public void enviaProd(Produto prod) {

        produto = prod;
    }

    public void salvar() {

        if (!impressao.getItensImpressaos().isEmpty()) {

            try {
//

                impressaoFacade.salvarCodigo(impressao);

                impressaoFacade.salvar(impressao);
                teste = "sim";
                id2 = impressaoFacade.listarTodosCompleto().get(0).getId();
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Ajuste salvo com sucesso", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } catch (Exception e) {

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Não foi possivel salvar a lista!", "");
                FacesContext.getCurrentInstance().addMessage(null, message);

            }

        } else {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "É necessario adicionar produtos para salvar!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);

        }

    }

    public List<Impressao> getListaImpressaos() {
        return impressaoFacade.listarTodosCompleto();
    }

    public Impressao getImpressao() {
        return impressao;
    }

    public void setImpressao(Impressao impressao) {
        this.impressao = impressao;
    }

}
