
public class StatManager {

    private Map<UUID, List<Stat>> stats;
    private HousingWorld house; 

    public StatManager(HousingWorld house) {
        this.house = house;
        this.stats = new HashMap<>();
    }

    public Stat getPlayerStatByName(Player player, String name) {
        List<Stat> playerStats = stats.getOrDefault(player.getUniqueId(), new ArrayList<>());

        for (Stat stat : playerStats) {
            if (stat.getStatName().equals(name)) {
                return stat;
            }
        }

        // If no stat found, return a default stat with value 0
        Stat defaultStat = new Stat(player, name, 0.0);
        playerStats.add(defaultStat);
        stats.put(player.getUniqueId(), playerStats);
        return defaultStat;
    }

    public List<Stat> getPlayerStats(Player player) {
        if (stats.containsKey(player.getUniqueID())) {
            return stats.get(player.getUniqueID());
        }
        return null;
    }

}