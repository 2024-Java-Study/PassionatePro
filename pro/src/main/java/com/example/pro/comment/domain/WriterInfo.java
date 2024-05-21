package com.example.pro.comment.domain;

import com.example.pro.common.BooleanTypeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WriterInfo {

    private String username;
    private String profile;

    @Convert(converter = BooleanTypeConverter.class)
    private boolean memberQuit;
}
