public class Commande {
    private String m_title;
    private String m_command;
    private String m_descrip;

    public Commande(String title, String command, String descrip) {
        this.m_title = title;
        this.m_command = command;
        this.m_descrip = descrip;
    }

    public String getM_command() {
        return this.m_command;
    }

    public void setM_command(String m_command) {
        this.m_command = m_command;
    }

    public String getM_descrip() {
        return this.m_descrip;
    }

    public void setM_descrip(String m_descrip) {
        this.m_descrip = m_descrip;
    }

    public String getM_title() {
        return this.m_title;
    }

    public void setM_title(String m_title) {
        this.m_title = m_title;
    }
}