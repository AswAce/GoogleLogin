package Security.init;

import Security.db.model.EnumsType.RoleTypes;
import Security.db.model.RoleEntity;
import Security.db.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DbInit implements CommandLineRunner {

    RoleRepository roleRepository;

    public DbInit(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void addRoles() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        Arrays.stream(RoleTypes.values()).forEach(roleTypes -> {
                    if (roleRepository.findByRole(roleTypes).orElse(null) == null)
                        roleEntities.add(new RoleEntity(roleTypes));
                }
        );
        roleRepository.saveAll(roleEntities);

    }

    @Override
    public void run(String... args) throws Exception {
        addRoles();
    }
}
