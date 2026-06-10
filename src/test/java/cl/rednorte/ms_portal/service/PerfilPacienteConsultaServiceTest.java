package cl.rednorte.ms_portal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import cl.rednorte.ms_portal.entity.readonly.PerfilPacienteView;
import cl.rednorte.ms_portal.repository.readonly.PerfilPacienteViewRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class PerfilPacienteConsultaServiceTest {

    @Autowired
    private PerfilPacienteConsultaService service;

    @MockBean
    private PerfilPacienteViewRepository repository;

    private PerfilPacienteView createMockPerfil(Long id, String idAuth, String prevision) {
        return PerfilPacienteView.builder()
                .id(id)
                .idAuth(idAuth)
                .prevision(prevision)
                .telefonoContacto("+56912345678")
                .pacienteId(100L)
                .build();
    }

    @Test
    public void testPorIdAuth_Success() {
        // Preparar
        String idAuth = "auth-123";
        PerfilPacienteView perfil = createMockPerfil(1L, idAuth, "Fonasa");
        when(repository.findByIdAuth(idAuth)).thenReturn(Optional.of(perfil));

        // Actuar
        PerfilPacienteView resultado = service.porIdAuth(idAuth);

        // Verificar
        assertNotNull(resultado);
        assertEquals(idAuth, resultado.getIdAuth());
        assertEquals("Fonasa", resultado.getPrevision());
    }

    @Test
    public void testPorIdAuth_NotFound() {
        // Preparar
        String idAuth = "auth-123";
        when(repository.findByIdAuth(idAuth)).thenReturn(Optional.empty());

        // Actuar y Verificar
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.porIdAuth(idAuth);
        });
        assertEquals("Perfil no encontrado", exception.getMessage());
    }
}
