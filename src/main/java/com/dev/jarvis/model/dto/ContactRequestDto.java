package com.dev.jarvis.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactRequestDto {

    String email;
    String phoneNumber;

}
