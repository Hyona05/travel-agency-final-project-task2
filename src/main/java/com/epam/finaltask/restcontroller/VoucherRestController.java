package com.epam.finaltask.restcontroller;

import com.epam.finaltask.dto.ApiResponse;
import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.model.HotelType;
import com.epam.finaltask.model.TourType;
import com.epam.finaltask.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
@Tag(name = "Vouchers", description = "Operations with travel vouchers")
public class VoucherRestController {

    private final VoucherService voucherService;

    public VoucherRestController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    // ===== BASIC CRUD & SEARCH =====

    @Operation(summary = "Get all vouchers", description = "Returns all vouchers")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<VoucherDTO>>> findAll() {
        List<VoucherDTO> vouchers = voucherService.findAll();
        return ResponseEntity.ok(
                ApiResponse.ok("Voucher list", vouchers)
        );
    }

    @Operation(summary = "Get vouchers by user", description = "Returns all vouchers belonging to a specific user")
    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<VoucherDTO>>> findAllByUserId(@PathVariable String userId) {
        List<VoucherDTO> vouchers = voucherService.findAllByUserId(userId);
        return ResponseEntity.ok(
                ApiResponse.ok("Voucher list by user", vouchers)
        );
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create voucher", description = "Creates new voucher. Only ADMIN can create voucher.")
    public ResponseEntity<ApiResponse<VoucherDTO>> createVoucher(@RequestBody VoucherDTO voucherDTO) {
        VoucherDTO created = voucherService.create(voucherDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Voucher is successfully created", created));
    }

    @PatchMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update voucher", description = "Updates existing voucher by ID. Only ADMIN can update voucher.")
    public ResponseEntity<ApiResponse<VoucherDTO>> updateVoucher(@PathVariable String id,
                                                                 @RequestBody VoucherDTO voucherDTO) {
        VoucherDTO updated = voucherService.update(id, voucherDTO);
        return ResponseEntity.ok(
                ApiResponse.ok("Voucher is successfully updated", updated)
        );
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete voucher", description = "Deletes voucher by ID. Only ADMIN can delete voucher.")
    public ResponseEntity<ApiResponse<Void>> deleteVoucher(@PathVariable String id) {
        voucherService.delete(id);
        String msg = "Voucher with Id %s has been deleted".formatted(id);
        return ResponseEntity.ok(ApiResponse.ok(msg, null));
    }

    @PatchMapping(value = "/{id}/status",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(
            summary = "Change voucher hot status",
            description = "Changes 'isHot' flag of voucher. Accessible by ADMIN and MANAGER."
    )
    public ResponseEntity<ApiResponse<VoucherDTO>> changeVoucherStatus(@PathVariable String id,
                                                                       @RequestBody VoucherDTO voucherDTO) {
        VoucherDTO updated = voucherService.changeHotStatus(id, voucherDTO);
        return ResponseEntity.ok(
                ApiResponse.ok("Voucher status is successfully changed", updated)
        );
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Search vouchers",
            description = "Search vouchers by tour type, hotel type, transfer type with pagination & sorting."
    )
    public ResponseEntity<ApiResponse<List<VoucherDTO>>> search(
            @RequestParam(required = false) TourType tourType,
            @RequestParam(required = false) HotelType hotelType,
            @RequestParam(required = false) String transferType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "price,asc") String sort) {

        String[] sortParts = sort.split(",");
        String sortBy = sortParts[0];
        String direction = sortParts.length > 1 ? sortParts[1] : "asc";

        Pageable pageable = PageRequest.of(
                page,
                size,
                direction.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending()
        );

        Page<VoucherDTO> voucherPage =
                voucherService.search(tourType, hotelType, transferType, pageable);

        return ResponseEntity.ok(
                ApiResponse.ok("Search successful", voucherPage.getContent())
        );
    }

    // ===== METHODS YOU SAID YOU WANT TO USE =====

    @PostMapping("/{id}/order")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(
            summary = "Order voucher",
            description = "Assigns voucher to user and changes its status accordingly"
    )
    public ResponseEntity<ApiResponse<VoucherDTO>> orderVoucher(
            @PathVariable String id,
            @RequestParam String userId
    ) {
        VoucherDTO ordered = voucherService.order(id, userId);
        return ResponseEntity.ok(
                ApiResponse.ok("Voucher ordered successfully", ordered)
        );
    }

    @GetMapping("/by-tour")
    @Operation(
            summary = "Find vouchers by tour type",
            description = "Returns all vouchers for given tour type"
    )
    public ResponseEntity<ApiResponse<List<VoucherDTO>>> findByTourType(
            @RequestParam TourType tourType
    ) {
        List<VoucherDTO> list = voucherService.findAllByTourType(tourType);
        return ResponseEntity.ok(
                ApiResponse.ok("Voucher list by tour type", list)
        );
    }

    @GetMapping("/by-transfer")
    @Operation(
            summary = "Find vouchers by transfer type",
            description = "Returns all vouchers for given transfer type"
    )
    public ResponseEntity<ApiResponse<List<VoucherDTO>>> findByTransferType(
            @RequestParam String transferType
    ) {
        List<VoucherDTO> list = voucherService.findAllByTransferType(transferType);
        return ResponseEntity.ok(
                ApiResponse.ok("Voucher list by transfer type", list)
        );
    }

    @GetMapping("/by-price")
    @Operation(
            summary = "Find vouchers by price",
            description = "Returns all vouchers with given price"
    )
    public ResponseEntity<ApiResponse<List<VoucherDTO>>> findByPrice(
            @RequestParam Double price
    ) {
        List<VoucherDTO> list = voucherService.findAllByPrice(price);
        return ResponseEntity.ok(
                ApiResponse.ok("Voucher list by price", list)
        );
    }

    @GetMapping("/by-hotel")
    @Operation(
            summary = "Find vouchers by hotel type",
            description = "Returns all vouchers for given hotel type"
    )
    public ResponseEntity<ApiResponse<List<VoucherDTO>>> findByHotelType(
            @RequestParam HotelType hotelType
    ) {
        List<VoucherDTO> list = voucherService.findAllByHotelType(hotelType);
        return ResponseEntity.ok(
                ApiResponse.ok("Voucher list by hotel type", list)
        );
    }
}
