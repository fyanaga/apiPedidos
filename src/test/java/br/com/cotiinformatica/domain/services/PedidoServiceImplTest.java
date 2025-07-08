package br.com.cotiinformatica.domain.services;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import br.com.cotiinformatica.domain.dtos.requests.PedidoRequest;
import br.com.cotiinformatica.domain.dtos.responses.PedidoResponse;
import br.com.cotiinformatica.domain.entities.OutboxMessage;
import br.com.cotiinformatica.domain.entities.Pedido;
import br.com.cotiinformatica.domain.exceptions.PedidoNaoEncontradoException;
import br.com.cotiinformatica.repositories.OutboxMessageRepository;
import br.com.cotiinformatica.repositories.PedidoRepository;
public class PedidoServiceImplTest {
	private PedidoServiceImpl pedidoServiceImpl;
	private PedidoRepository pedidoRepository;
	private OutboxMessageRepository outboxMessageRepository;
	private PlatformTransactionManager transactionManager;
	private ObjectMapper objectMapper;
	private ModelMapper modelMapper;
	@BeforeEach
	void setUp() {

		pedidoRepository = mock(PedidoRepository.class);
		outboxMessageRepository = mock(OutboxMessageRepository.class);
		transactionManager = mock(PlatformTransactionManager.class);
		objectMapper = mock(ObjectMapper.class);
		modelMapper = new ModelMapper();
		TransactionTemplate realTransactionTemplate = new TransactionTemplate(transactionManager);
		TransactionTemplate transactionTemplate = spy(realTransactionTemplate);
		doAnswer(invocation -> {
			TransactionCallbackWithoutResult callback = invocation.getArgument(0);
			callback.doInTransaction(mock(TransactionStatus.class));
			return null;
		}).when(transactionTemplate).executeWithoutResult(any());
		pedidoServiceImpl = new PedidoServiceImpl();
		pedidoServiceImpl.setPedidoRepository(pedidoRepository);
		pedidoServiceImpl.setOutboxMessageRepository(outboxMessageRepository);
		pedidoServiceImpl.setTransactionManager(transactionManager);
		pedidoServiceImpl.setModelMapper(modelMapper);
		pedidoServiceImpl.setObjectMapper(objectMapper);
	}
	@Test
	@DisplayName("Deve criar pedido com sucesso.")
	void testCriarPedidoComSucesso() throws Exception {
		var request = gerarPedidoRequest();
		var pedido = gerarPedido(UUID.randomUUID(), request);
		var payloadJson = "{\"pedido\":true}";
		when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
		when(objectMapper.writeValueAsString(any())).thenReturn(payloadJson);
		// Executa a lógica do método
		var response = pedidoServiceImpl.criar(request);
		// Verificações
		assertNotNull(response);
		assertEquals(request.getCliente(), response.getCliente());
		// Verifica se salvou a mensagem na outbox
		verify(outboxMessageRepository, times(1)).save(any(OutboxMessage.class));
	}
	@Test
	@DisplayName("Deve alterar pedido com sucesso.")
	void testAlterarPedidoComSucesso() {
		UUID id = UUID.randomUUID();
		PedidoRequest request = gerarPedidoRequest();
		Pedido pedido = gerarPedido(id, request);
		when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));
		when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
		PedidoResponse response = pedidoServiceImpl.alterar(id, request);
		assertNotNull(response);
		assertEquals(id, response.getId());
		assertEquals(request.getCliente(), response.getCliente());
	}
	@Test
	@DisplayName("Deve lançar erro se tentar alterar pedido não encontrado.")
	void testAlterarPedidoNaoEncontrado() {
		UUID id = UUID.randomUUID();
		PedidoRequest request = gerarPedidoRequest();
		when(pedidoRepository.findById(id)).thenReturn(Optional.empty());
		assertThrows(PedidoNaoEncontradoException.class, () -> {
			pedidoServiceImpl.alterar(id, request);
		});
	}
	@Test
	@DisplayName("Deve excluir pedido com sucesso.")
	void testExcluirPedidoComSucesso() {
		UUID id = UUID.randomUUID();
		PedidoRequest request = gerarPedidoRequest();
		Pedido pedido = gerarPedido(id, request);
		when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));
		PedidoResponse response = pedidoServiceImpl.excluir(id);
		assertEquals(id, response.getId());
		assertEquals(request.getCliente(), response.getCliente());
		verify(pedidoRepository, times(1)).delete(pedido);
	}
	@Test
	@DisplayName("Deve lançar erro se tentar excluir pedido não encontrado.")
	void testExcluirPedidoNaoEncontrado() {
		UUID id = UUID.randomUUID();
		when(pedidoRepository.findById(id)).thenReturn(Optional.empty());
		assertThrows(PedidoNaoEncontradoException.class, () -> {
			pedidoServiceImpl.excluir(id);
		});
	}
	@Test
	@DisplayName("Deve obter pedido com sucesso.")
	void testObterPedidoComSucesso() {
		UUID id = UUID.randomUUID();
		PedidoRequest request = gerarPedidoRequest();
		Pedido pedido = gerarPedido(id, request);
		when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));
		PedidoResponse response = pedidoServiceImpl.obter(id);
		assertNotNull(response);
		assertEquals(id, response.getId());
		assertEquals(request.getCliente(), response.getCliente());
	}
	@Test
	@DisplayName("Deve lançar erro se tentar obter pedido não encontrado.")
	void testObterPedidoNaoEncontrado() {
		UUID id = UUID.randomUUID();
		when(pedidoRepository.findById(id)).thenReturn(Optional.empty());
		assertThrows(PedidoNaoEncontradoException.class, () -> {
			pedidoServiceImpl.obter(id);
		});
	}
	@Test
	@DisplayName("Deve consultar pedidos com sucesso.")
	void testConsultarPedidoComSucesso() {
		PedidoRequest request1 = gerarPedidoRequest();
		PedidoRequest request2 = gerarPedidoRequest();
		Pedido pedido1 = gerarPedido(UUID.randomUUID(), request1);
		Pedido pedido2 = gerarPedido(UUID.randomUUID(), request2);
		List<Pedido> pedidos = Arrays.asList(pedido1, pedido2);
		Pageable pageable = PageRequest.of(0, 2);
		Page<Pedido> page = new PageImpl<>(pedidos, pageable, pedidos.size());
		when(pedidoRepository.findAll(pageable)).thenReturn(page);
		Page<PedidoResponse> responsePage = pedidoServiceImpl.consultar(pageable);
		assertNotNull(responsePage);
		assertEquals(2, responsePage.getContent().size());
		assertEquals(pedido1.getCliente(), responsePage.getContent().get(0).getCliente());
		assertEquals(pedido2.getCliente(), responsePage.getContent().get(1).getCliente());
		verify(pedidoRepository, times(1)).findAll(pageable);
	}
	// Utilitários
	private PedidoRequest gerarPedidoRequest() {
		var request = new PedidoRequest();
		var faker = new Faker();
		request.setCliente(faker.name().fullName());
		request.setDataHora("2025-07-01");
		request.setValor(faker.number().randomDouble(2, 1, 1000));
		request.setStatus(faker.number().numberBetween(0, 4));
		return request;
	}
	private Pedido gerarPedido(UUID id, PedidoRequest request) {
		var pedido = modelMapper.map(request, Pedido.class);
		pedido.setId(id);
		return pedido;
	}
}


