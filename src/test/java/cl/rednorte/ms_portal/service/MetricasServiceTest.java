package cl.rednorte.ms_portal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import cl.rednorte.ms_portal.dto.CentroMetricaDTO;
import cl.rednorte.ms_portal.dto.DashboardResumenDTO;
import cl.rednorte.ms_portal.dto.DashboardSecretariaDTO;
import cl.rednorte.ms_portal.entity.readonly.ReservaView.EstadoReserva;
import cl.rednorte.ms_portal.entity.readonly.UsuarioView.RolUsuario;
import cl.rednorte.ms_portal.repository.readonly.CentroMedicoViewRepository;
import cl.rednorte.ms_portal.repository.readonly.EspecialidadViewRepository;
import cl.rednorte.ms_portal.repository.readonly.ReservaViewRepository;
import cl.rednorte.ms_portal.repository.readonly.UsuarioViewRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class MetricasServiceTest {

    @Autowired
    private MetricasService service;

    @MockBean
    private ReservaViewRepository reservaRepo;

    @MockBean
    private UsuarioViewRepository usuarioRepo;

    @MockBean
    private CentroMedicoViewRepository centroRepo;

    @MockBean
    private EspecialidadViewRepository especialidadRepo;

    @Test
    public void testObtenerResumenGlobal() {
        // Preparar
        when(reservaRepo.count()).thenReturn(100L);
        when(reservaRepo.countByEstado(EstadoReserva.VIGENTE)).thenReturn(40L);
        when(reservaRepo.countByEstado(EstadoReserva.CANCELADA)).thenReturn(10L);
        when(usuarioRepo.countByRol(RolUsuario.PACIENTE)).thenReturn(50L);
        when(usuarioRepo.countByRol(RolUsuario.MEDICO)).thenReturn(15L);
        when(centroRepo.count()).thenReturn(5L);
        when(especialidadRepo.count()).thenReturn(8L);

        // Actuar
        DashboardResumenDTO resultado = service.obtenerResumenGlobal();

        // Verificar
        assertNotNull(resultado);
        assertEquals(100L, resultado.getTotalReservas());
        assertEquals(40L, resultado.getReservasVigentes());
        assertEquals(10L, resultado.getReservasCanceladas());
        assertEquals(50L, resultado.getTotalPacientes());
        assertEquals(15L, resultado.getTotalMedicos());
        assertEquals(5L, resultado.getTotalCentros());
        assertEquals(8L, resultado.getTotalEspecialidades());
    }

    @Test
    public void testObtenerMetricasPorCentro() {
        // Preparar
        CentroMetricaDTO dto = new CentroMetricaDTO("Centro Norte", 25L);
        when(reservaRepo.obtenerMetricasPorCentro()).thenReturn(List.of(dto));

        // Actuar
        List<CentroMetricaDTO> resultado = service.obtenerMetricasPorCentro();

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Centro Norte", resultado.get(0).getNombreCentro());
        assertEquals(25L, resultado.get(0).getCantidadReservas());
    }

    @Test
    public void testObtenerDashboardSecretaria_Success() {
        // Preparar
        Long centroId = 1L;
        LocalDate fecha = LocalDate.now();
        Object[] conteos = new Object[]{15L, 8L, 4L, 3L, 2L};
        when(reservaRepo.obtenerConteosDashboardSecretaria(centroId, fecha)).thenReturn(conteos);
        when(reservaRepo.countMedicosByCentroId(centroId)).thenReturn(6L);

        // Actuar
        DashboardSecretariaDTO resultado = service.obtenerDashboardSecretaria(centroId, fecha);

        // Verificar
        assertNotNull(resultado);
        assertEquals(15L, resultado.getTotalReservasHoy());
        assertEquals(8L, resultado.getCitasVigentes());
        assertEquals(4L, resultado.getCitasConfirmadas());
        assertEquals(3L, resultado.getPendientesCheckin());
        assertEquals(2L, resultado.getCitasCanceladasHoy());
        assertEquals(6L, resultado.getTotalMedicosCentro());
    }

    @Test
    public void testObtenerDashboardSecretaria_Exception() {
        // Preparar
        Long centroId = 1L;
        LocalDate fecha = LocalDate.now();
        when(reservaRepo.obtenerConteosDashboardSecretaria(centroId, fecha))
                .thenThrow(new RuntimeException("Database error"));

        // Actuar
        DashboardSecretariaDTO resultado = service.obtenerDashboardSecretaria(centroId, fecha);

        // Verificar
        assertNotNull(resultado);
        assertEquals(0L, resultado.getTotalReservasHoy());
        assertEquals(0L, resultado.getCitasVigentes());
        assertEquals(0L, resultado.getCitasConfirmadas());
        assertEquals(0L, resultado.getPendientesCheckin());
        assertEquals(0L, resultado.getCitasCanceladasHoy());
        assertEquals(0L, resultado.getTotalMedicosCentro());
    }
}
