/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.entidade.ContasPagar;
import br.com.curso.entidade.Pessoa;
import br.com.curso.entidade.Produto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Miguel
 */
@Stateless
public class PessoaFacade extends AbstractFacade<Pessoa> {

    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Pessoa> listaUm() {
        Query q = em.createQuery("FROM Pessoa As a WHERE a.id = 0");

        return q.getResultList();
    }

    public PessoaFacade() {
        super(Pessoa.class);
    }

    public void carregaItensOs(Pessoa os) {
        os.setRestricaos(em.createQuery("FROM Restricao AS iop WHERE iop.pessoa=" + os).getResultList());
    }

    public List<Pessoa> autoCompletePessoa(String campo, String cons) {
        cons = cons.toUpperCase();
        String consulta = "FROM Pessoa AS i"
                + " WHERE i.validaFuncionario is false AND i.ativo is true AND (i." + campo + ") LIKE ('" + cons + "%')"
                + " ORDER BY i." + campo;

        Query query = getEntityManager().createQuery(consulta);
        return query.setMaxResults(20).getResultList();
    }

    public List<Pessoa> listarTodosCompletoPessoa() {
        String consulta = "FROM Pessoa where validaFuncionario is false order by id desc";
        Query query = getEntityManager().createQuery(consulta);
        return query.getResultList();
    }

}
