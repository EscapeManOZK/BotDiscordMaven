package Test;

import Bot.Commande.Poll;
import net.dv8tion.jda.core.entities.Emote;

import java.util.Scanner;

public class Test {
    private Poll formulaire = new Poll();

    private void Test1(String msg) {

    }

    public void run(){
        Boolean b=false;
        while (!b) {
            Scanner scanner = new Scanner(System.in);
            String cmd = scanner.next();
            if (cmd.equalsIgnoreCase("stop")) {
                b=true;
            }
            if (cmd.contains("s!")){
                while (!cmd.contains(";")){
                    cmd += " "+scanner.next();
                }
                Test1(cmd);
                String message="";
                for(int i=0;i<formulaire.getOptionSize();i++){
                    message+=formulaire.getOption(i).getM_titre()+" "+formulaire.getOption(i).getEmojiName();
                }
                System.out.printf("Titre :"+formulaire.getM_titre()+"\nCorp : "+formulaire.getM_question()+"\n Soluce : "+message);
            }
        }
    }
    public static final void main(String[] args)  {
        Test t = new Test();
        t.run();
    }
}
