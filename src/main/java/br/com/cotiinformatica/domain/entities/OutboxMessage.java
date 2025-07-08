package br.com.cotiinformatica.domain.entities;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Entity
@Table(name = "tb_outbox_messages")
public class OutboxMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "tipo_evento")
	private String tipoEvento;

	@Column(name = "payload", length = 1000)
	private String payload;
	
	@Column(name = "enviado")
	private Boolean enviado = false;
	
	@Column(name = "data_hora_criacao")
	private LocalDateTime dataHoraCriacao = LocalDateTime.now();
}


