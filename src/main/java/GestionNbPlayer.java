import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class GestionNbPlayer {
    private BufferedReader m_buffer;
    private String url = "http://steamcharts.com/app/630100";

    public String getNb_joueur() {
        return nb_joueur;
    }

    public void setNb_joueur(String nb_joueur) {
        this.nb_joueur = nb_joueur;
    }


    private String nb_joueur;

    public GestionNbPlayer() throws IOException {
        this.m_buffer = new BufferedReader(new InputStreamReader((new URL(this.url)).openStream()));
        String s;
        boolean first = true;
        while((s = this.m_buffer.readLine()) != null&&first) {
            if (s.contains("<span class=\"num\">")){
                nb_joueur=s.split("<span class=\"num\">")[1].split("</span>")[0];
                first=false;
            }
        }
    }
    public boolean NbplayerActualize(GestionNbPlayer nb) {
        boolean change = false;
        if (!nb.getNb_joueur().equals(this.nb_joueur)){
            this.nb_joueur=nb.getNb_joueur();
            change =true;
        }
        return change;
    }
}
