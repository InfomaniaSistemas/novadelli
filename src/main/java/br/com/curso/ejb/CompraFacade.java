/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.ejb;

import br.com.curso.entidade.Caixa;
import br.com.curso.entidade.CartaoCredito;
import br.com.curso.entidade.CartaoDebito;
import br.com.curso.entidade.Cheque;
import br.com.curso.entidade.Compra;
import br.com.curso.entidade.ContasPagar;
import br.com.curso.entidade.LancamentoCaixa;
import br.com.curso.entidade.Produto;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Miguel
 */
@Stateless
public class CompraFacade extends AbstractFacade<Compra> {

    @PersistenceContext(unitName = "novadelliPU")
    private EntityManager em;
    @EJB
    private ContasPagarFacade contasPagarFacade;
    @EJB
    private ProdutoFacade produtoFacade;
    @EJB
    private ChequeFacade chequeFacade;
    @EJB
    private CartaoCreditoFacade cartaoCreditoFacade;

    @EJB
    private CartaoDebitoFacade cartaoDebitoFacade;
    @EJB
    private LancamentoCaixaFacade lancamentoCaixaFacade;
    @EJB
    private PessoaFacade pessoaFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CompraFacade() {
        super(Compra.class);
    }

    public void carregaItensOs(Compra os) {
        os.setItensCompra(em.createQuery("FROM ItemCompra AS iop WHERE iop.compra=" + os).getResultList());
        os.setParcelas(em.createQuery("FROM Parcela AS ios WHERE ios.compra=" + os).getResultList());
    }

    public void entraProd(Compra venda) {

        for (int i = 0; i < venda.getItensCompra().size(); i++) {

            Produto prod = venda.getItensCompra().get(i).getProduto();
            BigDecimal novoEstoque = prod.getEstoque().add(venda.getItensCompra().get(i).getQuantidade());
            prod.setEstoque(novoEstoque);
            produtoFacade.salvar(prod);
        }

    }

    public void extornarProd(Compra venda) {

        for (int i = 0; i < venda.getItensCompra().size(); i++) {

            Produto prod = venda.getItensCompra().get(i).getProduto();
            BigDecimal novoEstoque = prod.getEstoque().subtract(venda.getItensCompra().get(i).getQuantidade());
            prod.setEstoque(novoEstoque);
            produtoFacade.salvar(prod);
        }

    }

    @Override
    public void salvar(Compra entity) {
        entity = em.merge(entity);

//        salvaCont(entity, entity.getPessoa());
    }

    public void faturar(Compra entity) {
        entity = em.merge(entity);
        geraContasPagar(entity);
        entraProd(entity);
        alteraProd(entity);
//        salvaCont(entity, entity.getPessoa());

    }

    public void faturar1(Compra entity) {

        if (entity.getDocumento().equals("DINHEIRO")) {

            entity = em.merge(entity);

            entraProd(entity);
            alteraProd(entity);
            System.out.println("Aqui dinheiro");
            lancarCaixa(entity);
        } else if (entity.getDocumento().equals("CHEQUE")) {
            entity = em.merge(entity);
            geraCheque(entity);
            System.out.println("Aqui cheque");
            entraProd(entity);
            alteraProd(entity);
        } else if (entity.getDocumento().equals("CARTÃO CRÉDITO")) {
            entity = em.merge(entity);
            geraCartaoCredito(entity);
            entraProd(entity);
            alteraProd(entity);
            //CONTROLE CARTÃO CREDITO
        } else if (entity.getDocumento().equals("CARTÃO DÉBITO")) {
            entity = em.merge(entity);
            geraCartaoDebito(entity);
            entraProd(entity);
            alteraProd(entity);
            //CONTROLE CARTÃO DÉBITO

        } else if (entity.getDocumento().equals("CREDIÁRIO")) {
            entity = em.merge(entity);
            geraContasPagar1(entity);
            entraProd(entity);
            alteraProd(entity);
            //CONTROLE CARTÃO DÉBITO

        }
    }

    private void geraContasPagar1(Compra venda) {
        if (venda.getTipoPagamento().equals("A VISTA")) {
            ContasPagar cr = new ContasPagar();

            Long id1 = contasPagarFacade.listarTodos().get(0).getId();
            cr.setId(id1 + 1);
            cr.setEmpresa(venda.getEmpresa());
            cr.setDtMovimento(venda.getDataEntrada());
            cr.setDtPagamento(venda.getDataEntrada());
            cr.setDtVencimento(venda.getDataEntrada());
            cr.setValorPrincipal(venda.getTotalDesc());
            cr.setValor(cr.getValorPrincipal());
            cr.setValorAPagar(cr.getValor());
            cr.setValorPago(venda.getTotalDesc());
            cr.setCompra(venda);
            cr.setDescricao(venda.getObs());
            cr.setPessoa(venda.getFornecedor());
            cr.setStatus("PAGO");
            contasPagarFacade.salvar(cr);
        } else {
            for (int i = 0; i < venda.getQtdParcela(); i++) {
                ContasPagar cr = new ContasPagar();
                Long id1 = contasPagarFacade.listarTodos().get(0).getId();
                cr.setId(id1 + 1);
                cr.setEmpresa(venda.getEmpresa());
                cr.setDtMovimento(venda.getDataEntrada());
                cr.setDtVencimento(venda.getParcelas().get(i).getVencimento());
                cr.setDtPagamento(venda.getDataEntrada());
                cr.setValorPrincipal(venda.getParcelas().get(i).getValor());
                cr.setValor(cr.getValorPrincipal());
                cr.setValorAPagar(cr.getValor());
                cr.setDescricao(venda.getObs());
                cr.setPessoa(venda.getFornecedor());
                cr.setCompra(venda);
                cr.setStatus("A PAGAR");
                contasPagarFacade.salvar(cr);
            }
        }

    }

    private void geraCartaoDebito(Compra venda) {

        if (venda.getTipoPagamento().equals("A PRAZO")) {

            for (int i = 0; i < venda.getQtdParcela(); i++) {

                CartaoDebito cr = new CartaoDebito();
                cr.setDtvencimento(venda.getParcelas().get(i).getVencimento());
                cr.setDtmovimento(new Date());
                cr.setValorSaida(venda.getParcelas().get(i).getValor());
                cr.setValor(BigDecimal.ZERO);
                cr.setCompra(venda);
                cr.setPessoa(venda.getFornecedor());
                cr.setSituacao("A DEBITAR");
                cr.setNumeroCheque(venda.getParcelas().get(i).getDoc());
                cartaoDebitoFacade.salvar(cr);

            }
        } else {
            CartaoDebito cr = new CartaoDebito();
            cr.setDtmovimento(venda.getDataEntrada());

            cr.setValorSaida(venda.getTotalDesc());
            cr.setPessoa(venda.getFornecedor());
            cr.setValor(BigDecimal.ZERO);
            cr.setCompra(venda);
            cr.setSituacao("A DEBITAR");
            cr.setNumeroCheque(venda.getNumDoc());
            cartaoDebitoFacade.salvar(cr);

        }

        if (venda.getTipoPagamento().equals("A VISTA")) {
            ContasPagar cr = new ContasPagar();
            Long id1 = contasPagarFacade.listarTodos().get(0).getId();
            cr.setId(id1 + 1);
            cr.setDtMovimento(venda.getDataEntrada());
            cr.setDtPagamento(venda.getDataEntrada());
            cr.setDtVencimento(venda.getDataEntrada());
            cr.setValorPrincipal(venda.getTotalDesc());
            cr.setValor(cr.getValorPrincipal());
            cr.setValorAPagar(cr.getValor());
            cr.setCompra(venda);
            cr.setTipoDocumento("CARTÃO DÉBITO");
            cr.setDescricao(venda.getObs());
            cr.setPessoa(venda.getFornecedor());
            cr.setStatus("PAGO");
            contasPagarFacade.salvar(cr);

        } else {

            for (int i = 0; i < venda.getQtdParcela(); i++) {
                ContasPagar cr = new ContasPagar();
                Long id1 = contasPagarFacade.listarTodos().get(0).getId();
                cr.setId(id1 + 1);
                cr.setDtMovimento(venda.getDataEntrada());
                cr.setDtVencimento(venda.getParcelas().get(i).getVencimento());
                cr.setDtPagamento(venda.getDataEntrada());
                cr.setValorPrincipal(venda.getParcelas().get(i).getValor());
                cr.setValor(cr.getValorPrincipal());
                cr.setValorAPagar(cr.getValor());
                cr.setDescricao(venda.getObs());
                cr.setPessoa(venda.getFornecedor());
                cr.setTipoDocumento("CARTÃO DÉBITO");
                cr.setNumDoc(venda.getParcelas().get(i).getDoc());
                cr.setCompra(venda);
                cr.setStatus("PAGO");
                contasPagarFacade.salvar(cr);
            }
        }

    }

    private void geraCartaoCredito(Compra venda) {

        if (venda.getTipoPagamento().equals("A PRAZO")) {

            for (int i = 0; i < venda.getQtdParcela(); i++) {

                CartaoCredito cr = new CartaoCredito();
                cr.setDtvencimento(venda.getParcelas().get(i).getVencimento());
                cr.setDtmovimento(new Date());
                cr.setValorSaida(venda.getParcelas().get(i).getValor());
                cr.setValor(BigDecimal.ZERO);
                cr.setCompra(venda);
                cr.setPessoa(venda.getFornecedor());
                cr.setSituacao("A DEBITAR");
                cr.setNumeroCheque(venda.getParcelas().get(i).getDoc());
                cartaoCreditoFacade.salvar(cr);

            }
        } else {
            CartaoCredito cr = new CartaoCredito();
            cr.setDtmovimento(venda.getDataEntrada());
            cr.setValorSaida(venda.getTotalDesc());
            cr.setCompra(venda);
            cr.setValor(BigDecimal.ZERO);
            cr.setPessoa(venda.getFornecedor());
            cr.setSituacao("A DEBITAR");
            cr.setNumeroCheque(venda.getNumDocValida());
            cartaoCreditoFacade.salvar(cr);

        }

        if (venda.getTipoPagamento().equals("A VISTA")) {
            ContasPagar cr = new ContasPagar();
            Long id1 = contasPagarFacade.listarTodos().get(0).getId();
            cr.setId(id1 + 1);
            cr.setDtMovimento(venda.getDataEntrada());
            cr.setDtPagamento(venda.getDataEntrada());
            cr.setDtVencimento(venda.getDataEntrada());
            cr.setValorPrincipal(venda.getTotalDesc());
            cr.setValor(cr.getValorPrincipal());
            cr.setValorAPagar(cr.getValor());
            cr.setCompra(venda);
            cr.setTipoDocumento("CARTÃO CRÉDITO");
            cr.setDescricao(venda.getObs());
            cr.setPessoa(venda.getFornecedor());
            cr.setStatus("PAGO");
            contasPagarFacade.salvar(cr);

        } else {

            for (int i = 0; i < venda.getQtdParcela(); i++) {
                ContasPagar cr = new ContasPagar();
                Long id1 = contasPagarFacade.listarTodos().get(0).getId();
                cr.setId(id1 + 1);
                cr.setDtMovimento(venda.getDataEntrada());
                cr.setDtVencimento(venda.getParcelas().get(i).getVencimento());
                cr.setDtPagamento(venda.getDataEntrada());
                cr.setValorPrincipal(venda.getParcelas().get(i).getValor());
                cr.setValor(cr.getValorPrincipal());
                cr.setValorAPagar(cr.getValor());
                cr.setValorPago(venda.getParcelas().get(i).getValor());
                cr.setDescricao(venda.getObs());
                cr.setPessoa(venda.getFornecedor());
                cr.setTipoDocumento("CARTÃO CRÉDITO");
                cr.setNumDoc(venda.getParcelas().get(i).getDoc());
                cr.setCompra(venda);
                cr.setStatus("PAGO");
                contasPagarFacade.salvar(cr);
            }
        }

    }

    private void geraCheque(Compra venda) {

        if (venda.getTipoPagamento().equals("A PRAZO")) {

            for (int i = 0; i < venda.getQtdParcela(); i++) {

                Cheque cr = new Cheque();

                cr.setDtvencimento(venda.getParcelas().get(i).getVencimento());
                cr.setDtmovimento(new Date());
                cr.setValorSaida(venda.getParcelas().get(i).getValor());
                cr.setValor(BigDecimal.ZERO);
                cr.setPessoa(venda.getFornecedor());
                cr.setCompra(venda);
                cr.setSituacao("A DEBITAR");
                cr.setNumeroCheque(venda.getParcelas().get(i).getDoc());
                chequeFacade.salvar(cr);

            }
        } else {
            Cheque cr = new Cheque();
            cr.setDtmovimento(venda.getDataEntrada());
            cr.setValorSaida(venda.getTotalDesc());
            cr.setCompra(venda);
            cr.setValor(BigDecimal.ZERO);
            cr.setPessoa(venda.getFornecedor());
            cr.setSituacao("A DEBITAR");
            cr.setNumeroCheque(venda.getNumDocValida());
            chequeFacade.salvar(cr);

        }

        if (venda.getTipoPagamento().equals("A VISTA")) {
            ContasPagar cr = new ContasPagar();
            Long id1 = contasPagarFacade.listarTodosCompleto().get(0).getId();
            cr.setId(id1 + 1);

            cr.setDtMovimento(venda.getDataEntrada());
            cr.setDtPagamento(venda.getDataEntrada());
            cr.setDtVencimento(venda.getDataEntrada());
            cr.setValorPrincipal(venda.getTotalDesc());
            cr.setValor(cr.getValorPrincipal());
            cr.setValorAPagar(cr.getValor());
            cr.setValorPago(venda.getTotalDesc());
            cr.setCompra(venda);
            cr.setTipoDocumento("CHEQUE");
            cr.setDescricao(venda.getObs());
            cr.setPessoa(venda.getFornecedor());
            cr.setStatus("PAGO");
            contasPagarFacade.salvar(cr);

        } else {

            for (int i = 0; i < venda.getQtdParcela(); i++) {
                ContasPagar cr = new ContasPagar();
                Long id1 = contasPagarFacade.listarTodosCompleto().get(0).getId();
                cr.setId(id1 + 1);
                cr.setDtMovimento(venda.getDtEntradaSaida());
                cr.setDtVencimento(venda.getParcelas().get(i).getVencimento());
                cr.setDtPagamento(venda.getDataEntrada());
                cr.setValorPrincipal(venda.getParcelas().get(i).getValor());
                cr.setValor(cr.getValorPrincipal());
                cr.setValorAPagar(cr.getValor());
                cr.setValorPago(venda.getParcelas().get(i).getValor());
                cr.setDescricao(venda.getObs());
                cr.setPessoa(venda.getFornecedor());
                cr.setTipoDocumento("CHEQUE");
                cr.setNumDoc(venda.getParcelas().get(i).getDoc());
                cr.setCompra(venda);
                cr.setStatus("PAGO");
                contasPagarFacade.salvar(cr);
            }
        }

    }

    public void lancarCaixa(Compra venda) {
        List<Caixa> aux = em.createQuery("FROM Caixa As a WHERE a.id = 1").getResultList();

        BigDecimal novosaldo = aux.get(0).getSaldo().add(venda.getTotalDesc());
        aux.get(0).setSaldo(novosaldo);
        LancamentoCaixa la = new LancamentoCaixa();

        la.setPessoa(venda.getFornecedor());
        la.setTipo("SAÍDA");
        la.setCaixa(aux.get(0));
        la.setLacamentoSaida(venda.getTotalDesc());
        la.setLacamentoEntrada(BigDecimal.ZERO);
        la.setDtEntrada(venda.getDataEntrada());

        lancamentoCaixaFacade.salvar(la);
    }

    private void alteraProd(Compra compra) {

        for (int i = 0; i < compra.getItensCompra().size(); i++) {

            Produto prod = compra.getItensCompra().get(i).getProduto();
            prod.setUltimoFornecedor(compra.getFornecedor());
            prod.setDtUltimaCompra(compra.getDataNota());
            prod.setIpi(compra.getItensCompra().get(i).getIpi());
            prod.setSubTrib(compra.getItensCompra().get(i).getSt());
            prod.setFrete(compra.getItensCompra().get(i).getFrete());
            prod.setCustoOp(compra.getItensCompra().get(i).getCusto());
            prod.setValorVendaAvista(compra.getItensCompra().get(i).getValorVendaAvista());
            prod.setValorUni(compra.getItensCompra().get(i).getValor());
            prod.setValorVendaPrazo(compra.getItensCompra().get(i).getValorVendaAprazo());
            prod.setCustoOpMaisEncargos(compra.getItensCompra().get(i).getCustoTotalUnitario());
            prod.setDespesasAssessorias(compra.getItensCompra().get(i).getDespassessorias());
            prod.setComissao(compra.getItensCompra().get(i).getComissao());
            prod.setCfopD(compra.getItensCompra().get(i).getCfopD());
            prod.setCfopF(compra.getItensCompra().get(i).getCfopF());
            prod.setNcmString(compra.getItensCompra().get(i).getNcmString());

        }

    }

    public void geraContasPagar(Compra compra) {
        if (compra.getTipoPagamento().equals("A VISTA")) {

            ContasPagar cr = new ContasPagar();
            Long id1 = contasPagarFacade.listarTodosCompleto().get(0).getId();
            cr.setId(id1 + 1);
            cr.setDtMovimento(compra.getDataEntrada());
            cr.setDtVencimento(compra.getDataEntrada());
            cr.setDtPagamento(compra.getDataEntrada());
            cr.setValor(compra.getTotalNota());
            cr.setValorPrincipal(cr.getValor());
            cr.setValorAPagar(cr.getValorPrincipal());
            cr.setCompra(compra);
            cr.setPessoa(compra.getFornecedor());
            cr.setStatus("PAGO");
            contasPagarFacade.salvar(cr);
            lancarCaixa(compra);

        } else {
            for (int i = 0; i < compra.getParcelas().size(); i++) {
                ContasPagar cr = new ContasPagar();
                Long id1 = contasPagarFacade.listarTodosCompleto().get(0).getId();
                cr.setId(id1 + 1);
                cr.setDtMovimento(compra.getDataEntrada());
                cr.setDtVencimento(compra.getParcelas().get(i).getVencimento());
                cr.setValor(compra.getParcelas().get(i).getValor());
                cr.setValorPrincipal(cr.getValor());
                cr.setValorAPagar(cr.getValorPrincipal());
                cr.setPessoa(compra.getFornecedor());
                cr.setCompra(compra);
                cr.setParcelas(compra.getParcelas());
                cr.setStatus("A PAGAR");
                contasPagarFacade.salvar(cr);

            }
        }

    }
}
