package com.epam.finaltask.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.mapper.VoucherMapper;
import com.epam.finaltask.model.HotelType;
import com.epam.finaltask.model.TourType;
import com.epam.finaltask.model.TransferType;
import com.epam.finaltask.model.User;
import com.epam.finaltask.model.Voucher;
import com.epam.finaltask.model.VoucherStatus;
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

        if (voucher.getStatus() == null) {
            voucher.setStatus(VoucherStatus.REGISTERED);
        }

        if (voucherDTO.userId() != null) {
            User user = userRepository.findById(voucherDTO.userId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            voucher.setUserId(user.getId());
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

        voucher.setUserId(user.getId());

        voucher.setStatus(VoucherStatus.REGISTERED);

        Voucher saved = voucherRepository.save(voucher);
        return voucherMapper.toVoucherDTO(saved);
    }

    @Override
    public VoucherDTO update(String id, VoucherDTO voucherDTO) {
        UUID voucherId = UUID.fromString(id);

        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new IllegalArgumentException("Voucher not found"));

        voucher.setTitle(voucherDTO.title());
        voucher.setDescription(voucherDTO.description());
        voucher.setPrice(voucherDTO.price());

        if (voucherDTO.transferType() != null) {
            voucher.setTourType(TourType.valueOf(voucherDTO.tourType()));
        }
        if (voucherDTO.transferType() != null) {
            voucher.setTransferType(TransferType.valueOf(voucherDTO.transferType()));
        }
        if (voucherDTO.hotelType() != null) {
            voucher.setHotelType(HotelType.valueOf(voucherDTO.hotelType()));
        }
        if (voucherDTO.status() != null) {
            voucher.setStatus(VoucherStatus.valueOf(voucherDTO.status()));
        }

        voucher.setArrivalDate(voucherDTO.arrivalDate());
        voucher.setEvictionDate(voucherDTO.evictionDate());

        if (voucherDTO.userId() != null) {
            User user = userRepository.findById(voucherDTO.userId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            voucher.setUserId(user.getId());
        }

        if (voucherDTO.isHot() != null) {
            voucher.setHot(voucherDTO.isHot());
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

        if (voucherDTO.isHot() != null) {
            voucher.setHot(voucherDTO.isHot());
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

    @Override
    public Page<VoucherDTO> search(TourType tourType,
                                   HotelType hotelType,
                                   String transferType,
                                   Pageable pageable) {

        TransferType transferEnum = null;
        if (transferType != null && !transferType.isBlank()) {
            transferEnum = TransferType.valueOf(transferType);
        }

        Page<Voucher> page = voucherRepository.search(tourType, hotelType, transferEnum, pageable);

        return page.map(voucherMapper::toVoucherDTO);
    }
}
