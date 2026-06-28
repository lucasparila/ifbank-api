package com.ifbank.api_ifbank.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ifbank.api_ifbank.model.DTO.extrato.ExtratoPdfResponseDTO;
import com.ifbank.api_ifbank.model.DTO.movimentacao.MovimentacaoResponseDTO;
import com.ifbank.api_ifbank.service.interfaces.IExtratoPdfService;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

@Service
public class ExtratoPdfService implements IExtratoPdfService {

    public byte[] gerarPdfExtrato(ExtratoPdfResponseDTO dto) {

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, baos);
            document.open();

            // ========================
            // CABEÇALHO
            // ========================
            Paragraph titulo = new Paragraph("IFBank", TITULO);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            Paragraph subtitulo = new Paragraph("EXTRATO DA CONTA", SUBTITULO);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitulo);

            document.add(new Paragraph(" "));
            document.add(new LineSeparator());
            document.add(new Paragraph(" "));

            // ========================
            // DADOS
            // ========================
            document.add(new Paragraph("Titular: " + dto.getNomeTitular(), TEXTO));
            document.add(new Paragraph("Conta: " + dto.getNumeroConta(), TEXTO));

            document.add(new Paragraph(
                    "Saldo atual: R$ " + formatarValor(dto.getSaldoAtual()),
                    TEXTO_NEGRITO
            ));

            document.add(new Paragraph(
                    "Emitido em: " + dto.getDataEmissao(),
                    TEXTO
            ));

            document.add(new Paragraph(" "));
            document.add(new LineSeparator());
            document.add(new Paragraph(" "));

            // ========================
            // MOVIMENTAÇÕES
            // ========================
            List<MovimentacaoResponseDTO> movs = dto.getMovimentacoes()
                    .stream()
                    .sorted(Comparator.comparing(MovimentacaoResponseDTO::getDataMovimento))
                    .toList();

            BigDecimal saldo = calcularSaldoInicial(dto.getSaldoAtual(), movs);

            Paragraph movTitulo = new Paragraph("MOVIMENTAÇÕES", SUBTITULO);
            movTitulo.setAlignment(Element.ALIGN_LEFT);
            document.add(movTitulo);

            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 4, 3, 3});

            table.addCell(new Paragraph("Data", TEXTO_NEGRITO));
            table.addCell(new Paragraph("Tipo", TEXTO_NEGRITO));
            table.addCell(new Paragraph("Valor", TEXTO_NEGRITO));
            table.addCell(new Paragraph("Saldo", TEXTO_NEGRITO));

            DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (MovimentacaoResponseDTO m : movs) {
            	
            	String dataFormatada = m.getDataMovimento().format(DATE_FORMATTER);
                String tipo = m.getTipoMovimento();

                boolean entrada = isEntrada(tipo);
                boolean saida = isSaida(tipo);

                if (entrada) {
                    saldo = saldo.add(m.getValor());
                } else if (saida) {
                    saldo = saldo.subtract(m.getValor());
                }

                Font valorFont = corFont(entrada ? VERDE : VERMELHO);

                table.addCell(new Paragraph(
                        dataFormatada,
                        TEXTO
                ));

                table.addCell(new Paragraph(tipo, TEXTO));

                table.addCell(new Paragraph(
                        (entrada ? "+ " : "- ") + formatarValor(m.getValor()),
                        valorFont
                ));

                table.addCell(new Paragraph(
                        "R$ " + formatarValor(saldo),
                        TEXTO_NEGRITO
                ));
            }

            document.add(table);

            // ========================
            // RODAPÉ
            // ========================
            document.add(new Paragraph(" "));
            document.add(new LineSeparator());

            Paragraph rodape = new Paragraph(
                    "Documento gerado automaticamente pelo IFBank",
                    TEXTO
            );
            rodape.setAlignment(Element.ALIGN_CENTER);
            document.add(rodape);

            document.close();

            return baos.toByteArray();

        } catch (DocumentException e) {
            throw new RuntimeException("Erro ao gerar PDF.", e);
        }
    }

    // ========================
    // SALDO INICIAL
    // ========================
    private BigDecimal calcularSaldoInicial(BigDecimal saldoAtual,
                                            List<MovimentacaoResponseDTO> movs) {

        BigDecimal saldo = saldoAtual;

        for (MovimentacaoResponseDTO m : movs) {

            if (isEntrada(m.getTipoMovimento())) {
                saldo = saldo.subtract(m.getValor());
            } else if (isSaida(m.getTipoMovimento())) {
                saldo = saldo.add(m.getValor());
            }
        }

        return saldo;
    }

    // ========================
    // REGRAS
    // ========================
    private boolean isEntrada(String tipo) {
        return tipo.equals("DEPOSITO")
                || tipo.equals("TRANSFERENCIA_RECEBIDA")
                || tipo.equals("RESGATE_INVESTIMENTO");
    }

    private boolean isSaida(String tipo) {
        return tipo.equals("SAQUE")
                || tipo.equals("TRANSFERENCIA_ENVIADA")
                || tipo.equals("APLICACAO_INVESTIMENTO");
    }

    // ========================
    // CONVERSÃO DE COR → FONT
    // ========================
    private Font corFont(Color color) {
        return new Font(Font.HELVETICA, 11, Font.BOLD, color);
    }

    // ========================
    // FORMATAÇÃO
    // ========================
    private String formatarValor(BigDecimal valor) {
        if (valor == null) return "0,00";
        return String.format("%,.2f", valor);
    }

    // ========================
    // FONTS
    // ========================
    private static final Font TITULO =
            new Font(Font.HELVETICA, 20, Font.BOLD);

    private static final Font SUBTITULO =
            new Font(Font.HELVETICA, 14, Font.BOLD);

    private static final Font TEXTO =
            new Font(Font.HELVETICA, 11, Font.NORMAL);

    private static final Font TEXTO_NEGRITO =
            new Font(Font.HELVETICA, 11, Font.BOLD);

    // ========================
    // CORES
    //========================
    private static final Color VERDE = new Color(0, 128, 0);
    private static final Color VERMELHO = new Color(200, 0, 0);
}