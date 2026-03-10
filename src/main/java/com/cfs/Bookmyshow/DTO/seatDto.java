package com.cfs.Bookmyshow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class seatDto {

    private Long id;
    private String seattype;
    private  String seatNumber;
    private String baseprice;
}
