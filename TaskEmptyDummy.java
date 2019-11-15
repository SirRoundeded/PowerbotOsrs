package scripts;

import org.powerbot.script.rt4.ClientContext;

import java.io.IOException;


/**
 * This is a dummy task, used instead of returning null.
 */
public class TaskEmptyDummy extends Task<ClientContext> {
    public TaskEmptyDummy(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return false;
    }

    @Override
    public void execute() throws IOException {

    }
}
