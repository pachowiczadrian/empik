package pl.pachowicz.empik.complaint;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pachowicz.empik.common.CustomerId;
import pl.pachowicz.empik.common.ProductId;

import java.util.Optional;

interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    Optional<Complaint> findByProductIdAndCustomerId(ProductId productId, CustomerId customerId);

}
