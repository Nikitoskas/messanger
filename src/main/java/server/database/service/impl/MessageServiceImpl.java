package server.database.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import server.database.entity.Message;
import server.database.entity.Status;
import server.database.repository.MessageRepository;
import server.database.service.MessageService;

import java.util.List;

public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Message create(Message message) {
        message.setStatus(Status.ACTIVE);

        Message createdMessage = messageRepository.saveAndFlush(message);
        return createdMessage;
    }

    @Override
    public List<Message> getAll() {
        List<Message> messages = messageRepository.findAll();
        return messages;
    }

    @Override
    public List<Message> getAllByAuthorId(Long id) {
        List<Message> messages = messageRepository.findAllByAuthor(id);
        return null;
    }

    @Override
    public List<Message> getAllByChatId(Long id) {
        List<Message> messages = messageRepository.findAllByChat(id);
        return messages;
    }

    @Override
    public Message findById(Long id) {
        Message foundedMessage = messageRepository.findById(id).orElse(null);
        return foundedMessage;
    }

    @Override
    public void delete(Long id) {
        messageRepository.deleteById(id);

    }
}
