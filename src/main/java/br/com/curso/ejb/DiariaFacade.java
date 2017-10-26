/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.entidade.ContasPagar;
import br.com.curso.entidade.Diaria;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Miguel
 */
@Stateless
public class DiariaFacade extends AbstractFacade<Diaria> {

    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;
    @EJB
    private ContasPagarFacade contasPagarFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DiariaFacade() {
        super(Diaria.class);
    }

    @Override
    public void salvar(Diaria entity) {
        entity = em.merge(entity);
        geraDespesaPessoal(entity);
    }

    public void geraDespesaPessoal(Diaria venda) {

        Date dataParcela = new Date(venda.getDtFin().getYear(), venda.getDtFin().getMonth(), venda.getDtFin().getDate());
        dataParcela.setDate(dataParcela.getDate() + 30);
        ContasPagar cr = new ContasPagar();
        cr.setDtMovimento(venda.getDtFin());
        cr.setDtVencimento(dataParcela);
        cr.setValorPrincipal(venda.getValor());
        cr.setValor(cr.getValorPrincipal());
        cr.setValorAPagar(cr.getValor());
        cr.setDescricao("DESPESA COM DIARIAS DE FUNCIONÁRIOS");
        cr.setPessoa(venda.getFuncionario());
        cr.setDiaria(venda);
        cr.setStatus("A PAGAR");
        contasPagarFacade.salvar(cr);
    }
}
