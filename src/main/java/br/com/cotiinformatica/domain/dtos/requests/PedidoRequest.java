package br.com.cotiinformatica.domain.dtos.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PedidoRequest {
	@NotBlank(message = "O nome do cliente é obrigatório.")
	@Size(min = 6, max = 100, message = "O nome do cliente deve ter entre 6 e 100 caracteres.")
	private String cliente;
	
	@NotBlank(message = "A data e hora é obrigatório.")
	@Pattern(
		regexp = "^\\d{4}-\\d{2}-\\d{2}$",
		message = "A data deve estar no formato yyyy-MM-dd."
	)
	private String dataHora;
	
	@NotNull(message = "O valor é obrigatório.")
	@DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior do que zero.")
	private Double valor;
	
	@NotNull(message = "O status é obrigatório.")
	@Min(value = 0, message = "O status mínimo permitido é 0.")
	@Max(value = 0, message = "O status máximo permitido é 4.")
	private Integer status;
}
