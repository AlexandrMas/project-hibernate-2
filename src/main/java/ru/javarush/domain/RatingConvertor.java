package ru.javarush.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RatingConvertor implements AttributeConverter<Rating, String> {

    @Override
    public String convertToDatabaseColumn(Rating rating) {
        return rating.getValue();
    }

    @Override
    public Rating convertToEntityAttribute(String value) {
        Rating[] values = Rating.values();
        for (Rating rating : values) {
            if (rating.getValue().equals(value)) {
                return rating;
            }
        }
        return null;
    }
}
