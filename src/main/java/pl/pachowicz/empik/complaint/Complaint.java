package pl.pachowicz.empik.complaint;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import pl.pachowicz.empik.common.CustomerId;
import pl.pachowicz.empik.common.ProductId;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(
        name = "complaints",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "customer_id"})
)
@EqualsAndHashCode(of = "id")
@DynamicUpdate
class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "productId"))
    })
    private ProductId productId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "customerId"))
    })
    private CustomerId customerId;

    @Setter
    private String content;

    private String country;

    private int counter;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void increaseCounter() {
        counter++;
    }
}
