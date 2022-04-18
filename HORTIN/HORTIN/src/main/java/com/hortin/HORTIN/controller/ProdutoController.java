package com.hortin.HORTIN.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.hortin.HORTIN.entity.Produto;
import com.hortin.HORTIN.entity.Vendedor;
import com.hortin.HORTIN.repository.produtoRepository;
import com.hortin.HORTIN.repository.vendedorRepository;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

	@Autowired
	private produtoRepository repo;
	@Autowired
	private vendedorRepository vendedorRepo;
	
	@PostMapping("/vendedor/{vendedor_id}")
	public String insereProduto(@RequestBody Produto produto,@PathVariable long vendedor_id, UriComponentsBuilder uriBuilder){
		System.out.println(produto);
		Optional<Vendedor> vendedor = vendedorRepo.findById(vendedor_id);
		if(vendedor.isEmpty()) {
			return "{\"status\": \"error\"}";
		}
		produto.setVendedor(vendedor.get());
		repo.save(produto);
		
		URI uri = uriBuilder.path("/produtos/{id}").buildAndExpand(produto.getId_produto()).toUri();
		return "{\"status\": \"success\"}";
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<Produto>> deletaProduto(@PathVariable Long id){
		Optional<Produto> prodAchado = repo.findById(id);
		if(prodAchado.isEmpty()) {
			ResponseEntity.notFound().build();
		}
		repo.deleteById(id);
		
		return ResponseEntity.ok(prodAchado);
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<Produto> atualizaProduto(@PathVariable Long id, @RequestBody Produto produto){
		Optional<Produto> produtoAchado = repo.findById(id);
		
		if(produtoAchado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		produtoAchado.get().setDescricaoProduto(produto.getDescricaoProduto());
		produtoAchado.get().setNomeProduto(produto.getNomeProduto());
		produtoAchado.get().setValorProduto(produto.getValorProduto());
		
		return ResponseEntity.ok(produtoAchado.get());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> pegaProdutoPorId(@PathVariable Long id){
		Optional<Produto> produtoAchado = repo.findById(id);
		
		if(produtoAchado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(produtoAchado.get());
	}
	
	@GetMapping("/vendedor/{id}")
	public ResponseEntity<List<Produto>> pegaProdutosPorVendedor(@PathVariable Long id){
		List<Produto> listaProduto = repo.findByVendedorId(id);
		
		if(listaProduto.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(listaProduto);
		
	}
}
