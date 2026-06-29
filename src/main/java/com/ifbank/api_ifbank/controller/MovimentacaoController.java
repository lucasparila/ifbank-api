package com.ifbank.api_ifbank.controller;

import com.ifbank.api_ifbank.model.DTO.movimentacao.MovimentacaoRequestDTO;
import com.ifbank.api_ifbank.model.DTO.movimentacao.MovimentacaoResponseDTO;
import com.ifbank.api_ifbank.model.DTO.movimentacao.TransferenciaRequestDTO;
import com.ifbank.api_ifbank.service.interfaces.IMovimentacaoService;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/movimentacoes")
@CrossOrigin(origins = "*")
public class MovimentacaoController {

    private final IMovimentacaoService movimentacaoService;

    public MovimentacaoController(IMovimentacaoService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }

    @PostMapping("/deposito")
    public ResponseEntity<?> depositar(@RequestBody MovimentacaoRequestDTO dto) {
        try {
            MovimentacaoResponseDTO resposta = movimentacaoService.depositar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/saque")
    public ResponseEntity<?> sacar(@RequestBody MovimentacaoRequestDTO dto) {
        try {
            MovimentacaoResponseDTO resposta = movimentacaoService.sacar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PostMapping("/transferencia")
    public ResponseEntity<?> transferir(@RequestBody TransferenciaRequestDTO dto) {
        try {
            MovimentacaoResponseDTO resposta = movimentacaoService.transferir(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{idConta}")
    public ResponseEntity<?> listarPorConta(@PathVariable Long idConta) {
        try {
            List<MovimentacaoResponseDTO> lista = movimentacaoService.listarPorConta(idConta);
            return ResponseEntity.ok(lista);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{idConta}/extrato")
    public ResponseEntity<?> extratoPorConta(@PathVariable Long idConta,
                                            @RequestParam(required = false) String nome,
                                            @RequestParam(required = false) java.math.BigDecimal valor, 
                                            
                                            @RequestParam(required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate dataInicio,
                                            
                                            @RequestParam(required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate dataFim,
                                            
                                            @RequestParam(defaultValue = "0") int pagina,
                                            @RequestParam(defaultValue = "10") int tamanho,
                                            @RequestParam(defaultValue = "dataMovimento") String ordenacao,
                                            @RequestParam(defaultValue = "DESC") String direcao) {
        try {
            Page<MovimentacaoResponseDTO> resultado = movimentacaoService.buscarExtrato(idConta, nome, valor,dataInicio,dataFim, pagina, tamanho, ordenacao, direcao);
            return ResponseEntity.ok(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}