package Kabbe.org.AuthService.service.impl;

import Kabbe.org.AuthService.entity.*;
import Kabbe.org.AuthService.exception.UserAlreadyExistsException;
import Kabbe.org.AuthService.repository.RoleRepository;
import Kabbe.org.AuthService.repository.UserRepository;
import Kabbe.org.AuthService.repository.VerificationTokenRepository;
import Kabbe.org.AuthService.securityconfig.CustomUserDetailsService;
import Kabbe.org.AuthService.service.PasswordResetTokenService;
import Kabbe.org.AuthService.service.UserService;
import Kabbe.org.AuthService.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final  UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;
    private  final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final PasswordResetTokenService passwordResetTokenService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public User addUser(User user) throws UserAlreadyExistsException {
        Optional<User> userExists = this.findByEmail(user.getEmail());
        if(userExists.isPresent()){
            throw new UserAlreadyExistsException("user with email " + user.getEmail() + " exist.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userRepository.save(user);

        Set<Role> roles = newUser.getRoles();
        if (roles == null) {
            roles = new HashSet<>();  // Initialize the roles set if it's null
            newUser.setRoles(roles);  // Set the initialized roles set back to the newUser object
            roles.add(new Role(RoleType.USER));
        }
        var jwtToken = jwtService.generateToken(newUser.getEmail(), roleRepository.findRoleByRoleName(roles.stream()
                .map(r->r.getRoleName()).collect(Collectors.toList()).stream().findAny().get()));
        Token token = Token.builder()
                .user(newUser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);

        return newUser;
    }

    private boolean emailExists(String email) {
        return userRepository.findUserByEmail(email) != null;
    }

    @Override
    public User updatePassword(String email) {
        return userRepository.findUserByEmail(email).orElse(null);
    }

    private void revokeAllUserTokens(User user){

        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getUserId());
        if(validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(t  -> { t.setExpired(true);
        t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


    public String generateToken(String username,Role role){
        return jwtService.generateToken(username,role);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String passwordToken) {
        passwordResetTokenService.createPasswordResetTokenForUser(user, passwordToken);
    }

    @Override
    public String validatePasswordResetToken(String passwordResetToken) {
        return passwordResetTokenService.validatePasswordResetToken(passwordResetToken);
    }

    @Override
    public User findUserByPasswordToken(String passwordResetToken) {
        return passwordResetTokenService.findUserByPasswordToken(passwordResetToken).get();
    }

    @Override
    public void resetUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean isOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String isTokenValid(String theToken) {
        VerificationToken token = verificationTokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if(token.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0){
            verificationTokenRepository.delete(token);
            return "Token expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public void lockUser(String email, boolean accountNonLocked) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setAccountNonLocked(accountNonLocked);
        userRepository.save(user);
    }

}
