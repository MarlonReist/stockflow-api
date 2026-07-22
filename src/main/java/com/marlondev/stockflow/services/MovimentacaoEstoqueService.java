package com.marlondev.stockflow.services;

import com.marlondev.stockflow.domain.*;
import com.marlondev.stockflow.domain.enums.TipoMovimentacao;
import com.marlondev.stockflow.dto.MovimentacaoEstoqueResponseDTO;
import com.marlondev.stockflow.repositories.MovimentacaoEstoqueRepository;
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
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class MovimentacaoEstoqueService {

    private static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Color CINZA_ESCURO = new Color(36, 39, 44);
    private static final Color CINZA_CLARO = new Color(232, 235, 238);
    private static final Color BORDA = new Color(170, 170, 170);

    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public MovimentacaoEstoqueService(MovimentacaoEstoqueRepository movimentacaoEstoqueRepository) {
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }

    public MovimentacaoEstoqueResponseDTO buscarPorId(Long id){
        MovimentacaoEstoque movimentacaoExiste = movimentacaoEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        return new MovimentacaoEstoqueResponseDTO(movimentacaoExiste);
    }

    public List<MovimentacaoEstoqueResponseDTO> listarTodos(){
        List<MovimentacaoEstoque> list = movimentacaoEstoqueRepository.findAll();
        return list.stream().map(MovimentacaoEstoqueResponseDTO::new).collect(Collectors.toList());
    }

    public List<MovimentacaoEstoqueResponseDTO> listarComFiltros(String busca,
                                                                  String origem,
                                                                  TipoMovimentacao tipo,
                                                                  LocalDate dataInicial,
                                                                  LocalDate dataFinal) {
        return filtrarMovimentacoes(busca, origem, tipo, dataInicial, dataFinal)
                .stream()
                .map(MovimentacaoEstoqueResponseDTO::new)
                .collect(Collectors.toList());
    }

    public byte[] gerarPdfHistorico(String busca,
                                    String origem,
                                    TipoMovimentacao tipo,
                                    LocalDate dataInicial,
                                    LocalDate dataFinal) {
        List<MovimentacaoEstoque> movimentacoes = filtrarMovimentacoes(busca, origem, tipo, dataInicial, dataFinal);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate(), 28, 28, 24, 24);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            adicionarCabecalhoHistorico(document, busca, origem, tipo, dataInicial, dataFinal);
            adicionarTabelaHistorico(document, movimentacoes);

            document.close();
            return outputStream.toByteArray();
        } catch (DocumentException ex) {
            throw new DatabaseException("Erro ao gerar PDF do hist\u00f3rico de movimenta\u00e7\u00f5es");
        } catch (Exception ex) {
            throw new DatabaseException("Erro inesperado ao gerar PDF do hist\u00f3rico de movimenta\u00e7\u00f5es");
        }
    }

    private List<MovimentacaoEstoque> filtrarMovimentacoes(String busca,
                                                           String origem,
                                                           TipoMovimentacao tipo,
                                                           LocalDate dataInicial,
                                                           LocalDate dataFinal) {
        String buscaNormalizada = normalizar(busca);
        String origemNormalizada = normalizar(origem);

        return movimentacaoEstoqueRepository.findAll()
                .stream()
                .filter(mov -> tipo == null || mov.getTipo() == tipo)
                .filter(mov -> dataInicial == null || !mov.getDataMovimentacao().isBefore(dataInicial))
                .filter(mov -> dataFinal == null || !mov.getDataMovimentacao().isAfter(dataFinal))
                .filter(mov -> origemNormalizada.isBlank() || normalizar(origem(mov)).contains(origemNormalizada))
                .filter(mov -> buscaNormalizada.isBlank() || correspondeBusca(mov, buscaNormalizada))
                .sorted(Comparator.comparing(MovimentacaoEstoque::getDataMovimentacao).reversed()
                        .thenComparing(MovimentacaoEstoque::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    private boolean correspondeBusca(MovimentacaoEstoque mov, String busca) {
        return normalizar(mov.getProduto().getNome()).contains(busca)
                || normalizar(mov.getAlmoxarifado().getNome()).contains(busca)
                || normalizar(mov.getTipo().name()).contains(busca)
                || normalizar(origem(mov)).contains(busca)
                || formatarIdOrigem(mov).contains(busca)
                || String.valueOf(mov.getQuantidade()).contains(busca);
    }

    private void adicionarCabecalhoHistorico(Document document,
                                             String busca,
                                             String origem,
                                             TipoMovimentacao tipo,
                                             LocalDate dataInicial,
                                             LocalDate dataFinal) throws DocumentException {
        Paragraph sistema = new Paragraph("STOCKFLOW", fonteNegrito(16, CINZA_ESCURO));
        sistema.setAlignment(Element.ALIGN_CENTER);
        sistema.setSpacingAfter(4);
        document.add(sistema);

        Paragraph titulo = new Paragraph("RELAT\u00d3RIO DE HIST\u00d3RICO DE MOVIMENTA\u00c7\u00d5ES", fonteNegrito(13, Color.BLACK));
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(12);
        document.add(titulo);

        PdfPTable filtros = new PdfPTable(5);
        filtros.setWidthPercentage(100);
        filtros.setWidths(new float[]{1.4f, 1.1f, 1f, 1f, 1f});
        filtros.setSpacingAfter(10);

        adicionarTituloSecao(filtros, "FILTROS", 5);
        filtros.addCell(celulaCampo("Busca", texto(busca)));
        filtros.addCell(celulaCampo("Origem", texto(origem)));
        filtros.addCell(celulaCampo("Tipo", tipo == null ? "" : tipo.name()));
        filtros.addCell(celulaCampo("Data inicial", formatarData(dataInicial)));
        filtros.addCell(celulaCampo("Data final", formatarData(dataFinal)));

        document.add(filtros);
    }

    private void adicionarTabelaHistorico(Document document, List<MovimentacaoEstoque> movimentacoes)
            throws DocumentException {
        PdfPTable tabela = new PdfPTable(7);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{0.9f, 0.9f, 2.2f, 1.8f, 0.8f, 1.3f, 0.8f});
        tabela.setSpacingAfter(8);

        adicionarTituloSecao(tabela, "MOVIMENTA\u00c7\u00d5ES", 7);
        adicionarCabecalhoTabela(tabela, "Data");
        adicionarCabecalhoTabela(tabela, "Tipo");
        adicionarCabecalhoTabela(tabela, "Produto");
        adicionarCabecalhoTabela(tabela, "Almoxarifado");
        adicionarCabecalhoTabela(tabela, "Qtd.");
        adicionarCabecalhoTabela(tabela, "Origem");
        adicionarCabecalhoTabela(tabela, "ID Origem");

        if (movimentacoes.isEmpty()) {
            PdfPCell vazio = new PdfPCell(new Phrase("Nenhuma movimenta\u00e7\u00e3o encontrada para os filtros informados.", fonteNormal(8, Color.BLACK)));
            vazio.setColspan(7);
            vazio.setPadding(6);
            vazio.setBorderColor(BORDA);
            tabela.addCell(vazio);
        } else {
            for (MovimentacaoEstoque mov : movimentacoes) {
                tabela.addCell(celulaTabela(formatarData(mov.getDataMovimentacao())));
                tabela.addCell(celulaTabela(mov.getTipo().name()));
                tabela.addCell(celulaTabela(mov.getProduto().getNome()));
                tabela.addCell(celulaTabela(mov.getAlmoxarifado().getNome()));
                tabela.addCell(celulaTabela(String.valueOf(mov.getQuantidade())));
                tabela.addCell(celulaTabela(origem(mov)));
                tabela.addCell(celulaTabela(formatarIdOrigem(mov)));
            }
        }

        document.add(tabela);
    }

    private String origem(MovimentacaoEstoque mov) {
        if (mov.getEntradaEstoque() != null) {
            return "Entrada";
        }
        if (mov.getSaidaEstoque() != null) {
            return "Sa\u00edda";
        }
        if (mov.getTransferenciaAlmoxarifado() != null) {
            return "Transfer\u00eancia";
        }
        if (mov.getOrdemDeServico() != null) {
            return "Ordem de Servi\u00e7o";
        }
        return "";
    }

    private Long idOrigem(MovimentacaoEstoque mov) {
        if (mov.getEntradaEstoque() != null) {
            return mov.getEntradaEstoque().getId();
        }
        if (mov.getSaidaEstoque() != null) {
            return mov.getSaidaEstoque().getId();
        }
        if (mov.getTransferenciaAlmoxarifado() != null) {
            return mov.getTransferenciaAlmoxarifado().getId();
        }
        if (mov.getOrdemDeServico() != null) {
            return mov.getOrdemDeServico().getId();
        }
        return null;
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

    private PdfPCell celulaCampo(String label, String valor) {
        Paragraph conteudo = new Paragraph();
        conteudo.add(new Phrase(label + "\n", fonteNegrito(7, CINZA_ESCURO)));
        conteudo.add(new Phrase(texto(valor), fonteNormal(8, Color.BLACK)));

        PdfPCell cell = new PdfPCell(conteudo);
        cell.setPadding(6);
        cell.setMinimumHeight(34);
        cell.setBorderColor(BORDA);
        return cell;
    }

    private PdfPCell celulaTabela(String valor) {
        PdfPCell cell = new PdfPCell(new Phrase(texto(valor), fonteNormal(7, Color.BLACK)));
        cell.setPadding(5);
        cell.setBorderColor(BORDA);
        return cell;
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

    private String texto(String valor) {
        return valor == null || valor.isBlank() ? "" : valor;
    }

    private String formatarIdOrigem(MovimentacaoEstoque mov) {
        Long idOrigem = idOrigem(mov);
        return idOrigem == null ? "" : String.valueOf(idOrigem);
    }

    private String normalizar(String valor) {
        if (valor == null) {
            return "";
        }
        String semAcento = Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcento.toLowerCase(Locale.ROOT).trim();
    }

    public void registrarEntrada(EntradaItem entradaItem) {
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setAlmoxarifado(entradaItem.getEntradaEstoque().getAlmoxarifado());
        mov.setProduto(entradaItem.getProduto());
        mov.setQuantidade(entradaItem.getQuantidade());
        mov.setDataMovimentacao(LocalDate.now());
        mov.setTipo(TipoMovimentacao.ENTRADA);
        mov.setEntradaEstoque(entradaItem.getEntradaEstoque());
        movimentacaoEstoqueRepository.save(mov);

    }

    public void registrarSaida(SaidaItem saidaItem){
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setAlmoxarifado(saidaItem.getSaidaEstoque().getAlmoxarifado());
        mov.setProduto(saidaItem.getProduto());
        mov.setQuantidade(saidaItem.getQuantidade());
        mov.setDataMovimentacao(LocalDate.now());
        mov.setTipo(TipoMovimentacao.SAIDA);
        mov.setSaidaEstoque(saidaItem.getSaidaEstoque());
        movimentacaoEstoqueRepository.save(mov);
    }

    public void registrarSaidaPorOrdemDeServico(OrdemServicoItem osItem){
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setAlmoxarifado(osItem.getAlmoxarifado());
        mov.setProduto(osItem.getProduto());
        mov.setQuantidade(osItem.getQuantidade());
        mov.setDataMovimentacao(LocalDate.now());
        mov.setTipo(TipoMovimentacao.SAIDA);
        mov.setOrdemDeServico(osItem.getOrdemDeServico());
        movimentacaoEstoqueRepository.save(mov);
    }

    public void registrarEntradaTransferencia(TransferenciaItem transferenciaItem) {
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setAlmoxarifado(transferenciaItem.getTransferencia().getAlmoxarifadoDestino());
        mov.setProduto(transferenciaItem.getProduto());
        mov.setQuantidade(transferenciaItem.getQuantidade());
        mov.setDataMovimentacao(LocalDate.now());
        mov.setTipo(TipoMovimentacao.ENTRADA);
        mov.setTransferenciaAlmoxarifado(transferenciaItem.getTransferencia());
        movimentacaoEstoqueRepository.save(mov);
    }

    public void registrarSaidaTransferencia(TransferenciaItem transferenciaItem) {
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setAlmoxarifado(transferenciaItem.getTransferencia().getAlmoxarifadoOrigem());
        mov.setProduto(transferenciaItem.getProduto());
        mov.setQuantidade(transferenciaItem.getQuantidade());
        mov.setDataMovimentacao(LocalDate.now());
        mov.setTipo(TipoMovimentacao.SAIDA);
        mov.setTransferenciaAlmoxarifado(transferenciaItem.getTransferencia());
        movimentacaoEstoqueRepository.save(mov);
    }
}
