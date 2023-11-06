// package com.zosh.controller;

// import java.util.List;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.zosh.dto.ChatRoom;
// import com.zosh.service.ChatService;

// import lombok.RequiredArgsConstructor;

// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/chat")
// public class ChatController {

//     private final ChatService service;

//     @PostMapping
//     public ChatRoom createRoom(@RequestParam String name){
//         return service.createRoom(name);
//     }

//     @GetMapping
//     public List<ChatRoom> findAllRooms(){
//         return service.findAllRoom();
//     }
// }
