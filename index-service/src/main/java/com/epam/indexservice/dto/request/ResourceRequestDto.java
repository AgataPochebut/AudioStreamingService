package com.epam.indexservice.dto.request;

import com.epam.indexservice.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceRequestDto extends BaseEntity {

    private String name;

    private String parent;

    private String path;

    private Long size;

    @Column(unique = true)
    private String checksum;

}
