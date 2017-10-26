/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.entidade.AjusteNcm;
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
public class AjusteNcmFacade extends AbstractFacade<AjusteNcm> {

    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;
    @EJB
    private ProdutoFacade produtoFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void geraNcm( AjusteNcm ajusteNcm) {

        for (int i = 0; i < ajusteNcm.getItensNcms().size(); i++) {

            Produto prod = ajusteNcm.getItensNcms().get(i);
            prod.setNcmString(ajusteNcm.getNcm());

            produtoFacade.salvar(prod);
        }
    }

    public AjusteNcmFacade() {
        super(AjusteNcm.class);
    }
}
