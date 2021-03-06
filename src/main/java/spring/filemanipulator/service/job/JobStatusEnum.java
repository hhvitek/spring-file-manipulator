package spring.filemanipulator.service.job;

public enum JobStatusEnum {
    CREATED,
    SCHEDULED_RUNNING,
    SIGNALED_TO_STOP,
    FINISHED_OK,
    FINISHED_ERROR;

    public boolean isConsideredFinished() {
        return name().startsWith("FINISHED_");
    }

    public static boolean isConsideredFinished(String uniqueJobStatusName) {
        if (existsByUniqueName(uniqueJobStatusName)) {
            return uniqueJobStatusName.startsWith("FINISHED_");
        }
        return false;
    }

    private static boolean existsByUniqueName(String uniqueNameId) {
        try {
            JobStatusEnum.valueOf(uniqueNameId);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}