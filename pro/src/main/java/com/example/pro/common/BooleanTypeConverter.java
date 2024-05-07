package com.example.pro.common;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BooleanTypeConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return attribute? "Y": "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return dbData.equalsIgnoreCase("Y");
    }
}
