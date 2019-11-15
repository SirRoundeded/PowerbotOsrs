package scripts;

import org.powerbot.script.PollingScript;

import java.util.ArrayList;

/**
 * @param <ClientContext>
 *
 *     A PollingScript, that can give you its active Task. Used in ScriptScheduler.
 */
public abstract class TaskbasedPollingScript<ClientContext extends org.powerbot.script.rt4.ClientContext> extends PollingScript<ClientContext> {

    @Override
    public void poll() {

    }

    protected ArrayList<Task> taskList = new ArrayList<>();


    /**
     * @return task that is active at the momement
     */
    public Task getActiveTask() {
        for (Task task : taskList){
            if (task.activate()){
                return task;
            }
        }
        return new TaskEmptyDummy(ctx); //if there is no active task, returns an empty dummy
    }
}
