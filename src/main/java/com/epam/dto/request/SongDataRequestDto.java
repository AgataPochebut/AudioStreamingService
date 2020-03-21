package com.epam.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@Data
public class SongDataRequestDto {

    @NotNull
    @Size(min = 1, max = 5000000)
    @Lob
    private byte[] data;

    @NotNull
    @NotEmpty
    private SongRequestDto metadata;

}
