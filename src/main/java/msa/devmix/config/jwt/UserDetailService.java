package msa.devmix.config.jwt;

import lombok.RequiredArgsConstructor;
import msa.devmix.config.oauth.userinfo.UserPrincipal;
import msa.devmix.domain.user.User;
import msa.devmix.exception.CustomException;
import msa.devmix.exception.ErrorCode;
import msa.devmix.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.USER_NOT_FOUND, "User not found with username : " + username)
                );

        return UserPrincipal.from(user);
    }

    @Transactional
    public UserDetails loadUserByUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                    new CustomException(ErrorCode.USER_NOT_FOUND, "User not found with userId : " + id)
        );

        return UserPrincipal.from(user);
    }
}
