import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GestionServer {
    private BufferedReader m_buffer;
    private List<GroupServer> m_serveur;
    private List<String> pays;
    private String url = "https://www.g-status.com/game/soulworker";

    public GestionServer() throws IOException {
        this.m_buffer = new BufferedReader(new InputStreamReader((new URL(this.url)).openStream()));
        this.m_serveur = new ArrayList();
        this.pays = new ArrayList();
        this.InitPays();
        GroupServer group = null;
        Serveur srv = null;
        boolean premier = true;
        int i = 0;

        String s;
        while((s = this.m_buffer.readLine()) != null) {
            if (s.contains("tab_title")) {
                String title = s.split("<div class=\"tab_title\"><h3>")[1].split("</h3></div>")[0];
                if (!premier) {
                    this.m_serveur.add(group);
                } else {
                    premier = false;
                }

                group = new GroupServer(title);
            } else if (s.contains("flag text-align-center")) {
                srv = new Serveur();
                srv.setM_pays((String)this.pays.get(i));
                ++i;
            } else if (s.contains("server_name")) {
                srv.setM_name(s.split("\"server_name\">")[1].split("</di")[0]);
            } else if (s.contains("last_offline_date")) {
                srv.setM_date(s.split("line_date\">")[1].split("</")[0]);
            } else if (s.contains("div class=\"status")) {
                if (s.contains("ONLINE")) {
                    srv.setM_actif(true);
                } else {
                    srv.setM_actif(false);
                }

                group.addServeur(srv);
            }
        }

        this.m_serveur.add(group);
    }

    private void InitPays() {
        this.pays.add("EU");
        this.pays.add("DE");
        this.pays.add("EN");
        this.pays.add("ES");
        this.pays.add("FR");
        this.pays.add("IT");
        this.pays.add("PL");
        this.pays.add("DE");
        this.pays.add("EN");
        this.pays.add("ES");
        this.pays.add("FR");
        this.pays.add("IT");
        this.pays.add("PL");
    }

    public int getSize() {
        return this.m_serveur.size();
    }

    public GroupServer takeGroupServerById(int id) {
        return (GroupServer)this.m_serveur.get(id);
    }

    public boolean GroupServerActualise(GestionServer Gst) {
        boolean change = false;

        for(int i = 0; i < this.m_serveur.size() && !change; ++i) {
            for(int j = 0; j < ((GroupServer)this.m_serveur.get(i)).getSize() && !change; ++j) {
                if (((GroupServer)this.m_serveur.get(i)).getServeurbyId(j).getM_actif() != ((GroupServer)Gst.m_serveur.get(i)).getServeurbyId(j).getM_actif()) {
                    change = true;
                }
            }
        }

        if (change) {
            this.m_serveur = Gst.m_serveur;
        }

        return change;
    }

    public GroupServer getGroupeServerByName(String name) {
        boolean find = false;
        GroupServer tmp = null;
        Iterator var4 = this.m_serveur.iterator();

        while(var4.hasNext()) {
            GroupServer s = (GroupServer)var4.next();
            if (!find && s.getM_title().equals(name)) {
                find = true;
                tmp = s;
            }
        }

        return tmp;
    }

    public boolean contains(String group, String srv) {
        GroupServer tmp = this.getGroupeServerByName(group);
        if (tmp != null) {
            return tmp.getServeurbyName(srv) != null;
        } else {
            return false;
        }
    }
}
