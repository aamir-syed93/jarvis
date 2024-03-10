package com.dev.jarvis.service;

import com.dev.jarvis.model.dto.ContactRequestDto;
import com.dev.jarvis.model.dto.ContactResponseDto;
import com.dev.jarvis.model.entity.Contact;
import com.dev.jarvis.repo.ContactRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactResponseDto saveContact(ContactRequestDto contactRequestDto){
        List<Contact> existingContactList = contactRepository.findAllByPhoneNumberOrEmail(
                contactRequestDto.getPhoneNumber(), contactRequestDto.getEmail());

        Contact contact;
        if(existingContactList == null || existingContactList.isEmpty()){

            //No existing contact found - creating PRIMARY contact
            contact = Contact.builder()
                    .email(contactRequestDto.getEmail())
                    .phoneNumber(contactRequestDto.getPhoneNumber())
                    .linkPrecedence("PRIMARY")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

        }else{

            contact = Contact.builder()
                    .email(contactRequestDto.getEmail())
                    .phoneNumber(contactRequestDto.getPhoneNumber())
                    .linkPrecedence("SECONDARY")
                    .linkedId(existingContactList.get(0).getId())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

        }
        Contact savedContact = contactRepository.save(contact);
        return null;
    }
}
