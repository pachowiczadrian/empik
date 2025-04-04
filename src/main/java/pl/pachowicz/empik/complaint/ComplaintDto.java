package pl.pachowicz.empik.complaint;

import java.util.Objects;

record ComplaintDto (Long productId, Long customerId, String content) {
    ComplaintDto {
        if (Objects.isNull(productId)) {
            throw new IllegalArgumentException ("productId cannot be null");
        }
        if (Objects.isNull(customerId)) {
            throw new IllegalArgumentException ("customerId cannot be null");
        }
        if (Objects.isNull(content)) {
            throw new IllegalArgumentException ("content cannot be null");
        }
    }
}
