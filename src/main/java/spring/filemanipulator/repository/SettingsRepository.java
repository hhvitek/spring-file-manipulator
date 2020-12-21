package spring.filemanipulator.repository;

import org.springframework.stereotype.Repository;
import spring.filemanipulator.entity.SettingsEntity;

@Repository
public interface SettingsRepository extends AbstractRepository<Integer, SettingsEntity> {
}
