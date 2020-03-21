package com.epam.dto.response;

import com.epam.dto.request.SongRequestDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

public class SongDataResponseDto {

    private byte[] data;

    private SongRequestDto metadata;

}
