package org.grizz.game;

import org.grizz.game.model.PlayerResponse;

import java.util.Scanner;

public class TestMain {
    public static void main(String[] args) {
        Game game = GameFactory.getInstance((playerName, response) -> System.out.println(playerName + ": " + response.toString()));
        game.runCommand("lista graczy", "grizz");

        Scanner sc = new Scanner(System.in);

        try {
            String player = "Grizz";
            String command = "spojrz";

            do {
                PlayerResponse response = game.runCommand(command, player);
                System.out.println(response);
            } while (!(command = sc.nextLine()).equals("q"));
        } finally {
            sc.close();
        }
    }
}
