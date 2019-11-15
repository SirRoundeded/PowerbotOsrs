package scripts;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.PollingScript;

import java.util.ArrayList;
import java.util.List;

//@Script.Manifest(name="FishAndCookInAlkharid", description = "", properties = "author=me; topic=999; client=4;")


public class ScriptScheduler extends PollingScript<ClientContext> {

    protected List<ScheduleTaskEntry> scheduleList = new ArrayList<>();
    protected ScheduleTaskEntry activeScheduleEntry;

    protected double startTimeOfTask;

    /* use DEFAULT_VALUE_BREAK as duration when scheduling a break handler as task, or if you want to immediately end a script,
     when a certain task of a script is activated for the first time (e.g. for transitioning between scripts/movement between scripts)
      */
    protected int DEFAULT_VALUE_BREAK = 10000;

    @Override
    public void start() {
    }

    @Override
    public void poll() {

        System.out.println("Active Task Class: " + activeScheduleEntry.getTaskbasedScript().getActiveTask().getClass());
        System.out.println("Task runtime: " + getActiveTaskRuntime());
        System.out.println("Stop task at: " + activeScheduleEntry.getDuration());

        //if script is over its intended runtime/duration, THE NEXT TIME it starts the task, that it is allowed to stop on, it will do so. It does NOT stop immediately!
        if (getActiveTaskRuntime() > activeScheduleEntry.getDuration()
                && activeScheduleEntry.getTaskbasedScript().getActiveTask().getClass() == activeScheduleEntry.getTaskToStopOn()) {

            if (scheduleList.size() > 1) {
                scheduleList.remove(0);
                changeActiveScript(scheduleList.get(0));
            } else{
                //todo: how to handle when script is done? Right now the bot is idle until it auto-logs.
            }
        } else {
            //if its not over its duration, execute it
            activeScheduleEntry.getTaskbasedScript().poll();

        }

    }

    @Override
    public void stop() {

    }

    /**
     * @param nextScheduleEntry next script, that should be active
     * Calls the "stop()" of the currently active script. Sets the new script as active script and calls its "start()".
     */
    public void changeActiveScript(ScheduleTaskEntry<Task> nextScheduleEntry) {

        if (nextScheduleEntry != activeScheduleEntry) {
            if (activeScheduleEntry != null) {
                activeScheduleEntry.getTaskbasedScript().stop();
            }

            if (nextScheduleEntry != null) {
                activeScheduleEntry = nextScheduleEntry;
                activeScheduleEntry.getTaskbasedScript().start();
                startTimeOfTask = System.currentTimeMillis();
            }
        }
    }

    public double getActiveTaskRuntime(){
        return System.currentTimeMillis() - startTimeOfTask;
    }
}
