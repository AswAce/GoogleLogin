package Security.service;

import Security.db.repository.RoleRepository;
import Security.db.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private RoleRepository roleRepository;
    private UserRepository userRepository;

    public UserService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

}
