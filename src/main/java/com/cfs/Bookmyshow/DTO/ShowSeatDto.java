package com.cfs.Bookmyshow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowSeatDto {
    private  Long id;
    private  seatDto seat;
    private String status;
    private double price;
}
