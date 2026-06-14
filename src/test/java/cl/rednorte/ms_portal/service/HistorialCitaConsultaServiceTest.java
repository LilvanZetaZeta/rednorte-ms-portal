package cl.rednorte.ms_portal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import cl.rednorte.ms_portal.entity.readonly.HistorialCitaView;
import cl.rednorte.ms_portal.repository.readonly.HistorialCitaViewRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class HistorialCitaConsultaServiceTest {

    @Autowired
    private HistorialCitaConsultaService service;

    @MockBean
    private HistorialCitaViewRepository repository;

    private HistorialCitaView createMockHistorial(Long id, Long pacienteId, Long reservaId) {
        return HistorialCitaView.builder()
                .id(id)
                .fechaAtencion(LocalDateTime.now())
                .observaciones("Observacion de prueba")
                .procedimientoRealizado("Procedimiento de prueba")
                .reservaId(reservaId)
                .build();
    }

    @Test
    public void testPorPaciente() {
        // Preparar
        Long pacienteId = 10L;
        HistorialCitaView historial = createMockHistorial(1L, pacienteId, 100L);
        when(repository.findByPacienteId(pacienteId)).thenReturn(List.of(historial));

        // Actuar
        List<HistorialCitaView> resultado = service.porPaciente(pacienteId);

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Observacion de prueba", resultado.get(0).getObservaciones());
    }

    @Test
    public void testPorReserva() {
        // Preparar
        Long reservaId = 100L;
        HistorialCitaView historial = createMockHistorial(1L, 10L, reservaId);
        when(repository.findByReservaId(reservaId)).thenReturn(Optional.of(historial));

        // Actuar
        Optional<HistorialCitaView> resultado = service.porReserva(reservaId);

        // Verificar
        assertTrue(resultado.isPresent());
        assertEquals(reservaId, resultado.get().getReservaId());
    }
}
