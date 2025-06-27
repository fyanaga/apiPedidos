package br.com.cotiinformatica.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cotiinformatica.domain.entities.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID>{

}
