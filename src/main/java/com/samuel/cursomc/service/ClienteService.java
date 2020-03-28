package com.samuel.cursomc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.samuel.cursomc.domain.Cliente;
import com.samuel.cursomc.dto.ClienteDTO;
import com.samuel.cursomc.repositories.ClienteRepository;
import com.samuel.cursomc.service.exceptions.DataIntegrityException;
import com.samuel.cursomc.service.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public Cliente buscar(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
		}
	
	public Cliente update (Cliente obj) {
		Cliente newObj = buscar(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete (Integer id) {
		buscar(id);
		try {
			repo.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma categoria que possuí produtos");
			
		}
		
	}
	
	
	public List <Cliente> findAll (){
		return repo.findAll();
	}
	
	public Page <Cliente> findPage (Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction),
				orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO (ClienteDTO objDto) {
		return new Cliente (objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
}
