package dev.umc.auth.global.auth.service;

import dev.umc.auth.domain.user.entity.User;
import dev.umc.auth.domain.user.entity.UserRepository;
import dev.umc.auth.global.auth.entity.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with the name " + username));
        return new PrincipalDetails(user);
    }

    public UserDetails loadUserByStudentId(Long studentId) throws UsernameNotFoundException {
        User user = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with the student id " + studentId));
        return new PrincipalDetails(user);
    }
}
