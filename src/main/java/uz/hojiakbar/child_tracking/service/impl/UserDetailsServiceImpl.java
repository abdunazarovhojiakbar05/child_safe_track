package uz.hojiakbar.child_tracking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.repository.ChildRepository;
import uz.hojiakbar.child_tracking.repository.UsersRepository;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements  org.springframework.security.core.userdetails.UserDetailsService{

    private final UsersRepository usersRepository;
    private final ChildRepository childRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. Parent — email bilan
        Users user = usersRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user != null) {
            return new CustomUserDetails(user);
        }

        // 2. Child — phone bilan (register dan keyingi tokenlar)
        Child childByPhone = childRepository.findByPhone(username);
        if (childByPhone != null) {
            return new CustomUserDetails(childByPhone);
        }

        // 3. Child — email bilan (invite tokenlar, register dan oldin)  ✅ YANGI
        Child childByEmail = childRepository.findByEmail(username);
        if (childByEmail != null) {
            return new CustomUserDetails(childByEmail);
        }

        throw new UsernameNotFoundException("Foydalanuvchi topilmadi: " + username);
    }
}