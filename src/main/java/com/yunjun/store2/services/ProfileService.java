package com.yunjun.store2.services;

import com.yunjun.store2.repositories.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional
    public void showProfilesGreaterThan() {
        var profiles = profileRepository.findProfilesGreaterThan(2L);
        /*System.out.print(p.getId());
            System.out.print(p.getEmail());
            System.out.println(p.getLoyaltyPoints());*/
        profiles.forEach(System.out::println);

    }
}
