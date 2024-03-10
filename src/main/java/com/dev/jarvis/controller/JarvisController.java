package com.dev.jarvis.controller;

import com.dev.jarvis.model.dto.ContactRequestDto;
import com.dev.jarvis.model.dto.ContactResponseDto;
import com.dev.jarvis.service.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jarvis/api")
@AllArgsConstructor
public class JarvisController {

    private final ContactService contactService;

    @PostMapping("/identity")
    public ResponseEntity<ContactResponseDto> saveContact(@RequestBody ContactRequestDto requestDto){

        ContactResponseDto responseDto = contactService.saveContact(requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }



}
