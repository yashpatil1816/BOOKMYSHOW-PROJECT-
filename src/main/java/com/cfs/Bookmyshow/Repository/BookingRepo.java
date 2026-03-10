package com.cfs.Bookmyshow.Repository;

import com.cfs.Bookmyshow.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepo extends JpaRepository<Booking,Long> {

    List<Booking> findByUserId(Long userId);

    Optional<Booking> findByBookingNumber(String Bookingnumber);

    List<Booking> findByShowId(Long id);

}
