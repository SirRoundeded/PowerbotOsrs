package scripts.BreakHandler;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import scripts.Task;

import java.io.IOException;

public class TaskWait extends Task<ClientContext> {


    final int SLEEP_TIME;

    public TaskWait(ClientContext ctx, int sleepTime) {
        super(ctx);
        SLEEP_TIME = sleepTime;
    }

    @Override
    public boolean activate() {
        return !ctx.game.loggedIn();
    }

    @Override
    public void execute() throws IOException {
        Condition.sleep(SLEEP_TIME);
    }
}
