package com.ifbank.api_ifbank.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

	public GerenteService(ContaRepository contaRepository, UsuarioRepository usuarioRepository,
			GerenteRepository gerenteRepository, IEmailService emailService) {
		this.contaRepository = contaRepository;
		this.usuarioRepository = usuarioRepository;
		this.gerenteRepository = gerenteRepository;
		this.emailService = emailService;
	}

	@Override
	public Page<PerfilClienteCompletoDTO> buscarContasPorStatus(StatusConta status, Pageable pageable) {

		Page<Conta> paginaContas = contaRepository.findByStatusContaFetchPaginado(status.getId(), pageable);

		// .map() do Page já preserva a paginação (totalElements, totalPages, etc),
		// só transforma o conteúdo de Conta -> PerfilClienteCompletoDTO
		return paginaContas.map(conta -> {
			var cli = conta.getCliente();
			var usr = cli.getUsuario();

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

			TelefoneDTO telefoneDTO = new TelefoneDTO(cli.getTelefone().getCodPais(), cli.getTelefone().getCodArea(),
					cli.getTelefone().getNumero());

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
		});
	}

	@Override
	public void aprovarContaCliente(Long idConta) {
		Conta conta = contaRepository.findById(idConta)
				.orElseThrow(() -> new RuntimeException("Conta não encontrada."));

		StatusConta statusAtual = conta.getStatusConta();

		// Aprovação é permitida a partir de PENDENTE (análise inicial)
		// ou REJEITADA (reanálise de crédito)
		if (statusAtual != StatusConta.PENDENTE && statusAtual != StatusConta.REJEITADA) {
			throw new RuntimeException(
					"Conta não pode ser aprovada a partir do status atual: " + statusAtual.name());
		}

		conta.setIdStatusConta(StatusConta.ATIVA.getId());
		contaRepository.save(conta);
		emailService.enviarEmailContaAprovada(conta.getCliente(), conta, conta.getCliente().getUsuario().getEmail());
	}

	@Override
	public void reprovarContaCliente(Long idConta) {
		Conta conta = contaRepository.findById(idConta)
				.orElseThrow(() -> new RuntimeException("Conta não encontrada."));

		// Reprovação só é permitida a partir de PENDENTE (análise inicial)
		if (conta.getStatusConta() != StatusConta.PENDENTE) {
			throw new RuntimeException(
					"Conta não está mais pendente. Status atual: " + conta.getStatusConta().name());
		}

		conta.setIdStatusConta(StatusConta.REJEITADA.getId());
		contaRepository.save(conta);
		emailService.enviarEmailContaReprovada(conta.getCliente(), conta.getCliente().getUsuario().getEmail());
	}

	@Override
	public PerfilGerenteCompletoDTO obterPerfilCompleto(Long idUsuario) {

		Usuario usuario = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

		Gerente gerente = gerenteRepository.findByUsuarioId(idUsuario);
		if (gerente == null) {
			throw new RuntimeException("Dados de cliente não encontrados para este usuário.");
		}

		GerenteDTO gerenteDTO = new GerenteDTO(gerente.getId(), gerente.getNome(), gerente.getDataNascimento());
		return new PerfilGerenteCompletoDTO(usuario.getId(), usuario.getEmail(), usuario.getCpf(),
				usuario.getTipoUsuario().name(), gerenteDTO);
	}
}