package spring.filemanipulator.utilities.operations.whitelist;

import org.junit.jupiter.api.BeforeEach;
import spring.filemanipulator.utilities.string_filters.operations.whitelist.WhitelistCharacterFilter;

class WhitelistCharacterFilterTest extends WhitelistFilterTest {

    @BeforeEach
    void init() {
        operation = new WhitelistCharacterFilter();
    }

}