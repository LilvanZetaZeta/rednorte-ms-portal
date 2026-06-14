package cl.rednorte.ms_portal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import cl.rednorte.ms_portal.entity.readonly.ListaEsperaLocalView;
import cl.rednorte.ms_portal.repository.readonly.ListaEsperaLocalViewRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ListaEsperaConsultaServiceTest {

    @Autowired
    private ListaEsperaConsultaService service;

    @MockBean
    private ListaEsperaLocalViewRepository repository;

    private ListaEsperaLocalView createMockListaEspera(Long id, Integer prioridad) {
        return ListaEsperaLocalView.builder()
                .id(id)
                .prioridad(prioridad)
                .build();
    }

    @Test
    public void testListarTodas() {
        // Preparar
        ListaEsperaLocalView view = createMockListaEspera(1L, 1);
        when(repository.findAll()).thenReturn(List.of(view));

        // Actuar
        List<ListaEsperaLocalView> resultado = service.listarTodas();

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getPrioridad());
    }

    @Test
    public void testObtenerPorId_Success() {
        // Preparar
        ListaEsperaLocalView view = createMockListaEspera(1L, 2);
        when(repository.findById(1L)).thenReturn(Optional.of(view));

        // Actuar
        ListaEsperaLocalView resultado = service.obtenerPorId(1L);

        // Verificar
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(2, resultado.getPrioridad());
    }

    @Test
    public void testObtenerPorId_NotFound() {
        // Preparar
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Actuar y Verificar
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.obtenerPorId(1L);
        });
        assertEquals("No encontrado", exception.getMessage());
    }

    @Test
    public void testObtenerPorPaciente() {
        // Preparar
        Long pacienteId = 5L;
        ListaEsperaLocalView view = createMockListaEspera(1L, 3);
        when(repository.findByPacienteId(pacienteId)).thenReturn(List.of(view));

        // Actuar
        List<ListaEsperaLocalView> resultado = service.obtenerPorPaciente(pacienteId);

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(3, resultado.get(0).getPrioridad());
    }

    @Test
    public void testObtenerPorCentro() {
        // Preparar
        Long centroId = 10L;
        ListaEsperaLocalView view = createMockListaEspera(1L, 1);
        when(repository.findByCentroIdOrderByPrioridadAsc(centroId)).thenReturn(List.of(view));

        // Actuar
        List<ListaEsperaLocalView> resultado = service.obtenerPorCentro(centroId);

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }
}
