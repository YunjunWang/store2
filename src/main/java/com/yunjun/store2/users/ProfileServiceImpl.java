package com.yunjun.store2.users;

import com.yunjun.store2.repositories.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional
    public void showProfilesGreaterThan() {
//        var profiles = profileRepository.findProfilesGreaterThan(2L);
//        var profiles = profileRepository.findByLoyaltyPointsGreaterThanOrderByUserEmail(2);
//        var profiles = profileRepository.findLoyalProfiles1(2);
        var profiles = profileRepository.getLoyalProfileSummaries(2);
        /*System.out.print(p.getId());
            System.out.print(p.getEmail());
            System.out.println(p.getLoyaltyPoints());*/
//        profiles.forEach(System.out::println);
        profiles.forEach(p -> {
            System.out.print(p.getId() + ": ");
            System.out.println(p.getEmail());
        });

    }
}
