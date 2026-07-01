package com.ifbank.api_ifbank.service.interfaces;



import com.ifbank.api_ifbank.model.DTO.extrato.ExtratoPdfResponseDTO;

public interface IExtratoPdfService {
	
	public byte[] gerarPdfExtrato(ExtratoPdfResponseDTO dto);
}
