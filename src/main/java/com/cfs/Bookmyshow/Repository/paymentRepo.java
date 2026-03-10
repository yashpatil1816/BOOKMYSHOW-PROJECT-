package com.cfs.Bookmyshow.Repository;

import com.cfs.Bookmyshow.model.Movie;
import com.cfs.Bookmyshow.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface paymentRepo extends JpaRepository<Payment,Long> {

   Optional<Payment> findByTransactionId(String id);

}
