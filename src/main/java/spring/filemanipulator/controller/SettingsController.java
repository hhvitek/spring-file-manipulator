package spring.filemanipulator.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.filemanipulator.entity.SettingsEntity;
import spring.filemanipulator.repository.SettingsRepository;

@RestController
@RequestMapping("/api/settings")
public class SettingsController extends AbstractRestController<Integer, SettingsEntity> {

    public SettingsController(SettingsRepository repository) {
        super(repository);
    }
}
