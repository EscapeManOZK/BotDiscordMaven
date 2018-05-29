package Bot.Commande;

import net.dv8tion.jda.core.entities.Emote;

public class OptionPoll {
    private String m_titre;
    private String m_Emoji_Name;
    private Emote m_Emote;

    public OptionPoll(String titre, String emoji, Emote m){
        m_titre=titre;
        m_Emoji_Name=emoji;
        m_Emote=m;
    }

    public String getM_titre() {
        return m_titre;
    }

    public void setM_titre(String m_titre) {
        this.m_titre = m_titre;
    }

    public String getEmojiName() {
        return m_Emoji_Name;
    }

    public void setEmojiName(String m_Emoji) {
        this.m_Emoji_Name = m_Emoji;
    }

    public Emote getM_Emote() {
        return m_Emote;
    }

    public void setM_Emote(Emote m_Emote) {
        this.m_Emote = m_Emote;
    }
}
