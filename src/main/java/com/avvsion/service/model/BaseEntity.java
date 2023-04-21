package com.avvsion.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    @JsonIgnore
    public LocalDateTime createdAt = LocalDateTime.now();

    @CreatedBy
    @Column(updatable = false)
    @JsonIgnore
    public String createdBy = "Admin";

    @LastModifiedDate
    @Column(insertable = false)
    @JsonIgnore
    public LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(insertable = false)
    @JsonIgnore
    public String updatedBy;
}
