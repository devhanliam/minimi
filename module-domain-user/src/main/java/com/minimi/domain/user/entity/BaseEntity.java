package com.minimi.domain.user.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {
    private static final String DATE_FORMAT ="yyyy-MM-dd HH:mm:ss";
    private static final String TIME_ZONE ="Asia/Seoul";

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = DATE_FORMAT,timezone = TIME_ZONE)
    @Column(name = "create_time",updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = DATE_FORMAT,timezone = TIME_ZONE)
    @Column(name = "update_time")
    private LocalDateTime updateTime;


}
