package com.dev.jarvis.service;

import com.dev.jarvis.model.dto.ContactRequestDto;
import com.dev.jarvis.model.dto.ContactResponseDetails;
import com.dev.jarvis.model.dto.ContactResponseDto;
import com.dev.jarvis.model.entity.Contact;
import com.dev.jarvis.repo.ContactRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            //Existing contact found - checking whether it contains linkedId
            Optional<Integer> existingLinkedId =  existingContactList.stream()
                    .filter(cont -> cont.getLinkedId() != null)
                    .map(cont -> cont.getLinkedId())
                    .findFirst();

            //getting Primary Contact Id
            Contact primaryContact;
            if(existingLinkedId.isPresent()){
                primaryContact = contactRepository.getReferenceById(existingLinkedId.get());
                contact.setLinkedId(primaryContact.getId());
            }else{
                contact.setLinkedId(existingContactList.get(0).getId());
            }

        }
        Contact savedContact = contactRepository.save(contact);
        return generateResponse(savedContact);
    }

    private ContactResponseDto generateResponse(Contact currentContact){

        ContactResponseDto contactResponseDto;
        if(currentContact.getLinkedId() == null){
            contactResponseDto = ContactResponseDto.builder()
                    .contact(
                            ContactResponseDetails.builder()
                                    .emails(Set.of(currentContact.getEmail()))
                                    .phoneNumbers(Set.of(currentContact.getPhoneNumber()))
                                    .primaryContactId(currentContact.getId())
                                    .build()
                    )
                    .build();
        }else{

            //get all related contacts
            List<Contact> relatedContacts = contactRepository.findAllByLinkedId(currentContact.getLinkedId());

            //get primary contact
            Contact primaryContact = contactRepository.getReferenceById(currentContact.getLinkedId());

            //Merging all emails
            Set<String> emails = relatedContacts.stream().map(contact -> contact.getEmail()).collect(Collectors.toSet());
            emails.add(primaryContact.getEmail());

            //Merging all phoneNumbers
            Set<String> phoneNumbers = relatedContacts.stream().map(contact -> contact.getPhoneNumber()).collect(Collectors.toSet());
            phoneNumbers.add(primaryContact.getPhoneNumber());


            //Finding all secondary contactIds
            Set<Integer> secondaryContactIds = relatedContacts.stream()
                    .filter(contact -> contact.getLinkPrecedence().equalsIgnoreCase("SECONDARY"))
                    .map(contact -> contact.getId())
                    .collect(Collectors.toSet());


            ContactResponseDetails contactResponseDetails = ContactResponseDetails.builder()
                    .primaryContactId(primaryContact.getId())
                    .phoneNumbers(phoneNumbers)
                    .emails(emails)
                    .secondaryContactIds(secondaryContactIds)
                    .build();

            contactResponseDto = ContactResponseDto.builder()
                    .contact(contactResponseDetails)
                    .build();
        }

        return contactResponseDto;
    }
}
