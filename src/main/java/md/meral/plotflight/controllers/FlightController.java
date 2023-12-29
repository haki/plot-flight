package md.meral.plotflight.controllers;

import java.util.ArrayList;
import java.util.UUID;

public class FlightController {
    public static FlightController instance;

    public FlightController() {
        instance = this;
    }

    private final ArrayList<UUID> playersCanFly = new ArrayList<>();
    private final ArrayList<UUID> playersCantFly = new ArrayList<>();

    public boolean CanFly(UUID uuid) {
        return playersCanFly.contains(uuid);
    }

    public boolean CantFly(UUID uuid) {
        return playersCantFly.contains(uuid);
    }

    public ArrayList<UUID> GetPlayersCanFly() {
        return playersCanFly;
    }

    public void AddPlayerToFlyList(UUID uuid) {
        playersCanFly.add(uuid);
    }

    public void AddPlayerToCantFlyList(UUID uuid) {
        playersCantFly.add(uuid);
    }

    public void RemovePlayerFromFlyList(UUID uuid) {
        for (int i = 0; i < playersCanFly.size(); i++) {
            if (playersCanFly.get(i).equals(uuid)) {
                playersCanFly.remove(i);
                break;
            }
        }
    }

    public void RemovePlayerFromCantFlyList(UUID uuid) {
        for (int i = 0; i < playersCantFly.size(); i++) {
            if (playersCantFly.get(i).equals(uuid)) {
                playersCantFly.remove(i);
                break;
            }
        }
    }
}
