package uz.hojiakbar.child_tracking.config;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.enums.Gender;
import uz.hojiakbar.child_tracking.enums.UserRole;
import uz.hojiakbar.child_tracking.repository.UsersRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UsersRepository userRepository;


    @Override
    public void run(String... args) {
         if (!userRepository.existsByRole(UserRole.ADMIN)) {

            Users admin = Users.builder()
                    .full_name("Hojiakbar Admin")
                    .phone("+998901234567")
                    .email("abdunazarovhojiakbar05@gmail.com")
                     .role(UserRole.ADMIN)
                    .gender(Gender.MALE)
                    .date_of_birth(new  Date())
                    .isActive(true)
                    .build();



            userRepository.save(admin);

            System.out.println(">>> Default Admin yaratildi: +998901234567 / admin123");
        }

         if (!userRepository.existsByPhone("+998332080636")) {
             Users admin2 = Users.builder()
                     .full_name("Lutfulla Torayev")
                     .phone("+998332080636")
                     .email("kalibrimalik007@gmail.com")
                      .role(UserRole.PARENT)
                     .gender(Gender.MALE)
                     .date_of_birth(new Date())
                     .isActive(true)
                     .build();

             userRepository.save(admin2);
         }

    }

}