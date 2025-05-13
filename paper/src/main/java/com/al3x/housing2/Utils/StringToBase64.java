package com.al3x.housing2.Utils;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Instances.HousingWorld;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class StringToBase64 {
    private static final Gson gson = new Gson();

    public static String actionsToBase64(List<Action> action) {
        String actionString = gson.toJson(ActionData.fromList(action));
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(actionString);
            dataOutput.flush();
            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save action.", e);
        }
    }

    public static String decodeBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            String item;
            item = (String) dataInput.readObject();

            dataInput.close();
            return item;
        } catch (Exception e) {

        }
        return null;
    }

    public static ArrayList<Action> fromJson(String json, HousingWorld house) {
        JsonArray jsonArray = gson.fromJson(json, JsonArray.class);
        ArrayList<ActionData> actions = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            actions.add(gson.fromJson(jsonObject, ActionData.class));
        }
        return new ArrayList<>(ActionData.toList(actions, house));
    }

    public static ArrayList<Action> actionsFromBase64(String data, HousingWorld house) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            String item;
            item = (String) dataInput.readObject();


            dataInput.close();
            JsonArray jsonArray = gson.fromJson(item, JsonArray.class);
            ArrayList<ActionData> actions = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                actions.add(gson.fromJson(jsonObject, ActionData.class));
            }
            return new ArrayList<>(ActionData.toList(actions, house));
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load action.", e);
        }
    }

    public static Action actionFromBase64(String data, HousingWorld house) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            String item;
            item = (String) dataInput.readObject();
            dataInput.close();
            return ActionData.fromData(gson.fromJson(item, ActionData.class), house);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load action.", e);
        }
    }

    public static String actionToBase64(Action action) {
        String actionString = gson.toJson(ActionData.toData(action));
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(actionString);
            dataOutput.flush();
            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save action.", e);
        }
    }
}
