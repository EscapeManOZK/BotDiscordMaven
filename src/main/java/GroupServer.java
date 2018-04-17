import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GroupServer {
    private String m_title;
    private List<Serveur> m_serveur;

    public GroupServer(String title) {
        this.m_title = title;
        this.m_serveur = new ArrayList();
    }

    public void addServeur(Serveur s) {
        this.m_serveur.add(s);
    }

    public Serveur getServeurbyName(String name) {
        boolean find = false;
        Serveur tmp = null;
        Iterator var4 = this.m_serveur.iterator();

        while(var4.hasNext()) {
            Serveur s = (Serveur)var4.next();
            String name_srv = s.getM_pays();
            if (!find && name_srv.equals(name)) {
                find = true;
                tmp = s;
            }
        }

        return tmp;
    }

    public Serveur getServeurbyId(int id) {
        return (Serveur)this.m_serveur.get(id);
    }

    public int getSize() {
        return this.m_serveur.size();
    }

    public List<Serveur> getServeurON() {
        List<Serveur> s = new ArrayList();
        Iterator var2 = this.m_serveur.iterator();

        while(var2.hasNext()) {
            Serveur stmp = (Serveur)var2.next();
            if (stmp.getM_actif()) {
                s.add(stmp);
            }
        }

        return s;
    }

    public List<Serveur> getServeurOFF() {
        List<Serveur> s = new ArrayList();
        Iterator var2 = this.m_serveur.iterator();

        while(var2.hasNext()) {
            Serveur stmp = (Serveur)var2.next();
            if (!stmp.getM_actif()) {
                s.add(stmp);
            }
        }

        return s;
    }

    public String getM_title() {
        return this.m_title;
    }

    public void setM_title(String m_title) {
        this.m_title = m_title;
    }
}
