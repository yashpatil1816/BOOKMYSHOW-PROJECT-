package com.cfs.Bookmyshow.Repository;

import com.cfs.Bookmyshow.model.Screen;
import com.cfs.Bookmyshow.model.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowSeatRepo extends JpaRepository<ShowSeat,Long> {

    List<ShowSeat> findByShowId(Long Id);
    List<ShowSeat> findByBookingId(Long Id);//----------

    List<ShowSeat> findByShowIdAndStatus(Long ShowId,String status);


}
