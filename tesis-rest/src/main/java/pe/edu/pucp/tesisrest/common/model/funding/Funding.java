package pe.edu.pucp.tesisrest.common.model.funding;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "funding")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Funding extends Model {
    @Id
    @Column(name = "idFunding")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idFunding;

    @Column(name = "idFunding_Type")
    @Size(max = 10, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idFundingType;

    @Column(name = "Identifier")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String identifier;

    @Column(name = "Name")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String name;

    @Column(name = "Acronym")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String acronym;

    @Column(name = "PartOf")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String partOf;

    @Column(name = "StartDate")
    private Date startDate;

    @Column(name = "EndDate")
    private Date endDate;

    @Column(name = "CurrCode")
    @Size(max = 3, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String currCode;

    @Column(name = "Amount")
    private BigDecimal amount;

    @Column(name = "ExecutedAmount")
    private BigDecimal executedAmount;

    @Column(name = "Description")
    @Size(max = 500, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String description;
}
