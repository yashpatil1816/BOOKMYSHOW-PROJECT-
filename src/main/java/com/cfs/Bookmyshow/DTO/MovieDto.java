package com.cfs.Bookmyshow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
    private Long id;
    private String movieName;
    private String language;   // ✅ FIX: was 'Language' (capital) → JSON sent as 'Language', frontend reads 'language'
    private String genre;
    private String duration;
    private String realeseDate;
    private String posterUrl;
    private String bannerUrl;
    private String description;
    private Double amount;
}