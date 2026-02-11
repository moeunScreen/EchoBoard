package org.example.echoBoard.controller.api;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.echoBoard.model.Notification;
import org.example.echoBoard.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping()
    public String notifications(Model model,
                                HttpSession session) {

        Long userId = (Long) session.getAttribute("USER_ID");
        List<Notification> notifications =
                notificationService.getAll(userId);
        model.addAttribute("notifications", notifications);
        return "notifications";

    }

    // 알림 하나 읽음
    @PostMapping("/{id}/read")
    @ResponseBody
    @Transactional
    public Map<String, Object> readOne(@PathVariable Long id,HttpSession session) {
        Notification notification =
                notificationService.findById(id);
        notificationService.setRead(id);
        Long userId = (Long) session.getAttribute("USER_ID");
        long unreadCount = notificationService.countByReceiver_IdAndReadFalse(userId);
        return Map.of(
                "success", true,
                "unreadCount", unreadCount
        );
    }

    // 모두 읽음
    @PostMapping("/read-all")
    @Transactional
    public String readAll(HttpSession session) {

        Long userId = (Long) session.getAttribute("USER_ID");
        notificationService.readAll(userId);

        return "notifications";
    }
}
