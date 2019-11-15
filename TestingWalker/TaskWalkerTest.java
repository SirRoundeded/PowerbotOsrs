package scripts.TestingWalker;


import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import scripts.AntipatternWalker;


public class TaskWalkerTest extends AntipatternWalker {



    public TaskWalkerTest(ClientContext ctx, Tile[] pathCoordinates) {
        super(ctx, pathCoordinates);
    }

    @Override
    public boolean activate() {
        return true;
    }
}
