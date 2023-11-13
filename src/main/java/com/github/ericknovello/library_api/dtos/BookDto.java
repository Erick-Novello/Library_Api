package com.github.ericknovello.library_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String autor;

    @NotEmpty
    private String isbn;


}
