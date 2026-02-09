package org.example.echoBoard.controller.page;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.echoBoard.model.Notification;
import org.example.echoBoard.model.User;
import org.example.echoBoard.service.NotificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationPageController {
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
    @Transactional
    public String readOne(@PathVariable Long id) {
        Notification notification =
                notificationService.findById(id);

        notificationService.setRead(id);
        return  "redirect:" + notification.getUrl();
    }

    // 모두 읽음
    @PostMapping("/notifications/read-all")
    @Transactional
    public String readAll(HttpSession session) {

        Long userId = (Long) session.getAttribute("USER_ID");
        notificationService.readAll(userId);

        return "notifications";
    }
}
