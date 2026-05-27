package cl.rednorte.ms_portal.config;

import cl.rednorte.ms_portal.entity.readonly.UsuarioView;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RolUsuarioConverter implements AttributeConverter<UsuarioView.RolUsuario, String> {

    @Override
    public String convertToDatabaseColumn(UsuarioView.RolUsuario attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public UsuarioView.RolUsuario convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        return UsuarioView.RolUsuario.valueOf(dbData.toUpperCase());
    }
}