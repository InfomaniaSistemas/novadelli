/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.curso.controle;

import br.com.curso.converter.GenericConverter;
import br.com.curso.ejb.ContasReceberFacade;
import br.com.curso.ejb.ContasRecebidasFacade;
import br.com.curso.ejb.ReciboFacade;
import br.com.curso.ejb.TaxaJurosFacade;
import br.com.curso.entidade.Banco;
import br.com.curso.entidade.Bandeira;
import br.com.curso.entidade.ContaBancaria;
import br.com.curso.entidade.ContasReceber;
import br.com.curso.entidade.ContasRecebidas;
import br.com.curso.entidade.Pessoa;
import br.com.curso.entidade.Recibo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


@ManagedBean
@SessionScoped
public class ContasReceberControle implements Serializable {

    private ContasReceber contasReceber1;
    private ContasRecebidas contasRecebidas;
    private Pessoa pessoa;
    private Long tipo;
    private ContaBancaria contaBancaria;
    private String aux = "NAO";
    @EJB
    private ContasReceberFacade contasReceber1Facade;
    @EJB
    private ContasRecebidasFacade contasRecebidasFacade;
    @EJB
    private TaxaJurosFacade taxaJurosFacade;
    private GenericConverter converter;
    Date aux1;
    private String tipoDocumento;
    private String numeroDoc;
    private Date dtVencimentoFatura;
    private Bandeira bandeira;
    private Banco banco;

    public ContasRecebidasFacade getContasRecebidasFacade() {
        return contasRecebidasFacade;
    }

    public void setContasRecebidasFacade(ContasRecebidasFacade contasRecebidasFacade) {
        this.contasRecebidasFacade = contasRecebidasFacade;
    }

    public ContasRecebidas getContasRecebidas() {
        return contasRecebidas;
    }

    public void setContasRecebidas(ContasRecebidas contasRecebidas) {
        this.contasRecebidas = contasRecebidas;
    }

    public String getNumeroDoc() {
        return numeroDoc;
    }

    public void setNumeroDoc(String numeroDoc) {
        this.numeroDoc = numeroDoc;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Bandeira getBandeira() {
        return bandeira;
    }

    public void setBandeira(Bandeira bandeira) {
        this.bandeira = bandeira;
    }

    public Date getDtVencimentoFatura() {
        return dtVencimentoFatura;
    }

    public void setDtVencimentoFatura(Date dtVencimentoFatura) {
        this.dtVencimentoFatura = dtVencimentoFatura;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public void validaLista() {
        try {
            contasReceber1Facade.validaMensalidadeAPagar2(pessoa);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Cliente não encontrado!", ""));

        }

    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public ContasReceberFacade getContasReceber1Facade() {
        return contasReceber1Facade;
    }

    public void setContasReceber1Facade(ContasReceberFacade contasReceber1Facade) {
        this.contasReceber1Facade = contasReceber1Facade;
    }

    public GenericConverter getConverter() {
        return converter;
    }

    public void setConverter(GenericConverter converter) {
        this.converter = converter;
    }

    public ContasReceberControle() {
        this.aux1 = new Date();
        this.dtPagamentoSelecao = new Date();
        this.auxiliar = BigDecimal.ZERO;
        this.tipoDocumento = "DINHEIRO";
        this.confirmaOpcaoDeJuros = "JUROS";
    }

    public Date getAux1() {
        return aux1;
    }

    public void setAux1(Date aux1) {
        this.aux1 = aux1;
    }

    public GenericConverter converter() {
        if (converter == null) {
            converter = new GenericConverter(contasReceber1Facade);
        }
        return converter;
    }

    public String editar() {
        Date dt = new Date();
        contasReceber1.setTipoDocumento("DINHEIRO");
        if (contasReceber1.getRestosAPagar().compareTo(BigDecimal.ZERO) == 0) {

            if (dt.after(contasReceber1.getDtVencimento())) {

                diferenca = new Date().getTime() - contasReceber1.getDtVencimento().getTime();
                long diferencaSegundos = diferenca / 1000;
                long diferencaMinutos = diferencaSegundos / 60;
                long diferencaHoras = diferencaMinutos / 60;
                diferenca = diferencaHoras / 24;

                dias = new BigDecimal(diferenca.toString());
                System.out.println("-->" + dias);
                BigDecimal auxJuros = taxaJurosFacade.listarTodosCompleto().get(0).getTaxa().multiply(dias);
                BigDecimal auxPor = contasReceber1.getValorAPagar().multiply(auxJuros);
                BigDecimal auxPor1 = auxPor.divide(new BigDecimal("100"));
                contasReceber1.setValor(contasReceber1.getValor().add(auxPor1));
            }

            contasReceber1.setRestosAPagar(contasReceber1.getValor());
            contasReceber1.setJuros(BigDecimal.ZERO);
            contasReceber1.setUltimoPagamento(BigDecimal.ZERO);
            return "form_1?faces-redirect=true";

        } else {

            if (dt.after(contasReceber1.getDtVencimento())) {

                diferenca = new Date().getTime() - contasReceber1.getDtVencimento().getTime();
                long diferencaSegundos = diferenca / 1000;
                long diferencaMinutos = diferencaSegundos / 60;
                long diferencaHoras = diferencaMinutos / 60;
                diferenca = diferencaHoras / 24;

                dias = new BigDecimal(diferenca.toString());
                System.out.println("-->" + dias);
                BigDecimal auxJuros = taxaJurosFacade.listarTodosCompleto().get(0).getTaxa().multiply(dias);
                BigDecimal auxPor = contasReceber1.getValorAPagar().multiply(auxJuros);
                BigDecimal auxPor1 = auxPor.divide(new BigDecimal("100"));
                contasReceber1.setValor(contasReceber1.getValor().add(auxPor1));
            }

            contasReceber1.setUltimoPagamento(contasReceber1.getValorPago());
            contasReceber1.setValorPago(BigDecimal.ZERO);
            contasReceber1.setJuros(BigDecimal.ZERO);

            contasReceber1.setDtPagamento(null);
            contasReceber1.setValor(contasReceber1.getRestosAPagar());
            contasReceber1.setRestosAPagar(contasReceber1.getValor());
            return "form_1?faces-redirect=true";
        }

    }

    private Date dtPagamentoSelecao;
    private BigDecimal totalSelecao;

    public Date getDtPagamentoSelecao() {
        return dtPagamentoSelecao;
    }

    public void setDtPagamentoSelecao(Date dtPagamentoSelecao) {
        this.dtPagamentoSelecao = dtPagamentoSelecao;
    }

    public BigDecimal getTotalSelecao() {
        return totalSelecao;
    }

    public void setTotalSelecao(BigDecimal totalSelecao) {
        this.totalSelecao = totalSelecao;
    }

    public void somaTotalSelecao() {

        BigDecimal soma = BigDecimal.ZERO;
        auxiliar = BigDecimal.ZERO;
        List<ContasReceber> lista = listaPagamento;
        for (ContasReceber lista1 : lista) {

            soma = soma.add(lista1.getValorAPagar());
        }
        totalSelecao = soma;
        jurosSelecao = soma;
        somaJurosSelecao();
        desconto = BigDecimal.ZERO;

        if (jurosSelecao.compareTo(BigDecimal.ZERO) == 0) {
            totalDesconto = soma;
        } else if (jurosSelecao.compareTo(BigDecimal.ZERO) == 1) {
            totalDesconto = jurosSelecao;
        }

        org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('form5').show();");
    }

    public void calculaDesconto() {

        totalDesconto = jurosSelecao.subtract(desconto);
    }
    private BigDecimal desconto = BigDecimal.ZERO;
    private BigDecimal totalDesconto = BigDecimal.ZERO;

    public BigDecimal getTotalDesconto() {
        return totalDesconto;
    }

    public void setTotalDesconto(BigDecimal totalDesconto) {
        this.totalDesconto = totalDesconto;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public void somaJurosSelecao() {

        BigDecimal juro = BigDecimal.ZERO;

        Date dt = new Date();

        for (int i = 0; i < listaPagamento.size(); i++) {
            if (dt.after(listaPagamento.get(i).getDtVencimento())) {

                diferenca = new Date().getTime() - listaPagamento.get(i).getDtVencimento().getTime();
                long diferencaSegundos = diferenca / 1000;
                long diferencaMinutos = diferencaSegundos / 60;
                long diferencaHoras = diferencaMinutos / 60;
                diferenca = diferencaHoras / 24;

                dias = new BigDecimal(diferenca.toString());
                System.out.println("-->" + dias);
                BigDecimal auxJuros = taxaJurosFacade.listarTodosCompleto().get(0).getTaxa().multiply(dias);
                BigDecimal auxPor = listaPagamento.get(i).getValorAPagar().multiply(auxJuros);
                BigDecimal auxPor1 = auxPor.divide(new BigDecimal("100"), 2, RoundingMode.UP);

                System.out.println("JuroAqui" + listaPagamento.get(i).getJuros());
                if (listaPagamento.get(i).getRestosAPagar().compareTo(BigDecimal.ZERO) == 0) {
                    listaPagamento.get(i).setValorAPagar(listaPagamento.get(i).getValorAPagar().add(auxPor1));
                    listaPagamento.get(i).setJuros(auxPor1);
                } else if (listaPagamento.get(i).getRestosAPagar().compareTo(BigDecimal.ZERO) == 1) {

                    if (listaPagamento.get(i).getJuros().compareTo(auxPor1) == 1) {

                        listaPagamento.get(i).setJuros(auxPor1);
                        listaPagamento.get(i).setRestosAPagar(listaPagamento.get(i).getValorAPagar().add(auxPor1));
                        listaPagamento.get(i).setValorAPagar(listaPagamento.get(i).getValorAPagar().add(auxPor1));
                    }

                }

            }
            juro = juro.add(listaPagamento.get(i).getValorAPagar());
            jurosSelecao = juro;
        }

    }

    Long diferenca;
    BigDecimal dias;
    private BigDecimal jurosSelecao;

    public TaxaJurosFacade getTaxaJurosFacade() {
        return taxaJurosFacade;
    }

    public void setTaxaJurosFacade(TaxaJurosFacade taxaJurosFacade) {
        this.taxaJurosFacade = taxaJurosFacade;
    }

    public Long getDiferenca() {
        return diferenca;
    }

    public void setDiferenca(Long diferenca) {
        this.diferenca = diferenca;
    }

    public BigDecimal getDias() {
        return dias;
    }

    public void setDias(BigDecimal dias) {
        this.dias = dias;
    }

    public BigDecimal getJurosSelecao() {
        return jurosSelecao;
    }

    public void setJurosSelecao(BigDecimal jurosSelecao) {
        this.jurosSelecao = jurosSelecao;
    }

    public void editar2(ContasReceber cr) {

        if (cr.getRestosAPagar().compareTo(BigDecimal.ZERO) == 0) {
            Date dt = new Date();
            if (dt.after(cr.getDtVencimento())) {

                diferenca = new Date().getTime() - cr.getDtVencimento().getTime();
                long diferencaSegundos = diferenca / 1000;
                long diferencaMinutos = diferencaSegundos / 60;
                long diferencaHoras = diferencaMinutos / 60;
                diferenca = diferencaHoras / 24;

                dias = new BigDecimal(diferenca.toString());
                System.out.println("-->" + dias);
                BigDecimal auxJuros = taxaJurosFacade.listarTodosCompleto().get(0).getTaxa().multiply(dias);
                BigDecimal auxPor = cr.getValorAPagar().multiply(auxJuros);
                BigDecimal auxPor1 = auxPor.divide(new BigDecimal("100"), 2, RoundingMode.UP);
                cr.setValor(cr.getValor().add(auxPor1));

            }

            cr.setRestosAPagar(BigDecimal.ZERO);
            cr.setJuros(BigDecimal.ZERO);
            cr.setUltimoPagamento(BigDecimal.ZERO);

        } else {
            Date dt = new Date();
            if (dt.after(cr.getDtVencimento())) {

                diferenca = new Date().getTime() - cr.getDtVencimento().getTime();
                long diferencaSegundos = diferenca / 1000;
                long diferencaMinutos = diferencaSegundos / 60;
                long diferencaHoras = diferencaMinutos / 60;
                diferenca = diferencaHoras / 24;

                dias = new BigDecimal(diferenca.toString());
                System.out.println("-->" + dias);
                BigDecimal auxJuros = taxaJurosFacade.listarTodosCompleto().get(0).getTaxa().multiply(dias);
                BigDecimal auxPor = cr.getValorAPagar().multiply(auxJuros);
                BigDecimal auxPor1 = auxPor.divide(new BigDecimal("100"), 2, RoundingMode.UP);
                cr.setValor(cr.getValor().add(auxPor1));
            }

            cr.setUltimoPagamento(cr.getValorPago());
            cr.setValorPago(BigDecimal.ZERO);
            cr.setJuros(BigDecimal.ZERO);

            cr.setDtPagamento(null);
            cr.setValor(cr.getRestosAPagar());
            cr.setRestosAPagar(cr.getValor());
        }
    }

//    public void geraBoleto(ContasReceber cr) {
////        listaEmpresa();
//        /*
//         * INFORMANDO DADOS SOBRE O CEDENTE.
//         */
//        Cedente cedente = new Cedente("ACADEMIA BELLA FORMA - LTDA", "17.699.162/0001-62");
//
//        /*
//         * INFORMANDO DADOS SOBRE O SACADO.
//         */
////        Sacado sacado = new Sacado("JavaDeveloper Pronto Para Férias", "222.222.222-22");
//        Sacado sacado = new Sacado(cr.getPessoa().getNome());
//
//        // Informando o endereço do sacado.
//        Endereco enderecoSac = new Endereco();
//        enderecoSac.setUF(UnidadeFederativa.PR);
//        enderecoSac.setLocalidade(cr.getPessoa().getCidade().getNome());
////        enderecoSac.setCep(cr.getPessoa().getCep());
//        enderecoSac.setBairro(cr.getPessoa().getBairro());
//        enderecoSac.setLogradouro(cr.getPessoa().getEndereco());
//        enderecoSac.setNumero(cr.getPessoa().getNumero());
//        sacado.addEndereco(enderecoSac);
//
//        /*
//         * INFORMANDO DADOS SOBRE O SACADOR AVALISTA.
//         */
//        SacadorAvalista sacadorAvalista = new SacadorAvalista("Miguel Gustavo Miiller", "334.248.578-74");
//
//        // Informando o endereço do sacador avalista.
//        Endereco enderecoSacAval = new Endereco();
//        enderecoSacAval.setUF(UnidadeFederativa.PR);
//        enderecoSacAval.setLocalidade("Marilena");
//        enderecoSacAval.setCep("87960-000");
//        enderecoSacAval.setBairro("Centro");
//        enderecoSacAval.setLogradouro("Rua Tal");
//        enderecoSacAval.setNumero("2");
//        sacadorAvalista.addEndereco(enderecoSacAval);
//
//        /*
//         * INFORMANDO OS DADOS SOBRE O TÍTULO.
//         */
//        // Informando dados sobre a conta bancária do título.
//        ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCO_BRADESCO.create());
//        contaBancaria.setNumeroDaConta(new NumeroDaConta(14521, "8"));
//        contaBancaria.setCarteira(new Carteira(16));
//        contaBancaria.setAgencia(new Agencia(520, "7"));
//
//        Titulo titulo = new Titulo(contaBancaria, sacado, cedente, sacadorAvalista);
//        titulo.setNumeroDoDocumento("0023");
//        titulo.setNossoNumero("14200815114");
//        titulo.setDigitoDoNossoNumero("5");
//        titulo.setValor(cr.getValorPrincipal());
//        titulo.setDataDoDocumento(new Date());
//        titulo.setDataDoVencimento(cr.getDtVencimento());
//        titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);
//        titulo.setAceite(Titulo.Aceite.A);
////        titulo.setDesconto(new BigDecimal(0.05));
////        titulo.setDeducao(BigDecimal.ZERO);
////        titulo.setMora(BigDecimal.ZERO);
////        titulo.setAcrecimo(BigDecimal.ZERO);
////        titulo.setValorCobrado(BigDecimal.ZERO);
//
//        /*
//         * INFORMANDO OS DADOS SOBRE O BOLETO.
//         */
//        Boleto boleto = new Boleto(titulo);
//
//        boleto.setLocalPagamento("PAGAVEL EM QUALQUER BANCO ATE O VENCIMENTO");
//
//        boleto.setInstrucaoAoSacado("Após o vencimento Mora dia R$ 2.50");
//        boleto.setInstrucao1("Após o vencimento multa de 2.00%");
//        boleto.setInstrucao2("Protestar após 7 dias do vencimento");
////        boleto.setInstrucao3("PARA PAGAMENTO 3 até Depois de amanhã, OK, não cobre.");
//        boleto.setInstrucao4("APOS SETE DIAS DO VENCIMENTO SERA ENCAMINHADO PARA PROTESTO");
////        boleto.setInstrucao5("PARA PAGAMENTO 5 até 05/xx/xxxx COBRAR O VALOR DE: R$ 02,00");
////        boleto.setInstrucao6("PARA PAGAMENTO 6 até 06/xx/xxxx COBRAR O VALOR DE: R$ 03,00");
////        boleto.setInstrucao7("PARA PAGAMENTO 7 até xx/xx/xxxx COBRAR O VALOR QUE VOCÊ QUISER!");
////        boleto.setInstrucao8("APÓS o Vencimento, Pagável Somente na Rede X.");
//
//        /*
//         * GERANDO O BOLETO BANCÁRIO.
//         */
//        // Instanciando um objeto "BoletoViewer", classe responsável pela
//        // geração do boleto bancário.
//        BoletoViewer boletoViewer = new BoletoViewer(boleto);
//
//        byte[] pdfAsBytes = boletoViewer.getPdfAsByteArray();
//
//        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//
//        try {
//
//            response.setContentType("application/pdf");
//            response.setHeader("Content-Disposition", "attachment; filename=boleto.pdf");
//
//            OutputStream output = response.getOutputStream();
//            output.write(pdfAsBytes);
//            response.flushBuffer();
//
//            FacesContext.getCurrentInstance().responseComplete();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public String novo() {
        contasReceber1 = new ContasReceber();
        return "form?faces-redirect=true";
    }

    public String novo1() {
        contasReceber1 = new ContasReceber();
        return "form1?faces-redirect=true";
    }

//    public BigDecimal getTotal() {
//
//        BigDecimal credito = BigDecimal.ZERO;
//
//        List<ContasReceber> lista = contasReceber1Facade.listarAreceber();
//        for (ContasReceber lista1 : lista) {
//
//            credito = credito.add(lista1.getValorPrincipal());
//        }
//        return credito;
//    }
    public BigDecimal getTotalPorCliente() {

        BigDecimal credito = BigDecimal.ZERO;

        List<ContasReceber> lista = contasReceber1Facade.validaMensalidadeAPagar2(pessoa);
        for (ContasReceber lista1 : lista) {

            credito = credito.add(lista1.getValorPrincipal());
        }
        return credito;
    }

//    public BigDecimal getAreceber() {
//
//        BigDecimal credito = BigDecimal.ZERO;
//
//        List<ContasReceber> lista = contasReceber1Facade.listarAreceber();
//        for (ContasReceber lista1 : lista) {
//
//            credito = credito.add(lista1.getValorAPagar());
//        }
//        return credito;
//    }
//    
    public BigDecimal getAreceberPorPessoa() {

        BigDecimal credito = BigDecimal.ZERO;

        List<ContasReceber> lista = contasReceber1Facade.validaMensalidadeAPagar2(pessoa);
        for (ContasReceber lista1 : lista) {

            credito = credito.add(lista1.getValorAPagar());
        }
        return credito;
    }

    public BigDecimal getVencimentoPorPessoa() {

        BigDecimal vencimento = BigDecimal.ZERO;

        List<ContasReceber> lista = contasReceber1Facade.contasVencidasPorPessoa(aux1, pessoa);
        for (ContasReceber lista1 : lista) {

            vencimento = vencimento.add(lista1.getValorAPagar());
        }
        return vencimento;
    }

//    public BigDecimal getVencimento() {
//
//        BigDecimal vencimento = BigDecimal.ZERO;
//
//        List<ContasReceber> lista = contasReceber1Facade.contasVencidas(aux1);
//        for (ContasReceber lista1 : lista) {
//
//            vencimento = vencimento.add(lista1.getValorAPagar());
//        }
//        return vencimento;
//    }
    public List<ContasReceber> autoComplete(String query) {
        return contasReceber1Facade.autoComplete("nome", query);
    }

    private List<ContasReceber> listaPagamento;

    public String salvar() {

        if (contasReceber1.getId() == null) {
            Long id1 = contasReceber1Facade.listarTodos().get(0).getId();
            contasReceber1.setId(id1 + 1);
        }
        contasReceber1.setValorAPagar(contasReceber1.getValor());
        contasReceber1.setValorPrincipal(contasReceber1.getValor());
        contasReceber1.setValorPago(BigDecimal.ZERO);
        contasReceber1.setRestosAPagar(BigDecimal.ZERO);

        contasReceber1.setStatus("A RECEBER");
        contasReceber1Facade.salvar(contasReceber1);
        return "list?faces-redirect=true";
    }

    public List<ContasReceber> getListaPagamento() {
        return listaPagamento;
    }

    public void setListaPagamento(List<ContasReceber> listaPagamento) {
        this.listaPagamento = listaPagamento;
    }

    public String salvar1() {

        contasReceber1.validaP();

        if (contasReceber1.getTipoDocumento().equals("DINHEIRO")) {
            contasReceber1Facade.lancarCaixa(contasReceber1);

        } else if (contasReceber1.getTipoDocumento().equals("CARTÃO CRÉDITO")) {

            contasReceber1Facade.lancarCartao(contasReceber1, dtVencimentoFatura, bandeira);

        } else if (contasReceber1.getTipoDocumento().equals("CARTÃO DÉBITO")) {

            contasReceber1Facade.lancarCartaoDebito(contasReceber1, dtVencimentoFatura, bandeira);

        } else if (contasReceber1.getTipoDocumento().equals("CHEQUE")) {

            contasReceber1Facade.lancarCheque(contasReceber1, numeroDoc, dtVencimentoFatura, banco);
        } else if (contasReceber1.getTipoDocumento().equals("CONTA BANCÁRIA")) {

            contasReceber1Facade.lancarConta(contasReceber1, contasReceber1.getUltimoPagamento(), contaBancaria.getId());
        }

        contasReceber1Facade.salvar(contasReceber1);
        return "list?faces-redirect=true";

    }

    private BigDecimal valorPagoLista;

    public BigDecimal getValorPagoLista() {
        return valorPagoLista;
    }

    public void setValorPagoLista(BigDecimal valorPagoLista) {
        this.valorPagoLista = valorPagoLista;
    }

    private BigDecimal auxiliar = BigDecimal.ZERO;
    private BigDecimal auxiliarRecibo;

    public BigDecimal getAuxiliarRecibo() {
        return auxiliarRecibo;
    }

    public void setAuxiliarRecibo(BigDecimal auxiliarRecibo) {
        this.auxiliarRecibo = auxiliarRecibo;
    }

    public BigDecimal getAuxiliar() {
        return auxiliar;
    }

    public void setAuxiliar(BigDecimal auxiliar) {
        this.auxiliar = auxiliar;
    }

    private String confirmaOpcaoDeRecebimento = "NAO";
    private String confirmaOpcaoDeJuros = "JUROS";

    public String getConfirmaOpcaoDeJuros() {
        return confirmaOpcaoDeJuros;
    }

    public void setConfirmaOpcaoDeJuros(String confirmaOpcaoDeJuros) {
        this.confirmaOpcaoDeJuros = confirmaOpcaoDeJuros;
    }

    public void verificaOpcaoRecebimento() {

        if (auxiliar.compareTo(totalDesconto) == 1) {
            confirmaOpcaoDeJuros = "JUROS";
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('confirmaMaior').show();");

        }

        if (auxiliar.compareTo(totalDesconto) == -1) {
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('confirmaMenor').show();");

        }

        if (auxiliar.compareTo(totalDesconto) == 0) {
            org.primefaces.context.RequestContext.getCurrentInstance().execute("PF('confirma').show();");

        }

    }

    public String getConfirmaOpcaoDeRecebimento() {
        return confirmaOpcaoDeRecebimento;
    }

    public void setConfirmaOpcaoDeRecebimento(String confirmaOpcaoDeRecebimento) {
        this.confirmaOpcaoDeRecebimento = confirmaOpcaoDeRecebimento;
    }

    public void confirmaJuros() {

        confirmaOpcaoDeRecebimento = "JUROS";

    }

    public String salvar12() {
        System.out.println("Comecei a salvar");
        auxiliarRecibo = auxiliar;
        somaJurosSelecao();
        if (auxiliarRecibo.compareTo(totalDesconto) == 0) {

            for (int i = 0; i < listaPagamento.size(); i++) {
                listaPagamento.get(i).setDtPagamento(dtPagamentoSelecao);
                listaPagamento.get(i).setStatus("RECEBIDO");
                listaPagamento.get(i).setValorPago(listaPagamento.get(i).getValorAPagar());
                listaPagamento.get(i).setValor(listaPagamento.get(i).getValorAPagar());
                listaPagamento.get(i).setUltimoPagamento(listaPagamento.get(i).getValorAPagar());
                listaPagamento.get(i).setRestosAPagar(BigDecimal.ZERO);
                if (tipoDocumento.equals("DINHEIRO")) {
                    contasReceber1Facade.lancarCaixa(listaPagamento.get(i));

                } else if (tipoDocumento.equals("CARTÃO CRÉDITO")) {

                    contasReceber1Facade.lancarCartao(listaPagamento.get(i), dtVencimentoFatura, bandeira);

                } else if (tipoDocumento.equals("CARTÃO DÉBITO")) {

                    contasReceber1Facade.lancarCartaoDebito(listaPagamento.get(i), dtVencimentoFatura, bandeira);

                } else if (tipoDocumento.equals("CHEQUE")) {

                    contasReceber1Facade.lancarCheque(listaPagamento.get(i), numeroDoc, dtVencimentoFatura, banco);
                } else if (tipoDocumento.equals("CONTA BANCÁRIA")) {

                     contasReceber1Facade.lancarConta(listaPagamento.get(i), listaPagamento.get(i).getUltimoPagamento(), contaBancaria.getId());
                }

                contasReceber1Facade.salvar(listaPagamento.get(i));

                if (listaPagamento.get(i).getValorPago().compareTo(BigDecimal.ZERO) == 1) {

                    contasRecebidas = new ContasRecebidas();
                    Long id1 = contasRecebidasFacade.listarTodos().get(0).getId();
                    contasRecebidas.setId(id1 + 1);
                    contasRecebidas.setPessoa(listaPagamento.get(i).getPessoa());
                    contasRecebidas.setValorPrincipal(listaPagamento.get(i).getValorPago());
                    contasRecebidas.setDtPagamento(listaPagamento.get(i).getDtPagamento());
                    contasRecebidas.setDescricao(listaPagamento.get(i).getId().toString());
                    contasRecebidas.setVenda(listaPagamento.get(i).getVenda());
                    contasRecebidas.setStatus("RECEBIDO");
                    contasRecebidasFacade.salvar(contasRecebidas);

                }

            }

        }
        if (auxiliarRecibo.compareTo(totalDesconto) == 1) {
            System.out.println("Juros Confirmados");
            System.out.println("TotalDesconto" + totalDesconto);
            System.out.println("Juros++" + confirmaOpcaoDeRecebimento);
            if (confirmaOpcaoDeJuros.equals("JUROS")) {
                BigDecimal percentualJuros = BigDecimal.ZERO;
                BigDecimal auxJuros = BigDecimal.ZERO;
                BigDecimal auxMult = BigDecimal.ZERO;
                BigDecimal auxDiv = BigDecimal.ZERO;
                BigDecimal auxSoma = BigDecimal.ZERO;
                for (int i = 0; i < listaPagamento.size(); i++) {

                    listaPagamento.get(i).setStatus("RECEBIDO");
                    listaPagamento.get(i).setDtPagamento(dtPagamentoSelecao);
                    auxJuros = auxiliarRecibo.subtract(totalDesconto);
                    percentualJuros = auxJuros.multiply(new BigDecimal("100")).divide(totalDesconto, 6, RoundingMode.UP);

                    auxMult = percentualJuros.multiply(listaPagamento.get(i).getValorAPagar());
                    auxDiv = auxMult.divide(new BigDecimal("100"), 6, RoundingMode.UP);
                    auxSoma = auxDiv;
                    listaPagamento.get(i).setValorAPagar(listaPagamento.get(i).getValorAPagar().add(auxSoma));
                    listaPagamento.get(i).setValorPago(listaPagamento.get(i).getValorAPagar());
                    listaPagamento.get(i).setUltimoPagamento(listaPagamento.get(i).getValorAPagar());
                    listaPagamento.get(i).setJuros(auxSoma);
                    listaPagamento.get(i).setRestosAPagar(BigDecimal.ZERO);
                    if (tipoDocumento.equals("DINHEIRO")) {
                        contasReceber1Facade.lancarCaixa(listaPagamento.get(i));

                    } else if (tipoDocumento.equals("CARTÃO CRÉDITO")) {

                        contasReceber1Facade.lancarCartao(listaPagamento.get(i), dtVencimentoFatura, bandeira);

                    } else if (tipoDocumento.equals("CARTÃO DÉBITO")) {

                        contasReceber1Facade.lancarCartaoDebito(listaPagamento.get(i), dtVencimentoFatura, bandeira);

                    } else if (tipoDocumento.equals("CHEQUE")) {

                        contasReceber1Facade.lancarCheque(listaPagamento.get(i), numeroDoc, dtVencimentoFatura, banco);
                    } else if (tipoDocumento.equals("CONTA BANCÁRIA")) {

                         contasReceber1Facade.lancarConta(listaPagamento.get(i), listaPagamento.get(i).getUltimoPagamento(), contaBancaria.getId());
                    }

                    contasReceber1Facade.salvar(listaPagamento.get(i));
                    if (listaPagamento.get(i).getValorPago().compareTo(BigDecimal.ZERO) == 1) {

                        contasRecebidas = new ContasRecebidas();
                        Long id1 = contasRecebidasFacade.listarTodos().get(0).getId();
                        contasRecebidas.setId(id1 + 1);
                        contasRecebidas.setPessoa(listaPagamento.get(i).getPessoa());
                        contasRecebidas.setValorPrincipal(listaPagamento.get(i).getValorPago());
                        contasRecebidas.setDtPagamento(listaPagamento.get(i).getDtPagamento());
                        contasRecebidas.setDescricao(listaPagamento.get(i).getId().toString());
                        contasRecebidas.setVenda(listaPagamento.get(i).getVenda());
                        contasRecebidas.setStatus("RECEBIDO");
                        contasRecebidasFacade.salvar(contasRecebidas);

                    }
                }

            }
        }
        if (auxiliarRecibo.compareTo(totalDesconto) == -1) {

            if (confirmaOpcaoDeRecebimento.equals("DESCONTO")) {
                BigDecimal percentualJuros = BigDecimal.ZERO;
                BigDecimal auxJuros = BigDecimal.ZERO;
                BigDecimal auxMult = BigDecimal.ZERO;
                BigDecimal auxDiv = BigDecimal.ZERO;
                BigDecimal auxSoma = BigDecimal.ZERO;
                for (int i = 0; i < listaPagamento.size(); i++) {

                    listaPagamento.get(i).setStatus("RECEBIDO");
                    listaPagamento.get(i).setDtPagamento(dtPagamentoSelecao);
                    auxJuros = totalDesconto.subtract(auxiliarRecibo);
                    percentualJuros = auxJuros.multiply(new BigDecimal("100")).divide(totalDesconto, 6, RoundingMode.UP);

                    auxMult = percentualJuros.multiply(listaPagamento.get(i).getValorAPagar());
                    auxDiv = auxMult.divide(new BigDecimal("100"), 6, RoundingMode.UP);
                    auxSoma = auxDiv;
                    listaPagamento.get(i).setValorAPagar(listaPagamento.get(i).getValorAPagar().subtract(auxSoma));
                    listaPagamento.get(i).setValorPago(listaPagamento.get(i).getValorAPagar());
                    listaPagamento.get(i).setUltimoPagamento(listaPagamento.get(i).getValorAPagar());
                    listaPagamento.get(i).setDesconto(auxSoma);
                    listaPagamento.get(i).setRestosAPagar(BigDecimal.ZERO);
                    if (tipoDocumento.equals("DINHEIRO")) {
                        contasReceber1Facade.lancarCaixa(listaPagamento.get(i));

                    } else if (tipoDocumento.equals("CARTÃO CRÉDITO")) {

                        contasReceber1Facade.lancarCartao(listaPagamento.get(i), dtVencimentoFatura, bandeira);

                    } else if (tipoDocumento.equals("CARTÃO DÉBITO")) {

                        contasReceber1Facade.lancarCartaoDebito(listaPagamento.get(i), dtVencimentoFatura, bandeira);

                    } else if (tipoDocumento.equals("CHEQUE")) {

                        contasReceber1Facade.lancarCheque(listaPagamento.get(i), numeroDoc, dtVencimentoFatura, banco);
                    } else if (tipoDocumento.equals("CONTA BANCÁRIA")) {

                         contasReceber1Facade.lancarConta(listaPagamento.get(i), listaPagamento.get(i).getUltimoPagamento(), contaBancaria.getId());
                    }

                    contasReceber1Facade.salvar(listaPagamento.get(i));
                    if (listaPagamento.get(i).getValorPago().compareTo(BigDecimal.ZERO) == 1) {

                        contasRecebidas = new ContasRecebidas();
                        Long id1 = contasRecebidasFacade.listarTodos().get(0).getId();
                        contasRecebidas.setId(id1 + 1);
                        contasRecebidas.setPessoa(listaPagamento.get(i).getPessoa());
                        contasRecebidas.setValorPrincipal(listaPagamento.get(i).getValorPago());
                        contasRecebidas.setDtPagamento(listaPagamento.get(i).getDtPagamento());
                        contasRecebidas.setDescricao(listaPagamento.get(i).getId().toString());
                        contasRecebidas.setVenda(listaPagamento.get(i).getVenda());
                        contasRecebidas.setStatus("RECEBIDO");
                        contasRecebidasFacade.salvar(contasRecebidas);

                    }
                }

            }
            if (confirmaOpcaoDeRecebimento.equals("BAIXAPARCIAL")) {

                for (int i = 0; i < listaPagamento.size(); i++) {
                    if (auxiliar.compareTo(listaPagamento.get(i).getValorAPagar()) >= 0) {
                        listaPagamento.get(i).setRestosAPagar(BigDecimal.ZERO);
                        listaPagamento.get(i).setUltimoPagamento(listaPagamento.get(i).getValorAPagar());
                        listaPagamento.get(i).setValorPago(listaPagamento.get(i).getValorAPagar());
                        listaPagamento.get(i).setValorAPagar(BigDecimal.ZERO);
                        listaPagamento.get(i).setStatus("RECEBIDO");
                        listaPagamento.get(i).setDtPagamento(dtPagamentoSelecao);
                        auxiliar = auxiliar.subtract(listaPagamento.get(i).getValorPago());

                        if (tipoDocumento.equals("DINHEIRO")) {
                            contasReceber1Facade.lancarCaixa(listaPagamento.get(i));

                        } else if (tipoDocumento.equals("CARTÃO CRÉDITO")) {

                            contasReceber1Facade.lancarCartao(listaPagamento.get(i), dtVencimentoFatura, bandeira);

                        } else if (tipoDocumento.equals("CARTÃO DÉBITO")) {

                            contasReceber1Facade.lancarCartaoDebito(listaPagamento.get(i), dtVencimentoFatura, bandeira);

                        } else if (tipoDocumento.equals("CHEQUE")) {

                            contasReceber1Facade.lancarCheque(listaPagamento.get(i), numeroDoc, dtVencimentoFatura, banco);
                        } else if (tipoDocumento.equals("CONTA BANCÁRIA")) {

                            contasReceber1Facade.lancarConta(listaPagamento.get(i), listaPagamento.get(i).getUltimoPagamento(), contaBancaria.getId());
                        }

                    }

                    contasReceber1Facade.salvar(listaPagamento.get(i));
                    if (auxiliar.compareTo(listaPagamento.get(i).getValorAPagar()) == -1) {

                        listaPagamento.get(i).setStatus("RESTOS A PAGAR");
                        listaPagamento.get(i).setDtPagamento(dtPagamentoSelecao);
                        listaPagamento.get(i).setValorPago(auxiliar);
                        listaPagamento.get(i).setUltimoPagamento(auxiliar);
                        listaPagamento.get(i).setValorAPagar(listaPagamento.get(i).getValorAPagar().subtract(auxiliar));
                        listaPagamento.get(i).setRestosAPagar(listaPagamento.get(i).getValorAPagar());
                        auxiliar = auxiliar.subtract(listaPagamento.get(i).getValorPago());

                        if (tipoDocumento.equals("DINHEIRO")) {
                            contasReceber1Facade.lancarCaixa(listaPagamento.get(i));

                        } else if (tipoDocumento.equals("CARTÃO CRÉDITO")) {

                            contasReceber1Facade.lancarCartao(listaPagamento.get(i), dtVencimentoFatura, bandeira);

                        } else if (tipoDocumento.equals("CARTÃO DÉBITO")) {

                            contasReceber1Facade.lancarCartaoDebito(listaPagamento.get(i), dtVencimentoFatura, bandeira);

                        } else if (tipoDocumento.equals("CHEQUE")) {

                            contasReceber1Facade.lancarCheque(listaPagamento.get(i), numeroDoc, dtVencimentoFatura, banco);
                        } else if (tipoDocumento.equals("CONTA BANCÁRIA")) {

                             contasReceber1Facade.lancarConta(listaPagamento.get(i), listaPagamento.get(i).getUltimoPagamento(), contaBancaria.getId());
                        }

                    }

                    contasReceber1Facade.salvar(listaPagamento.get(i));
                    if (listaPagamento.get(i).getValorPago().compareTo(BigDecimal.ZERO) == 1) {

                        contasRecebidas = new ContasRecebidas();
                        Long id1 = contasRecebidasFacade.listarTodos().get(0).getId();
                        contasRecebidas.setId(id1 + 1);
                        contasRecebidas.setPessoa(listaPagamento.get(i).getPessoa());
                        contasRecebidas.setValorPrincipal(listaPagamento.get(i).getValorPago());
                        contasRecebidas.setDtPagamento(listaPagamento.get(i).getDtPagamento());
                        contasRecebidas.setDescricao(listaPagamento.get(i).getId().toString());
                        contasRecebidas.setVenda(listaPagamento.get(i).getVenda());
                        contasRecebidas.setStatus("RECEBIDO");
                        contasRecebidasFacade.salvar(contasRecebidas);

                    }
                }
            }

        }

        totalDesconto = BigDecimal.ZERO;
        valorPagoLista = BigDecimal.ZERO;
        desconto = BigDecimal.ZERO;
        auxiliar = BigDecimal.ZERO;

        recibo = new Recibo();

        recibo.setDt(dtPagamentoSelecao);
        recibo.setPessoa(listaPagamento.get(0).getPessoa());
        recibo.setValor(auxiliarRecibo);

        reciboFacade.salvar(recibo);
        reciboImpressao = reciboFacade.listarTodos().get(0);
        return "confirma?faces-redirect=true";

    }

    public String voltarLista() {
        validaEmissão = "NAO";
        return "/contasReceber/list?faces-redirect=true";
    }

    private String validaEmissão = "NAO";
    private String validaNotaSimNao = "NÃO";

    public String getValidaEmissão() {
        return validaEmissão;
    }

    public void setValidaEmissão(String validaEmissão) {
        this.validaEmissão = validaEmissão;
    }

    public String getValidaNotaSimNao() {
        return validaNotaSimNao;
    }

    public void setValidaNotaSimNao(String validaNotaSimNao) {
        this.validaNotaSimNao = validaNotaSimNao;
    }

    public ReciboFacade getReciboFacade() {
        return reciboFacade;
    }

    public void setReciboFacade(ReciboFacade reciboFacade) {
        this.reciboFacade = reciboFacade;
    }

    public void validaEmissao() {
        validaEmissão = "SIM";
        validaNotaSimNao = "TR";
    }

    private Recibo recibo;
    private Recibo reciboImpressao;
    @EJB
    private ReciboFacade reciboFacade;

    public Recibo getReciboImpressao() {
        return reciboImpressao;
    }

    public void setReciboImpressao(Recibo reciboImpressao) {
        this.reciboImpressao = reciboImpressao;
    }

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }

    public String salvarGerar() {

        contasReceber1.validaP();
        contasReceber1Facade.salvar(contasReceber1);
        contasReceber1Facade.gerarSalvar(contasReceber1);

        return "list?faces-redirect=true";

    }

    public String excluir(ContasReceber g) {

        try {
            contasReceber1Facade.excluir(g);
        } catch (Exception e) {

            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Não foi possível estornar, verifique!", ""));
            return "list?faces-redirect=true";
        }

        FacesContext.getCurrentInstance().
                addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                "Item estornado com sucesso!", ""));
        return "list";

    }

    public List<ContasReceber> getListaContasReceber1s() {

        return contasReceber1Facade.validaMensalidadeAPagar2(pessoa);
    }

    public List<ContasReceber> getListaTodos() {
        return contasReceber1Facade.listarTodos();
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    public ContasReceber getContasReceber1() {
        return contasReceber1;
    }

    public void setContasReceber1(ContasReceber contasReceber1) {
        this.contasReceber1 = contasReceber1;
    }

    public String getAux() {
        return aux;
    }

    public void setAux(String aux) {
        this.aux = aux;
    }
}
