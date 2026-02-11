package org.example.echoBoard.controller.page;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.echoBoard.model.Notification;
import org.example.echoBoard.model.User;
import org.example.echoBoard.service.NotificationService;
import org.example.echoBoard.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomePageController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping("/")
    public String home(Model model,
                       HttpSession session) {

        Long userId = (Long) session.getAttribute("USER_ID");
        long unreadCount =notificationService.countByReceiver_IdAndReadFalse(userId);

        List<Notification> notifications =
                notificationService.getAll(userId);

        User user = userService.findById(userId);
        String username = user.getUsername();

        model.addAttribute("username",username);
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount",unreadCount);
        return "home"; // src/main/resources/templates/home.html
    }
}