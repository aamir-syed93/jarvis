package com.dev.jarvis.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ContactResponseDetails {

    Integer primaryContactId;
    Set<String> emails;
    Set<String> phoneNumbers;
    Set<Integer> secondaryContactIds;

}
