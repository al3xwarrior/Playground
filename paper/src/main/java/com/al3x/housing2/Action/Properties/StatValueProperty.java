package com.al3x.housing2.Action.Properties;

import com.al3x.housing2.Action.Action;
import com.al3x.housing2.Action.ActionProperty;
import com.al3x.housing2.Action.Actions.StatValue;
import com.al3x.housing2.Action.StatInstance;
import com.al3x.housing2.Data.ActionData;
import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Main;
import com.al3x.housing2.Menus.Actions.ActionEditMenu;
import com.al3x.housing2.Utils.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class StatValueProperty extends ActionProperty<StatValueProperty.StatValueInstance> implements ActionProperty.PropertySerializer<StatValueProperty.StatValueInstance, StatValueProperty.StatValueData> {
    StatInstance instance = null;

    public StatValueProperty(String id) {
        super(id, "Value", null, Material.BOOK);

        setValue(new StatValueInstance(false, "1.0", null));
    }

    public StatValueProperty(int index, StatInstance instance) {
        super("value" + index, "Value", null, Material.BOOK);
        this.instance = instance;
        setValue(instance.value);
    }

    @Override
    public StatValueInstance getValue() {
        if (instance != null) {
            return instance.value;
        }
        return super.getValue();
    }

    @Override
    public ItemBuilder getDisplayItem() {
        ItemBuilder item = ItemBuilder.create(getIcon())
                .name("Value")
                .info("&eCurrent Value", "");

        if (getValue().isExpression()) {
            item.info(null, getValue().getExpressionValue().toString())
                    .lClick(ItemBuilder.ActionType.EDIT_YELLOW);
        } else {
            item.info(null, getValue().getLiteralValue())
                    .lClick(ItemBuilder.ActionType.CHANGE_YELLOW);
        }

        item.info(null, "")
                .info("Expression", getValue().isExpression() ? "&aEnabled" : "&cDisabled")
                .mClick(ItemBuilder.ActionType.TOGGLE_EXPRESSION);
        return item;
    }

    @Override
    public void runnable(InventoryClickEvent event, HousingWorld house, Player player, ActionEditMenu menu) {
        if (event.getClick() == ClickType.MIDDLE) {
            // Toggle expression
            setValue(getValue().isExpression() ?
                    getValue().withLiteral(getValue().getLiteralValue()) :
                    getValue().withExpression(new StatValue())
            );
            menu.open();
            return;
        }

        if (getValue().isExpression()) {
            new ActionEditMenu(getValue().getExpressionValue(), main, player, house, menu).open();
        } else {
            menu.openChat(main, getValue().getLiteralValue(), (message) -> setValue(getValue().withLiteral(message), player));
        }
    }

    @Override
    public StatValueData serialize() {
        return new StatValueData(
                getValue().isExpression(),
                getValue().getLiteralValue(),
                ActionData.toData(getValue().getExpressionValue())
        );
    }

    @Override
    public StatValueInstance deserialize(Object value) {
        Action action = ActionData.fromData(value.expressionValue);
        if (!(action instanceof StatValue)) {
            action = new StatValue();
            Main.getInstance().getLogger().severe("Invalid action expression: " + action);
        }
        return new StatValueInstance(
                value.isExpression,
                value.literalValue,
                (StatValue) action
        );
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class StatValueInstance {
        private boolean isExpression;
        private String literalValue;
        private StatValue expressionValue;

        public StatValueInstance withExpression(StatValue expressionValue) {
            this.isExpression = true;
            this.expressionValue = expressionValue;
            return this;
        }

        public StatValueInstance withLiteral(String literalValue) {
            this.isExpression = false;
            this.literalValue = literalValue;
            return this;
        }

        public String calculate(Player player, HousingWorld world) {
            if (isExpression) {
                return expressionValue.calculate(player, world);
            }
            return literalValue;
        }
    }

    @AllArgsConstructor
    @Getter
    public static class StatValueData {
        private boolean isExpression;
        private String literalValue;
        private ActionData expressionValue;
    }
}
