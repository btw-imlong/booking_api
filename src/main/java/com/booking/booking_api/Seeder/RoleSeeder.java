package com.booking.booking_api.Seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.booking.booking_api.Enity.Role;
import com.booking.booking_api.Repositories.RoleRepository;

@Configuration
public class RoleSeeder {

    @Bean
    CommandLineRunner seedRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role(null, "ADMIN"));
                roleRepository.save(new Role(null, "PROVIDER"));
                roleRepository.save(new Role(null, "USER"));
            }
        };
    }
}
