package org.example.echoBoard.repository;

import org.example.echoBoard.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiver_IdAndReadFalse(Long receiverId);

    @Modifying(clearAutomatically = true)
    @Query("update Notification n set n.read = true where n.receiver.id = :receiverId")
    void readAllByUserId(@Param("receiverId") Long receiverId);

    @Modifying(clearAutomatically = true)
    @Query("update Notification n set n.read = true where n.id = :nId")
    void readById(@Param("nId") Long nId);

    long countByReceiver_IdAndReadFalse(Long userId);
}