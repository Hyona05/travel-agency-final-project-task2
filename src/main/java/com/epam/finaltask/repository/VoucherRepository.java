package com.epam.finaltask.repository;

import java.util.List;
import java.util.UUID;

import com.epam.finaltask.dto.VoucherDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.finaltask.model.HotelType;
import com.epam.finaltask.model.TourType;
import com.epam.finaltask.model.TransferType;
import com.epam.finaltask.model.Voucher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoucherRepository extends JpaRepository<Voucher, UUID> {
    List<Voucher> findAllByUserId(UUID userId);
    List<Voucher> findAllByTourType(TourType tourType);
    List<Voucher> findAllByTransferType(TransferType transferType);
    List<Voucher> findAllByPrice(Double price);
    List<Voucher> findAllByHotelType(HotelType hotelType);
    @Query("""
           SELECT v
           FROM Voucher v
           WHERE (:tourType IS NULL OR v.tourType = :tourType)
             AND (:hotelType IS NULL OR v.hotelType = :hotelType)
             AND (:transferType IS NULL OR v.transferType = :transferType)
           """)
    Page<Voucher> search(@Param("tourType") TourType tourType,
                         @Param("hotelType") HotelType hotelType,
                         @Param("transferType") TransferType transferType,
                         Pageable pageable);
    Page<Voucher> findAllByTourTypeAndHotelTypeAndTransferType(
            TourType tourType,
            HotelType hotelType,
            TransferType transferType,
            Pageable pageable
    );

}
