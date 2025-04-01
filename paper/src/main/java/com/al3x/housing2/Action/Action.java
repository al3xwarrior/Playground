package com.al3x.housing2.Action;

import com.al3x.housing2.Enums.EventType;
import com.al3x.housing2.Enums.Locations;
import com.al3x.housing2.Enums.PushDirection;
import com.al3x.housing2.Events.CancellableEvent;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Menu;
import com.al3x.housing2.Utils.HandlePlaceholders;
import com.al3x.housing2.Utils.ItemBuilder;
import com.al3x.housing2.Utils.Serialization;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;

import static com.al3x.housing2.Utils.Color.colorize;

/**
 * Represents an action that can be executed by a player.
 */
@Getter
@RequiredArgsConstructor
public abstract class Action {
    private static final Gson gson = new Gson();

    private final String id;
    private final String name;
    private final String description;
    private final Material icon;
    private final List<String> scriptingKeywords;

    @Setter
    private String comment;

    private final List<ActionProperty> properties = new ArrayList<>();

    // --- Methods ---

    public OutputType execute(Player player, HousingWorld house, CancellableEvent event, ActionExecutor executor) {
        return execute(player, house, event);
    }
    public OutputType execute(Player player, HousingWorld house, CancellableEvent event) {
        return execute(player, house);
    }
    public abstract OutputType execute(Player player, HousingWorld house);

    public ActionEditor editorMenu(HousingWorld house, Menu backMenu, Player player) {
        return editorMenu(house, backMenu);
    }
    public ActionEditor editorMenu(HousingWorld house, Menu backMenu) {
        return editorMenu(house);
    }
    public ActionEditor editorMenu(HousingWorld house) {
        return new ActionEditor(4, "<yellow>" + name + " Settings", properties);
    }

    public void createDisplayItem() {
        ItemBuilder builder = new ItemBuilder()
                .material(icon)
                .name("<yellow>" + name)
                .info("&eSettings", "")

                .lClick(ItemBuilder.ActionType.EDIT_YELLOW)
                .rClick(ItemBuilder.ActionType.REMOVE_YELLOW)
                .shiftClick();
        properties.forEach(property -> builder.info(property.getName(), property.getValue().toString()));

        if (!comment.isEmpty()) builder.extraLore(comment);
    }

    public void createAddDisplayItem(ItemBuilder builder) {
        builder.material(icon);
        builder.name("<yellow>" + name);
        builder.description(description);
        builder.lClick(ItemBuilder.ActionType.ADD_YELLOW);
    }

    public LinkedHashMap<String, Object> data() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("id", id);
        LinkedHashMap<String, Object> propertiesData = new LinkedHashMap<>();
        properties.forEach(property -> {
            switch (property.getValue()) {
                case Enum<?> e -> propertiesData.put(property.getId(), e.name());
                case ItemStack i -> propertiesData.put(property.getId(), Serialization.itemStackToBase64(i));
                default -> propertiesData.put(property.getId(), property.getValue());
            }
        });
        data.put("properties", propertiesData);
        return data;
    }

//    public static Action fromData(HashMap<String, Object> data) {
//        String id = (String) data.get("id");
//        ActionEnum actionEnum = ActionEnum.getActionById(id);
//        if (actionEnum == null) {
//            return null;
//        }
//        Action action = actionEnum.getActionInstance();
//        action.fromData(data, action.getClass());
//    }

    public abstract boolean requiresPlayer();

    public Object getField(String name) throws NoSuchFieldException, IllegalAccessException, NumberFormatException {
        Field field = this.getClass().getDeclaredField(name.split(" ")[0]);
        field.setAccessible(true);
        if (field.getType() == List.class && name.contains(" ")) {
            return ((List<?>) field.get(this)).get(Integer.parseInt(name.split(" ")[1]));
        }
        return field.get(this);
    }

    public int limit() {
        return -1;
    }

    public int nestLimit() {
        return -1;
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

    public List<EventType> disallowedEvents() {
        return null;
    }

    public boolean mustBeSync() {
        return false;
    }

    public void fromData(HashMap<String, Object> data, Class<? extends Action> actionClass) {
        for (String key : data.keySet()) {
            try {
                Field field = actionClass.getDeclaredField(key);
                field.setAccessible(true);
                if (field.getType().isEnum() && data.get(key) != null) {
                    if (data.get(key) instanceof String) {
                        field.set(this, Enum.valueOf((Class<Enum>) field.getType(), (String) data.get(key)));
                        continue;
                    } else if (data.get(key) instanceof Enum) {
                        field.set(this, data.get(key));
                        continue;
                    }
                }
                field.set(this, data.get(key));
            } catch (Exception ignored) {
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
            event.getWhoClicked().sendMessage(colorize("&ePlease enter the custom location in the chat. (x,y,z) or (x,y,z,yaw,pitch)"));
            editMenu.openChat(Main.getInstance(), (current == null ? "" : current), (message) -> {
                String oldMessage = message;
                message = HandlePlaceholders.parsePlaceholders((Player) event.getWhoClicked(), house, message);
                String[] split = message.split(",");
                if (split.length != 3 && split.length != 5) {
                    event.getWhoClicked().sendMessage(colorize("&cInvalid format! Please use: <x>,<y>,<z> or <x>,<y>,<z>,<yaw>,<pitch>"));
                    return;
                }

                try {
                    for (int i = 0; i < split.length; i++) {
                        if (split[i].startsWith("~")) {
                            split[i] = split[i].substring(1);
                        }

                        if (split[i].startsWith("^")) {
                            split[i] = split[i].substring(1);
                        }

                        if (split[i].isEmpty()) continue;
                        if (split[i].equalsIgnoreCase("null")) {
                            split[i] = "0";
                        }

                        Float.parseFloat(split[i]);
                    }
                    consumer.accept(oldMessage, Locations.CUSTOM);
                } catch (NumberFormatException e) {
                    event.getWhoClicked().sendMessage(colorize("&cInvalid format! Please use: <x>,<y>,<z> or <x>,<y>,<z>,<yaw>,<pitch>"));
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
        return getLocationFromString(player, player.getLocation(), player.getEyeLocation(), house, message);
    }

    protected Location getLocationFromString(Player player, Location baseLocation, Location eyeLocation, HousingWorld house, String message) {
        message = HandlePlaceholders.parsePlaceholders(player, house, message);
        String[] split = message.split(",");
        if (split.length != 3 && split.length != 5) {
            player.sendMessage(colorize("&cInvalid format! Please use: <x>,<y>,<z> or <x>,<y>,<z>,<yaw>,<pitch>"));
            return null;
        }

        try {
            if (message.contains("^")) {
                String x = split[0];
                String y = split[1];
                String z = split[2];

                double xV = (x.startsWith("^")) ? Double.parseDouble(x.substring(1)) : Double.parseDouble(x);
                double yV = (y.startsWith("^")) ? Double.parseDouble(y.substring(1)) : Double.parseDouble(y);
                double zV = (z.startsWith("^")) ? Double.parseDouble(z.substring(1)) : Double.parseDouble(z);

                //forward
                Vector forward = eyeLocation.getDirection().clone().multiply(zV);
                //right
                Vector right = eyeLocation.getDirection().clone().crossProduct(new Vector(0, 1, 0)).normalize().multiply(xV);
                //up
                Vector up = new Vector(0, yV, 0);

                return baseLocation.clone().add(forward).add(right).add(up);
            }

            double x = 0, y = 0, z = 0, yaw = baseLocation.getYaw(), pitch = baseLocation.getPitch();

            for (int i = 0; i < split.length; i++) {
                double handledValue = handleRelative(i, split[i], baseLocation, eyeLocation);
                if (i == 0) {
                    x = handledValue;
                } else if (i == 1) {
                    y = handledValue;
                } else if (i == 2) {
                    z = handledValue;
                } else if (i == 3) {
                    yaw = handledValue;
                } else {
                    pitch = handledValue;
                }
            }

            return new Location(baseLocation.getWorld(), x, y, z, (float) yaw, (float) pitch);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            player.sendMessage(colorize("&cInvalid format! Please use: <x>,<y>,<z>"));
            return null;
        }
    }

    private double handleRelative(int index, String part, Location baseLocation, Location eyeLocation) {
        //Sorry if this looks like a mess :(
        double value = (part.startsWith("~")) ?
                (part.length() == 1 ? 0 : Double.parseDouble(part.substring(1))) :
                Double.parseDouble(part);

        double base = switch (index) {
            case 0 -> baseLocation.getX();
            case 1 -> baseLocation.getY();
            case 2 -> baseLocation.getZ();
            case 3 -> baseLocation.getYaw();
            case 4 -> baseLocation.getPitch();
            default -> 0;
        };

        if (part.startsWith("~")) {
            return base + value;
        }
        return value;
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
        ActionEnum actionEnum = ActionEnum.getActionById(getName());
        if (actionEnum == null) {
            return null;
        }
        HashMap<String, Object> data = data();
        for (String key : data.keySet()) {
            if (data.get(key) != null && data.get(key).getClass().isEnum()) {
                data.put(key, ((Enum<?>) data.get(key)).name());
            }
        }
        action = actionEnum.getActionInstance(data, this.comment);
        return action;
    }
}

