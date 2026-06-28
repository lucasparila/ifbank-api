package com.ifbank.api_ifbank.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ifbank.api_ifbank.model.Cliente;
import com.ifbank.api_ifbank.model.Conta;
import com.ifbank.api_ifbank.model.Gerente;
import com.ifbank.api_ifbank.model.Usuario;
import com.ifbank.api_ifbank.model.DTO.cliente.ClienteDTO;
import com.ifbank.api_ifbank.model.DTO.cliente.ContaDTO;
import com.ifbank.api_ifbank.model.DTO.cliente.EnderecoDTO;
import com.ifbank.api_ifbank.model.DTO.cliente.TelefoneDTO;
import com.ifbank.api_ifbank.model.DTO.gerente.GerenteDTO;
import com.ifbank.api_ifbank.model.DTO.perfil.PerfilClienteCompletoDTO;
import com.ifbank.api_ifbank.model.DTO.perfil.PerfilGerenteCompletoDTO;
import com.ifbank.api_ifbank.model.enums.StatusConta;
import com.ifbank.api_ifbank.repository.ClienteRepository;
import com.ifbank.api_ifbank.repository.ContaRepository;
import com.ifbank.api_ifbank.repository.GerenteRepository;
import com.ifbank.api_ifbank.repository.UsuarioRepository;
import com.ifbank.api_ifbank.service.interfaces.IEmailService;
import com.ifbank.api_ifbank.service.interfaces.IGerenteService;

@Service
public class GerenteService implements IGerenteService {
	

	private final ContaRepository contaRepository;
	private final UsuarioRepository usuarioRepository;
	private final GerenteRepository gerenteRepository;
	private final IEmailService emailService;
	
	public GerenteService(ContaRepository contaRepository,UsuarioRepository usuarioRepository,GerenteRepository gerenteRepository,IEmailService emailService) {
		this.contaRepository = contaRepository;
		this.usuarioRepository = usuarioRepository;
		this.gerenteRepository = gerenteRepository;
		this.emailService = emailService;
	}

    @Override
    public List<PerfilClienteCompletoDTO> buscarContasPendentes() {
      
        List<Conta> contasPendentes = contaRepository.findByStatusContaFetch(StatusConta.PENDENTE.getId());

        // 2. Transforma a lista de Contas (Entidade) na lista de PerfilCompletoDTO
        return contasPendentes.stream().map(conta -> {
            var cli = conta.getCliente();
            var usr = cli.getUsuario();

            // Monta o DTO de Endereço se existir na entidade
            EnderecoDTO enderecoDTO = new EnderecoDTO();
            if (cli.getEndereco() != null) {
                enderecoDTO.setLogradouro(cli.getEndereco().getLogradouro());
                enderecoDTO.setNumero(cli.getEndereco().getNumero());
                enderecoDTO.setCidade(cli.getEndereco().getCidade());
                enderecoDTO.setEstado(cli.getEndereco().getEstado());
                enderecoDTO.setBairro(cli.getEndereco().getBairro());
                enderecoDTO.setCep(cli.getEndereco().getCep());
                enderecoDTO.setComplemento(cli.getEndereco().getComplemento());
            }
            
            TelefoneDTO telefoneDTO = new TelefoneDTO(cli.getTelefone().getCodPais(),cli.getTelefone().getCodArea(),cli.getTelefone().getNumero());

         
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setId(cli.getId());
            clienteDTO.setNome(cli.getNome());
            clienteDTO.setDataNascimento(cli.getDataNascimento());
            clienteDTO.setFotoUrl(cli.getFotoUrl());
            clienteDTO.setDataCadastro(cli.getDataCadastro());
            clienteDTO.setEndereco(enderecoDTO);
            clienteDTO.setTelefone(telefoneDTO);

           
            ContaDTO contaDTO = new ContaDTO();
            contaDTO.setId(conta.getId());
            contaDTO.setStatusConta(conta.getStatusConta().name());
            contaDTO.setNumeroConta(conta.getNumeroConta());
            contaDTO.setSaldo(conta.getSaldo());
            contaDTO.setDataAbertura(conta.getDataAbertura());
            contaDTO.setStatusConta(conta.getStatusConta().name());

       
            PerfilClienteCompletoDTO perfilDTO = new PerfilClienteCompletoDTO();
            perfilDTO.setIdUsuario(usr.getId());
            perfilDTO.setEmail(usr.getEmail());
            perfilDTO.setCpf(usr.getCpf());
            perfilDTO.setPerfil(usr.getTipoUsuario().name());
            perfilDTO.setCliente(clienteDTO);
            perfilDTO.setConta(contaDTO);

            return perfilDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void aprovarContaCliente(Long idConta) {
        Conta conta = contaRepository.findById(idConta)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada."));

        if (!conta.getStatusConta().equals(StatusConta.PENDENTE)) {
            throw new RuntimeException("Conta não está mais pendente. Status atual: " 
                    + conta.getStatusConta().name());
        }

        conta.setIdStatusConta(StatusConta.ATIVA.getId());
        contaRepository.save(conta);
        emailService.enviarEmailContaAprovada(conta.getCliente(),conta,conta.getCliente().getUsuario().getEmail());
    }

    @Override
    public void reprovarContaCliente(Long idConta) {
        Conta conta = contaRepository.findById(idConta)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada."));

        if (!conta.getStatusConta().equals(StatusConta.PENDENTE)) {
            throw new RuntimeException("Conta não está mais pendente. Status atual: " 
                    + conta.getStatusConta().name());
        }

        conta.setIdStatusConta(StatusConta.REJEITADA.getId());
        contaRepository.save(conta);
        emailService.enviarEmailContaReprovada(conta.getCliente(),conta.getCliente().getUsuario().getEmail());
    }
	@Override
	public PerfilGerenteCompletoDTO obterPerfilCompleto(Long idUsuario) {
		
		Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Gerente gerente = gerenteRepository.findByUsuarioId(idUsuario);
        if (gerente == null) {
            throw new RuntimeException("Dados de cliente não encontrados para este usuário.");
        }
        
        GerenteDTO gerenteDTO = new GerenteDTO(gerente.getId(),gerente.getNome(),gerente.getDataNascimento());
        return new PerfilGerenteCompletoDTO(usuario.getId(),usuario.getEmail(),usuario.getCpf(),usuario.getTipoUsuario().name(),gerenteDTO);
	}
}
