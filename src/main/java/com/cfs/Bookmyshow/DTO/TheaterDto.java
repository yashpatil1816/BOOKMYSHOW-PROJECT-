package com.cfs.Bookmyshow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheaterDto {

    private long id;
    private String name;
    private String city;
    private String address;
    private String totalScreen;
}
