package scripts.BreakHandler;

import org.powerbot.script.PollingScript;
import org.powerbot.script.rt4.ClientContext;
import scripts.Task;
import scripts.TaskbasedPollingScript;
import scripts.UtilityMethods;

import java.io.IOException;
import java.util.Arrays;

/**
 * Intended to be used for setting up breaks inside a scheduled script
 */
public class ScriptBreakHandler extends TaskbasedPollingScript<ClientContext> {


    int duration;


    public ScriptBreakHandler(int breakDuration) {
        super();
        duration = breakDuration;
    }

    @Override
    public void start() {
        System.out.println("Started to take a break.");
        UtilityMethods util = new UtilityMethods(ctx);
        util.startScriptCameraSettings();

        taskList.add(new TaskLogout(ctx));
        taskList.add(new TaskWait(ctx,duration));
    }

    @Override
    public void poll() {

        for (Task task : taskList){
            if (task.activate()){
                try {
                    task.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
