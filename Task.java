package scripts;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.ClientContext;
import org.powerbot.script.rt4.Game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public abstract class Task<C extends ClientContext> extends ClientAccessor<C> {
    public Task(C ctx) {
        super(ctx);
    }

    public abstract boolean activate();
    public abstract void execute() throws IOException;


}