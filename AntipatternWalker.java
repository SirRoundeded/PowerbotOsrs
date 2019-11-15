package scripts;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Path;

import java.awt.*;

/**
 * Extend a task with AntipatternWalker and pass the tiles you want the bot to walk.
 * Meant to mimic player-like behaviour. Will slightly change up the path each time.
 *
 * Intended to be used for larger distances, do NOT use for narrow pathways or when precise clicks are required!
 */
public abstract class AntipatternWalker extends Task<ClientContext> {

    Boolean traverseOnMinimap; //if true, clicks on minimap to move, otherwise clicks on tiles directly ingame
    Tile[] originalPathCoords;
    Path pathToWalk;
    int runEnergyToggle; //determines at what run-energy-percentage the character should be run

    public AntipatternWalker(ClientContext ctx, Tile[] pathCoordinates) {

        super(ctx);
        //create a somewhat randomized path based on the tiles given
        originalPathCoords = pathCoordinates;
        pathToWalk = createAntipatternPath(pathCoordinates);

        //init
        runEnergyToggle = Random.nextInt(20, 80);
        if (Math.random() < 0.5) {
            traverseOnMinimap = true;
        } else {
            traverseOnMinimap = false;
        }

    }

    public abstract boolean activate();

    @Override
    public void execute() {

        //traverse
        try {
            traverseAntipattern(pathToWalk);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //toggle run when over cap
        if (ctx.movement.energyLevel() >= runEnergyToggle) {
            ctx.movement.running(true);
        }
    }


    /**
     * @param tileCoords array of tiles, that mark your path. It is intended to not have gaps between the tiles, but small gaps will still work in most cases.
     * @return A path that is slightly different then your original tiles. Wont give the same path every time.
     */
    public Path createAntipatternPath(Tile[] tileCoords) {

        Tile[] tilesForPath = new Tile[tileCoords.length];

        for (int i = 0; i < tileCoords.length; i++) {
            //changes coordinates [-1,0,+1]
            tilesForPath[i] = new Tile(tileCoords[i].x() + Random.nextInt(-1, 1), tileCoords[i].y() + Random.nextInt(-1, 1));
        }

        Path antiPath = ctx.movement.newTilePath(tilesForPath);
        return antiPath;
    }


    /**
     * @param path follows that path by
     */
    public void traverseClickIngame(Path path) {
        //click next point with random offset
        /*int randomOffsetX = Random.nextInt(-8,8);
        int randomOffsetY = Random.nextInt(-8,8);*/

        //rare issue with null pointer, check everything?
        if (path != null && path.next() != null) {

            Tile nextTile = path.next();
            if (nextTile != null) {

                //in case next tile is too close, use minimap traverse instead
                if (nextTile.distanceTo(ctx.players.local()) <= 5) {
                    traverseOnMinimap = true;
                }

                //System.out.println("Next Tile: " + nextTile.toString());
                Point nextPoint = nextTile.matrix(ctx).centerPoint();
                //Point nextPoint = new Point(nextTile.matrix(ctx).centerPoint().x + randomOffsetX, nextTile.matrix(ctx).centerPoint().y + randomOffsetY);

                //turn camera, if the point is not visable
                if (!ctx.game.inViewport(nextPoint) || Math.random() < 0.1) { //10% chance to do it even when visible, is this realistic/necessary?
                    ctx.camera.turnTo(nextTile);
                    Condition.sleep(Random.nextInt(1000, 1500));
                }

                //hover over the point
                if (nextPoint.x > 20 && nextPoint.y > 20) { //cursor sometimes moves close to top left corner, why? this is just a bandage.
                    ctx.input.move(nextPoint);
                } else {
                    traverseOnMinimap = true;
                }

                //check if left click option is "walk here", if true use left click
                MenuCommand[] menuEntries = ctx.menu.commands();
                if (menuEntries != null && menuEntries.length > 0 && menuEntries[0].toString().contains("Walk here")) {
                    System.out.println("Using left click to walk!");
                    ctx.input.click(nextPoint, true);
                    Condition.sleep(500);

                    //if something went wrong, and player is not moving after clicking
                    if (!ctx.players.local().inMotion()) {
                        traverseOnMinimap = true;
                        System.out.println("Left-click failed, switching to minimap");
                    }

                //if left-click is not "walk here", use right click -> walk here
                } else {
                    ctx.menu.click(menuCommand -> menuCommand.toString().contains("Walk here"));
                    System.out.println("Using right click to walk.");
                }

                //System.out.println("Walking to " + nextTile.x() + "," + nextTile.y());
                //System.out.println("Point " + nextPoint.x + "," + nextPoint.y);
            }
        }
    }


    /**
     * @param path follows the path in a player-like manner. Turns camera if target is not visible.
     *             Occasionally:
     *             - switches between clicking on the minimap and clicking directly on a tile
     *             - stops
     *             - turns camera towards walking direction
     *             - turns on run, if its over a randomized value
     *             to mimic player behaviour.
     */
    public void traverseAntipattern(Path path) {
        int randomDistanceToTile;
        System.out.println("Traverse on minimap: " + traverseOnMinimap);

        //walking by traversing over the minimap
        if (traverseOnMinimap) {
            path.traverse();
            randomDistanceToTile = Random.nextInt(3, 7);
            //System.out.println("New Destination at distance " + randomDistanceToTile);

            //sleep until until bot is a certain distance away from the clicked tile; is it ok to use while loops for waiting?
            while (ctx.movement.destination().distanceTo(ctx.players.local().tile()) > randomDistanceToTile && ctx.players.local().inMotion()) {
                Condition.sleep(Random.nextInt(40, 60));
            }

        //walking by clicking on tiles ingame
        } else {
            traverseClickIngame(path);
            randomDistanceToTile = Random.nextInt(3, 7);
            //System.out.println("New Destination at distance " + randomDistanceToTile);

            while (ctx.movement.destination().distanceTo(ctx.players.local().tile()) > randomDistanceToTile && ctx.players.local().inMotion()) {
                Condition.sleep(Random.nextInt(40, 60));
            }
        }

        //switching things up
        if (Math.random() < 0.2) {
            traverseOnMinimap = !traverseOnMinimap;
            System.out.println("Switching traverse method");
            Condition.sleep(Random.nextInt(50,150));
        }
        if (Math.random() < 0.05) {
            runEnergyToggle = Random.nextInt(20,80);
        }
        if (Math.random() < 0.05) {
            pathToWalk = createAntipatternPath(originalPathCoords); //redo the randomizing of the path. this is probably only useful, if the same walker-task is used over a longer period of time; too excessive?
        }

    }
}
