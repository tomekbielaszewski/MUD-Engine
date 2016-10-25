package old.org.grizz.game;

import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.service.simple.Notifier;
import old.org.grizz.game.ui.OutputFormatter;
import old.org.grizz.game.ui.impl.OutputFormatterImpl;

import java.util.Scanner;

/**
 * Created by Grizz on 2015-04-17.
 */
public class Starter {

    public static void mainOld(String[] args) {
        OutputFormatter formatter = new OutputFormatterImpl();
        Game game = GameFactory.getInstance(new Notifier() {
            @Override
            public void notify(String playerName, PlayerResponse response) {
                System.out.println(String.format("########## %s ############", playerName));
                String output = formatter.format(response);
                output = output.replaceAll("\n", "\n# ");
                output = "# " + output;
                System.out.println(output);
                System.out.println(String.format("########## EOF %s ########", playerName));
            }
        });

        Scanner sc = new Scanner(System.in);

        try {
//        System.out.println("Podaj nazwe gracza...");
//        String player = sc.nextLine();
            String player = "Grizz";
            String command = "spojrz";
//            String command = "wykuj sztabka zlota 1";

            PlayerResponse response = game.runCommand(command, player);
            System.out.println(formatter.format(response));

            while (!(command = sc.nextLine()).equals("q")) {
                response = game.runCommand(command, player);
                System.out.println(formatter.format(response));
            }
        } finally {
            sc.close();
        }
    }
}
