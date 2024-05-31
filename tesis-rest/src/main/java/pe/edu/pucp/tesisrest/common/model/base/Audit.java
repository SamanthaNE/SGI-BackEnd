package pe.edu.pucp.tesisrest.common.model.base;
import pe.edu.pucp.tesisrest.common.enums.ActiveEnums;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

import static pe.edu.pucp.tesisrest.common.model.base.Model.MUST_NOT_BE_GREATER_THAN_CHARS;

@Data
@Embeddable
public class Audit {

    /* COLUMNS */
    @Column(name = "estado")
    @NotNull
    @Min(0)
    @Max(1)
    private Integer estado;

    @Column(name = "usuario_creacion")
    @Size(max = 50, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    @NotBlank
    private String usuarioCreacion;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    @NotNull
    private Date fechaCreacion;

    @Column(name = "usuario_modificacion")
    @Size(max = 50, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    @NotBlank
    @NotNull
    private String usuarioModificacion;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "fecha_modificacion")
    private Date fechaModificacion;

    /* CREATE */
    public void auditForCreate(String usuarioCreacion, Integer estado, Date fechaCreacion) {
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    public void auditForCreate(String usuarioCreacion, Integer estado) {
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = new Date();
        this.estado = estado;
    }

    public void auditForCreate(String usuarioCreacion, Date fechaCreacion) {
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.estado = ActiveEnums.ACTIVE.getCode();
    }

    public void auditForCreate(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = new Date();
        this.estado = ActiveEnums.ACTIVE.getCode();
    }

    /* UPDATE */
    public void auditForUpdate(String usuarioModificacion, Integer estado, Date fechaModificacion) {
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModificacion = fechaModificacion;
        this.estado = estado;
    }

    public void auditForUpdate(String usuarioModificacion, Integer estado) {
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModificacion = new Date();
        this.estado = estado;
    }

    public void auditForUpdate(String usuarioModificacion, Date fechaCreacion) {
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModificacion = fechaCreacion;
        this.estado = ActiveEnums.ACTIVE.getCode();
    }

    public void auditForUpdate(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModificacion = new Date();
        this.estado = ActiveEnums.ACTIVE.getCode();
    }
}
