package com.al3x.housing2.Instances;

import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Data.LocationData;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.al3x.housing2.Utils.Color.colorize;

public class LaunchPad {

    private LocationData location; //New way of storing location data so that you dont need two classes to store location data
    private PushDirection pushDirection;
    private double verticalVelocity;
    private double horizontalVelocity;

    public LaunchPad(Location location) {
        this.location = LocationData.fromLocation(location);
        this.pushDirection = PushDirection.FORWARD;
        this.verticalVelocity = 1.0;
        this.horizontalVelocity = 2.0;
    }

//    public LaunchPad(Location location, PushDirection pushDirection, double verticalVelocity, double horizontalVelocity) {
//        this.location = LocationData.Companion.fromLocation(location);
//        this.pushDirection = pushDirection;
//        this.verticalVelocity = verticalVelocity;
//        this.horizontalVelocity = horizontalVelocity;
//    }

    public void launchPlayer(Player player) {
        switch (pushDirection) {
            case FORWARD, UP:
                player.setVelocity(player.getLocation().getDirection().multiply(horizontalVelocity).setY(verticalVelocity));
                break;
            case BACKWARD:
                player.setVelocity(player.getLocation().getDirection().multiply(-horizontalVelocity).setY(verticalVelocity));
                break;
            case LEFT:
                player.setVelocity(player.getLocation().getDirection().rotateAroundY(Math.toRadians(90)).multiply(horizontalVelocity).setY(verticalVelocity));
                break;
            case RIGHT:
                player.setVelocity(player.getLocation().getDirection().rotateAroundY(Math.toRadians(-90)).multiply(horizontalVelocity).setY(verticalVelocity));
                break;
            case DOWN:
                player.setVelocity(player.getLocation().getDirection().multiply(horizontalVelocity).setY(-verticalVelocity));
                break;
        }
    }

    public void openConfigGUI(Player player) {
        new LaunchPadGUI(Main.getInstance(), player, this).open();
    }

    public void setLocation(Location location) {
        this.location = LocationData.fromLocation(location);
    }

    public void setPushDirection(PushDirection pushDirection) {
        this.pushDirection = pushDirection;
    }

    public void setVerticalVelocity(double verticalVelocity) {
        this.verticalVelocity = verticalVelocity;
    }

    public void setHorizontalVelocity(double horizontalVelocity) {
        this.horizontalVelocity = horizontalVelocity;
    }

    public Location getLocation() {
        return location.toLocation();
    }

    public PushDirection getPushDirection() {
        return pushDirection;
    }

    public double getVerticalVelocity() {
        return verticalVelocity;
    }

    public double getHorizontalVelocity() {
        return horizontalVelocity;
    }

    // First time making a class inside a class !!
    // I would like to vomit :/ -Sin_ender
    public class LaunchPadGUI extends Menu {

        private Main main;
        private Player player;
        private LaunchPad launchPad;

        public LaunchPadGUI(Main main, Player player, LaunchPad launchPad) {
            super(player, "Launch Pad Config", 27);
            this.main = main;
            this.player = player;
            this.launchPad = launchPad;
        }

        @Override
        public void initItems() {
            addItem(11, ItemBuilder.create(Material.COMPASS)
                            .name("&aPush Direction")
                            .description("&7Set the direct the player is to be pushed in!")
                            .info("&7Current: §f", launchPad.getPushDirection().name())
                            .punctuation(false)
                            .lClick(ItemBuilder.ActionType.CYCLE_FORWARD)
                            .rClick(ItemBuilder.ActionType.CYCLE_BACKWARD)
                            .build(),
                    () -> {
                        switch (pushDirection) {
                            case FORWARD:
                                pushDirection = PushDirection.BACKWARD;
                                break;
                            case BACKWARD:
                                pushDirection = PushDirection.LEFT;
                                break;
                            case LEFT:
                                pushDirection = PushDirection.RIGHT;
                                break;
                            case RIGHT:
                                pushDirection = PushDirection.UP;
                                break;
                            case UP:
                                pushDirection = PushDirection.DOWN;
                                break;
                            case DOWN:
                                pushDirection = PushDirection.FORWARD;
                                break;
                        }
                        open();
                    }, () -> {
                        switch (pushDirection) {
                            case FORWARD:
                                pushDirection = PushDirection.DOWN;
                                break;
                            case BACKWARD:
                                pushDirection = PushDirection.FORWARD;
                                break;
                            case LEFT:
                                pushDirection = PushDirection.BACKWARD;
                                break;
                            case RIGHT:
                                pushDirection = PushDirection.LEFT;
                                break;
                            case UP:
                                pushDirection = PushDirection.RIGHT;
                                break;
                            case DOWN:
                                pushDirection = PushDirection.UP;
                                break;
                        }
                        open();
                    }
            );

            addItem(13, ItemBuilder.create(Material.SLIME_BLOCK)
                            .name("&aVertical Velocity")
                            .description("&7Set the vertical velocity of the\n&7player when launched!\n\n&7Current: §f" + launchPad.getVerticalVelocity())
                            .punctuation(false)
                            .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                            .build(),
                    () -> {
                        player.sendMessage(colorize("&eEnter the vertical velocity you want to set!"));
                        openChat(main, launchPad.getVerticalVelocity() + "", (message) -> {
                            try {
                                launchPad.setVerticalVelocity(Double.parseDouble(message));
                            } catch (NumberFormatException e) {
                                player.sendMessage(colorize("&cInvalid number!"));
                            }
                        });
                    }
            );

            addItem(15, ItemBuilder.create(Material.SLIME_BLOCK)
                            .name("&aHorizontal Velocity")
                            .description("&7Set the horizonal velocity of the\n&7player when launched!\n\n&7Current: §f" + launchPad.getHorizontalVelocity())
                            .punctuation(false)
                            .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                            .build(),
                    () -> {
                        player.sendMessage(colorize("&eEnter the horizontal velocity you want to set!"));
                        openChat(main, launchPad.getHorizontalVelocity() + "", (message) -> {
                            try {
                                launchPad.setHorizontalVelocity(Double.parseDouble(message));
                            } catch (NumberFormatException e) {
                                player.sendMessage(colorize("&cInvalid number!"));
                            }
                        });
                    }
            );
        }
    }

}