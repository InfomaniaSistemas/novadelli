/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

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
public class ProdutoFacade extends AbstractFacade<Produto> {

    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;

    public List<Produto> listarRapidoCadastro(String campo, String prod, Boolean ativo, String valida) {
        prod = prod.toUpperCase();
        if (valida.equals("todos")) {

            String consulta = "FROM Produto AS i"
                    + " WHERE (i." + campo + ") LIKE ('%" + prod + "%')"
                    + " ORDER BY i.id DESC";

            Query query = getEntityManager().createQuery(consulta);

            return query.getResultList();

        } else {

            String consulta = "FROM Produto AS i"
                    + " WHERE LOWER(i." + campo + ") LIKE ('%" + prod + "%')"
                    + " AND i.ativo = " + ativo
                    + " ORDER BY i.id DESC";

            Query query = getEntityManager().createQuery(consulta);

            return query.getResultList();

        }
    }

public List<Produto> listarRapido(String campo, String prod) {
        prod = prod.toUpperCase();
        String consulta = "FROM Produto AS i"
                + " WHERE (i." + campo + ") LIKE ('" + prod + "%')"
                + " ORDER BY i.valorVendaPrazo DESC";

        Query query = getEntityManager().createQuery(consulta);

        return query.getResultList();

    }

    public List<Produto> autoCompletePrimeiraLetra(String campo, String cons) {
        cons = cons.toUpperCase();
        String consulta = "FROM Produto AS i"
                + " WHERE (i." + campo + ") LIKE ('" + cons + "%')"
                + " AND i.ativo is true ORDER BY i." + campo;

        Query query = getEntityManager().createQuery(consulta);
        return query.getResultList();
    }

    public List<Produto> autoCompleteNcm(String campo, String cons) {
        cons = cons.toUpperCase();
        String consulta = "FROM Produto AS i"
                + " WHERE ncmString = '' AND (i." + campo + ") LIKE ('" + cons + "%')"
                + " ORDER BY i." + campo;

        Query query = getEntityManager().createQuery(consulta);
        return query.getResultList();
    }

    public List<Produto> autoComplete2(String campo, String cons) {
        cons = cons.toUpperCase();
        String consulta = "FROM Produto AS i"
                + " WHERE (i." + campo + ") LIKE ('" + cons + "%')"
                + " ORDER BY i." + campo;

        Query query = getEntityManager().createQuery(consulta);
        return query.getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Produto> listaProdutosPorCod(String cod) {
        Query q = em.createQuery("FROM Produto As a WHERE a.codFornecedor = :para1");
        q.setParameter("para1", cod);

        return q.getResultList();
    }

    public void carregaItensOs(Produto os) {
        os.setItensMateriaPrima(em.createQuery("FROM ItemMateriaPrima AS iop WHERE iop.produto=" + os).getResultList());
    }

//    public List<Produto> listaRapido(String prod) {
//
////        String consulta = "FROM Produto AS i"
////                + " WHERE LOWER i.nome LIKE ('%" + prod + "%')"
////                + " ORDER BY i.nome";
////
////        Query query = getEntityManager().createQuery(consulta);
//
//        Query q = em.createQuery("FROM Produto As a WHERE LOWER i.nome LIKE ('% para1 %') ");
//        q.setParameter("para1", prod);
//        return q.setMaxResults(20).getResultList();
//    }
    public ProdutoFacade() {
        super(Produto.class);
    }

}
