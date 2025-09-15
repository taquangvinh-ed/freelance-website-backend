package com.freelancemarketplace.backend.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public class BaseEntity {

    private Timestamp created_at;

    private Timestamp updated_at;

}
