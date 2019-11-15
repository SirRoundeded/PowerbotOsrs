package scripts;

import org.powerbot.script.rt4.*;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;

import java.util.List;

public class UtilityMethods {

    ClientContext ctx;

    public UtilityMethods(ClientContext ctx) {
        this.ctx = ctx;
    }


    /**
     * Mimics turning the scrolling wheel 3 to 4 positions.
     */
    public void zoomOutOnceAntipattern(){
        int mousewheelStepsRand;
        if(Math.random() < 0.25){
            mousewheelStepsRand = 4;
        } else{
            mousewheelStepsRand = 3;
        }
        for (int i = 0; i < mousewheelStepsRand; i++) {
            Condition.sleep(Random.nextInt(30,40));
            ctx.input.scroll(true);
        }
    }

    /**
     * Mimics turning the scrolling wheel 3 to 4 positions multiple times.
     */
   public void zoomOutAntipattern(int numberOfZooms){
       for (int i = 0; i < numberOfZooms; i++) {
           zoomOutOnceAntipattern();
           Condition.sleep(Random.nextInt(200,350));
       }
   }


    public void zoomInOnceAntipattern(){
        int mousewheelStepsRand;
        double randomTmp = Math.random();
        if(Math.random() < 0.25){
            mousewheelStepsRand = 4;
        } else{
            mousewheelStepsRand = 3;
        }
        for (int i = 0; i < mousewheelStepsRand; i++) {
            Condition.sleep(Random.nextInt(30,40));
            ctx.input.scroll(false);
        }
    }

    public void zoomInAntipattern(int numberOfZooms){
        for (int i = 0; i < numberOfZooms; i++) {
            zoomInOnceAntipattern();
            Condition.sleep(Random.nextInt(200,350));
        }

    }


    /**
     * Sets pitch and zoom of the camera to make sure, its not zoomed all the way in; gives better chances that objects are visible.
     */
    public void startScriptCameraSettings(){
        ctx.camera.pitch(Random.nextInt(55,85));
        Condition.sleep(Random.nextInt(1500,2500));
        new UtilityMethods(ctx).zoomOutAntipattern(2);
        Condition.sleep(Random.nextInt(1500,2500));
    }


    /**
     * @param items items you want to shift drop
     * @return if dropping was successful
     */
    public boolean shiftDrop(ItemQuery<Item> items) {
        if((ctx.varpbits.varpbit(1055) & 131072) > 0) {//checks if shift drop is enabled
            ctx.input.send("{VK_SHIFT down}");
            for(Item i : items) {
                Condition.sleep(Random.nextInt(200, 400));
                if (!i.click(true)) {
                    ctx.input.send("{VK_SHIFT up}");
                    return false;
                }
            }

            ctx.input.send("{VK_SHIFT up}");
            return true;
        }
        for(Item i : items) {
            Condition.sleep(Random.nextInt(200, 400));
            if (!i.interact("Drop")){
                return false;
            }
        }
        return true;
    }


    /**
     * @param items items to shift drop
     *
     * mimics the motion of a player dropping items making Z-movements, occasionally skipping items
     *
     */
    public boolean shiftDropAntipattern(ItemQuery<Item> items) {
        if((ctx.varpbits.varpbit(1055) & 131072) > 0) {//checks if shift drop is enabled
            ctx.input.send("{VK_SHIFT down}");

            for(Item i : items) {
               // System.out.println("Inventory index of item: "+i.inventoryIndex());
                if((i.inventoryIndex() % 4 == 0 || i.inventoryIndex() % 4 == 1)
                        && Math.random() < 0.5){
                    if(Math.random() < 0.03){
                        Condition.sleep(Random.nextInt(500, 1000));
                    } else {
                        Condition.sleep(Random.nextInt(100, 250));
                    }
                    if (!i.click(true)) {
                        ctx.input.send("{VK_SHIFT up}");
                        return false;
                    }
                }
            }

            //ctx.input.send("{VK_SHIFT down}");
            for(Item i : items) {
                if ((i.inventoryIndex() % 4 == 2 || i.inventoryIndex() % 4 == 3)
                        && Math.random() < 0.5) {
                    if (Math.random() < 0.03) {
                        Condition.sleep(Random.nextInt(400, 1000));
                    } else {
                        Condition.sleep(Random.nextInt(100, 250));
                    }
                    if (!i.click(true)) {
                        ctx.input.send("{VK_SHIFT up}");
                        return false;
                    }
                }
            }

            //ctx.input.send("{VK_SHIFT down}");
            for(Item i : items) {
                if (Math.random() < 0.03) {
                    Condition.sleep(Random.nextInt(400, 1000));
                } else {
                    Condition.sleep(Random.nextInt(100, 250));
                }
                if (!i.click(true)) {
                    ctx.input.send("{VK_SHIFT up}");
                    return false;
                }
            }

            ctx.input.send("{VK_SHIFT up}");
        }
        return true;
    }


    public void openInventory(){
        if(!ctx.game.tab().equals(Game.Tab.INVENTORY)) {
            ctx.game.tab(Game.Tab.INVENTORY);
        }
    }


    /**
     * @param distance distance to another player
     * logs out, if any player is within a certain distance
     */
    public void logOutIfOtherPlayerIsNear(int distance) {
        List<Player> playerList = ctx.players.get();
        playerList.remove(0); //remove the own player from the playerlist

            for (Player p : playerList) {
                //System.out.println("Distance to player: "+p.tile().distanceTo(ctx.players.local().tile()));
                if (p.tile().distanceTo(ctx.players.local().tile()) <= distance) {
                    ctx.game.logout();
                    Condition.wait(() -> ctx.widgets.widget(182).component(12).visible());
                    if( ctx.widgets.widget(182).component(12).visible()){
                        ctx.widgets.widget(182).component(12).click();
                    }

                }
            }

    }

    public void logOut(){
        ctx.game.logout();
        Condition.wait(() -> ctx.widgets.widget(182).component(12).visible());
        if( ctx.widgets.widget(182).component(12).visible()){
            ctx.widgets.widget(182).component(12).click();
        }
    }

}
