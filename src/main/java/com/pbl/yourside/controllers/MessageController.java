package com.pbl.yourside.controllers;

import com.pbl.yourside.entities.Message;
import com.pbl.yourside.entities.Report;
import com.pbl.yourside.entities.Status;
import com.pbl.yourside.repositories.MessageRepository;
import com.pbl.yourside.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/restApi/messages")
public class MessageController {

    private MessageRepository messageRepository;

    @Autowired
    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @PostMapping
    public ResponseEntity<Message> addMessage(@RequestBody Message message) {
        messageRepository.save(message);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public List<Message> findMessages(@RequestParam(name = "reportid", required = false) Long reportid) {
        System.out.println(reportid);
        if(reportid != null) {
            List<Message> messages = messageRepository.findAll().stream().filter(message -> message.getReport().getId() == reportid).collect(Collectors.toList());
            messages.sort((Message m1, Message m2) -> (int) (m1.getTimestamp() - m2.getTimestamp()));
            System.out.println(messages);
            return messages;
        }
        System.out.println("hello there");
        return messageRepository.findAll();
    }
}
