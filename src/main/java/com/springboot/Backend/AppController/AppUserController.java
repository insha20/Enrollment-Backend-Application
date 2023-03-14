package com.springboot.Backend.AppController;

import com.springboot.Backend.AppRepository.AppUserRepository;
import com.springboot.Backend.AppUser.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("")
public class AppUserController {
    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping("/enroll")
    public String index(Model model){
        model.addAttribute("user",new AppUser());
        model.addAttribute("users",appUserRepository.findAll());
        return "index";
    }

    @PostMapping("/addUser")
    public String addUser(AppUser user, @RequestParam("file") MultipartFile file) throws IOException {
        user.setPhoto(Base64.getEncoder().encodeToString(file.getBytes()));
        System.out.println("User object: " + user); // add this logging statement
        try {
            appUserRepository.save(user);
        } catch (Exception e) {
            System.out.println("Error saving user: " + e.getMessage()); // add this logging statement
        }
        return "redirect:/enroll";
    }
}
