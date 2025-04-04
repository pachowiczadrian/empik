package pl.pachowicz.empik.complaint;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.pachowicz.empik.client.IpApiRestClient;
import pl.pachowicz.empik.common.CustomerId;
import pl.pachowicz.empik.common.ProductId;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ComplaintServiceTest {

    @Mock
    private ComplaintRepository complaintRepository;

    @Mock
    private IpApiRestClient ipApiRestClient;

    @InjectMocks
    private ComplaintService complaintService;


    private static final Long COMPLAINT_ID = 10L;
    private static final Long PRODUCT_ID = 1L;
    private static final Long DIFFERENT_PRODUCT_ID = 2L;
    private static final Long CUSTOMER_ID = 11L;
    private static final Long DIFFERENT_CUSTOMER_ID = 12L;
    private static final String IP_ADDRESS = "IP_ADDRESS";
    private static final String CONTENT = "CONTENT";
    private static final int COUNTER = 5;

    @ParameterizedTest
    @CsvSource(value = {
            "true, true, false",
            "true, false, true",
            "false, true, true",
            "false, false, true"
    })
    void addComplaintOrIncreaseCounter(boolean productIdWithExistingComplaint, boolean customerIdWithExistingComplaint, boolean shouldAddComplaint) {
        // given
        var complaintDto = new ComplaintDto(
                productIdWithExistingComplaint ? PRODUCT_ID : DIFFERENT_PRODUCT_ID,
                customerIdWithExistingComplaint ? CUSTOMER_ID : DIFFERENT_CUSTOMER_ID,
                CONTENT
        );
        var complaintSpy = spy(Complaint.builder()
                .id(COMPLAINT_ID)
                .productId(ProductId.of(PRODUCT_ID))
                .customerId(CustomerId.of(CUSTOMER_ID))
                .counter(COUNTER)
                .build());
        when(complaintRepository.findByProductIdAndCustomerId(ProductId.of(PRODUCT_ID), CustomerId.of(CUSTOMER_ID))).thenReturn(Optional.of(complaintSpy));
        when(complaintRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // when
        complaintService.addComplaintOrIncreaseCounter(complaintDto, IP_ADDRESS);

        // then
        verify(complaintRepository, times(shouldAddComplaint ? 1 : 0)).save(any());
        verify(ipApiRestClient, times(shouldAddComplaint ? 1 : 0)).getCountryByIp(IP_ADDRESS);
        verify(complaintSpy, times(shouldAddComplaint ? 0 : 1)).increaseCounter();
        assertEquals(shouldAddComplaint ? COUNTER : COUNTER + 1, complaintSpy.getCounter());

    }

    @Test
    void shouldGetComplaintById() {
        // given
        when(complaintRepository.findById(COMPLAINT_ID)).thenReturn(Optional.of(Complaint.builder().build()));

        // when
        complaintService.getById(COMPLAINT_ID);

        // then
        verify(complaintRepository).findById(COMPLAINT_ID);
    }

    @Test
    void shouldThrowExceptionIfComplaintNotFound() {
        // given
        when(complaintRepository.findById(COMPLAINT_ID)).thenReturn(Optional.empty());

        // when
        assertThrows(EntityNotFoundException.class, () -> complaintService.getById(COMPLAINT_ID));
    }

    @Test
    void shouldUpdateContent() {
        // given
        var complaintSpy = spy(Complaint.builder().build());
        when(complaintRepository.findById(COMPLAINT_ID)).thenReturn(Optional.of(complaintSpy));

        // when
        complaintService.updateContent(COMPLAINT_ID, new ComplaintContentDto(CONTENT));

        // then
        verify(complaintRepository).findById(COMPLAINT_ID);
        verify(complaintSpy).setContent(CONTENT);
    }

    @Test
    void shouldThrowExceptionDuringUpdateContentWhenComplaintNotFound() {
        // given
        when(complaintRepository.findById(COMPLAINT_ID)).thenReturn(Optional.empty());

        // when
        assertThrows(EntityNotFoundException.class, () ->
                complaintService.updateContent(COMPLAINT_ID, new ComplaintContentDto(CONTENT)));
    }
}