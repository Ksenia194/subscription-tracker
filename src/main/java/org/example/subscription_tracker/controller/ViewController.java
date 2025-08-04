package org.example.subscription_tracker.controller;

import org.example.subscription_tracker.dto.SubscriptionDTO;
import org.example.subscription_tracker.service.SubscriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ViewController {
    private final SubscriptionService subscriptionService;

    public ViewController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<SubscriptionDTO> subscriptions = subscriptionService.findAll();
        model.addAttribute("subscriptions", subscriptions);
        return "index";
    }

    @PostMapping("/web/subscriptions")
    public String createSubscription(@ModelAttribute SubscriptionDTO subscriptionDTO) {
        subscriptionService.save(subscriptionDTO);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        SubscriptionDTO subscriptionDTO = subscriptionService.findById(id);
        model.addAttribute("subscription", subscriptionDTO);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updateSubscription(@PathVariable Long id, @ModelAttribute("subscription") SubscriptionDTO subscriptionDTO) {
        subscriptionService.update(subscriptionDTO, id);
        return "redirect:/";
    }

    @PostMapping("/web/delete/{id}")
    public String deleteSubscription(@PathVariable Long id) {
        subscriptionService.delete(id);
        return "redirect:/";
    }
}
