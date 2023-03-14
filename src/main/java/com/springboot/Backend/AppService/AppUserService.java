package com.springboot.Backend.AppService;

import com.springboot.Backend.AppRepository.AppUserRepository;
import com.springboot.Backend.AppUser.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {
    @Autowired
    private AppUserRepository appUserRepository;

    public List<AppUser> getAllAppUsers(){
        return appUserRepository.findAll();
    }

    public Optional<AppUser> getAppUserById(Long id){
        return appUserRepository.findById(id);
    }

    public AppUser saveAppUser(AppUser appUser){
        return appUserRepository.save(appUser);
    }

    public void deleteAppUserById(Long id){
        appUserRepository.deleteById(id);
    }
}
