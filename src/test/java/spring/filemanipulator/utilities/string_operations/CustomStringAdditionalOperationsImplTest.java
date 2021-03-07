package spring.filemanipulator.utilities.string_operations;

import org.junit.jupiter.api.BeforeEach;

class CustomStringAdditionalOperationsImplTest extends StringAdditionalOperationsTest {

    @BeforeEach
    void init() {
        additionalOperations = new CustomStringAdditionalOperationsImpl();
    }

}