package com.epam.finaltask.restcontroller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.epam.finaltask.dto.ApiResponse;
import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.service.VoucherService;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherRestController {

    private final VoucherService voucherService;

    public VoucherRestController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    // GET /api/vouchers
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<VoucherDTO>>> findAll() {
        List<VoucherDTO> vouchers = voucherService.findAll();
        ApiResponse<List<VoucherDTO>> response = new ApiResponse<>();
        response.setResults(vouchers); // tests only read "results"
        response.setStatusCode("OK");
        response.setStatusMessage("Voucher list");
        return ResponseEntity.ok(response);
    }

    // GET /api/vouchers/user/{userId}
    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<VoucherDTO>>> findAllByUserId(@PathVariable String userId) {
        List<VoucherDTO> vouchers = voucherService.findAllByUserId(userId);
        ApiResponse<List<VoucherDTO>> response = new ApiResponse<>();
        response.setResults(vouchers);
        response.setStatusCode("OK");
        response.setStatusMessage("Voucher list by user");
        return ResponseEntity.ok(response);
    }

    // POST /api/vouchers
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VoucherDTO>> createVoucher(@RequestBody VoucherDTO voucherDTO) {
        VoucherDTO created = voucherService.create(voucherDTO);
        ApiResponse<VoucherDTO> response =
                ApiResponse.ok("Voucher is successfully created", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PATCH /api/vouchers/{id}
    @PatchMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VoucherDTO>> updateVoucher(@PathVariable String id,
                                                                 @RequestBody VoucherDTO voucherDTO) {
        VoucherDTO updated = voucherService.update(id, voucherDTO);
        ApiResponse<VoucherDTO> response =
                ApiResponse.ok("Voucher is successfully updated", updated);
        return ResponseEntity.ok(response);
    }

    // DELETE /api/vouchers/{id}
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteVoucher(@PathVariable String id) {
        voucherService.delete(id);
        String msg = String.format("Voucher with Id %s has been deleted", id);
        ApiResponse<Void> response = ApiResponse.ok(msg, null);
        return ResponseEntity.ok(response);
    }

    // PATCH /api/vouchers/{id}/status  (change hot flag, etc.)
    @PatchMapping(value = "/{id}/status",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<VoucherDTO>> changeVoucherStatus(@PathVariable String id,
                                                                       @RequestBody VoucherDTO voucherDTO) {
        VoucherDTO updated = voucherService.changeHotStatus(id, voucherDTO);
        ApiResponse<VoucherDTO> response =
                ApiResponse.ok("Voucher status is successfully changed", updated);
        return ResponseEntity.ok(response);
    }
}
