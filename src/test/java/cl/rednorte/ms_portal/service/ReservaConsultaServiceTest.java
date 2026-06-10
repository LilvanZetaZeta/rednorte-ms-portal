package cl.rednorte.ms_portal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import cl.rednorte.ms_portal.entity.readonly.ReservaView;
import cl.rednorte.ms_portal.entity.readonly.ReservaView.EstadoReserva;
import cl.rednorte.ms_portal.entity.readonly.UsuarioView;
import cl.rednorte.ms_portal.repository.readonly.ReservaViewRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

@SpringBootTest
public class ReservaConsultaServiceTest {

    @Autowired
    private ReservaConsultaService service;

    @MockBean
    private ReservaViewRepository repository;

    private ReservaView createMockReserva(Long id, EstadoReserva estado) {
        UsuarioView paciente = UsuarioView.builder().id(2L).idAuth("auth-paciente").build();
        UsuarioView medico = UsuarioView.builder().id(3L).idAuth("auth-medico").build();
        return ReservaView.builder()
                .id(id)
                .paciente(paciente)
                .medico(medico)
                .fechaHora(LocalDateTime.now())
                .estado(estado)
                .build();
    }

    @Test
    public void testListarTodas() {
        // Preparar
        ReservaView reserva = createMockReserva(1L, EstadoReserva.VIGENTE);
        when(repository.findAll()).thenReturn(List.of(reserva));

        // Actuar
        List<ReservaView> resultado = service.listarTodas();

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testObtenerPorId_Success() {
        // Preparar
        ReservaView reserva = createMockReserva(1L, EstadoReserva.VIGENTE);
        when(repository.findById(1L)).thenReturn(Optional.of(reserva));

        // Actuar
        ReservaView resultado = service.obtenerPorId(1L);

        // Verificar
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    public void testObtenerPorId_NotFound() {
        // Preparar
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Actuar y Verificar
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.obtenerPorId(1L);
        });
        assertEquals("Reserva no encontrada", exception.getMessage());
    }

    @Test
    public void testPorPacienteIdAuth() {
        // Preparar
        String idAuth = "auth-paciente";
        ReservaView reserva = createMockReserva(1L, EstadoReserva.VIGENTE);
        when(repository.findByPaciente_IdAuth(idAuth)).thenReturn(List.of(reserva));

        // Actuar
        List<ReservaView> resultado = service.porPacienteIdAuth(idAuth);

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(idAuth, resultado.get(0).getPaciente().getIdAuth());
    }

    @Test
    public void testPorCentro() {
        // Preparar
        Long centroId = 10L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("fechaHora").descending());
        ReservaView reserva = createMockReserva(1L, EstadoReserva.VIGENTE);
        Page<ReservaView> page = new PageImpl<>(List.of(reserva), pageable, 1);
        when(repository.findByCentroId(centroId, pageable)).thenReturn(page);

        // Actuar
        Page<ReservaView> resultado = service.porCentro(centroId, 0, 10);

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    public void testPorMedico() {
        // Preparar
        Long medicoId = 3L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("fechaHora").descending());
        ReservaView reserva = createMockReserva(1L, EstadoReserva.VIGENTE);
        Page<ReservaView> page = new PageImpl<>(List.of(reserva), pageable, 1);
        when(repository.findByMedicoId(medicoId, pageable)).thenReturn(page);

        // Actuar
        Page<ReservaView> resultado = service.porMedico(medicoId, 0, 10);

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
    }
}
