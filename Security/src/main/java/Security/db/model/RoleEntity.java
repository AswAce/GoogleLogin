package Security.db.model;

import Security.db.model.EnumsType.RoleTypes;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "roles")
public class RoleEntity {

    public RoleEntity(RoleTypes role) {
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleTypes role;

    public Long getId() {
        return id;
    }

    public RoleTypes getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRole(RoleTypes role) {
        this.role = role;
    }
}
