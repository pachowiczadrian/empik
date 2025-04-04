package pl.pachowicz.empik.complaint;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pachowicz.empik.client.IpApiRestClient;
import pl.pachowicz.empik.common.CustomerId;
import pl.pachowicz.empik.common.ProductId;

@Service
@AllArgsConstructor
@Log4j2
class ComplaintService {

    private final ComplaintRepository complaintRepository;

    private final IpApiRestClient ipApiRestClient;

    public Complaint getById(Long id) {
        return findComplaintOrThrowException(id);
    }

    @Transactional
    public Complaint addComplaintOrIncreaseCounter(ComplaintDto complaintDto, String ipAddress) {
        // verify if product and customer exist

        var complaintOptional = complaintRepository
                .findByProductIdAndCustomerId(
                        ProductId.of(complaintDto.productId()),
                        CustomerId.of(complaintDto.customerId()));

        return complaintOptional
                .map(this::increaseCounter)
                .orElseGet(() -> createNewComplaint(complaintDto, ipAddress));
    }

    @Transactional
    public void updateContent(Long id, ComplaintContentDto contentDto) {
        var complaint = findComplaintOrThrowException(id);
        complaint.setContent(contentDto.content());
        log.info("Updating complaint content {id={}, content={}}", id, contentDto.content());
    }

    private Complaint findComplaintOrThrowException(Long id) {
        return complaintRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Complaint not found {id=%s}", id)));
    }

    private Complaint increaseCounter(Complaint complaint) {
        complaint.increaseCounter();
        log.info("Complaint for specific productId and customerId already exists - counter increased {id={}, counter={}}",
                complaint.getId(), complaint.getCounter());
        return complaint;
    }

    private Complaint createNewComplaint(ComplaintDto complaintDto, String ipAddress) {
        var complaint = complaintRepository.save(
                Complaint.builder()
                        .productId(ProductId.of(complaintDto.productId()))
                        .customerId(CustomerId.of(complaintDto.customerId()))
                        .content(complaintDto.content())
                        .country(getCountryByIpAddress(ipAddress))
                        .counter(1)
                        .build()
        );
        log.info("New complaint created {id={}}", complaint.getId());
        return complaint;
    }

    private String getCountryByIpAddress(String ipAddress) {
        try {
            var ipCountryDto = ipApiRestClient.getCountryByIp(ipAddress);
            log.debug("Found country with ip address {}", ipCountryDto);
            return ipCountryDto.country();
        } catch (Exception e) {
            log.warn("Cannot get country with ip address {ip={}}", ipAddress);
            return "XX";
        }
    }

}
