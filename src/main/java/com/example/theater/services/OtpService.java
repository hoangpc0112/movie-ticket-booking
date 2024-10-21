package com.example.theater.services;

import com.example.theater.entities.AppUser;
import com.example.theater.repositories.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    @Autowired
    private AppUserRepo appUserRepo;

    @Scheduled(cron = "0 */5 * * * *")
    public void resetOtp() {
        for(AppUser appUser : appUserRepo.findAll()) {
            appUser.setEmailOtp("");
            appUserRepo.save(appUser);
        }
    }
}
