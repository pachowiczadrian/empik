package pl.pachowicz.empik.complaint;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/complaints")
@AllArgsConstructor
class ComplaintController {

    private final ComplaintService complaintService;

    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getById(@PathVariable Long id) {
        return ResponseEntity.ok(complaintService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Long> createOrUpdate(@RequestBody ComplaintDto complaintDto, HttpServletRequest request) {
        var complaint = complaintService.addComplaintOrIncreaseCounter(complaintDto, request.getRemoteAddr());
        var httpStatus = complaint.getCounter() == 1 ? HttpStatus.CREATED : HttpStatus.OK;
        return new ResponseEntity<>(complaint.getId(), httpStatus);
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateContent(@PathVariable Long id, @RequestBody ComplaintContentDto contentDto) {
        complaintService.updateContent(id, contentDto);
        return ResponseEntity.noContent().build();
    }

}
