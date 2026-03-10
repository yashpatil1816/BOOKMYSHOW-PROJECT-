package com.cfs.Bookmyshow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreenDto {
    private Long id;
    private String screenName;
    private Integer totalSeats;
    private String screenType;   // ✅ NEW: STANDARD / IMAX / 4DX / 3D / DOLBY / SCREEN_X
    private TheaterDto theater;
}