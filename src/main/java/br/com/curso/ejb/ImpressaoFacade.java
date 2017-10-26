/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.controle.RelatorioControle;
import br.com.curso.entidade.Impressao;
import br.com.curso.entidade.ItemImpressao;
import br.com.curso.entidade.Produto;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Miguel
 */
@Stateless
public class ImpressaoFacade extends AbstractFacade<Impressao> {

    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;
    RelatorioControle relatorioControle;
    @EJB
    private ProdutoFacade produtoFacade;

    public void salvarCodigo(Impressao venda) {

        for (int i = 0; i < venda.getItensImpressaos().size(); i++) {

            Produto prod = venda.getItensImpressaos().get(i).getPlantio();
            String aux = prod.getId().toString();
            if (aux.length()==3) {
                aux = "0" + aux;
                prod.setCodBarras(aux);
                }
            
            else if (aux.length()==2) {
                aux = "00" + aux;
                prod.setCodBarras(aux);
                }
            
            else if (aux.length()==1) {
                aux = "000" + aux;
                prod.setCodBarras(aux);
                }
            
            else {
                prod.setCodBarras(prod.getId().toString());
            }
            
            produtoFacade.salvar(prod);
        }

    }
    @EJB
    private ItemImpressaoFacade itemImpressaoFacade;

    public void salvaItem(Impressao venda) {

     
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ImpressaoFacade() {
        super(Impressao.class);
    }
}
