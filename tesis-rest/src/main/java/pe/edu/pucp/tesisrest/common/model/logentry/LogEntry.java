package pe.edu.pucp.tesisrest.common.model.logentry;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "log_table")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class LogEntry extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "record_id")
    private Integer recordId;

    @Column(name = "action")
    private String action;

    @Column(name = "action_date")
    private Timestamp actionDate;

    @Column(name = "processed")
    private Boolean processed = false;
}
