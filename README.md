# jarvis

This is a project for identifying the customers based on their email or phoneNumber. 

TechStacks : 
Java 17
Spring Boot 3.x
H2 Database

API Request : 
URL  -  /jarvis/api/identify
Reques - 
{
  "email" : "<some_string_value>",
  "phoneNumber" : "<some_string_value>"
}
Response - 
{
    "contact": {
        "primaryContactId": <primary_contact_id>,
        "emails": [], >>>>> all distinct related emails to the contact
        "phoneNumbers": [], >>>>> all distinct related phone numbers to the contact 
        "secondaryContactIds": [] >>>>> all the linked contactIds
    }
}




