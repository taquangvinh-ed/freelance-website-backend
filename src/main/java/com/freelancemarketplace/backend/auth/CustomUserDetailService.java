package com.freelancemarketplace.backend.auth;

import com.freelancemarketplace.backend.enums.AccountStatus;
import com.freelancemarketplace.backend.model.UserModel;
import com.freelancemarketplace.backend.repository.UserRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        UserModel user = userRepository.findByEmail(input).or(
                ()->userRepository.findByUsername(input)
        ).orElseThrow(()->new UsernameNotFoundException("Username not found with " + input));

        if(user.getAccountStatus() != AccountStatus.ACTIVE){
            if (user.getAccountStatus() == AccountStatus.PENDING)
                throw new DisabledException("Tài khoản chưa được kích hoạt. Vui lòng kiểm tra email.");
            if(user.getAccountStatus() == AccountStatus.DISABLED || user.getAccountStatus()== AccountStatus.BANNED){
                String reason = user.getDisableReason() != null ? user.getDisableReason() : "Không rõ lý do";
                throw new LockedException("Tài khoản đã bị khóa. Lý do: " + reason);
            }

        }
        return user;
    }
}
