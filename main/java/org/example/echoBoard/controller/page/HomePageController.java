package org.example.echoBoard.controller.page;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.echoBoard.model.User;
import org.example.echoBoard.service.NotificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class HomePageController {

    private final NotificationService notificationService;

    @GetMapping("/")
    public String home(Model model,
                       HttpSession session) {

        Long userId = (Long) session.getAttribute("USER_ID");
        long unreadCount =notificationService.countByReceiver_IdAndReadFalse(userId);

        model.addAttribute("unreadCount",unreadCount);
        return "home"; // src/main/resources/templates/home.html
    }
}