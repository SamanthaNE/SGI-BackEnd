package pe.edu.pucp.tesisrest.common.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApplicationProperties {

    @Value(value = "${config.key-code}")
    private String keyCode;

}
