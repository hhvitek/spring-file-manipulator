package spring.filemanipulator.utilities.string_filters.operations.whitelist;

import org.junit.jupiter.api.BeforeEach;

class WhitelistCharacterFilterTest extends WhitelistFilterTest {

    @BeforeEach
    void init() {
        operation = new WhitelistCharacterFilter();
    }

}