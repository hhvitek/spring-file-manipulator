package spring.filemanipulator.utilities.file_operation;

public enum FileOperationEnum {
    COPY {
        @Override
        public AbstractFileOperation getFileOperationInstance() {
            return Constants.COPY_OPERATION;
        }
    },
    RENAME {
        @Override
        public AbstractFileOperation getFileOperationInstance() {
            return Constants.RENAME_OPERATION;
        }
    },
    MOVE {
        @Override
        public AbstractFileOperation getFileOperationInstance() {
            return Constants.MOVE_OPERATION;
        }
    },
    DELETE {
        @Override
        public AbstractFileOperation getFileOperationInstance() {
            return Constants.DELETE_OPERATION;
        }
    };

    public abstract AbstractFileOperation getFileOperationInstance();

    private static final class Constants {
        public static final AbstractFileOperation COPY_OPERATION = new CopyFileOperation();
        public static final AbstractFileOperation RENAME_OPERATION = new RenameFileOperation();
        public static final AbstractFileOperation MOVE_OPERATION = new MoveFileOperation();
        public static final AbstractFileOperation DELETE_OPERATION = new DeleteFileOperation();
    }
}