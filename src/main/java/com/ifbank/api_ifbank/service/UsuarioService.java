package com.ifbank.api_ifbank.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ifbank.api_ifbank.model.Cliente;
import com.ifbank.api_ifbank.model.Conta;
import com.ifbank.api_ifbank.model.Endereco;
import com.ifbank.api_ifbank.model.Telefone;
import com.ifbank.api_ifbank.model.Usuario;
import com.ifbank.api_ifbank.model.DTO.cadastro.CadastroClienteRequestDTO;
import com.ifbank.api_ifbank.model.DTO.cliente.ClienteDTO;
import com.ifbank.api_ifbank.model.DTO.cliente.ContaDTO;
import com.ifbank.api_ifbank.model.DTO.cliente.EnderecoDTO;
import com.ifbank.api_ifbank.model.DTO.cliente.TelefoneDTO;
import com.ifbank.api_ifbank.model.DTO.login.LoginRequestDTO;
import com.ifbank.api_ifbank.model.DTO.login.LoginResponseDTO;
import com.ifbank.api_ifbank.model.DTO.perfil.PerfilCompletoDTO;
import com.ifbank.api_ifbank.model.enums.StatusConta;
import com.ifbank.api_ifbank.model.enums.TipoUsuario;
import com.ifbank.api_ifbank.repository.ClienteRepository;
import com.ifbank.api_ifbank.repository.ContaRepository;
import com.ifbank.api_ifbank.repository.EnderecoRepository;
import com.ifbank.api_ifbank.repository.TelefoneRepository;
import com.ifbank.api_ifbank.repository.UsuarioRepository;
import com.ifbank.api_ifbank.service.interfaces.IUsuarioService;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService implements IUsuarioService {

	@Value("${app.upload.dir}")
	private String uploadDir;
	
    private UsuarioRepository usuarioRepository;
	private EnderecoRepository enderecoRepository;
	private TelefoneRepository telefoneRepository;
	private ClienteRepository clienteRepository;
	private ContaRepository contaRepository;
	
	public UsuarioService(UsuarioRepository usuarioRepository,EnderecoRepository enderecoRepository,TelefoneRepository telefoneRepository,ClienteRepository clienteRepository,ContaRepository contaRepository) 
		{
		
			this.usuarioRepository = usuarioRepository;
			this.enderecoRepository = enderecoRepository;
			this.telefoneRepository = telefoneRepository;
			this.clienteRepository = clienteRepository;
			this.contaRepository = contaRepository;
		}

	@Override
    public LoginResponseDTO autenticar(LoginRequestDTO dadosLogin) {
        
        Usuario usuario = usuarioRepository.findByEmail(dadosLogin.getEmail());
        
        if(usuario == null) {
        	throw new RuntimeException("Usuário não encontrado.");
        }
        if (!usuario.getSenha().equals(dadosLogin.getSenha())) {
        	throw new RuntimeException("Senha incorreta.");
        }
        
        Cliente cliente = clienteRepository.findByUsuarioId(usuario.getId());
        if(cliente == null) {
        	throw new RuntimeException("Cliente não encontrado.");
        }
        
        Conta conta = contaRepository.findByClienteId(cliente.getId());
        
        if(conta == null) {
        	throw new RuntimeException("Conta não encontrado.");
        }
        
        if(conta.getStatusConta().equals(StatusConta.PENDENTE)) {
        	throw new RuntimeException("Sua conta está pendente de aprovação.");
        }
        

        return new LoginResponseDTO(usuario.getId(), usuario.getCpf(), usuario.getEmail(),usuario.getTipoUsuario().name());
     
    }
    
	@Override
    @Transactional 
    public String cadastrarCliente(CadastroClienteRequestDTO dto) {
        
		
    	Usuario usuarioPorEmail = usuarioRepository.findByEmail(dto.getEmail());
    	if (usuarioPorEmail != null) {
    	    throw new RuntimeException("E-mail já cadastrado.");
    	}

    	
    	Usuario usuarioPorCpf = usuarioRepository.findByCpf(dto.getCpf());
    	if (usuarioPorCpf != null) {
    	    throw new RuntimeException("CPF já cadastrado.");
    	}

        // 2. Salvar o Usuário 
        Usuario usuario = new Usuario();
        usuario.setCpf(dto.getCpf());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha()); 
        usuario.setIdTipoUsuario(TipoUsuario.CLIENTE.getId());
        usuario = usuarioRepository.save(usuario);
        
        String caminhoFoto = null;

        if (dto.getFoto() != null && !dto.getFoto().isEmpty()) {
            caminhoFoto = salvarFoto(dto.getFoto(), usuario.getId());
        }

        // 3. Salvar o Endereço
        Endereco endereco = new Endereco(null, dto.getLogradouro(), dto.getNumero(), dto.getComplemento(), dto.getBairro(), dto.getCidade(), dto.getEstado(), dto.getCep());
        endereco = enderecoRepository.save(endereco);

        // 4. Salvar o Telefone
        Telefone telefone = new Telefone(null, dto.getCodPais(), dto.getCodArea(), dto.getNumeroTelefone());
        telefone = telefoneRepository.save(telefone);

        // 5. Salvar o Cliente 
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setDataNascimento(dto.getDataNascimento());
        cliente.setDataCadastro(LocalDate.now());
        cliente.setFotoUrl(caminhoFoto);
        cliente.setUsuario(usuario);
        cliente.setEndereco(endereco);
        cliente.setTelefone(telefone);
        cliente = clienteRepository.save(cliente);

        
        String numeroContaGerado = new Random().nextInt(90000) + 10000 + "-" + new Random().nextInt(10);
        
        Conta conta = new Conta();
        conta.setNumeroConta(numeroContaGerado);
        conta.setSaldo(BigDecimal.ZERO); 
        conta.setDataAbertura(LocalDate.now());
        conta.setIdStatusConta(StatusConta.PENDENTE.getId()); 
        conta.setCliente(cliente);
        contaRepository.save(conta);

        return "Cliente cadastrado com sucesso! Conta criada em análise: " + numeroContaGerado;
    }
    
	@Override
    public PerfilCompletoDTO obterPerfilCompleto(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Cliente cliente = clienteRepository.findByUsuarioId(idUsuario);
        if (cliente == null) {
            throw new RuntimeException("Dados de cliente não encontrados para este usuário.");
        }

        Conta conta = contaRepository.findByClienteId(cliente.getId());

        // Instancia os sub-DTOs de Contato e Localização
        EnderecoDTO enderecoDTO = null;
        if (cliente.getEndereco() != null) {
            Endereco end = cliente.getEndereco();
            enderecoDTO = new EnderecoDTO(end.getLogradouro(), end.getNumero(), end.getComplemento(), end.getBairro(), end.getCidade(), end.getEstado(), end.getCep());
        }

        TelefoneDTO telefoneDTO = null;
        if (cliente.getTelefone() != null) {
            Telefone tel = cliente.getTelefone();
            telefoneDTO = new TelefoneDTO(tel.getCodPais(), tel.getCodArea(), tel.getNumero());
        }

        // Monta o ClienteDTO
        ClienteDTO clienteDTO = new ClienteDTO(cliente.getId(), cliente.getNome(), cliente.getDataNascimento(), cliente.getDataCadastro(),cliente.getFotoUrl(), enderecoDTO, telefoneDTO);

        // Monta o ContaDTO
        ContaDTO contaDTO = null;
        if (conta != null) {
            contaDTO = new ContaDTO(conta.getNumeroConta(), conta.getSaldo(), conta.getDataAbertura(),conta.getStatusConta().name());
        }

        // Monta PerfilCompletoDTO
        PerfilCompletoDTO perfil = new PerfilCompletoDTO(
        		    usuario.getId(),
        	        usuario.getEmail(),
        	        usuario.getCpf(),
        	        usuario.getTipoUsuario().name(),
        	        clienteDTO,
        	        contaDTO
        		);
       

        return perfil;
    }
	
	
	private String salvarFoto(MultipartFile arquivo, Long idUsuario) {
	    try {
	        Path pastaUpload = Paths.get(uploadDir, "clientes", idUsuario.toString());

	        if (!Files.exists(pastaUpload)) {
	            Files.createDirectories(pastaUpload);
	        }

	        String nomeOriginal = arquivo.getOriginalFilename();

	        if (nomeOriginal == null || !nomeOriginal.contains(".")) {
	            throw new RuntimeException("Arquivo inválido.");
	        }

	        String extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
	        String nomeUnico = UUID.randomUUID().toString() + extensao;

	        Path caminhoCompleto = pastaUpload.resolve(nomeUnico);

	        Files.write(caminhoCompleto, arquivo.getBytes());

	        return "/uploads/clientes/" + idUsuario + "/" + nomeUnico;

	    } catch (IOException e) {
	        throw new RuntimeException("Falha ao salvar a foto de perfil: " + e.getMessage());
	    }
	}
}
