package org.example.echoBoard.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;


    private String message;

    @Builder.Default
    @Column(name = "is_read")
    private Boolean read = false;

    private LocalDateTime createdAt;

    private String url;

    public Notification(User receiver, String message, String url) {
        this.receiver = receiver;
        this.message = message;
        this.url = url;
        this.read = false;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }


}
