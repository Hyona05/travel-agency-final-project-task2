package com.epam.finaltask.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.mapper.VoucherMapper;
import com.epam.finaltask.model.*;
import com.epam.finaltask.repository.UserRepository;
import com.epam.finaltask.repository.VoucherRepository;

@Service
@Transactional
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;
    private final UserRepository userRepository;

    public VoucherServiceImpl(VoucherRepository voucherRepository,
                              VoucherMapper voucherMapper,
                              UserRepository userRepository) {
        this.voucherRepository = voucherRepository;
        this.voucherMapper = voucherMapper;
        this.userRepository = userRepository;
    }

    @Override
    public VoucherDTO create(VoucherDTO voucherDTO) {
        Voucher voucher = voucherMapper.toVoucher(voucherDTO);

        // default status if not set
        if (voucher.getStatus() == null) {
            voucher.setStatus(VoucherStatus.REGISTERED);
        }

        // link user if provided
        if (voucherDTO.getUserId() != null) {
            User user = userRepository.findById(voucherDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            voucher.setUser(user);
        }

        Voucher saved = voucherRepository.save(voucher);
        return voucherMapper.toVoucherDTO(saved);
    }

    @Override
    public VoucherDTO order(String id, String userId) {
        UUID voucherId = UUID.fromString(id);
        UUID uId = UUID.fromString(userId);

        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new IllegalArgumentException("Voucher not found"));

        User user = userRepository.findById(uId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        voucher.setUser(user);
        voucher.setStatus(VoucherStatus.REGISTERED);

        Voucher saved = voucherRepository.save(voucher);
        return voucherMapper.toVoucherDTO(saved);
    }

    @Override
    public VoucherDTO update(String id, VoucherDTO voucherDTO) {
        UUID voucherId = UUID.fromString(id);

        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new IllegalArgumentException("Voucher not found"));

        // simple “overwrite” update – you can make it smarter if you want
        voucher.setTitle(voucherDTO.getTitle());
        voucher.setDescription(voucherDTO.getDescription());
        voucher.setPrice(voucherDTO.getPrice());

        if (voucherDTO.getTourType() != null) {
            voucher.setTourType(TourType.valueOf(voucherDTO.getTourType()));
        }
        if (voucherDTO.getTransferType() != null) {
            voucher.setTransferType(TransferType.valueOf(voucherDTO.getTransferType()));
        }
        if (voucherDTO.getHotelType() != null) {
            voucher.setHotelType(HotelType.valueOf(voucherDTO.getHotelType()));
        }
        if (voucherDTO.getStatus() != null) {
            voucher.setStatus(VoucherStatus.valueOf(voucherDTO.getStatus()));
        }

        voucher.setArrivalDate(voucherDTO.getArrivalDate());
        voucher.setEvictionDate(voucherDTO.getEvictionDate());

        if (voucherDTO.getUserId() != null) {
            User user = userRepository.findById(voucherDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            voucher.setUser(user);
        }

        if (voucherDTO.getIsHot() != null) {
            voucher.setHot(voucherDTO.getIsHot());
        }

        Voucher saved = voucherRepository.save(voucher);
        return voucherMapper.toVoucherDTO(saved);
    }

    @Override
    public void delete(String voucherId) {
        UUID id = UUID.fromString(voucherId);
        voucherRepository.deleteById(id);
    }

    @Override
    public VoucherDTO changeHotStatus(String id, VoucherDTO voucherDTO) {
        UUID voucherId = UUID.fromString(id);

        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new IllegalArgumentException("Voucher not found"));

        if (voucherDTO.getIsHot() != null) {
            voucher.setHot(voucherDTO.getIsHot());
        }

        Voucher saved = voucherRepository.save(voucher);
        return voucherMapper.toVoucherDTO(saved);
    }

    @Override
    public List<VoucherDTO> findAllByUserId(String userId) {
        UUID uId = UUID.fromString(userId);
        return voucherRepository.findAllByUserId(uId)
                .stream()
                .map(voucherMapper::toVoucherDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VoucherDTO> findAllByTourType(TourType tourType) {
        return voucherRepository.findAllByTourType(tourType)
                .stream()
                .map(voucherMapper::toVoucherDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VoucherDTO> findAllByTransferType(String transferType) {
        TransferType type = TransferType.valueOf(transferType);
        return voucherRepository.findAllByTransferType(type)
                .stream()
                .map(voucherMapper::toVoucherDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VoucherDTO> findAllByPrice(Double price) {
        return voucherRepository.findAllByPrice(price)
                .stream()
                .map(voucherMapper::toVoucherDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VoucherDTO> findAllByHotelType(HotelType hotelType) {
        return voucherRepository.findAllByHotelType(hotelType)
                .stream()
                .map(voucherMapper::toVoucherDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VoucherDTO> findAll() {
        return voucherRepository.findAll()
                .stream()
                .map(voucherMapper::toVoucherDTO)
                .collect(Collectors.toList());
    }
}
