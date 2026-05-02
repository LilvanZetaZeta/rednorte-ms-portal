package cl.rednorte.ms_portal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//Controller temporal para verificar que la seguridad JWT de Supabase funciona.

@RestController
@RequestMapping("/api/portal")
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(Map.of(
                "mensaje", "JWT válido",
                "sub", jwt.getSubject(), // UUID del usuario en Supabase
                "email", jwt.getClaimAsString("email"),
                "rol", jwt.getClaimAsString("role")));
    }
}
