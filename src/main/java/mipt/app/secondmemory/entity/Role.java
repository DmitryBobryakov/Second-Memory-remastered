package mipt.app.secondmemory.entity;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "files_roles")
@NoArgsConstructor(access = PROTECTED)
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "file_id", updatable = false)
    private FileEntity file;

    @Enumerated(STRING)
    @Column(updatable = false)
    private RoleType type;


    public Role(User user, FileEntity file, RoleType type) {
        this.user = user;
        this.file = file;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role)) return false;
        return Objects.equals(user, role.user) && Objects.equals(file, role.file) && type == role.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, file, type);
    }
}
