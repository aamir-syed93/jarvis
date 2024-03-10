package com.dev.jarvis.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactResponseDto {

    ContactResponseDetails contact;

    @Override
    public String toString() {
        return "{" +
                "contact" + contact +
                '}';
    }
}
