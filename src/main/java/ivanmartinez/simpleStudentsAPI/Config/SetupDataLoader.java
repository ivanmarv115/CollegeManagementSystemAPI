package ivanmartinez.simpleStudentsAPI.Config;

import ivanmartinez.simpleStudentsAPI.Entity.Role;
import ivanmartinez.simpleStudentsAPI.Entity.User;
import ivanmartinez.simpleStudentsAPI.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        User user = new User();

        String pass = passwordEncoder.encode("adminPredPass");
        Optional<User> optionalAdminUser = userRepository.findByUsername("adminpred");
        if(optionalAdminUser.isEmpty()) {
            user.setUsername("adminpred");
            user.setPassword(pass);
            user.setRole(Role.ADMIN);
            user.setIsNonLocked(true);
            userRepository.save(user);
        }
        else{
            User adminUser = optionalAdminUser.get();
            adminUser.setRole(Role.ADMIN);
            user.setIsNonLocked(true);
            userRepository.save(adminUser);
        }

        alreadySetup = true;
    }

}