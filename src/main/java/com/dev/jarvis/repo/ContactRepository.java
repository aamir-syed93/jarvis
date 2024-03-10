package com.dev.jarvis.repo;

import com.dev.jarvis.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    List<Contact> findAllByPhoneNumberOrEmail(String phoneNumber, String email);

    List<Contact> findAllByLinkedId(int linkedId);
}
