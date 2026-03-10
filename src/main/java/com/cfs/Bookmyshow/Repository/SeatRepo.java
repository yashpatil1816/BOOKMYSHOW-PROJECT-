package com.cfs.Bookmyshow.Repository;

import com.cfs.Bookmyshow.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepo extends JpaRepository<Seat, Long> {

    // Fetch all seats belonging to a specific screen
    List<Seat> findByScreenId(Long screenId);

}