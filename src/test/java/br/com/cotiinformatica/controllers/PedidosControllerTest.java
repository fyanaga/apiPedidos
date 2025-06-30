package br.com.cotiinformatica.controllers;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import br.com.cotiinformatica.domain.dtos.requests.PedidoRequest;
import br.com.cotiinformatica.domain.dtos.responses.PedidoResponse;
import br.com.cotiinformatica.domain.interfaces.PedidoService;
@WebMvcTest(PedidosController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PedidosControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@SuppressWarnings("removal")
	@MockBean
	private PedidoService pedidoService;

	private Faker faker;

	@BeforeEach
	void setUp() {
		faker = new Faker();
	}

	@Test
	@Order(1)
	@DisplayName("Deve executar POST /api/v1/pedidos com sucesso (201).")
	void testPostPedido_Sucesso() throws Exception {
		//Arrange
		var request = gerarPedidoRequest();
		var response = gerarPedidoResponse();

		//Act
		when(pedidoService.criar(any(PedidoRequest.class))).thenReturn(response);

		//Act / Assertions
		mockMvc.perform(post("/api/v1/pedidos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id").value(response.getId().toString()))
		.andExpect(jsonPath("$.cliente").value(response.getCliente()))
		.andExpect(jsonPath("$.valor").value(response.getValor()))
		.andExpect(jsonPath("$.dataHora").value(response.getDataHora()));
	}

	@Test
	@Order(2)
	@DisplayName("Deve executar POST /api/v1/pedidos com dados inválidos (400).")
	void testPostPedido_DadosInvalidos() throws Exception {
		//Arrange
		var request = new PedidoRequest();

		//Act / Assertions
		mockMvc.perform(post("/api/v1/pedidos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
		.andExpect(status().isBadRequest());	
	}

	@Test
	@Order(3)
	@DisplayName("Deve executar PUT /api/v1/pedidos com sucesso (200).")
	void testPutPedido_Sucesso() throws Exception {

		UUID id = UUID.randomUUID();

		PedidoRequest request = gerarPedidoRequest();
		PedidoResponse response = gerarPedidoResponse();
		response.setId(id);
		when(pedidoService.alterar(eq(id), any(PedidoRequest.class))).thenReturn(response);
		mockMvc.perform(put("/api/v1/pedidos/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(response.getId().toString()))
		.andExpect(jsonPath("$.cliente").value(response.getCliente()))
		.andExpect(jsonPath("$.valor").value(response.getValor()))
		.andExpect(jsonPath("$.dataHora").value(response.getDataHora()));
	}

	@Test
	@Order(4)
	@DisplayName("Deve executar PUT /api/v1/pedidos com dados inválidos (400).")
	void testPutPedido_DadosInvalidos() throws Exception {
		//Arrange
		var request = new PedidoRequest();

		//Act / Assertions
		mockMvc.perform(put("/api/v1/pedidos/{id}", UUID.randomUUID().toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
		.andExpect(status().isBadRequest());
	}

	@Test
	@Order(5)
	@DisplayName("Deve executar DELETE /api/v1/pedidos com sucesso (200).")
	void testDeletePedido_Sucesso() throws Exception {
		//Arrange
		UUID id = UUID.randomUUID();
		PedidoResponse response = gerarPedidoResponse();
		response.setId(id);
		//Act
		when(pedidoService.excluir(id)).thenReturn(response);
		//Act / Asserts
		mockMvc.perform(delete("/api/v1/pedidos/{id}", id))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(response.getId().toString()))
		.andExpect(jsonPath("$.cliente").value(response.getCliente()))
		.andExpect(jsonPath("$.valor").value(response.getValor()))
		.andExpect(jsonPath("$.dataHora").value(response.getDataHora()));
	}

	@Test
	@Order(6)
	@DisplayName("Deve executar GET /api/v1/pedidos com sucesso (200).")
	void testGetAllPedidos_Sucesso() throws Exception {
		//Arrange
		PedidoResponse p1 = gerarPedidoResponse();
		PedidoResponse p2 = gerarPedidoResponse();
		List<PedidoResponse> pedidos = List.of(p1, p2);
		Page<PedidoResponse> page = new PageImpl<>(pedidos, PageRequest.of(0, 25), pedidos.size());
		//Act
		when(pedidoService.consultar(ArgumentMatchers.any())).thenReturn(page);
		//Act / Asserts
		mockMvc.perform(get("/api/v1/pedidos")
				.param("page", "0")
				.param("size", "25")
				.param("sortBy", "id"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.content.length()").value(pedidos.size()));		
	}

	@Test
	@Order(7)
	@DisplayName("Deve executar GET /api/v1/pedidos/{id} com sucesso (200).")
	void testGetByIdPedido_Sucesso() throws Exception {
		UUID id = UUID.randomUUID();
		PedidoResponse response = gerarPedidoResponse();
		response.setId(id);
		when(pedidoService.obter(id)).thenReturn(response);
		mockMvc.perform(get("/api/v1/pedidos/{id}", id))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(response.getId().toString()))
		.andExpect(jsonPath("$.cliente").value(response.getCliente()))
		.andExpect(jsonPath("$.valor").value(response.getValor()))
		.andExpect(jsonPath("$.dataHora").value(response.getDataHora()));
	}

	private PedidoRequest gerarPedidoRequest() {

		var pedidoRequest = new PedidoRequest();

		pedidoRequest.setCliente(faker.name().fullName());
		pedidoRequest.setDataHora("2025-07-01");
		pedidoRequest.setValor(faker.number().randomDouble(2, 1, 1000));
		pedidoRequest.setStatus(faker.number().numberBetween(0, 4));

		return pedidoRequest;
	}

	private PedidoResponse gerarPedidoResponse() {

		var pedidoResponse = new PedidoResponse();

		pedidoResponse.setId(UUID.randomUUID());
		pedidoResponse.setCliente(faker.name().fullName());
		pedidoResponse.setDataHora("2025-07-01");
		pedidoResponse.setValor(faker.number().randomDouble(2, 1, 1000));
		pedidoResponse.setStatus(null);

		return pedidoResponse;
	}
}



