/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import br.com.curso.ejb.StatusNotaFacade;
import br.com.curso.ejb.VendaFacade;
import br.com.curso.entidade.Empresa;
import br.com.curso.entidade.ItemVenda;
import br.com.curso.entidade.StatusNota;
import br.com.curso.entidade.Venda;
import br.com.nfe.webservice.externo.Acao;
import br.com.nfe.webservice.externo.Cliente;
import br.com.nfe.webservice.externo.ComunicacaoWs;
import br.com.nfe.webservice.externo.Danfe;
import br.com.nfe.webservice.externo.DuplicataNfeWs;
import br.com.nfe.webservice.externo.ItemNfeWs;
import br.com.nfe.webservice.externo.NfeResposta;
import br.com.nfe.webservice.externo.NotaFiscalWsExterno;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author ricardo
 */
@Stateless
@ManagedBean
@RequestScoped
public class Consulta implements Serializable {

    private int idVenda;
    @EJB
    private VendaFacade vendaFacade;
    private Empresa empresa;
    @EJB
    private StatusNotaFacade statusNotaFacade;
    private ItemVenda itemVenda;
    private String acao = "Transmissao";

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public void enviarNfe55(Venda venda) {
        String xml1;
        vendaFacade.carregaVenda(venda);
        try {
            Cliente c = new Cliente();
            c.setBairro(venda.getPessoa().getBairro());
            c.setCep(venda.getPessoa().getCep());
            c.setCidade(venda.getPessoa().getCidade().getNome());

            if (venda.getCpfNota() == null && venda.getCnpjNota() == null) {
                c.setCpfCnpj("");

            } else if (venda.getCnpjNota() == null) {

                c.setCpfCnpj(venda.getCpfNota());
                c.setRazaoSocial("");

                System.out.println("___>" + c.getCpfCnpj());

            } else if (venda.getCpfNota() == null) {

                c.setCpfCnpj(venda.getCnpjNota());
                c.setRazaoSocial(venda.getPessoa().getRazao());

            }

            c.setEmail(venda.getPessoa().getEmail());
            c.setFantasia(venda.getPessoa().getNome());
            c.setFone(venda.getPessoa().getFone());
            c.setIe(venda.getPessoa().getDocumentoEstadual());
            c.setLogradouro(venda.getPessoa().getEndereco());
            c.setNumero(venda.getPessoa().getNumero());
            c.setUf(venda.getPessoa().getCidade().getEstado().getSigla());

            NotaFiscalWsExterno nota = new NotaFiscalWsExterno();
            nota.setCpfNaNota("");
            nota.setInfComplementar(venda.getObs());
            nota.setNatOp("Venda");
            nota.setEmitente(0);
            nota.setFormaPg("A");
            nota.setVendaId(Integer.parseInt(venda.getId().toString()));
            nota.setModelo("55");
            nota.setCliente(c);

            for (int i = 0; i < venda.getParcelas().size(); i++) {

                Integer num = i + 1;
                DuplicataNfeWs duplicata = new DuplicataNfeWs();
                duplicata.setNfeWs(nota);
                duplicata.setNumeroDup(num.toString());
                duplicata.setValorDup(venda.getParcelas().get(i).getValor().toString());
                duplicata.setVencimento(venda.getParcelas().get(i).getVencimento());
                System.out.println("Aqui parcela" + duplicata.getValorDup());
                System.out.println("Aqui parcela" + duplicata.getVencimento());
                nota.getDuplicatas().add(duplicata);

            }
            BigDecimal contador = BigDecimal.ZERO;
            BigDecimal verificaDesconto = BigDecimal.ZERO;
            Integer aux = -1;
            for (int i = 0; i < venda.getItensVenda().size(); i++) {

                ItemNfeWs item = new ItemNfeWs();
                item.setCodigo(venda.getItensVenda().get(i).getPlantio().getId().toString());
                item.setDescricao(venda.getItensVenda().get(i).getPlantio().getNome());
                item.setNcm(venda.getItensVenda().get(i).getPlantio().getNcmString());
                item.setNfeWs(nota);
                 //------ICMS-------
                item.setIcms(venda.getItensVenda().get(i).getPlantio().getPercentualDeIcms());
                item.setIcmsCst(venda.getItensVenda().get(i).getPlantio().getIcmsCst());
                item.setIcmsModBc(venda.getItensVenda().get(i).getPlantio().getModalidade());
                item.setIcmsMotDes(venda.getItensVenda().get(i).getPlantio().getMotivoDesoneracao());
                item.setIcmsOrigem(venda.getItensVenda().get(i).getPlantio().getOrigem());
                item.setIcmsPredBc(venda.getItensVenda().get(i).getPlantio().getPercentualDeIcmsBaseC());
                //--------IPI-----------
                item.setIpi(venda.getItensVenda().get(i).getPlantio().getPercentualIpi());
                item.setIpiCst(venda.getItensVenda().get(i).getPlantio().getIpiCst());
                item.setIpiCenq(venda.getItensVenda().get(i).getPlantio().getCodEnquadramentoIpi());
                //--------PIS-----------
                item.setPis("0.00");
                item.setPisCst(venda.getItensVenda().get(i).getPlantio().getPisCst());
                //--------COFINS-----------
                item.setCofins("0.00");
                item.setCofinsCst(venda.getItensVenda().get(i).getPlantio().getCofinsCst());
                
                item.setQtdComercial(venda.getItensVenda().get(i).getQuantidade().toString());
                item.setQtdTributrada(venda.getItensVenda().get(i).getQuantidade().toString());
                item.setUnComercial("uni");
                item.setUnTributada("uni");
                
                if (venda.getEmpresa().getId() == 2) {

                    if (venda.getPessoa().getCidade().getEstado().getNome().equals("MATO GROSSO DO SUL")) {

                        item.setCfop("5405");

                    }
                    if (!venda.getPessoa().getCidade().getEstado().getNome().equals("MATO GROSSO DO SUL")) {

                        item.setCfop("6405");
                    }

                } else {

                    if (venda.getPessoa().getCidade().getEstado().getNome().equals("PARANÁ")) {

                        if (!venda.getItensVenda().get(i).getPlantio().getCfopD().equals("")) {

                            item.setCfop(venda.getItensVenda().get(i).getPlantio().getCfopD());

                        }

                    }
                    if (!venda.getPessoa().getCidade().getEstado().getNome().equals("PARANÁ")) {

                        if (!venda.getItensVenda().get(i).getPlantio().getCfopF().equals("")) {

                            item.setCfop(venda.getItensVenda().get(i).getPlantio().getCfopF());

                        }
                    }
                }
                item.setValorUnComercial(venda.getItensVenda().get(i).getValor().toString());
                item.setValorUnTributrada(venda.getItensVenda().get(i).getValor().toString());

                BigDecimal percentualDesconto;

                if (venda.getValorDesconto().compareTo(BigDecimal.ZERO) == 1) {

                    if (i == venda.getItensVenda().size() - 1) {
                        System.out.println("HOLA QUE TAL" + i);
                        System.out.println("Contador" + contador);
                        percentualDesconto = venda.getValorDesconto().subtract(verificaDesconto);
                        System.out.println("Percentual" + percentualDesconto);
                        item.setValorDesconto(percentualDesconto.toString());
                    } else {
                        System.out.println("Sem contador");
                        percentualDesconto = venda.getItensVenda().get(i).getSubTotal()
                                .multiply(venda.getDesconto().divide(new BigDecimal("100")));

                        contador = contador.add(percentualDesconto);

                        percentualDesconto = percentualDesconto.setScale(2, RoundingMode.HALF_UP);
                        item.setValorDesconto(percentualDesconto.toString());
                    }
                } else {
                    item.setValorDesconto("0");
                }

                verificaDesconto = verificaDesconto.add(new BigDecimal(item.getValorDesconto()));
                System.out.println("-->VerificaDesconto" + verificaDesconto);
                nota.getItens().add(item);
                aux = i;
            }

            List<StatusNota> statusNota = statusNotaFacade.listaUm(venda.getEmpresa().getId());

            NfeResposta nr = ComunicacaoWs.comunicar(nota, "107.170.17.170", "8080", "infomania",
                    statusNota.get(0).getLogin(), statusNota.get(0).getSenha(),
                    statusNota.get(0).getNome(), Acao.TRANSMITIR);
//            NfeResposta nr = ComunicacaoWs.comunicar(nota, "67.205.107.224", "10769", "infomania", "giovani", "giovani", "2", Acao.TRANSMITIR);

            if (nr != null) {
                if (nr.getStatus().equals("1")) {
                    xml1 = nr.getResposta().get(0);
                    venda.setXml(nr.getResposta().get(0));
                    venda.setModeloNota("55");
                    venda.setFiscal(Boolean.TRUE);
                    System.out.println("AQUI PORRA" + venda.getXml());

                    FacesContext fc = FacesContext.getCurrentInstance();
                    ServletContext sc = (ServletContext) fc.getExternalContext().getContext();
                    String caminhoReal = sc.getRealPath("/");
                    Danfe.gerarDanfe(nr.getResposta().get(0), "C:\\bokao1.png", "C:\\danfe\\nota" + venda.getId() + ".pdf", caminhoReal);
                    System.out.println("123-->" + caminhoReal);
                    vendaFacade.salvarNota(venda);
                } else {
                    for (String o : nr.getResposta()) {
                        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, o, "");
                        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                        venda.setNaoTransmitida(Boolean.TRUE);
                        venda.setModeloNota("55");
                        vendaFacade.salvarNota(venda);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public StatusNotaFacade getStatusNotaFacade() {
        return statusNotaFacade;
    }

    public void setStatusNotaFacade(StatusNotaFacade statusNotaFacade) {
        this.statusNotaFacade = statusNotaFacade;
    }

    public void enviarNfe55Retorno(Venda venda) {
        String xml1;
        vendaFacade.carregaVenda(venda);
        try {
            Cliente c = new Cliente();
            c.setBairro(venda.getPessoa().getBairro());
            c.setCep(venda.getPessoa().getCep());
            c.setCidade(venda.getPessoa().getCidade().getNome());

            if (venda.getCpfNota() == null && venda.getCnpjNota() == null) {
                c.setCpfCnpj("");

            } else if (venda.getCnpjNota() == null) {

                c.setCpfCnpj(venda.getCpfNota());
                c.setRazaoSocial("");

                System.out.println("___>" + c.getCpfCnpj());

            } else if (venda.getCpfNota() == null) {

                c.setCpfCnpj(venda.getCnpjNota());
                c.setRazaoSocial(venda.getPessoa().getRazao());

            }

            c.setEmail(venda.getPessoa().getEmail());
            c.setFantasia(venda.getPessoa().getNome());
            c.setFone(venda.getPessoa().getFone());
            c.setIe(venda.getPessoa().getDocumentoEstadual());
            c.setLogradouro(venda.getPessoa().getEndereco());
            c.setNumero(venda.getPessoa().getNumero());
            c.setUf(venda.getPessoa().getCidade().getEstado().getSigla());

            NotaFiscalWsExterno nota = new NotaFiscalWsExterno();
            nota.setCpfNaNota("");
            nota.setInfComplementar(venda.getObs());
            nota.setNatOp("Venda");
            nota.setEmitente(0);
            nota.setFormaPg("A");
            nota.setVendaId(Integer.parseInt(venda.getId().toString()));
            nota.setModelo("55");
            nota.setCliente(c);

            for (int i = 0; i < venda.getParcelas().size(); i++) {

                Integer num = i + 1;
                DuplicataNfeWs duplicata = new DuplicataNfeWs();
                duplicata.setNfeWs(nota);
                duplicata.setNumeroDup(num.toString());
                duplicata.setValorDup(venda.getParcelas().get(i).getValor().toString());
                duplicata.setVencimento(venda.getParcelas().get(i).getVencimento());
                System.out.println("Aqui parcela" + duplicata.getValorDup());
                System.out.println("Aqui parcela" + duplicata.getVencimento());
                nota.getDuplicatas().add(duplicata);

            }
            BigDecimal contador = BigDecimal.ZERO;
            BigDecimal verificaDesconto = BigDecimal.ZERO;
            Integer aux = -1;
            for (int i = 0; i < venda.getItensVenda().size(); i++) {

                ItemNfeWs item = new ItemNfeWs();
                item.setCodigo(venda.getItensVenda().get(i).getPlantio().getId().toString());
                item.setDescricao(venda.getItensVenda().get(i).getPlantio().getNome());
                item.setNcm(venda.getItensVenda().get(i).getPlantio().getNcmString());
                item.setNfeWs(nota);
                item.setQtdComercial(venda.getItensVenda().get(i).getQuantidade().toString());
                item.setQtdTributrada(venda.getItensVenda().get(i).getQuantidade().toString());
                item.setUnComercial("uni");
                item.setUnTributada("uni");
                if (venda.getPessoa().getCidade().getEstado().getNome().equals("PARANÁ")) {

                    if (!venda.getItensVenda().get(i).getPlantio().getCfopD().equals("")) {

                        item.setCfop(venda.getItensVenda().get(i).getPlantio().getCfopD());

                    }

                }
                if (!venda.getPessoa().getCidade().getEstado().getNome().equals("PARANÁ")) {

                    if (!venda.getItensVenda().get(i).getPlantio().getCfopF().equals("")) {

                        item.setCfop(venda.getItensVenda().get(i).getPlantio().getCfopF());

                    }
                }
                item.setValorUnComercial(venda.getItensVenda().get(i).getValor().toString());
                item.setValorUnTributrada(venda.getItensVenda().get(i).getValor().toString());

                BigDecimal percentualDesconto;

                if (venda.getValorDesconto().compareTo(BigDecimal.ZERO) == 1) {

                    if (i == venda.getItensVenda().size() - 1) {
                        System.out.println("HOLA QUE TAL" + i);
                        System.out.println("Contador" + contador);
                        percentualDesconto = venda.getValorDesconto().subtract(verificaDesconto);
                        System.out.println("Percentual" + percentualDesconto);
                        item.setValorDesconto(percentualDesconto.toString());
                    } else {
                        System.out.println("Sem contador");
                        percentualDesconto = venda.getItensVenda().get(i).getSubTotal()
                                .multiply(venda.getDesconto().divide(new BigDecimal("100")));

                        contador = contador.add(percentualDesconto);

                        percentualDesconto = percentualDesconto.setScale(2, RoundingMode.HALF_UP);
                        item.setValorDesconto(percentualDesconto.toString());
                    }
                } else {
                    item.setValorDesconto("0");
                }

                verificaDesconto = verificaDesconto.add(new BigDecimal(item.getValorDesconto()));
                System.out.println("-->VerificaDesconto" + verificaDesconto);
                nota.getItens().add(item);
                aux = i;
            }

            List<StatusNota> statusNota = statusNotaFacade.listaUm(venda.getEmpresa().getId());

            NfeResposta nr = ComunicacaoWs.comunicar(nota, "107.170.17.170", "8080", "infomania",
                    statusNota.get(0).getLogin(), statusNota.get(0).getSenha(),
                    statusNota.get(0).getNome(), Acao.RETRANSMITIR);
//            NfeResposta nr = ComunicacaoWs.comunicar(nota, "67.205.107.224", "10769", "infomania", "giovani", "giovani", "2", Acao.TRANSMITIR);

            if (nr != null) {
                if (nr.getStatus().equals("1")) {
                    xml1 = nr.getResposta().get(0);
                    venda.setXml(nr.getResposta().get(0));
                    venda.setModeloNota("55");
                    venda.setFiscal(Boolean.TRUE);
                    System.out.println("AQUI PORRA" + venda.getXml());

                    FacesContext fc = FacesContext.getCurrentInstance();
                    ServletContext sc = (ServletContext) fc.getExternalContext().getContext();
                    String caminhoReal = sc.getRealPath("/");
                    Danfe.gerarDanfe(nr.getResposta().get(0), "C:\\bokao1.png", "C:\\danfe\\nota" + venda.getId() + ".pdf", caminhoReal);
                    System.out.println("123-->" + caminhoReal);
                    vendaFacade.salvarNota(venda);
                } else {
                    for (String o : nr.getResposta()) {
                        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, o, "");
                        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                        venda.setNaoTransmitida(Boolean.TRUE);
                        venda.setModeloNota("55");
                        vendaFacade.salvarNota(venda);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public void test() throws IOException {
        String xml = "<nfeProc xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"3.10\"><NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\"><infNFe Id=\"NFe41160508733639000198550010000001191444773806\" versao=\"3.10\"><ide><cUF>41</cUF><cNF>44477380</cNF><natOp>5102</natOp><indPag>0</indPag><mod>55</mod><serie>1</serie><nNF>119</nNF><dhEmi>2016-05-24T19:33:20-03:00</dhEmi><dhSaiEnt>2016-05-24T19:33:20-03:00</dhSaiEnt><tpNF>1</tpNF><idDest>1</idDest><cMunFG>4117107</cMunFG><tpImp>1</tpImp><tpEmis>1</tpEmis><cDV>6</cDV><tpAmb>2</tpAmb><finNFe>1</finNFe><indFinal>1</indFinal><indPres>1</indPres><procEmi>0</procEmi><verProc>0.0.1</verProc></ide><emit><CNPJ>08733639000198</CNPJ><xNome>SAMUEL OLIVEIRA DE LIMA  ME</xNome><xFant>RESTAURANTE DO BOCAO</xFant><enderEmit><xLgr>AV ARISTIDES MARTELO</xLgr><nro>218</nro><xBairro>CENTRO</xBairro><cMun>4117107</cMun><xMun>NOVA LONDRINA</xMun><UF>PR</UF><CEP>87970000</CEP><cPais>1058</cPais><xPais>BRASIL</xPais><fone>4434321054</fone></enderEmit><IE>9039989627</IE><CRT>1</CRT></emit><dest><CNPJ>99999999000191</CNPJ><xNome>NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL</xNome><enderDest><xLgr>RUA FELIPE DOS SANTOS</xLgr><nro>288</nro><xBairro>SAO JORGE</xBairro><cMun>4118402</cMun><xMun>PARANAVAI</xMun><UF>PR</UF><CEP>87710090</CEP><cPais>1058</cPais><xPais>BRASIL</xPais><fone>9999999999</fone></enderDest><indIEDest>2</indIEDest><email>ricardo@ricardo.com</email></dest><det nItem=\"1\"><prod><cProd>01</cProd><cEAN/><xProd>ANEL</xProd><NCM>90189029</NCM><CFOP>5102</CFOP><uCom>uni</uCom><qCom>1.0000</qCom><vUnCom>10.000000</vUnCom><vProd>10.00</vProd><cEANTrib/><uTrib>uni</uTrib><qTrib>1.0000</qTrib><vUnTrib>10.000000</vUnTrib><indTot>1</indTot></prod><imposto><ICMS><ICMSSN102><orig>0</orig><CSOSN>103</CSOSN></ICMSSN102></ICMS><IPI><cEnq>301</cEnq><IPINT><CST>52</CST></IPINT></IPI><PIS><PISNT><CST>07</CST></PISNT></PIS><COFINS><COFINSNT><CST>07</CST></COFINSNT></COFINS></imposto></det><det nItem=\"2\"><prod><cProd>02</cProd><cEAN/><xProd>BRINCO</xProd><NCM>90189029</NCM><CFOP>5102</CFOP><uCom>uni</uCom><qCom>1.0000</qCom><vUnCom>10.000000</vUnCom><vProd>10.00</vProd><cEANTrib/><uTrib>uni</uTrib><qTrib>1.0000</qTrib><vUnTrib>10.000000</vUnTrib><indTot>1</indTot></prod><imposto><ICMS><ICMSSN102><orig>0</orig><CSOSN>103</CSOSN></ICMSSN102></ICMS><IPI><cEnq>301</cEnq><IPINT><CST>52</CST></IPINT></IPI><PIS><PISNT><CST>07</CST></PISNT></PIS><COFINS><COFINSNT><CST>07</CST></COFINSNT></COFINS></imposto></det><total><ICMSTot><vBC>0.00</vBC><vICMS>0.00</vICMS><vICMSDeson>0.00</vICMSDeson><vBCST>0.00</vBCST><vST>0.00</vST><vProd>20.00</vProd><vFrete>0.00</vFrete><vSeg>0.00</vSeg><vDesc>0.00</vDesc><vII>0.00</vII><vIPI>0.00</vIPI><vPIS>0.00</vPIS><vCOFINS>0.00</vCOFINS><vOutro>0.00</vOutro><vNF>20.00</vNF><vTotTrib>0.00</vTotTrib></ICMSTot></total><transp><modFrete>9</modFrete></transp></infNFe><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/><Reference URI=\"#NFe41160508733639000198550010000001191444773806\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/><Transform Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/><DigestValue>oT+Uu2x+megWdlgTtXfzAaJR79M=</DigestValue></Reference></SignedInfo><SignatureValue>l1KUrI38P6v5c8OAtZaICq4/fI1eQ1aQJNzuNXxLCUJpjULIWX2VTKOH8S5TzRR0SnkzUC+mPSiVKutywC1c6QKr0TBFa0NNdrVf+wwS/etGKSZbmp8QdaDFl9cmOEX3IsJNRe/YzVHgtL7ZcRP0k5IrvfgUQARbiHHLMQYaNGBi4u6gERVXEh9qHxl7P4VPuxKT3GxMppyhoik4udRhhRSBaUo+Tny/lGQ9o9PY80rs8OmygSRvKdmPTRWWAn4DjgIsbcxnM9h8I6ivLaAveQrvtuTSkaa9XvkrLGvQOQ1RytMMHEJG/M44hX4XHdmA0bq/x1TmjGZdyy76nAFeRQ==</SignatureValue><KeyInfo><X509Data><X509Certificate>MIIIUTCCBjmgAwIBAgIQNJyZNXj6Lgb/O0x80uCbFjANBgkqhkiG9w0BAQsFADB4MQswCQYDVQQGEwJCUjETMBEGA1UEChMKSUNQLUJyYXNpbDE2MDQGA1UECxMtU2VjcmV0YXJpYSBkYSBSZWNlaXRhIEZlZGVyYWwgZG8gQnJhc2lsIC0gUkZCMRwwGgYDVQQDExNBQyBDZXJ0aXNpZ24gUkZCIEc0MB4XDTE1MDkxNTAwMDAwMFoXDTE2MDkxMzIzNTk1OVowge8xCzAJBgNVBAYTAkJSMRMwEQYDVQQKFApJQ1AtQnJhc2lsMQswCQYDVQQIEwJQUjEWMBQGA1UEBxQNTm92YSBMb25kcmluYTE2MDQGA1UECxQtU2VjcmV0YXJpYSBkYSBSZWNlaXRhIEZlZGVyYWwgZG8gQnJhc2lsIC0gUkZCMRYwFAYDVQQLFA1SRkIgZS1DTlBKIEExMSIwIAYDVQQLFBlBdXRlbnRpY2FkbyBwb3IgQVIgRkFDSUFQMTIwMAYDVQQDEylTQU1VRUwgT0xJVkVJUkEgREUgTElNQSBNRTowODczMzYzOTAwMDE5ODCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAM2ajrQNCg3bvuFGGn6iHsEVF9Ix/i/2GjWUnjYbOWW2notU36AMfwrc7a29r7szyj7+oUUJhKzo9JHUT4Ls5sXY+oby1VcPivlPt8tNrWcJdsYUatNb5r/tu9TRosmntt0VglepiAOOnlBwnalC8Zbw3z5wTgpi7Wu0PNjPmXrq1vMH/pwBOZ7FugdjKv2mcqBF12Ewpc2Q/k/lk47sIedaUjT+vhT30sV/dK6Yc+35h7h+Ev8ecf5qz5SEx5XJV1dJOYngdRctihILSwGOED9DCUnxoDeQfgSW0IFfHDtNKbFdt6iXr5YfcJWX8Jh4PvWOb4aNX2DOuhUNkBwzcscCAwEAAaOCA10wggNZMIHCBgNVHREEgbowgbegPgYFYEwBAwSgNQQzMTkwODE5ODQwNDA1OTE1NTk0MjAwMDAwMDAwMDAwMDAwMDAwMDczOTEwNTg3U0VTUFBSoCIGBWBMAQMCoBkEF1NBTVVFTCBPTElWRUlSQSBERSBMSU1BoBkGBWBMAQMDoBAEDjA4NzMzNjM5MDAwMTk4oBcGBWBMAQMHoA4EDDAwMDAwMDAwMDAwMIEdZXNjcml0b3Jpb25vcnRlaW9AaG90bWFpbC5jb20wCQYDVR0TBAIwADAfBgNVHSMEGDAWgBQukerWbeWyWYLcOIUpdjQWVjzQPjAOBgNVHQ8BAf8EBAMCBeAwfwYDVR0gBHgwdjB0BgZgTAECAQwwajBoBggrBgEFBQcCARZcaHR0cDovL2ljcC1icmFzaWwuY2VydGlzaWduLmNvbS5ici9yZXBvc2l0b3Jpby9kcGMvQUNfQ2VydGlzaWduX1JGQi9EUENfQUNfQ2VydGlzaWduX1JGQi5wZGYwggEWBgNVHR8EggENMIIBCTBXoFWgU4ZRaHR0cDovL2ljcC1icmFzaWwuY2VydGlzaWduLmNvbS5ici9yZXBvc2l0b3Jpby9sY3IvQUNDZXJ0aXNpZ25SRkJHNC9MYXRlc3RDUkwuY3JsMFagVKBShlBodHRwOi8vaWNwLWJyYXNpbC5vdXRyYWxjci5jb20uYnIvcmVwb3NpdG9yaW8vbGNyL0FDQ2VydGlzaWduUkZCRzQvTGF0ZXN0Q1JMLmNybDBWoFSgUoZQaHR0cDovL3JlcG9zaXRvcmlvLmljcGJyYXNpbC5nb3YuYnIvbGNyL0NlcnRpc2lnbi9BQ0NlcnRpc2lnblJGQkc0L0xhdGVzdENSTC5jcmwwHQYDVR0lBBYwFAYIKwYBBQUHAwIGCCsGAQUFBwMEMIGbBggrBgEFBQcBAQSBjjCBizBfBggrBgEFBQcwAoZTaHR0cDovL2ljcC1icmFzaWwuY2VydGlzaWduLmNvbS5ici9yZXBvc2l0b3Jpby9jZXJ0aWZpY2Fkb3MvQUNfQ2VydGlzaWduX1JGQl9HNC5wN2MwKAYIKwYBBQUHMAGGHGh0dHA6Ly9vY3NwLmNlcnRpc2lnbi5jb20uYnIwDQYJKoZIhvcNAQELBQADggIBAGmz2e6sgXPtza/mVsFH9zSdIJQccymh5bzUkrQMXPNrw5djECP5YKB3NWnOKKDfR1tioP4kOvqS3Mkp8+jZeNVBQFXhLQhCCsYCPlOO3IV7Bqt4kBqXKcFn2pL6y4huUzZgoCmtp5rS9juEu1qy0expXLryGeF67H8ZWZO0I/NypDgdtHWWDni2gauwGHdrTwNKLjRDikaQypEBox6ZtbLKOjFp1SdPW+VXDljjTHkiw8C7IdM8vX5PnOb9dF/BuauwI+E+zoZDd7xKwfoFm6iaDSFGYf271gSWf8x9DnlK/WeqqM+BuZKWZKWt7O8LpXKw73KKFzaZYv1XORssWrFYo4CwFDzmTLYMfNnq2hz9ZLDNQZhi4+O2gh6FpFb6+DZGHr+kwy5DdCRjXyUKtj55WwPLbmDwaBzz0mqXYX0HbQtaMRHUJn3BsHTL9i2u9lghffqfZd8MDOr/FEU5lrDZsXopRu5NfP9JG5MVoAMKh1/9hzz4v0tYsVQH4pEMTXZK/hIDKbYyIXkiNYeX1HmqJCNvOsUSWu9R3odcugRt7JGJm2C25Rzoh30GCA/MtwODszavApFajWQdypIqPAI8OwV1VY6wq+DSZO/nLi4Dj3NEV8CoO6SLf7bqr/kkQqYN6n2l+kA1QwMA4lYU3wNhBKqzoC89Hf8TvmsqBADP</X509Certificate></X509Data></KeyInfo></Signature></NFe><protNFe versao=\"3.10\"><infProt><tpAmb>2</tpAmb><verAplic>PR-v3_5_9</verAplic><chNFe>41160508733639000198550010000001191444773806</chNFe><dhRecbto>2016-05-24T19:33:25-03:00</dhRecbto><nProt>141160000488262</nProt><digVal>oT+Uu2x+megWdlgTtXfzAaJR79M=</digVal><cStat>100</cStat><xMotivo>Autorizado o uso da NF-e</xMotivo></infProt></protNFe></nfeProc>";
        FacesContext fc = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) fc.getExternalContext().getContext();
        String caminhoReal = sc.getRealPath("/");
        Danfe.gerarDanfe(xml, "C:\\bokao1.png", "C:\\nota.pdf", caminhoReal);
    }

    public void reimprimir(Venda venda) {

        try {

            Danfe.gerarCupom(venda.getXml(), "C:\\bokao1.png");
        } catch (JRException ex) {
            Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void enviarNfe(Venda venda) {
        try {
            String xml1;
            vendaFacade.carregaVenda(venda);

            NotaFiscalWsExterno nota = new NotaFiscalWsExterno();

            if (venda.getCpfNota() == null && venda.getCnpjNota() == null) {
                nota.setCpfNaNota("");

            } else if (venda.getCnpjNota() == null) {

                nota.setCpfNaNota(venda.getCpfNota());

                System.out.println("___>" + nota.getCpfNaNota());

            } else if (venda.getCpfNota() == null) {

                nota.setCpfNaNota(venda.getCnpjNota());

            }

            if (venda.getTipoPagamento().equals("A VISTA")) {

                nota.setFormaPg("A");
            } else {
                nota.setFormaPg("P");

            }

            nota.setEmitente(0);
            nota.setModelo("65");
            nota.setNatOp("Venda");
            nota.setInfComplementar("A");

            nota.setVendaId(Integer.parseInt(venda.getId().toString()));

            for (int i = 0; i < venda.getParcelas().size(); i++) {

                DuplicataNfeWs duplicata = new DuplicataNfeWs();
                duplicata.setNfeWs(nota);
                duplicata.setNumeroDup(venda.getQtdParcela().toString());
                duplicata.setValorDup(venda.getParcelas().get(i).getValor().toString());
                duplicata.setVencimento(venda.getParcelas().get(i).getVencimento());
                nota.getDuplicatas().add(duplicata);
            }

            for (int i = 0; i < venda.getItensVenda().size(); i++) {

                ItemNfeWs item = new ItemNfeWs();

                item.setCodigo(venda.getItensVenda().get(i).getPlantio().getId().toString());
                item.setDescricao(venda.getItensVenda().get(i).getPlantio().getNome());
                item.setNcm(venda.getItensVenda().get(i).getPlantio().getNcmString());
                if (venda.getItensVenda().get(i).getPlantio().getCfopD().equals("5403")) {
                    item.setCfop("5405");

                    item.setIcmsCst("500");
                }
                item.setNfeWs(nota);
                item.setQtdComercial(venda.getItensVenda().get(i).getQuantidade().toString());
                item.setQtdTributrada(venda.getItensVenda().get(i).getQuantidade().toString());
                item.setUnComercial("uni");
                item.setUnTributada("uni");
                item.setValorUnComercial(venda.getItensVenda().get(i).getValor().toString());
                item.setValorUnTributrada(venda.getItensVenda().get(i).getValor().toString());
                BigDecimal percentualDesconto;
                if (venda.getValorDesconto().compareTo(BigDecimal.ZERO) == 1) {
                    percentualDesconto = venda.getItensVenda().get(i).getSubTotal().
                            multiply(venda.getDesconto().divide(new BigDecimal("100")));

                    item.setValorDesconto(percentualDesconto.toString());
                } else {
                    item.setValorDesconto("0");
                }
                nota.getItens().add(item);
            }

            List<StatusNota> statusNota = statusNotaFacade.listaUm(venda.getEmpresa().getId());

            NfeResposta nr = ComunicacaoWs.comunicar(nota, "107.170.17.170", "8080", "infomania",
                    statusNota.get(0).getLogin(), statusNota.get(0).getSenha(),
                    statusNota.get(0).getNome(), Acao.TRANSMITIR);
//            NfeResposta nr = ComunicacaoWs.comunicar(nota, "67.205.107.224", "10769", "infomania", "giovani", "giovani", "2", Acao.TRANSMITIR);

            if (nr != null) {
                if (nr.getStatus().equals("1")) {
                    xml1 = nr.getResposta().get(0);
                    Danfe.gerarCupom(xml1, "C:\\bokao1.png");
                    venda.setXml(nr.getResposta().get(0));
                    venda.setModeloNota("65");

                    venda.setFiscal(Boolean.TRUE);
                    System.out.println("AQUI PORRA" + venda.getXml());
                    vendaFacade.salvarNota(venda);

                } else {
                    for (String o : nr.getResposta()) {
                        venda.setNaoTransmitida(Boolean.TRUE);
                        vendaFacade.salvarNota(venda);
                        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, o, "");
                        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void retransmitirNfe(Venda venda) {
        try {
            String xml1;
            vendaFacade.carregaVenda(venda);

            NotaFiscalWsExterno nota = new NotaFiscalWsExterno();

            if (venda.getCpfNota() == null && venda.getCnpjNota() == null) {
                nota.setCpfNaNota("");

            } else if (venda.getCnpjNota() == null) {

                nota.setCpfNaNota(venda.getCpfNota());

                System.out.println("___>" + nota.getCpfNaNota());

            } else if (venda.getCpfNota() == null) {

                nota.setCpfNaNota(venda.getCnpjNota());

            }

            if (venda.getTipoPagamento().equals("A VISTA")) {

                nota.setFormaPg("A");
            } else {
                nota.setFormaPg("P");

            }

            nota.setEmitente(0);
            nota.setModelo("65");
            nota.setNatOp("Venda");

            nota.setVendaId(Integer.parseInt(venda.getId().toString()));

            for (int i = 0; i < venda.getItensVenda().size(); i++) {

                ItemNfeWs item = new ItemNfeWs();
                item.setCodigo(venda.getItensVenda().get(i).getPlantio().getId().toString());
                item.setDescricao(venda.getItensVenda().get(i).getPlantio().getNome());
                item.setNcm(venda.getItensVenda().get(i).getPlantio().getNcmString());
                item.setNfeWs(nota);
                item.setQtdComercial(venda.getItensVenda().get(i).getQuantidade().toString());
                item.setQtdTributrada(venda.getItensVenda().get(i).getQuantidade().toString());
                item.setUnComercial("uni");
                item.setUnTributada("uni");
                item.setValorUnComercial(venda.getItensVenda().get(i).getValor().toString());
                item.setValorUnTributrada(venda.getItensVenda().get(i).getValor().toString());
                nota.getItens().add(item);
            }

            List<StatusNota> statusNota = statusNotaFacade.listaUm(venda.getEmpresa().getId());

            NfeResposta nr = ComunicacaoWs.comunicar(nota, "107.170.17.170", "8080", "infomania",
                    statusNota.get(0).getLogin(), statusNota.get(0).getSenha(),
                    statusNota.get(0).getNome(), Acao.TRANSMITIR);

            if (nr != null) {
                if (nr.getStatus().equals("1")) {
                    xml1 = nr.getResposta().get(0);
                    Danfe.gerarCupom(xml1, "C:\\bokao1.png");
                    venda.setXml(nr.getResposta().get(0));
                    venda.setModeloNota("65");
                    venda.setFiscal(Boolean.TRUE);
                    venda.setNaoTransmitida(Boolean.FALSE);
                    System.out.println("AQUI PORRA" + venda.getXml());
                    vendaFacade.salvarNota(venda);

                } else {
                    for (String o : nr.getResposta()) {
                        venda.setNaoTransmitida(Boolean.TRUE);
                        vendaFacade.salvarNota(venda);
                        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, o, "");
                        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                    }
                }
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    public void logar() {
        try {

            List<StatusNota> statusNota = statusNotaFacade.listaUm(empresa.getId());
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            ComunicacaoWs.login("107.170.17.170", "8080", "infomania", statusNota.get(0).getLogin(), statusNota.get(0).getSenha(), statusNota.get(0).getNome(), "http://localhost:8080/InfomaniaSistemas/index.xhtml", response);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception ex) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }

    }

    public VendaFacade getVendaFacade() {
        return vendaFacade;
    }

    public void setVendaFacade(VendaFacade vendaFacade) {
        this.vendaFacade = vendaFacade;
    }

    public ItemVenda getItemVenda() {
        return itemVenda;
    }

    public void setItemVenda(ItemVenda itemVenda) {
        this.itemVenda = itemVenda;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

}
