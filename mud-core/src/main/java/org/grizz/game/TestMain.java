package org.grizz.game;

public class TestMain {
    public static void main(String[] args) {
        Game game = GameFactory.getInstance((playerName, response) -> System.out.println(playerName + ": " + response.toString()));
        game.runCommand("grizz", "lista graczy");
    }
}
