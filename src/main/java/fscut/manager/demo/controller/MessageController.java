package fscut.manager.demo.controller;


import fscut.manager.demo.dto.UserDto;
import fscut.manager.demo.entity.CustomerMessage;
import fscut.manager.demo.entity.Message;
import fscut.manager.demo.service.CustomerService;
import fscut.manager.demo.service.MessageService;
import fscut.manager.demo.util.websocket.WebSocketServer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController{

     @Resource
     private MessageService messageService;

     @Resource
     private CustomerService customerService;

     @GetMapping("getNum")
     public ResponseEntity<Integer> getUnreadMessageNum() {
          Subject subject = SecurityUtils.getSubject();
          UserDto user = (UserDto) subject.getPrincipal();
          return ResponseEntity.ok(messageService.getUnreadMessageNum(user.getUserId()));
     }

     @GetMapping("getMessageList")
     public ResponseEntity<List<Message>> getMessageList() {
          Subject subject = SecurityUtils.getSubject();
          UserDto user = (UserDto) subject.getPrincipal();
          List<Message> messageList = messageService.getMessage(user.getUserId());
          return ResponseEntity.ok(messageList);
     }

     @PostMapping("readMessage")
     public ResponseEntity<Message> readMessage(@RequestBody CustomerMessage cMessage) {
          messageService.readMessage(cMessage.getMessageId(), cMessage.getCustomerId());
          return ResponseEntity.ok(null);
     }

     @DeleteMapping("deleteMessage")
     public ResponseEntity<Integer> deleteMessage(Integer messageId, String username) {
          Integer customerId = customerService.getIdByUsername(username);
         Integer res = messageService.deleteMessage(messageId, customerId);
         return ResponseEntity.ok(res);
     }

     @GetMapping("/socket/push")
     public Object pushToWeb() {
          Subject subject = SecurityUtils.getSubject();
          UserDto user = (UserDto) subject.getPrincipal();
          Integer num = messageService.getUnreadMessageNum(user.getUserId());
          if(num != 0) {
              WebSocketServer.sendInfo(messageService.getMessage(user.getUserId()), user.getUsername());
          }
          return "good";
     }

}