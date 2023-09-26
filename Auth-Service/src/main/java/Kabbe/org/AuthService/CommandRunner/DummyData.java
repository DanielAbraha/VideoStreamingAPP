package Kabbe.org.AuthService.CommandRunner;

import Kabbe.org.AuthService.dto.UserDto;
import Kabbe.org.AuthService.entity.Role;
import Kabbe.org.AuthService.entity.RoleType;
import Kabbe.org.AuthService.entity.User;
import Kabbe.org.AuthService.event.RegistrationCompleteEvent;
import Kabbe.org.AuthService.event.listener.RegistrationCompleteEventListener;
import Kabbe.org.AuthService.repository.RoleRepository;
import Kabbe.org.AuthService.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Transactional
public class DummyData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;
    private HttpServletRequest request;
    private  final RestTemplate restTemplate;
    private final ModelMapper mapper;

    @Override
    public void run(String... args) throws Exception {
        // Check if the 'role' table is empty
        if (roleRepository.count() == 0) {
            // Create RoleType values
            List<RoleType> roleTypes = Arrays.asList(RoleType.ADMIN, RoleType.CLIENT, RoleType.USER);

            // Save RoleType values to the 'role' table
            for (RoleType roleType : roleTypes) {
                Role role = new Role();
                role.setRoleName(roleType);
                roleRepository.save(role);
            }
        }

        // Check if the 'admin' user exists
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            // Create the 'admin' user with the ADMIN role
            User user = new User();
            user.setEmail("admin@gmail.com");
            user.setPassword(passwordEncoder.encode("admin"));

            Role adminRole = roleRepository.findRoleByRoleName(RoleType.ADMIN);//.orElseThrow(() -> new IllegalStateException("Admin role not found"));
            user.setRoles(Set.of(adminRole));

            UserDto userDto = mapper.map(user, UserDto.class);
            restTemplate.postForLocation("http://localhost:8091/api/v1/users/signup", userDto, UserDto.class);
            userRepository.save(user);
            publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        }

        System.out.println("Dummy data generated successfully.");
    }
    private String applicationUrl(HttpServletRequest request) {
        return " http://Samuel.attlocal.net:9091";
    }
}
