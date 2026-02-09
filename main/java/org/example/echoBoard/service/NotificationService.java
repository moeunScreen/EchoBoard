package org.example.echoBoard.service;

import lombok.RequiredArgsConstructor;
import org.example.echoBoard.model.Notification;
import org.example.echoBoard.model.User;
import org.example.echoBoard.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    @Transactional
    public List<Notification> getAll(Long userId) {
        List<Notification> list =
                notificationRepository.findByReceiver_IdAndReadFalse(userId);
        return list;
    }

    public Long countByReceiver_IdAndReadFalse(Long userId){
        long unreadCount =
                notificationRepository.countByReceiver_IdAndReadFalse(userId);
        return unreadCount;
    }

    @Transactional
    public void readAll(Long userId){
        notificationRepository.readAllByUserId(userId);
    }


    @Transactional
    public void setRead(Long nId){
        notificationRepository.readById(nId);
    }

    public Notification findById(Long nId){

        return notificationRepository.findById(nId)
                .orElseThrow();

    }
}
