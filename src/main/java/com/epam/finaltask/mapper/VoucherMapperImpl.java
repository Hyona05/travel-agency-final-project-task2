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

        if (dto.getId() != null) {
            voucher.setId(UUID.fromString(dto.getId()));
        }

        voucher.setTitle(dto.getTitle());
        voucher.setDescription(dto.getDescription());
        voucher.setPrice(dto.getPrice());

        if (dto.getTourType() != null) {
            voucher.setTourType(TourType.valueOf(dto.getTourType()));
        }
        if (dto.getTransferType() != null) {
            voucher.setTransferType(TransferType.valueOf(dto.getTransferType()));
        }
        if (dto.getHotelType() != null) {
            voucher.setHotelType(HotelType.valueOf(dto.getHotelType()));
        }
        if (dto.getStatus() != null) {
            voucher.setStatus(VoucherStatus.valueOf(dto.getStatus()));
        }

        voucher.setArrivalDate(dto.getArrivalDate());
        voucher.setEvictionDate(dto.getEvictionDate());
        if (dto.getIsHot() != null) {
            voucher.setHot(dto.getIsHot());
        }

        // User relation should be set in service (by loading User by dto.getUserId())
        return voucher;
    }

    @Override
    public VoucherDTO toVoucherDTO(Voucher voucher) {
        if (voucher == null) {
            return null;
        }
        VoucherDTO dto = new VoucherDTO();
        if (voucher.getId() != null) {
            dto.setId(voucher.getId().toString());
        }
        dto.setTitle(voucher.getTitle());
        dto.setDescription(voucher.getDescription());
        dto.setPrice(voucher.getPrice());
        if (voucher.getTourType() != null) {
            dto.setTourType(voucher.getTourType().name());
        }
        if (voucher.getTransferType() != null) {
            dto.setTransferType(voucher.getTransferType().name());
        }
        if (voucher.getHotelType() != null) {
            dto.setHotelType(voucher.getHotelType().name());
        }
        if (voucher.getStatus() != null) {
            dto.setStatus(voucher.getStatus().name());
        }
        dto.setArrivalDate(voucher.getArrivalDate());
        dto.setEvictionDate(voucher.getEvictionDate());
        if (voucher.getUser() != null && voucher.getUser().getId() != null) {
            dto.setUserId(voucher.getUser().getId());
        }
        dto.setIsHot(voucher.isHot());
        return dto;
    }
}
