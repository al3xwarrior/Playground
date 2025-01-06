package com.al3x.housing2.Instances;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Main;
import com.al3x.housing2.Utils.JsonConfig;
import com.al3x.housing2.Utils.StringToBase64;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClipboardManager {
    Main main;
    private final File dataFile;
    private final JsonConfig config;
    public ClipboardManager(Main main, File pluginDataFolder) {
        this.main = main;
        this.dataFile = new File(pluginDataFolder, "clipboard.json");
        this.config = new JsonConfig(main, dataFile);
    }

    public void save(String uuid, List<String> clipboard) {
        config.getConfig().put(uuid, clipboard);
        config.save();
    }

    //This is a list of serialized actions
    public List<String> addOrLoad(String uuid) {
        List<String> clipboard = new ArrayList<>();
        if (!config.getConfig().containsKey(uuid)) {
            config.getConfig().put(uuid, new ArrayList<>());
            config.save();
        }

        Object obj = config.getConfig().get(uuid);
        if (obj instanceof List<?> list) {
            for (Object o : list) {
                if (o instanceof String) { //Variable safety is important
                    clipboard.add((String) o);
                }
            }
        }
        return clipboard;
    }

    public List<Action> fromClipboard(String uuid) {
        List<Action> actions = new ArrayList<>();
        for (String serialized : addOrLoad(uuid)) {
            actions.add(StringToBase64.actionFromBase64(serialized));
        }
        return actions;
    }

    public Action addAction(String uuid, Action action) {
        List<String> clipboard = addOrLoad(uuid);
        clipboard.add(StringToBase64.actionToBase64(action));
        save(uuid, clipboard);
        return action;
    }

    public void removeAction(String uuid, Action action) {
        List<Action> actions = fromClipboard(uuid);
        actions.remove(action);
        saveActions(uuid, actions);
    }

    public List<String> toClipboard(List<Action> actions) {
        List<String> clipboard = new ArrayList<>();
        for (Action action : actions) {
            clipboard.add(StringToBase64.actionToBase64(action));
        }
        return clipboard;
    }

    public void saveActions(String uuid, List<Action> actions) {
        save(uuid, toClipboard(actions));
    }
}
