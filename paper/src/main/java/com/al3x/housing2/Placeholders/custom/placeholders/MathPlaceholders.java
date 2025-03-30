package com.al3x.housing2.Placeholders.custom.placeholders;

import com.al3x.housing2.Instances.HousingWorld;
import com.al3x.housing2.Placeholders.custom.Placeholder;
import com.al3x.housing2.Utils.StringUtilsKt;
import org.bukkit.entity.Player;

import java.util.List;

public class MathPlaceholders {
    public MathPlaceholders() {
        new Round();
        new RandomInt();
        new RandomDouble();
        new Add();
        new Subtract();
        new Multiply();
        new Divide();
        new Modulus();
        new Power();
        new SQRT();
        new ABS();
        new Floor();
        new Sin();
        new Cos();
        new Tan();
        new ASin();
        new ACos();
        new ATan();
    }

    private static class Round extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.round/[number] [places]%";
        }

        @Override
        public List<String> getAliases() {
            return List.of("%round/[number] [places]%");
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 2) {
                return "0";
            }
            String split = StringUtilsKt.substringAfter(input, "/");
            String[] number = split.split(" ");
            try {
                double val = Double.parseDouble(Placeholder.handlePlaceholders(number[0], house, player, true));
                int places = Integer.parseInt(Placeholder.handlePlaceholders(number[1], house, player, true));
                if (places == 0) {//If the places is 0, round the value to the nearest whole number
                    return String.valueOf((int) Math.round(val));
                }

                //Round the value to the specified number of decimal places
                String returning = String.valueOf(Math.round(val * Math.pow(10, places)) / Math.pow(10, places));
                //If the value is a decimal, and the decimal is not the same as the places, add 0's to the end
                if (returning.contains(".") && returning.split("\\.")[1].length() < places) {
                    returning += "0".repeat(places - returning.split("\\.")[1].length());
                }
                return returning;
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private static class RandomInt extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.randomInt/[min] [max]%";
        }

        @Override
        public List<String> getAliases() {
            return List.of("%random.int/[min] [max]%");
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 2) {
                return "0";
            }
            String split = StringUtilsKt.substringAfter(input, "/");
            String[] number = split.replace("%", "").split(" ");
            try {
                int min = Integer.parseInt(Placeholder.handlePlaceholders(number[0], house, player, true));
                int max = Integer.parseInt(Placeholder.handlePlaceholders(number[1], house, player, true));
                return String.valueOf((int) (Math.random() * (max - min + 1) + min));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private static class RandomDouble extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.randomDouble/[min] [max]%";
        }

        @Override
        public List<String> getAliases() {
            return List.of("%random.double/[min] [max]%");
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 2) {
                return "0";
            }
            String split = StringUtilsKt.substringAfter(input, "/");
            String[] number = split.split(" ");
            try {
                double min = Double.parseDouble(Placeholder.handlePlaceholders(number[0], house, player, true));
                double max = Double.parseDouble(Placeholder.handlePlaceholders(number[1], house, player, true));
                return String.valueOf(Math.random() * (max - min) + min);
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private static class Add extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.add/[number1] [number2]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 2) {
                return "0";
            }
            String split = StringUtilsKt.substringAfter(input, "/");
            String[] numbers = split.split(" ");
            try {
                double num1 = Double.parseDouble(Placeholder.handlePlaceholders(numbers[0], house, player, true));
                double num2 = Double.parseDouble(Placeholder.handlePlaceholders(numbers[1], house, player, true));
                return String.valueOf(num1 + num2);
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private static class Subtract extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.subtract/[number1] [number2]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 2) {
                return "0";
            }
            String split = StringUtilsKt.substringAfter(input, "/");
            String[] numbers = split.split(" ");
            try {
                double num1 = Double.parseDouble(Placeholder.handlePlaceholders(numbers[0], house, player, true));
                double num2 = Double.parseDouble(Placeholder.handlePlaceholders(numbers[1], house, player, true));
                return String.valueOf(num1 - num2);
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private static class Multiply extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.multiply/[number1] [number2]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 2) {
                return "0";
            }
            String split = StringUtilsKt.substringAfter(input, "/");
            String[] numbers = split.split(" ");
            try {
                double num1 = Double.parseDouble(Placeholder.handlePlaceholders(numbers[0], house, player, true));
                double num2 = Double.parseDouble(Placeholder.handlePlaceholders(numbers[1], house, player, true));
                return String.valueOf(num1 * num2);
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private static class Divide extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.divide/[number1] [number2]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 2) {
                return "0";
            }
            String split = StringUtilsKt.substringAfter(input, "/");
            String[] numbers = split.split(" ");
            try {
                double num1 = Double.parseDouble(Placeholder.handlePlaceholders(numbers[0], house, player, true));
                double num2 = Double.parseDouble(Placeholder.handlePlaceholders(numbers[1], house, player, true));
                return String.valueOf(num1 / num2);
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private static class Modulus extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.modulus/[number1] [number2]%";
        }

        @Override
        public List<String> getAliases() {
            return List.of("%math.mod/[number1] [number2]%");
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 2) {
                return "0";
            }
            String split = StringUtilsKt.substringAfter(input, "/");
            String[] numbers = split.split(" ");
            try {
                double num1 = Double.parseDouble(Placeholder.handlePlaceholders(numbers[0], house, player, true));
                double num2 = Double.parseDouble(Placeholder.handlePlaceholders(numbers[1], house, player, true));
                return String.valueOf(num1 % num2);
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private static class Power extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.power/[number1] [number2]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 2) {
                return "0";
            }
            String split = StringUtilsKt.substringAfter(input, "/");
            String[] numbers = split.split(" ");
            try {
                double num1 = Double.parseDouble(Placeholder.handlePlaceholders(numbers[0], house, player, true));
                double num2 = Double.parseDouble(Placeholder.handlePlaceholders(numbers[1], house, player, true));
                return String.valueOf(Math.pow(num1, num2));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private static class SQRT extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.sqrt/[number]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 1) {
                return "0";
            }
            String number = StringUtilsKt.substringAfter(input, "/");
            try {
                double num = Double.parseDouble(Placeholder.handlePlaceholders(number, house, player, true));
                return String.valueOf(Math.sqrt(num));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private static class ABS extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.abs/[number]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 1) {
                return "0";
            }
            String number = StringUtilsKt.substringAfter(input, "/");
            try {
                double num = Double.parseDouble(Placeholder.handlePlaceholders(number, house, player, true));
                return String.valueOf(Math.abs(num));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private class Floor extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.floor/[number]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 1) {
                return "0";
            }
            String number = StringUtilsKt.substringAfter(input, "/");
            try {
                double num = Double.parseDouble(Placeholder.handlePlaceholders(number, house, player, true));
                return String.valueOf(Math.floor(num));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private class Sin extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.sin/[number]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 1) {
                return "0";
            }
            String number = StringUtilsKt.substringAfter(input, "/");
            try {
                double num = Double.parseDouble(Placeholder.handlePlaceholders(number, house, player, true));
                return String.valueOf(Math.sin(Math.toRadians(num)));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private class Cos extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.cos/[number]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 1) {
                return "0";
            }
            String number = StringUtilsKt.substringAfter(input, "/");
            try {
                double num = Double.parseDouble(Placeholder.handlePlaceholders(number, house, player, true));
                return String.valueOf(Math.cos(Math.toRadians(num)));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private class Tan extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.tan/[number]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 1) {
                return "0";
            }
            String number = StringUtilsKt.substringAfter(input, "/");
            try {
                double num = Double.parseDouble(Placeholder.handlePlaceholders(number, house, player, true));
                return String.valueOf(Math.tan(Math.toRadians(num)));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private class ASin extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.asin/[number]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 1) {
                return "0";
            }
            String number = StringUtilsKt.substringAfter(input, "/");
            try {
                double num = Double.parseDouble(Placeholder.handlePlaceholders(number, house, player, true));
                return String.valueOf(Math.toDegrees(Math.asin(num)));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private class ACos extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.acos/[number]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 1) {
                return "0";
            }
            String number = StringUtilsKt.substringAfter(input, "/");
            try {
                double num = Double.parseDouble(Placeholder.handlePlaceholders(number, house, player, true));
                return String.valueOf(Math.toDegrees(Math.acos(num)));
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private class ATan extends Placeholder {
        @Override
        public String getPlaceholder() {
            return "%math.atan/[num1] [num2]%";
        }

        @Override
        public boolean hasArgs() {
            return true;
        }

        @Override
        public String handlePlaceholder(String input, HousingWorld house, Player player) {
            if (input.split("/").length < 1) {
                return "0";
            }
            String split = StringUtilsKt.substringAfter(input, "/");
            String[] numbers = split.split(" ");
            try {
                double num1 = Double.parseDouble(Placeholder.handlePlaceholders(numbers[0], house, player, true));
                double num2 = Double.parseDouble(Placeholder.handlePlaceholders(numbers[1], house, player, true));
                return String.valueOf(Math.toDegrees(Math.atan2(num1, num2)));
            } catch (Exception e) {
                return "0";
            }
        }
    }
}
