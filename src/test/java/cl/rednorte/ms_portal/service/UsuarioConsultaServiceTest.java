package cl.rednorte.ms_portal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import cl.rednorte.ms_portal.entity.readonly.UsuarioView;
import cl.rednorte.ms_portal.entity.readonly.UsuarioView.RolUsuario;
import cl.rednorte.ms_portal.repository.readonly.UsuarioViewRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class UsuarioConsultaServiceTest {

    @Autowired
    private UsuarioConsultaService service;

    @MockBean
    private UsuarioViewRepository repository;

    private UsuarioView createMockUsuario(Long id, String idAuth, String rut, RolUsuario rol) {
        return UsuarioView.builder()
                .id(id)
                .idAuth(idAuth)
                .rut(rut)
                .nombreCompleto("Juan Perez")
                .correo("juan@gmail.com")
                .rol(rol)
                .build();
    }

    @Test
    public void testListarTodos() {
        // Preparar
        UsuarioView usuario = createMockUsuario(1L, "auth-1", "111-1", RolUsuario.PACIENTE);
        when(repository.findAll()).thenReturn(List.of(usuario));

        // Actuar
        List<UsuarioView> resultado = service.listarTodos();

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan Perez", resultado.get(0).getNombreCompleto());
    }

    @Test
    public void testObtenerPorId_Success() {
        // Preparar
        UsuarioView usuario = createMockUsuario(1L, "auth-1", "111-1", RolUsuario.PACIENTE);
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        // Actuar
        UsuarioView resultado = service.obtenerPorId(1L);

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
        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    public void testObtenerPorIdAuth_Success() {
        // Preparar
        String idAuth = "auth-1";
        UsuarioView usuario = createMockUsuario(1L, idAuth, "111-1", RolUsuario.PACIENTE);
        when(repository.findByIdAuth(idAuth)).thenReturn(Optional.of(usuario));

        // Actuar
        UsuarioView resultado = service.obtenerPorIdAuth(idAuth);

        // Verificar
        assertNotNull(resultado);
        assertEquals(idAuth, resultado.getIdAuth());
    }

    @Test
    public void testObtenerPorIdAuth_NotFound() {
        // Preparar
        String idAuth = "auth-1";
        when(repository.findByIdAuth(idAuth)).thenReturn(Optional.empty());

        // Actuar y Verificar
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.obtenerPorIdAuth(idAuth);
        });
        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    public void testBuscarPorRut() {
        // Preparar
        String rut = "111-1";
        UsuarioView usuario = createMockUsuario(1L, "auth-1", rut, RolUsuario.PACIENTE);
        when(repository.findByRut(rut)).thenReturn(Optional.of(usuario));

        // Actuar
        Optional<UsuarioView> resultado = service.buscarPorRut(rut);

        // Verificar
        assertTrue(resultado.isPresent());
        assertEquals(rut, resultado.get().getRut());
    }

    @Test
    public void testBuscarMedicosPorEspecialidad() {
        // Preparar
        String especialidad = "Cardiologia";
        UsuarioView medico = createMockUsuario(2L, "auth-medico", "222-2", RolUsuario.MEDICO);
        when(repository.findByRolAndEspecialidades_NombreIgnoreCase(RolUsuario.MEDICO, especialidad))
                .thenReturn(List.of(medico));

        // Actuar
        List<UsuarioView> resultado = service.buscarMedicosPorEspecialidad(especialidad);

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(RolUsuario.MEDICO, resultado.get(0).getRol());
    }

    @Test
    public void testListarPersonalStaff() {
        // Preparar
        UsuarioView medico = createMockUsuario(2L, "auth-medico", "222-2", RolUsuario.MEDICO);
        when(repository.findByRolNot(RolUsuario.PACIENTE)).thenReturn(List.of(medico));

        // Actuar
        List<UsuarioView> resultado = service.listarPersonalStaff();

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertNotEquals(RolUsuario.PACIENTE, resultado.get(0).getRol());
    }

    @Test
    public void testListarAdministradoresDisponibles() {
        // Preparar
        UsuarioView admin = createMockUsuario(3L, "auth-admin", "333-3", RolUsuario.ADMINISTRATIVO);
        when(repository.findByRolAndCentroMedicoIsNull(RolUsuario.ADMINISTRATIVO)).thenReturn(List.of(admin));

        // Actuar
        List<UsuarioView> resultado = service.listarAdministradoresDisponibles();

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(RolUsuario.ADMINISTRATIVO, resultado.get(0).getRol());
    }

    @Test
    public void testListarCandidatosParaStaff() {
        // Preparar
        UsuarioView paciente = createMockUsuario(1L, "auth-1", "111-1", RolUsuario.PACIENTE);
        when(repository.findByRolAndCentroMedicoIsNull(RolUsuario.PACIENTE)).thenReturn(List.of(paciente));

        // Actuar
        List<UsuarioView> resultado = service.listarCandidatosParaStaff();

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(RolUsuario.PACIENTE, resultado.get(0).getRol());
    }
}
