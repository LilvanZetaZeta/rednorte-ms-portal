package cl.rednorte.ms_portal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import cl.rednorte.ms_portal.entity.readonly.CentroMedicoView;
import cl.rednorte.ms_portal.repository.readonly.CentroMedicoViewRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class CentroMedicoConsultaServiceTest {

    @Autowired
    private CentroMedicoConsultaService service;

    @MockBean
    private CentroMedicoViewRepository repository;

    private CentroMedicoView createMockCentro(Long id, String nombre, String region, String comuna) {
        return CentroMedicoView.builder()
                .id(id)
                .nombreSucursal(nombre)
                .region(region)
                .comuna(comuna)
                .direccion("Direccion Falsa 123")
                .build();
    }

    @Test
    public void testListarTodos() {
        // Preparar
        CentroMedicoView centro = createMockCentro(1L, "Centro Santiago", "Metropolitana", "Santiago");
        when(repository.findAll()).thenReturn(List.of(centro));

        // Actuar
        List<CentroMedicoView> resultado = service.listarTodos();

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Centro Santiago", resultado.get(0).getNombreSucursal());
    }

    @Test
    public void testObtenerPorId_Success() {
        // Preparar
        CentroMedicoView centro = createMockCentro(1L, "Centro Santiago", "Metropolitana", "Santiago");
        when(repository.findById(1L)).thenReturn(Optional.of(centro));

        // Actuar
        CentroMedicoView resultado = service.obtenerPorId(1L);

        // Verificar
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Centro Santiago", resultado.getNombreSucursal());
    }

    @Test
    public void testObtenerPorId_NotFound() {
        // Preparar
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Actuar y Verificar
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.obtenerPorId(1L);
        });
        assertEquals("Centro no encontrado", exception.getMessage());
    }

    @Test
    public void testBuscarPorLocalizacion_BothNotNull() {
        // Preparar
        CentroMedicoView centro = createMockCentro(1L, "Centro Santiago", "Metropolitana", "Santiago");
        when(repository.findByRegionAndComunaIgnoreCase("Metropolitana", "Santiago"))
                .thenReturn(List.of(centro));

        // Actuar
        List<CentroMedicoView> resultado = service.buscarPorLocalizacion("Metropolitana", "Santiago");

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Santiago", resultado.get(0).getComuna());
    }

    @Test
    public void testBuscarPorLocalizacion_OnlyComunaNotNull() {
        // Preparar
        CentroMedicoView centro = createMockCentro(1L, "Centro Santiago", "Metropolitana", "Santiago");
        when(repository.findByComunaIgnoreCase("Santiago")).thenReturn(List.of(centro));

        // Actuar
        List<CentroMedicoView> resultado = service.buscarPorLocalizacion(null, "Santiago");

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testBuscarPorLocalizacion_BothNull() {
        // Preparar
        CentroMedicoView centro = createMockCentro(1L, "Centro Santiago", "Metropolitana", "Santiago");
        when(repository.findAll()).thenReturn(List.of(centro));

        // Actuar
        List<CentroMedicoView> resultado = service.buscarPorLocalizacion(null, null);

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }
}
