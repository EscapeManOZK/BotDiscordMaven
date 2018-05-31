package Bot.Commande;

import Bot.MainBot;
import Bot.Serveur.GestionNbPlayer;
import Bot.Serveur.GestionServer;
import Bot.Serveur.GroupServer;
import Bot.Serveur.Serveur;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GestionCommande {

    private static String Prefix="s!";

    private List<Commande> m_command= new ArrayList<Commande>();

    public GestionCommande(){
        Commande help = new Commande("HELP","help","Affiches toutes les commandes disponibles ainsi que la liste de tous les serveurs");
        Commande etat = new Commande("ETAT","etat","affiche l'état de tous les serveurs ( si ils sont online ou offline");
        Commande server = new Commande("SERVER","srv","**<nom_Groupe_de_Serveur> <Initial_de_la_Langue_du_Serveur>**  affiche l'état du serveur spécifier");
        Commande on = new Commande("ONLINE", "online","Affiche les serveurs disponibles");
        Commande off = new Commande("OFFLINE", "offline","Affiche les serveurs non disponible");
        Commande clean = new Commande("CLEAN","clean","<Nb de message> Supprime le nombre indiqué de messages dans le channel ");
        Commande player = new Commande("PLAYER","player","Affiche le nombre de personne connecter");
        Commande Poll = new Commande("POLL","poll","_t: <Nom du formulaire>(facultatif) _d: <Corp du formulaire> _o: ['Nom_du_Premier_choix'<emoji> 'Nom_du_deuxième_choix'<emoji> .... ] Cette commande permet de créer un formulaire assez simple avec plusieurs choix de réponse");
        Commande Message = new Commande("","mp","");
        m_command.add(help);
        m_command.add(etat);
        m_command.add(server);
        m_command.add(on);
        m_command.add(off);
        m_command.add(player);
        m_command.add(Poll);
        m_command.add(clean);
        m_command.add(Message);
    }

    public List<Commande> getM_command() {
        return m_command;
    }

    public Commande takeCommandById(int id){
        return m_command.get(id);
    }

    public static String getPrefix() {
        return Prefix;
    }


    public EmbedBuilder CommandHelp(GestionServer Gsrv, User event){
        EmbedBuilder build = new EmbedBuilder();
        build.setTitle("**HELP**");
        build.setColor(Color.red);
        build.setDescription("Voici tous les commandes disponible : \n\nPour chaque commande rajouter devant \""+Prefix+"\" \n\n ");

        for (Commande c : m_command) {
            if (c.getM_title()!="")
                build.appendDescription("[COMMANDE]["+c.getM_title() + "]\n> **" + c.getM_command() + "**  " + c.getM_descrip()+"\n\n");
        }
        build.appendDescription("==========================================================\n\nVoici tous les serveurs : \n ");
        for (int j=0;j<Gsrv.getSize();j++) {
            GroupServer s = Gsrv.takeGroupServerById(j);
            build.appendDescription("\n[SERVER GROUP : "+s.getM_title()+"]\n\n");
            for(int i=0;i<s.getSize();i++){
                Serveur srv = s.getServeurbyId(i);
                build.appendDescription("["+srv.getM_pays()+"]"+srv.getM_name()+"\n");
            }
        }
        build.setFooter("©By "+event.getName()+" created by EscapeMan",event.getAvatarUrl());
        return build;
    }

    public EmbedBuilder CommandEtat(GestionServer Gsrv, GestionNbPlayer GPlayer, User event){
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat h = new SimpleDateFormat("hh:mm");

        Date currentTime_1 = new Date();

        String dateString = d.format(currentTime_1);
        String heureString = h.format(currentTime_1);
        EmbedBuilder build = new EmbedBuilder();
        build.setTitle("**ETAT  [ " + heureString + " , " + dateString + " ]**");
        build.setColor(Color.red);
        build.appendDescription("\nIl y a actuellement "+GPlayer.getNb_joueur()+" joueurs connectés");
        build.appendDescription("\nVoici tous les serveurs : \n ");
        seeServerData(Gsrv, build);
        build.setFooter("©By "+event.getName()+" created by EscapeMan",event.getAvatarUrl());
        return build;
    }

    public EmbedBuilder CommandServer(GestionServer Gsrv, String msg){
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat h = new SimpleDateFormat("hh:mm");

        Date currentTime_1 = new Date();

        String dateString = d.format(currentTime_1);
        String heureString = h.format(currentTime_1);
        EmbedBuilder build = new EmbedBuilder();
        build.setTitle("**SERVEUR  [ " + heureString + " , " + dateString + " ]**");
        build.setColor(Color.red);
        if (!(msg.split(" ").length<3&&msg.split(" ").length>4)) {
            String group="";
            String srv="";
            if (msg.split(" ").length==3) {
                group = msg.split(" ")[1];
                srv = msg.split(" ")[2];
            }else {
                group = msg.split(" ")[1]+" "+msg.split(" ")[2];
                srv = msg.split(" ")[3];
            }
            if (Gsrv.contains(group, srv)) {
                Serveur serv = Gsrv.getGroupeServerByName(group).getServeurbyName(srv);
                String actif = "";
                if (serv.getM_actif()) actif = "ONLINE";
                else actif = "OFFLINE";
                build.appendDescription("[" + serv.getM_pays() + "]" + serv.getM_name() + "   " + serv.getM_date() + "   " + actif);
            } else {
                build.appendDescription("__**/!\\ERREUR/!\\**__ \n");
                if (Gsrv.getGroupeServerByName(group) == null) {
                    build.appendDescription("Veuillez indiquer un nom de Groupe de serveur correcte \nExemple :" +
                            Gsrv.takeGroupServerById(1).getM_title()
                            + "\nPour plus d'information faite la commande **s!help**");
                } else if (Gsrv.getGroupeServerByName(group).getServeurbyName(srv) == null) {
                    if (!srv.contains("[") && srv.contains("]")) {
                        build.appendDescription("Veuillez indiquer le Pays du server \nExemple :" +
                                Gsrv.takeGroupServerById(1).getServeurbyId(3).getM_pays());
                    } else {
                        build.appendDescription("Veuillez indiquer un nom de server correcte \nExemple :" +
                                Gsrv.takeGroupServerById(1).getServeurbyId(3).getM_pays()
                                + "\nPour plus d'information faite la commande **s!help**");
                    }
                }

                // message d'erreur;
            }
        }else{
            build.appendDescription("__**/!\\ERREUR/!\\**__ \n");
            build.appendDescription("Veuillez indiquer un nom de Groupe de serveur et/ou un pays correcte \nExemple : s!srv" +
                    Gsrv.takeGroupServerById(1).getM_title()+" "+Gsrv.takeGroupServerById(1).getServeurbyId(1).getM_pays()
                    + "\nPour plus d'information faite la commande **s!help**");
        }

        return build;
    }

    public EmbedBuilder CommandOn(GestionServer Gsrv){
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat h = new SimpleDateFormat("hh:mm");

        Date currentTime_1 = new Date();

        String dateString = d.format(currentTime_1);
        String heureString = h.format(currentTime_1);
        EmbedBuilder build = new EmbedBuilder();
        build.setTitle("**SERVER-ON  [ " + heureString + " , " + dateString + " ]**");
        build.setColor(Color.red);
        build.appendDescription("\nVoici tous les serveurs online: \n");
        for(int i=0;i<Gsrv.getSize();i++){
            build.appendDescription("["+Gsrv.takeGroupServerById(i).getM_title()+"]\n");
            for (Serveur on:Gsrv.takeGroupServerById(i).getServeurON()) {
                String actif=""; if(on.getM_actif())actif="ONLINE"; else actif="OFFLINE";
                build.appendDescription("["+on.getM_pays()+"]"+on.getM_name()+"   "+on.getM_date()+"   "+actif+"\n");
            }
        }
        return build;
    }

    public EmbedBuilder CommandOff(GestionServer Gsrv){
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat h = new SimpleDateFormat("hh:mm");

        Date currentTime_1 = new Date();

        String dateString = d.format(currentTime_1);
        String heureString = h.format(currentTime_1);
        EmbedBuilder build = new EmbedBuilder();
        build.setTitle("**SERVER-OFF  [ " + heureString + " , " + dateString + " ]**");
        build.setColor(Color.red);
        build.appendDescription("\nVoici tous les serveurs offline: \n");
        for(int i=0;i<Gsrv.getSize();i++){
            build.appendDescription("["+Gsrv.takeGroupServerById(i).getM_title()+"]\n");
            for (Serveur on:Gsrv.takeGroupServerById(i).getServeurOFF()) {
                String actif=""; if(on.getM_actif())actif="ONLINE"; else actif="OFFLINE";
                build.appendDescription("["+on.getM_pays()+"]"+on.getM_name()+"   "+on.getM_date()+"   "+actif+"\n");
            }
        }
        return build;
    }

    public EmbedBuilder ChangeServeur(GestionServer Gsrv, GestionNbPlayer GPlayer, User event){
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat h = new SimpleDateFormat("hh:mm");

        Date currentTime_1 = new Date();

        String dateString = d.format(currentTime_1);
        String heureString = h.format(currentTime_1);
        EmbedBuilder build = new EmbedBuilder();
        build.setTitle("**ETAT_SERVEUR_CHANGE  [ " + heureString + " , " + dateString + " ]**");
        build.setColor(Color.red);
        build.appendDescription("Il y a actuellement "+GPlayer.getNb_joueur()+"\n");
        build.appendDescription("\nVoici tous les serveurs : \n ");
        seeServerData(Gsrv, build);
        build.setFooter("©By "+event.getName()+" created by EscapeMan",event.getAvatarUrl());
        return build;
    }

    private void seeServerData(GestionServer Gsrv, EmbedBuilder build) {
        for (int j=0;j<Gsrv.getSize();j++) {
            GroupServer s = Gsrv.takeGroupServerById(j);
            build.appendDescription("\n[SERVER GROUP : "+s.getM_title()+"]\n\n");
            for(int i=0;i<s.getSize();i++){
                Serveur srv = s.getServeurbyId(i);
                String actif ="";
                if (srv.getM_actif()) actif="ONLINE"; else actif = "OFFLINE";
                build.appendDescription("["+srv.getM_pays()+"]"+srv.getM_name()+"  "+srv.getM_date()+"  "+actif+"\n");
            }
        }
    }

    public EmbedBuilder CommandClean(MessageReceivedEvent event,String msg,User usr){
        EmbedBuilder build = new EmbedBuilder();
        System.out.println(msg.split(" ").length);
        if (msg.split(" ").length>2||msg.split(" ").length<=1){
            build.appendDescription("Paramètres incorrects");
        }else {
            int nb = Integer.parseInt(msg.split(" ")[1]);
            List<Message> m_msg;
            if (nb>100){
                build.appendDescription("indiquez un nombre entre 1 et 100");
            }else {
                m_msg=event.getTextChannel().getHistory().retrievePast(nb).complete();
                event.getTextChannel().deleteMessages(m_msg).queue();
                build.appendDescription(nb+" messages supprimés avec succés");
            }
        }
        build.setFooter("©By "+usr.getName()+" created by EscapeMan",null);
        return build;
    }
    public EmbedBuilder CommandMessage(User usr, String msg,MessageReceivedEvent event){ // à refaire
        EmbedBuilder build = new EmbedBuilder();
        String[] split = msg.split(" ");
        if (split.length<=1){
            return null;
        }else {
            String titre="";
            int i;
            for (i=1; i< split.length&&!split[i].equals("_d:"); i++){
                titre+= split[i]+" ";
            }
            String Mes="";
            for (int j = i; j< split.length; j++){
                if (!split[j].equals("_d:"))
                    Mes += split[j]+" ";
            }
            build.setAuthor(event.getAuthor().getName(),null,event.getAuthor().getAvatarUrl());
            build.setColor(Color.BLACK);
            build.setTitle(titre);
            build.appendDescription(Mes);
            build.setFooter("©By "+usr.getName()+" created by EscapeMan",null);
        }
        return build;
    }
    public EmbedBuilder CommandPlayer(GestionNbPlayer gp, User event){
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat h = new SimpleDateFormat("hh:mm");

        Date currentTime_1 = new Date();

        String dateString = d.format(currentTime_1);
        String heureString = h.format(currentTime_1);
        EmbedBuilder build = new EmbedBuilder();
        build.setColor(Color.red);
        build.setTitle("**Nombre de joueur connecté[ "+heureString+" , "+dateString+" ]**");
        build.appendDescription("Il y a actuellement "+gp.getNb_joueur()+" joueurs connectés");
        build.setFooter("©By "+event.getName()+" created by EscapeMan",event.getAvatarUrl());
        return build;
    }
    public void CommandPoll(User Bot, MessageReceivedEvent event, String msg, MessageChannel channel,JDA jda){
        EmbedBuilder build = new EmbedBuilder();
        String[] split = msg.split(" ");
        String erreur ="";
        if (split.length>1){
            Poll formulaire = new Poll();
            boolean fini=false;
            int i = 1;
            int tmpi =1;
            while (!fini) {
                if (split[i].equals("_t:")) {
                    i++;
                    while (!split[i].equals("_d:") && !split[i].equals("_o:") && !fini) {
                        formulaire.addToTitre(split[i] + " ");
                        if (i+1<split.length) {
                            i++;
                        }else {
                            fini=true;
                        }
                    }
                } else if (split[i].equals("_d:")) {
                    i++;
                    while (!split[i].equals("_t:") && !split[i].equals("_o:") && !fini) {
                        formulaire.addToQuestion(split[i] + " ");
                        if (i+1<split.length) {
                            i++;
                        }else {
                            fini=true;
                        }
                    }
                } else if (split[i].equals("_o:")) {
                    i++;
                    if(split.length-i<=1){
                        erreur="Il faux saisir deux option au minimun";
                    }else {
                        while (!split[i].equals("_d:") && !split[i].equals("_t:") && !fini) {
                            if (!split[i].equals("")) {
                                String[] option = split[i].split("'");
                                if (option.length >= 2) {
                                    boolean trouve = false;
                                    Emote m = null;
                                    for (int j = 0; j < event.getMessage().getEmotes().size() && !trouve; j++) {
                                        if (option[2].split(":")[1].contains(event.getMessage().getEmotes().get(j).getName())) {
                                            trouve = true;
                                            m = event.getMessage().getEmotes().get(j);
                                        }
                                    }
                                    if (m == null) {
                                        erreur = "Erreur emoji veillez réessayer avec les emojis du serveur";
                                    }
                                    formulaire.addOption(option[1], option[2], m);
                                } else {
                                    erreur = "Il faux saisir le nom et l'emoji";
                                }
                            }
                            if (i + 1 < split.length) {
                                i++;
                            } else {
                                fini = true;
                            }

                        }
                    }
                }
                if(tmpi==i){
                    i++;
                    tmpi++;
                }
            }
            boolean cas;
            String message="";
            build.setTitle("**[FORMULAIRE] "+formulaire.getM_titre()+"**");
            if (!formulaire.getM_question().equals("")&&formulaire.getOptionSize()>0&&erreur==""){
                cas=true;
                message=formulaire.getM_question()+"\n";
                for(i=0;i<formulaire.getOptionSize();i++){
                    String[] tmp=formulaire.getOption(i).getM_titre().split("_");
                    String tmpn="";
                    for(int x=0;x<tmp.length;x++){
                        tmpn+=tmp[x];
                        if(x+1<tmp.length)
                            tmpn+=" ";
                    }
                    message+=tmpn;
                    if (i+1<formulaire.getOptionSize()){
                        message+="  /  ";
                    }
                }
                build.appendDescription(message);
            }else {
                cas=false;
                if (erreur=="")erreur="Veillez poser votre question";
                build.appendDescription("**__[ERREUR] Donnée manquante__** \n \n"+erreur+"\n"+event.getAuthor().getName()+" suivais l'exemple suivant\nExemple : s!poll _t: Titre _d: Description _o: 'option1':unicorn~1: 'option2':Dabbing: ");
            }
            build.setFooter("©By "+Bot.getName()+" created by EscapeMan",Bot.getAvatarUrl());
            channel.sendMessage(build.build()).queue();
            if (cas) {
                event.getMessage().delete().queue();
                List<Message> m = event.getTextChannel().getHistory().retrievePast(2).complete();
                Message poll = m.get(0);
                for (i = 0; i < formulaire.getOptionSize(); i++) {
                    try {
                        poll.addReaction(formulaire.getOption(i).getM_Emote()).queue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
