package com.ifbank.api_ifbank.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifbank.api_ifbank.model.DTO.investimento.AplicarInvestimentoRequestDTO;
import com.ifbank.api_ifbank.model.DTO.investimento.InvestimentoDTO;
import com.ifbank.api_ifbank.model.DTO.investimento.ResumoInvestimentoDTO;
import com.ifbank.api_ifbank.service.InvestimentoService;

@RestController
@RequestMapping("/api/investimentos")
@CrossOrigin(origins = "*")
public class InvestimentoController {

    private final InvestimentoService investimentoService;

    public InvestimentoController(InvestimentoService investimentoService) {
        this.investimentoService = investimentoService;
    }

    @GetMapping("/{idConta}")
    public ResponseEntity<?> listar(@PathVariable Long idConta) {
        try {
            List<InvestimentoDTO> lista = investimentoService.listarPorConta(idConta);
            return ResponseEntity.ok(lista);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{idConta}/resumo")
    public ResponseEntity<?> resumo(@PathVariable Long idConta) {
        try {
            ResumoInvestimentoDTO resumo = investimentoService.obterResumo(idConta);
            return ResponseEntity.ok(resumo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/aplicar")
    public ResponseEntity<?> aplicar(@RequestBody AplicarInvestimentoRequestDTO dto) {
        try {
            InvestimentoDTO resultado = investimentoService.aplicar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

      @GetMapping("/tipos")
      public ResponseEntity<?> listarTipos() {
          try {
              return ResponseEntity.ok(investimentoService.listarTipos());
          } catch (RuntimeException e) {
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                      .body(e.getMessage());
          }
      }

      


    @PostMapping("/resgatar/{idAplicacao}")
    public ResponseEntity<?> resgatar(@PathVariable Long idAplicacao, @RequestBody Map<String, Long> body) {
        try {
            Long idConta = body.get("idConta");
            String mensagem = investimentoService.resgatar(idAplicacao, idConta);
            return ResponseEntity.ok(mensagem);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    
}