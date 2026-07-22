package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.Cliente;
import com.marlondev.stockflow.domain.Colaborador;
import com.marlondev.stockflow.domain.Almoxarifado;
import com.marlondev.stockflow.domain.AlmoxarifadoEstoque;
import com.marlondev.stockflow.domain.OrdemDeServico;
import com.marlondev.stockflow.domain.OrdemServicoItem;
import com.marlondev.stockflow.repositories.AlmoxarifadoEstoqueRepository;
import com.marlondev.stockflow.repositories.AlmoxarifadoRepository;
import com.marlondev.stockflow.repositories.OrdemDeServicoRepository;
import com.marlondev.stockflow.repositories.OrdemServicoItemRepository;
import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class PdfService {

    private static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat MOEDA_FORMATTER = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
    private static final Color CINZA_ESCURO = new Color(36, 39, 44);
    private static final Color CINZA_CLARO = new Color(232, 235, 238);
    private static final Color BORDA = new Color(170, 170, 170);

    private final OrdemDeServicoRepository ordemDeServicoRepository;
    private final OrdemServicoItemRepository ordemServicoItemRepository;
    private final AlmoxarifadoRepository almoxarifadoRepository;
    private final AlmoxarifadoEstoqueRepository almoxarifadoEstoqueRepository;

    public PdfService(OrdemDeServicoRepository ordemDeServicoRepository,
                      OrdemServicoItemRepository ordemServicoItemRepository,
                      AlmoxarifadoRepository almoxarifadoRepository,
                      AlmoxarifadoEstoqueRepository almoxarifadoEstoqueRepository) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.ordemServicoItemRepository = ordemServicoItemRepository;
        this.almoxarifadoRepository = almoxarifadoRepository;
        this.almoxarifadoEstoqueRepository = almoxarifadoEstoqueRepository;
    }

    public byte[] gerarPdfOrdemServico(Long id) {
        OrdemDeServico os = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 28, 28, 24, 24);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            adicionarCabecalho(document, os);
            adicionarDadosCliente(document, os.getCliente());
            adicionarDadosOrdemServico(document, os);
            adicionarObservacao(document);
            adicionarControleAtendimento(document);
            adicionarAssinaturas(document, os.getColaborador(), os.getCliente());

            document.close();
            return outputStream.toByteArray();
        } catch (DocumentException ex) {
            throw new DatabaseException("Erro ao gerar PDF da ordem de servi\u00e7o");
        } catch (Exception ex) {
            throw new DatabaseException("Erro inesperado ao gerar PDF da ordem de servi\u00e7o");
        }
    }

    public byte[] gerarRelatorioProdutosAlmoxarifado(Long almoxarifadoId) {
        Almoxarifado almoxarifado = almoxarifadoRepository.findById(almoxarifadoId)
                .orElseThrow(() -> new ResourceNotFoundException(almoxarifadoId));
        List<AlmoxarifadoEstoque> estoques = almoxarifadoEstoqueRepository
                .findByAlmoxarifadoIdOrderByProdutoNomeAsc(almoxarifadoId);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 28, 28, 24, 24);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            adicionarCabecalhoRelatorioAlmoxarifado(document, almoxarifado);
            adicionarTabelaProdutosAlmoxarifado(document, estoques);

            document.close();
            return outputStream.toByteArray();
        } catch (DocumentException ex) {
            throw new DatabaseException("Erro ao gerar relat\u00f3rio de produtos do almoxarifado");
        } catch (Exception ex) {
            throw new DatabaseException("Erro inesperado ao gerar relat\u00f3rio de produtos do almoxarifado");
        }
    }

    public byte[] gerarRelatorioProdutosOrdemServico(Long id) {
        OrdemDeServico os = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        List<OrdemServicoItem> itens = ordemServicoItemRepository.findByOrdemDeServicoId(id);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 28, 28, 24, 24);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            adicionarCabecalhoRelatorioProdutos(document, os);
            adicionarTabelaProdutos(document, itens);

            document.close();
            return outputStream.toByteArray();
        } catch (DocumentException ex) {
            throw new DatabaseException("Erro ao gerar relat\u00f3rio de produtos da ordem de servi\u00e7o");
        } catch (Exception ex) {
            throw new DatabaseException("Erro inesperado ao gerar relat\u00f3rio de produtos da ordem de servi\u00e7o");
        }
    }

    private void adicionarCabecalho(Document document, OrdemDeServico os) throws DocumentException {
        PdfPTable tabela = new PdfPTable(2);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{1.5f, 1f});
        tabela.setSpacingAfter(8);

        PdfPCell marca = celulaSemBorda();
        marca.addElement(new Paragraph("STOCKFLOW", fonteNegrito(14, CINZA_ESCURO)));
        marca.addElement(new Paragraph("Sistema de controle de estoque e ordem de servi\u00e7o", fonteNormal(8, CINZA_ESCURO)));

        PdfPCell numero = celulaSemBorda();
        numero.addElement(paragrafoDireita("OS N\u00ba " + os.getId(), fonteNegrito(13, CINZA_ESCURO)));
        numero.addElement(paragrafoDireita("Status: " + texto(os.getStatus() == null ? "" : os.getStatus().name()), fonteNormal(8, CINZA_ESCURO)));
        numero.addElement(paragrafoDireita("Emiss\u00e3o: " + formatarData(LocalDate.now()), fonteNormal(8, CINZA_ESCURO)));
        numero.addElement(paragrafoDireita("Motivo: " + texto(os.getDescricao()), fonteNormal(8, CINZA_ESCURO)));

        tabela.addCell(marca);
        tabela.addCell(numero);
        document.add(tabela);

        Paragraph titulo = new Paragraph("ORDEM DE SERVI\u00c7O", fonteNegrito(15, Color.BLACK));
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(10);
        document.add(titulo);
    }

    private void adicionarDadosCliente(Document document, Cliente cliente) throws DocumentException {
        PdfPTable tabela = new PdfPTable(4);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{1.5f, 1f, 1f, 1.2f});
        tabela.setSpacingAfter(8);

        adicionarTituloSecao(tabela, "DADOS DO CLIENTE", 4);
        tabela.addCell(celulaCampo("Cliente", cliente.getNome(), 2));
        tabela.addCell(celulaCampo("CPF", cliente.getCpf(), 1));
        tabela.addCell(celulaCampo("Telefone", cliente.getTelefone(), 1));
        tabela.addCell(celulaCampo("Endere\u00e7o completo", cliente.getEndereco(), 3));
        tabela.addCell(celulaCampo("E-mail", cliente.getEmail(), 1));

        document.add(tabela);
    }

    private void adicionarDadosOrdemServico(Document document, OrdemDeServico os) throws DocumentException {
        PdfPTable tabela = new PdfPTable(4);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{1f, 1f, 1f, 1.3f});
        tabela.setSpacingAfter(8);

        adicionarTituloSecao(tabela, "DADOS DA OS", 4);
        tabela.addCell(celulaCampo("ID", String.valueOf(os.getId()), 1));
        tabela.addCell(celulaCampo("Data abertura", formatarData(os.getDataAbertura()), 1));
        tabela.addCell(celulaCampo("Data fechamento", formatarData(os.getDataFechamento()), 1));
        tabela.addCell(celulaCampo("Status", os.getStatus() == null ? "" : os.getStatus().name(), 1));
        tabela.addCell(celulaCampo("T\u00e9cnico / Colaborador", nomeColaborador(os.getColaborador()), 4));

        document.add(tabela);
    }

    private void adicionarObservacao(Document document) throws DocumentException {
        PdfPTable tabela = new PdfPTable(1);
        tabela.setWidthPercentage(100);
        tabela.setSpacingAfter(8);

        adicionarTituloSecao(tabela, "OBSERVA\u00c7\u00c3O", 1);

        PdfPCell observacao = new PdfPCell();
        observacao.setPadding(6);
        observacao.setMinimumHeight(150);
        observacao.setBorderColor(BORDA);

        Paragraph horarios = new Paragraph("Hor\u00e1rio de chegada: ____:____     Hor\u00e1rio de sa\u00edda: ____:____", fonteNormal(8, Color.BLACK));
        horarios.setSpacingAfter(8);
        observacao.addElement(horarios);

        Paragraph linhas = new Paragraph(
                "________________________________________________________________________________\n\n"
                        + "________________________________________________________________________________\n\n"
                        + "________________________________________________________________________________\n\n"
                        + "________________________________________________________________________________",
                fonteNormal(8, Color.BLACK)
        );
        linhas.setLeading(12);
        observacao.addElement(linhas);

        tabela.addCell(observacao);
        document.add(tabela);
    }

    private void adicionarControleAtendimento(Document document) throws DocumentException {
        PdfPTable tabela = new PdfPTable(2);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{1f, 1f});
        tabela.setSpacingAfter(10);

        adicionarTituloSecao(tabela, "CONTROLE DO ATENDIMENTO", 2);
        tabela.addCell(celulaCampo("Atendimento conclu\u00eddo", "( )", 1));
        tabela.addCell(celulaCampo("Atendimento solucionado", "( )", 1));

        document.add(tabela);
    }

    private void adicionarAssinaturas(Document document, Colaborador colaborador, Cliente cliente)
            throws DocumentException {
        PdfPTable tabela = new PdfPTable(2);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{1f, 1f});
        tabela.setSpacingBefore(8);

        tabela.addCell(celulaCampo("Cliente", texto(cliente.getNome()), 1));
        tabela.addCell(celulaCampo("T\u00e9cnico / Colaborador", nomeColaborador(colaborador), 1));
        tabela.addCell(celulaAssinatura("Assinatura do cliente"));
        tabela.addCell(celulaAssinatura("Assinatura do t\u00e9cnico"));

        document.add(tabela);
    }

    private void adicionarCabecalhoRelatorioProdutos(Document document, OrdemDeServico os) throws DocumentException {
        Paragraph sistema = new Paragraph("STOCKFLOW", fonteNegrito(16, CINZA_ESCURO));
        sistema.setAlignment(Element.ALIGN_CENTER);
        sistema.setSpacingAfter(4);
        document.add(sistema);

        Paragraph titulo = new Paragraph("RELAT\u00d3RIO DE PRODUTOS UTILIZADOS NA OS", fonteNegrito(13, Color.BLACK));
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(12);
        document.add(titulo);

        PdfPTable dados = new PdfPTable(4);
        dados.setWidthPercentage(100);
        dados.setWidths(new float[]{0.8f, 2f, 1.1f, 1.4f});
        dados.setSpacingAfter(10);

        adicionarTituloSecao(dados, "DADOS DO RELAT\u00d3RIO", 4);
        dados.addCell(celulaCampo("OS", String.valueOf(os.getId()), 1));
        dados.addCell(celulaCampo("Cliente", os.getCliente().getNome(), 2));
        dados.addCell(celulaCampo("Emiss\u00e3o", formatarData(LocalDate.now()), 1));
        dados.addCell(celulaCampo("Data abertura", formatarData(os.getDataAbertura()), 1));
        dados.addCell(celulaCampo("Status", os.getStatus() == null ? "" : os.getStatus().name(), 1));
        dados.addCell(celulaCampo("T\u00e9cnico / Colaborador", nomeColaborador(os.getColaborador()), 2));

        document.add(dados);
    }

    private void adicionarTabelaProdutos(Document document, List<OrdemServicoItem> itens) throws DocumentException {
        PdfPTable tabela = new PdfPTable(6);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{0.8f, 2.4f, 1.5f, 0.8f, 1.1f, 1.1f});
        tabela.setSpacingAfter(8);

        adicionarTituloSecao(tabela, "PRODUTOS UTILIZADOS", 6);
        adicionarCabecalhoTabela(tabela, "ID");
        adicionarCabecalhoTabela(tabela, "Produto");
        adicionarCabecalhoTabela(tabela, "Almoxarifado");
        adicionarCabecalhoTabela(tabela, "Qtd.");
        adicionarCabecalhoTabela(tabela, "Valor unit.");
        adicionarCabecalhoTabela(tabela, "Valor total");

        if (itens.isEmpty()) {
            PdfPCell vazio = new PdfPCell(new Phrase("Nenhum produto vinculado a esta ordem de servi\u00e7o.", fonteNormal(8, Color.BLACK)));
            vazio.setColspan(6);
            vazio.setPadding(6);
            vazio.setBorderColor(BORDA);
            tabela.addCell(vazio);
        } else {
            for (OrdemServicoItem item : itens) {
                tabela.addCell(celulaTabela(String.valueOf(item.getProduto().getId())));
                tabela.addCell(celulaTabela(item.getProduto().getNome()));
                tabela.addCell(celulaTabela(item.getAlmoxarifado().getNome()));
                tabela.addCell(celulaTabela(String.valueOf(item.getQuantidade())));
                tabela.addCell(celulaTabela(formatarMoeda(item.getValorUnitario())));
                tabela.addCell(celulaTabela(formatarMoeda(item.valorTotal())));
            }
        }

        PdfPCell total = new PdfPCell(new Phrase("Total geral: " + formatarMoeda(calcularValorTotal(itens)), fonteNegrito(8, Color.BLACK)));
        total.setColspan(6);
        total.setHorizontalAlignment(Element.ALIGN_RIGHT);
        total.setPadding(6);
        total.setBorderColor(BORDA);
        tabela.addCell(total);

        document.add(tabela);
    }

    private void adicionarCabecalhoRelatorioAlmoxarifado(Document document, Almoxarifado almoxarifado)
            throws DocumentException {
        Paragraph sistema = new Paragraph("STOCKFLOW", fonteNegrito(16, CINZA_ESCURO));
        sistema.setAlignment(Element.ALIGN_CENTER);
        sistema.setSpacingAfter(4);
        document.add(sistema);

        Paragraph titulo = new Paragraph("RELAT\u00d3RIO DE PRODUTOS DO ALMOXARIFADO", fonteNegrito(13, Color.BLACK));
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(12);
        document.add(titulo);

        PdfPTable dados = new PdfPTable(3);
        dados.setWidthPercentage(100);
        dados.setWidths(new float[]{0.8f, 2.5f, 1f});
        dados.setSpacingAfter(10);

        adicionarTituloSecao(dados, "DADOS DO RELAT\u00d3RIO", 3);
        dados.addCell(celulaCampo("ID", String.valueOf(almoxarifado.getId()), 1));
        dados.addCell(celulaCampo("Almoxarifado", almoxarifado.getNome(), 1));
        dados.addCell(celulaCampo("Emiss\u00e3o", formatarData(LocalDate.now()), 1));

        document.add(dados);
    }

    private void adicionarTabelaProdutosAlmoxarifado(Document document, List<AlmoxarifadoEstoque> estoques)
            throws DocumentException {
        PdfPTable tabela = new PdfPTable(6);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{0.7f, 2.5f, 1.5f, 1f, 0.9f, 1.1f});
        tabela.setSpacingAfter(8);

        adicionarTituloSecao(tabela, "PRODUTOS EM ESTOQUE", 6);
        adicionarCabecalhoTabela(tabela, "ID");
        adicionarCabecalhoTabela(tabela, "Produto");
        adicionarCabecalhoTabela(tabela, "Categoria");
        adicionarCabecalhoTabela(tabela, "Unidade");
        adicionarCabecalhoTabela(tabela, "Qtd.");
        adicionarCabecalhoTabela(tabela, "Pre\u00e7o");

        if (estoques.isEmpty()) {
            PdfPCell vazio = new PdfPCell(new Phrase("Nenhum produto cadastrado neste almoxarifado.", fonteNormal(8, Color.BLACK)));
            vazio.setColspan(6);
            vazio.setPadding(6);
            vazio.setBorderColor(BORDA);
            tabela.addCell(vazio);
        } else {
            for (AlmoxarifadoEstoque estoque : estoques) {
                tabela.addCell(celulaTabela(String.valueOf(estoque.getProduto().getId())));
                tabela.addCell(celulaTabela(estoque.getProduto().getNome()));
                tabela.addCell(celulaTabela(estoque.getProduto().getCategoria().getNome()));
                tabela.addCell(celulaTabela(estoque.getProduto().getUnidadeMedida().name()));
                tabela.addCell(celulaTabela(String.valueOf(estoque.getQuantidade())));
                tabela.addCell(celulaTabela(formatarMoeda(estoque.getProduto().getPreco())));
            }
        }

        PdfPCell total = new PdfPCell(new Phrase("Total de itens em estoque: " + calcularQuantidadeTotal(estoques), fonteNegrito(8, Color.BLACK)));
        total.setColspan(6);
        total.setHorizontalAlignment(Element.ALIGN_RIGHT);
        total.setPadding(6);
        total.setBorderColor(BORDA);
        tabela.addCell(total);

        document.add(tabela);
    }

    private void adicionarTituloSecao(PdfPTable tabela, String texto, int colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, fonteNegrito(8, Color.WHITE)));
        cell.setColspan(colspan);
        cell.setBackgroundColor(CINZA_ESCURO);
        cell.setPadding(5);
        cell.setBorderColor(CINZA_ESCURO);
        tabela.addCell(cell);
    }

    private void adicionarCabecalhoTabela(PdfPTable tabela, String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, fonteNegrito(7, CINZA_ESCURO)));
        cell.setBackgroundColor(CINZA_CLARO);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(BORDA);
        tabela.addCell(cell);
    }

    private PdfPCell celulaCampo(String label, String valor, int colspan) {
        Paragraph conteudo = new Paragraph();
        conteudo.add(new Phrase(label + "\n", fonteNegrito(7, CINZA_ESCURO)));
        conteudo.add(new Phrase(texto(valor), fonteNormal(8, Color.BLACK)));

        PdfPCell cell = new PdfPCell(conteudo);
        cell.setColspan(colspan);
        cell.setPadding(6);
        cell.setMinimumHeight(34);
        cell.setBorderColor(BORDA);
        return cell;
    }

    private PdfPCell celulaAssinatura(String texto) {
        Paragraph conteudo = new Paragraph("\n\n\n____________________________________\n" + texto, fonteNormal(8, Color.BLACK));
        conteudo.setAlignment(Element.ALIGN_CENTER);

        PdfPCell cell = new PdfPCell(conteudo);
        cell.setPadding(8);
        cell.setMinimumHeight(70);
        cell.setBorderColor(BORDA);
        return cell;
    }

    private PdfPCell celulaTabela(String valor) {
        PdfPCell cell = new PdfPCell(new Phrase(texto(valor), fonteNormal(7, Color.BLACK)));
        cell.setPadding(5);
        cell.setBorderColor(BORDA);
        return cell;
    }

    private PdfPCell celulaSemBorda() {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPadding(2);
        return cell;
    }

    private Paragraph paragrafoDireita(String texto, Font fonte) {
        Paragraph paragraph = new Paragraph(texto, fonte);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        return paragraph;
    }

    private Font fonteNormal(float tamanho, Color cor) {
        return FontFactory.getFont(FontFactory.HELVETICA, tamanho, cor);
    }

    private Font fonteNegrito(float tamanho, Color cor) {
        return FontFactory.getFont(FontFactory.HELVETICA_BOLD, tamanho, cor);
    }

    private String formatarData(LocalDate data) {
        return data == null ? "" : data.format(DATA_FORMATTER);
    }

    private String formatarMoeda(Double valor) {
        return valor == null ? "" : MOEDA_FORMATTER.format(valor);
    }

    private Double calcularValorTotal(List<OrdemServicoItem> itens) {
        double total = 0.0;
        for (OrdemServicoItem item : itens) {
            total += item.valorTotal();
        }
        return total;
    }

    private Integer calcularQuantidadeTotal(List<AlmoxarifadoEstoque> estoques) {
        int total = 0;
        for (AlmoxarifadoEstoque estoque : estoques) {
            total += estoque.getQuantidade();
        }
        return total;
    }

    private String nomeColaborador(Colaborador colaborador) {
        if (colaborador == null) {
            return "";
        }
        String cargo = texto(colaborador.getCargo());
        if (cargo.isBlank()) {
            return texto(colaborador.getNome());
        }
        return texto(colaborador.getNome()) + " - " + cargo;
    }

    private String texto(String valor) {
        return valor == null || valor.isBlank() ? "" : valor;
    }
}
