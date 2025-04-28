package em.domain.repository;

import em.domain.entity.GlobalPermission;
import em.domain.entity.enums.GlobalPermissionName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalPermissionRepository extends JpaRepository<GlobalPermission, Long> {

    GlobalPermission findByName(GlobalPermissionName name);
}
