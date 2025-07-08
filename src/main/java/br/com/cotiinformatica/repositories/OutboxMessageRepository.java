package br.com.cotiinformatica.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.cotiinformatica.domain.entities.OutboxMessage;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Integer>{
	/*
	 * Consulta para retornar apenas outboxes não enviados (enviado = false)
	 */
	List<OutboxMessage> findByEnviadoFalseAndTipoEvento(String tipoEvento);
	
	/*
	 * Consulta para retornar apenas outboxes não enviados (enviado = false)
	 * Versão 'JPQL'
	 */
	@Query("""
			SELECT om FROM OutboxMessage om
			WHERE om.enviado = false and om.tipoEvento = :tipoEvento
			""")
	List<OutboxMessage> findByNaoEnviadosPorTipo(@Param("tipoEvento") String tipoEvento);
}
