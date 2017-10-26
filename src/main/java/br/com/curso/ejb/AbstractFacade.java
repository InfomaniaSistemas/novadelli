/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Miguel
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void inserir(T entity) {
        getEntityManager().persist(entity);
    }

    public void salvar(T entity) {
        getEntityManager().merge(entity);
    }

    public void excluir(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T pesquisar(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> listarTodos() {
        String consulta = "FROM " + entityClass.getSimpleName() + " order by id desc";
        Query query = getEntityManager().createQuery(consulta);
        return query.setMaxResults(2).getResultList();
    }

    public List<T> listarTodosCompleto() {
        String consulta = "FROM " + entityClass.getSimpleName() + " order by id desc";
        Query query = getEntityManager().createQuery(consulta);
        return query.getResultList();
    }

    public List<T> listarRapido(String campo, String prod) {
        prod = prod.toUpperCase();
        String consulta = "FROM" + entityClass.getSimpleName() + "AS i"
                + " WHERE (i." + campo + ") LIKE ('%" + prod + "%')"
                + " ORDER BY i." + campo;

        Query query = getEntityManager().createQuery(consulta);

        return query.setMaxResults(20).getResultList();

    }

    public List<T> autoComplete(String campo, String cons) {
        cons = cons.toUpperCase();
        String consulta = "FROM " + entityClass.getSimpleName() + " AS i"
                + " WHERE (i." + campo + ") LIKE ('%" + cons + "%')"
                + " ORDER BY i." + campo;

        Query query = getEntityManager().createQuery(consulta);
        return query.setMaxResults(50).getResultList();
    }

}
