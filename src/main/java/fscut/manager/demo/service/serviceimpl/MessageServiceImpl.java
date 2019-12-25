package fscut.manager.demo.service.serviceimpl;

import fscut.manager.demo.dao.CustomerRepository;
import fscut.manager.demo.dao.MessageRepository;
import fscut.manager.demo.entity.Message;
import fscut.manager.demo.entity.Story;
import fscut.manager.demo.service.MessageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private MessageRepository messageRepository;

    @Override
    public void addCreateMessage(Story story) {
        if(story == null) {
            return;
        }
        String content = String.format("%s %s %s 需求", customerRepository.findRealNameByCustomerId(story.getEditId()), "新建了", story.getStoryName());
        Message message = new Message();
        BeanUtils.copyProperties(story, message);
        message.setContent(content);
        message = messageRepository.save(message);
        messageRepository.addCustomerMessage(message.getMessageId(), story.getEditId());
        messageRepository.addCustomerMessage(message.getMessageId(), story.getDesignId());
        messageRepository.addCustomerMessage(message.getMessageId(), story.getDevId());
        messageRepository.addCustomerMessage(message.getMessageId(), story.getTestId());
    }

    @Override
    public void addUpdateMessage(Story story) {
        if (story == null) {
            return;
        }
        Message message = new Message();
        BeanUtils.copyProperties(story, message);
        String content = String.format("%s %s %s 需求", customerRepository.findRealNameByCustomerId(story.getEditId()), "修改了", story.getStoryName());
        message.setContent(content);
        message = messageRepository.save(message);
        messageRepository.addCustomerMessage(message.getMessageId(), story.getEditId());
        messageRepository.addCustomerMessage(message.getMessageId(), story.getDesignId());
        messageRepository.addCustomerMessage(message.getMessageId(), story.getDevId());
        messageRepository.addCustomerMessage(message.getMessageId(), story.getTestId());
    }


    @Override
    public List<Message> getMessage(Integer customerId) {
        List<Integer> messageIds = messageRepository.getMessageId(customerId);
        return messageRepository.findMessagesByMessageIdIn(messageIds);
    }

    @Override
    public List<Message> getMessage(String username) {
        List<Integer> messageIds = messageRepository.getMessageId(username);
        return messageRepository.findMessagesByMessageIdIn(messageIds);
    }

    @Override
    public void readMessage(Integer messageId, Integer customerId) {
        messageRepository.readMessage(messageId, customerId);
    }

    @Override
    public Integer getUnreadMessageNum(Integer customerId) {
        return messageRepository.getUnreadMessageNum(customerId);
    }

    @Override
    public Integer getUnreadMessageNum(String username) {
        return messageRepository.getUnreadMessageNum(username);
    }

    @Override
    public Integer deleteMessage(Integer messageId, Integer customerId) {
        return messageRepository.deleteCustomerMessage(messageId, customerId);
    }

}