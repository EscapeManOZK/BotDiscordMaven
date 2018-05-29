package Bot.Commande;

import Bot.Commande.OptionPoll;
import net.dv8tion.jda.core.entities.Emote;

import java.util.*;

public class Poll {
    private String m_titre;
    private String m_question;
    private List<OptionPoll> m_option;

    public Poll(){
        m_titre="";
        m_question="";
        m_option=new ArrayList<OptionPoll>();
    }

    public String getM_titre() {
        return m_titre;
    }

    public void addToTitre(String m_titre) {
        this.m_titre += m_titre;
    }

    public String getM_question() {
        return m_question;
    }

    public void addToQuestion(String m_question) {
        this.m_question += m_question;
    }

    public int getOptionSize() {
        return m_option.size();
    }

    public OptionPoll getOption(int i){
        return m_option.get(i);
    }

    public void addOption(String titre, String Emoji,Emote m) {
        OptionPoll poll = new OptionPoll(titre,Emoji,m);
        m_option.add(poll);
    }
}
