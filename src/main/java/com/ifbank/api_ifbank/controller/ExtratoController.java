package com.ifbank.api_ifbank.controller;

import java.math.BigDecimal;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ifbank.api_ifbank.model.DTO.extrato.ExtratoPdfResponseDTO;
import com.ifbank.api_ifbank.service.interfaces.IExtratoPdfService;
import com.ifbank.api_ifbank.service.interfaces.IMovimentacaoService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/extratos")
public class ExtratoController {

    private final IExtratoPdfService extratoPdfService;
    private final IMovimentacaoService movimentacaoService;

    public ExtratoController(IExtratoPdfService extratoPdfService,IMovimentacaoService movimentacaoService) {
        this.extratoPdfService = extratoPdfService;
        this.movimentacaoService = movimentacaoService;
    }

    @GetMapping("/{idConta}/pdf")
    public ResponseEntity<byte[]> gerarPdf(
            @PathVariable Long idConta,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) BigDecimal valor,
            @RequestParam(required = false) String ordenacao,
            @RequestParam(required = false) String direcao
    ) {

        ExtratoPdfResponseDTO dto = movimentacaoService
                .montarExtratoPdf(idConta, nome, valor, ordenacao, direcao);

        byte[] pdf = extratoPdfService.gerarPdfExtrato(dto);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=extrato-conta-" + idConta + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}