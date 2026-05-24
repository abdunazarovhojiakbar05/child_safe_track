package uz.hojiakbar.child_tracking.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService    {

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
