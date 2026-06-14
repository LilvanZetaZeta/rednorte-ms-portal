package cl.rednorte.ms_portal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import cl.rednorte.ms_portal.entity.readonly.EspecialidadView;
import cl.rednorte.ms_portal.repository.readonly.EspecialidadViewRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class EspecialidadConsultaServiceTest {

    @Autowired
    private EspecialidadConsultaService service;

    @MockBean
    private EspecialidadViewRepository repository;

    private EspecialidadView createMockEspecialidad(Long id, String nombre) {
        return EspecialidadView.builder()
                .id(id)
                .nombre(nombre)
                .build();
    }

    @Test
    public void testListar() {
        // Preparar
        EspecialidadView esp = createMockEspecialidad(1L, "Pediatría");
        when(repository.findAll()).thenReturn(List.of(esp));

        // Actuar
        List<EspecialidadView> resultado = service.listar();

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Pediatría", resultado.get(0).getNombre());
    }

    @Test
    public void testObtenerPorId_Success() {
        // Preparar
        EspecialidadView esp = createMockEspecialidad(1L, "Pediatría");
        when(repository.findById(1L)).thenReturn(Optional.of(esp));

        // Actuar
        EspecialidadView resultado = service.obtenerPorId(1L);

        // Verificar
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pediatría", resultado.getNombre());
    }

    @Test
    public void testObtenerPorId_NotFound() {
        // Preparar
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Actuar y Verificar
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.obtenerPorId(1L);
        });
        assertEquals("Especialidad no encontrada", exception.getMessage());
    }
}
