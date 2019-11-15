package scripts;

/**
 * @param <Task>
 *     Contains the script, that should be run, for how long it should run and what task it is allowed to stop on, once the time is up.
 */
public class ScheduleTaskEntry<Task> {

    final TaskbasedPollingScript taskbasedScript;
    final Task taskToStopOn;
    final int duration;

    public TaskbasedPollingScript getTaskbasedScript() {
        return taskbasedScript;
    }

    public Task getTaskToStopOn() {
        return taskToStopOn;
    }

    public int getDuration() {
        return duration;
    }

    public ScheduleTaskEntry(TaskbasedPollingScript taskbasedScript, Task taskToStopOn, int duration) {

        this.taskbasedScript = taskbasedScript;
        this.taskToStopOn = taskToStopOn;
        this.duration = duration;
    }

}
