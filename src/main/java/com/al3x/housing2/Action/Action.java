package com.al3x.housing2.Action;

import com.al3x.housing2.Action.Actions.CancelAction;
import com.al3x.housing2.Condition.Condition;
import com.al3x.housing2.Condition.ConditionEnum;
import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Instances.HousingData.ActionData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.al3x.housing2.Utils.Color.colorize;

/**
 * Represents an action that can be executed by a player.
 */
public abstract class Action {
    private static final Gson gson = new Gson();
    protected String name;

    public Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String toString();

    public abstract void createDisplayItem(ItemBuilder builder);
    public void createDisplayItem(ItemBuilder builder, HousingWorld house) {
        createDisplayItem(builder);
    }

    public abstract void createAddDisplayItem(ItemBuilder builder);

    public ActionEditor editorMenu(HousingWorld house) {
        return null;
    }

    public ActionEditor editorMenu(HousingWorld house, Menu backMenu, Player player) {
        return null;
    }

    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        return null;
    }

    public abstract boolean execute(Player player, HousingWorld house);

    public boolean execute(Player player, HousingWorld house, Cancellable event) {
        return execute(player, house);
    }

    public boolean execute(Player player, HousingWorld house, Cancellable event, ActionExecutor executor) {
        return execute(player, house, event);
    }

    public abstract LinkedHashMap<String, Object> data();

    public abstract boolean requiresPlayer();

    public Object getField(String name) throws NoSuchFieldException, IllegalAccessException, NumberFormatException {
        Field field = this.getClass().getDeclaredField(name.split(" ")[0]);
        field.setAccessible(true);
        if(field.getType() == List.class && name.contains(" ")) {
            return ((List<?>) field.get(this)).get(Integer.parseInt(name.split(" ")[1]));
        }
        return field.get(this);
    }

    public int limit() {
        return -1;
    }

    public boolean canBeNested() {
        return true;
    }

    /**
     * Returns a list of events that are allowed for this action.
     * Null if all events are allowed.
     *
     * @return a list of strings representing the allowed events
     */
    public List<EventType> allowedEvents() {
        return null;
    }


    public boolean mustBeSync() {
        return false;
    }

    public void fromData(HashMap<String, Object> data, Class< ? extends Action> actionClass) {
        for (String key : data.keySet()) {
            try {
                Field field = actionClass.getDeclaredField(key);
                field.setAccessible(true);
                if (field.getType().isEnum() && data.get(key) != null) {
                    field.set(this, Enum.valueOf((Class<Enum>) field.getType(), (String) data.get(key)));
                    continue;
                }
                field.set(this, data.get(key));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    protected <T> List<T> dataToList(JsonArray jsonArray, Class<T> clazz) {
        ArrayList<T> actions = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            actions.add(gson.fromJson(jsonObject, clazz));
        }
        return actions;
    }

    protected boolean getDirection(InventoryClickEvent event, Object obj, HousingWorld house, Menu editMenu, BiConsumer<String, PushDirection> consumer) {
        if (obj instanceof PushDirection direction && direction == PushDirection.CUSTOM) {
            event.getWhoClicked().sendMessage(colorize("&ePlease enter the custom direction in the chat. (pitch,yaw)"));
            editMenu.openChat(Main.getInstance(), (message) -> {
                //pitch,yaw
                String[] split = message.split(",");
                if (split.length != 2) {
                    event.getWhoClicked().sendMessage(colorize("&cInvalid format! Please use: <pitch>,<yaw>"));
                    return;
                }

                try {
                    //Check if the placeholders are valid
                    Float.parseFloat(HandlePlaceholders.parsePlaceholders((Player) event.getWhoClicked(), house, split[0]));
                    Float.parseFloat(HandlePlaceholders.parsePlaceholders((Player) event.getWhoClicked(), house, split[1]));
                    consumer.accept(message, PushDirection.CUSTOM);
                } catch (NumberFormatException e) {
                    event.getWhoClicked().sendMessage(colorize("&cInvalid format! Please use: <pitch>,<yaw>"));
                }
            });
        }

        if ((obj instanceof PushDirection direction) && direction != PushDirection.CUSTOM) {
            consumer.accept(null, direction);
        }
        return false;
    }

    protected boolean getCoordinate(InventoryClickEvent event, Object obj, String current, HousingWorld house, Menu editMenu, BiConsumer<String, Locations> consumer) {
        if (obj instanceof Locations location && location == Locations.CUSTOM) {
            event.getWhoClicked().sendMessage(colorize("&ePlease enter the custom location in the chat. (x,y,z)"));
            editMenu.openChat(Main.getInstance(), (current == null ? "" : current), (message) -> {
                //pitch,yaw
                String[] split = message.split(",");
                if (split.length != 3) {
                    event.getWhoClicked().sendMessage(colorize("&cInvalid format! Please use: <x>,<y>,<z>"));
                    return;
                }

                try {
                    for (int i = 0; i < split.length; i++) {
                        if (split[i].startsWith("~")) {
                            split[i] = split[i].substring(1);
                        }

                        if (split[i].isEmpty()) continue;

                        Float.parseFloat(HandlePlaceholders.parsePlaceholders((Player) event.getWhoClicked(), house, split[i]));
                    }
                    consumer.accept(message, Locations.CUSTOM);
                } catch (NumberFormatException e) {
                    event.getWhoClicked().sendMessage(colorize("&cInvalid format! Please use: <x>,<y>,<z>"));
                    return;
                }
            });
        }

        if ((obj instanceof Locations direction) && direction != Locations.CUSTOM) {
            consumer.accept(null, direction);
        }
        return false;
    }

    protected Location getLocationFromString(Player player, HousingWorld house, String message) {
        String[] split = message.split(",");
        if (split.length != 3) {
            player.sendMessage(colorize("&cInvalid format! Please use: <x>,<y>,<z>"));
            return null;
        }

        Location loc = player.getLocation();
        try {
            String x = HandlePlaceholders.parsePlaceholders(player, house, split[0]);
            String y = HandlePlaceholders.parsePlaceholders(player, house, split[1]);
            String z = HandlePlaceholders.parsePlaceholders(player, house, split[2]);

            double x1 = (x.startsWith("~")) ? loc.getX() : Double.parseDouble(x);
            double y1 = (y.startsWith("~")) ? loc.getY() : Double.parseDouble(y);
            double z1 = (z.startsWith("~")) ? loc.getZ() : Double.parseDouble(z);
            if (x.startsWith("~") && x.length() > 1) {
                x1 += Double.parseDouble(x.substring(1));
            }
            if (y.startsWith("~") && y.length() > 1) {
                y1 += Double.parseDouble(y.substring(1));
            }
            if (z.startsWith("~") && z.length() > 1) {
                z1 += Double.parseDouble(z.substring(1));
            }
            return new Location(player.getWorld(), x1, y1, z1, loc.getYaw(), loc.getPitch());
        } catch (NumberFormatException e) {
            player.sendMessage(colorize("&cInvalid format! Please use: <x>,<y>,<z>"));
            return null;
        }
    }

    protected Location getLocationFromString(Player player, Location baseLocation, HousingWorld house, String message) {
        String[] split = message.split(",");
        if (split.length < 3 || split.length > 5) {
            player.sendMessage(colorize("&cInvalid format! Please use: <x>,<y>,<z> or <x>,<y>,<z>,<yaw>,<pitch>"));
            return null;
        }

        Location loc = baseLocation.clone();
        try {
            String x = HandlePlaceholders.parsePlaceholders(player, house, split[0]);
            String y = HandlePlaceholders.parsePlaceholders(player, house, split[1]);
            String z = HandlePlaceholders.parsePlaceholders(player, house, split[2]);

            double x1 = (x.startsWith("~")) ? loc.getX() : Double.parseDouble(x);
            double y1 = (y.startsWith("~")) ? loc.getY() : Double.parseDouble(y);
            double z1 = (z.startsWith("~")) ? loc.getZ() : Double.parseDouble(z);
            if (x.startsWith("~") && x.length() > 1) {
                x1 += Double.parseDouble(x.substring(1));
            }
            if (y.startsWith("~") && y.length() > 1) {
                y1 += Double.parseDouble(y.substring(1));
            }
            if (z.startsWith("~") && z.length() > 1) {
                z1 += Double.parseDouble(z.substring(1));
            }
            return new Location(player.getWorld(), x1, y1, z1, loc.getYaw(), loc.getPitch());
        } catch (NumberFormatException e) {
            player.sendMessage(colorize("&cInvalid format! Please use: <x>,<y>,<z>"));
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        if (super.equals(o)) return true;
        return Objects.equals(getName(), action.getName()) && data().equals(action.data());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    public Action clone() {
        Action action;
        ActionEnum actionEnum = ActionEnum.getActionByName(getName());
        if (actionEnum == null) {
            return null;
        }
        HashMap<String, Object> data = data();
        for (String key : data.keySet()) {
            if (data.get(key) != null && data.get(key).getClass().isEnum()) {
                data.put(key, ((Enum<?>) data.get(key)).name());
            }
        }
        action = actionEnum.getActionInstance(data);
        return action;
    }

    // I couldnt figure this out :(
//    private static final Gson exportGson = new GsonBuilder().setPrettyPrinting().create();
//
//    public void export(Player player) {
//        ByteArrayDataOutput out = ByteStreams.newDataOutput();
//        out.writeUTF("export");
//        out.writeUTF(exportGson.toJson(ActionData.Companion.toData(this)));
//        player.sendPluginMessage(Main.getInstance(), "housing:export",
//                out.toByteArray()
//        );
//    }
}
