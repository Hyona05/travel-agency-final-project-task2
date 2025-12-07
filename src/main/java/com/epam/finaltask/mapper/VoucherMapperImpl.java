package com.epam.finaltask.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.model.*;

@Component
public class VoucherMapperImpl implements VoucherMapper {

    @Override
    public Voucher toVoucher(VoucherDTO dto) {
        if (dto == null) {
            return null;
        }

        Voucher voucher = new Voucher();

        if (dto.id() != null && !dto.id().isBlank()) {
            voucher.setId(UUID.fromString(dto.id()));
        }

        voucher.setTitle(dto.title());
        voucher.setDescription(dto.description());
        voucher.setPrice(dto.price());

        if (dto.tourType() != null) {
            voucher.setTourType(TourType.valueOf(dto.tourType()));
        }
        if (dto.transferType() != null) {
            voucher.setTransferType(TransferType.valueOf(dto.transferType()));
        }
        if (dto.hotelType() != null) {
            voucher.setHotelType(HotelType.valueOf(dto.hotelType()));
        }
        if (dto.status() != null) {
            voucher.setStatus(VoucherStatus.valueOf(dto.status()));
        }

        voucher.setArrivalDate(dto.arrivalDate());
        voucher.setEvictionDate(dto.evictionDate());

        if (dto.isHot() != null) {
            voucher.setHot(dto.isHot());
        }
        return voucher;
    }

    @Override
    public VoucherDTO toVoucherDTO(Voucher voucher) {
        if (voucher == null) {
            return null;
        }

        String id = voucher.getId() != null ? voucher.getId().toString() : null;
        String tourType = voucher.getTourType() != null ? voucher.getTourType().name() : null;
        String transferType = voucher.getTransferType() != null ? voucher.getTransferType().name() : null;
        String hotelType = voucher.getHotelType() != null ? voucher.getHotelType().name() : null;
        String status = voucher.getStatus() != null ? voucher.getStatus().name() : null;
        UUID userId = (voucher.getUserId() != null) ? voucher.getUserId(): null;
        Boolean isHot = voucher.isHot();

        return new VoucherDTO(
                id,
                voucher.getTitle(),
                voucher.getDescription(),
                voucher.getPrice(),
                tourType,
                transferType,
                hotelType,
                status,
                voucher.getArrivalDate(),
                voucher.getEvictionDate(),
                userId,
                isHot
        );
    }
}
