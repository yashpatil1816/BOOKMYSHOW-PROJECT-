package com.cfs.Bookmyshow.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingrequestDto {

    // ✅ FIX: @JsonProperty maps lowercase JSON keys to these fields
    // Frontend sends: { userId, showId, seatId, paymentMethod }
    @JsonProperty("userId")
    private Long UserId;

    @JsonProperty("movieId")
    private Long MovieId;

    @JsonProperty("seatId")
    private List<Long> seatId;

    private String paymentMethod;

    @JsonProperty("showId")
    private Long ShowId;
}