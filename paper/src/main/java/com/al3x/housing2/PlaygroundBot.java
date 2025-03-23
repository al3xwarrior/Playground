package com.al3x.housing2;

import com.al3x.housing2.Enums.HousePrivacy;
import com.al3x.housing2.Instances.HousesManager;
import com.al3x.housing2.Instances.HousingWorld;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.*;

public class PlaygroundBot {
    private String token;
    private String guildId;
    private JDA bot;
    private Category houseCategory;
    private long updateRateLimit = 0;

    PlaygroundBot(String token, String guildId) throws InterruptedException {
        this.token = token;
        this.guildId = guildId;

        bot = JDABuilder.createDefault(token)
                .setActivity(Activity.watching("the playground"))
                .build();

        bot.awaitReady();

        Guild guild = bot.getGuildById(Long.parseLong(guildId));
        List<Category> guildCategories = guild.getCategories();
        for (Category category : guildCategories) {
            if (category.getName().equals("Housings")) {
                houseCategory = category;
                break;
            }
        }
        if (houseCategory == null) {
            guild.createCategory("Housings")
                    .queue(category -> {
                        houseCategory = category;
                        category.getManager().putPermissionOverride(guild.getPublicRole(), null, Collections.singleton(Permission.VOICE_CONNECT)).queue();
                    });
        }
    }

    public void updateHousings() {
        if (System.currentTimeMillis() - updateRateLimit < 1000) {
            return;
        }
        updateRateLimit = System.currentTimeMillis();

        HousesManager houseManager = Main.getInstance().getHousesManager();
        List<HousingWorld> housingWorlds = new ArrayList<>(houseManager.getLoadedHouses());

        housingWorlds.sort(Comparator.comparingInt((HousingWorld h) -> h.getWorld().getPlayerCount()));
        housingWorlds = housingWorlds.stream().filter(n -> n.getPrivacy() == HousePrivacy.PUBLIC).toList();

        List<HousingWorld> top5 = housingWorlds.subList(0, Math.min(5, housingWorlds.size()));

        List<VoiceChannel> existingChannels = new ArrayList<>(houseCategory.getVoiceChannels());
        List<VoiceChannel> top5Channels = new ArrayList<>();

        for (HousingWorld house : top5) {
            String houseName = "\uD83D\uDEAA" + MiniMessage.miniMessage().stripTags(house.getName())
                    .replaceAll("&[A-Fa-f0-9lmorknLMORKN]", "")
                    .replaceAll("&#[A-Fa-f0-9]{6}", "");
            if (houseName.length() > 100) houseName = houseName.substring(0, 99);
            // Check if the channel already exists
            String finalHouseName = houseName;
            Optional<VoiceChannel> existingChannelOpt = existingChannels.stream()
                    .filter(vc -> vc.getName().equals(finalHouseName))
                    .findFirst();

            if (existingChannelOpt.isPresent()) {
                VoiceChannel existingChannel = existingChannelOpt.get();
                top5Channels.add(existingChannel);
                existingChannels.remove(existingChannel);
            } else {
                // If the channel doesn't exist, create a new one
                VoiceChannel newChannel = houseCategory.createVoiceChannel(houseName).complete();
                top5Channels.add(newChannel);
            }
        }

        for (VoiceChannel remainingChannel : existingChannels) {
            remainingChannel.delete().queue();
        }

        // Reorder the channels in the category to match the top 5 order
        for (int i = 0; i < top5Channels.size(); i++) {
            VoiceChannel channel = top5Channels.get(i);
            channel.getManager().setPosition(i).queue();
        }
    }
}
