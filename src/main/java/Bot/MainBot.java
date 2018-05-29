package Bot;

import Bot.Commande.GestionCommande;
import Bot.Serveur.GestionNbPlayer;
import Bot.Serveur.GestionServer;
import net.dv8tion.jda.client.entities.Group;

import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MainBot extends ListenerAdapter {

    private static boolean stop=false;
    private GestionCommande GCommand;
    private GestionServer Gserver;
    private GestionNbPlayer GPlayer;
    private boolean GserverInitialise = false;
    private TextChannel Information;
    private Message c;
    private Timer t;
    private User Bot;
    private Log log;
    JDA jda ;



    /**
     * This is the method where the program starts.
     */

    private void initCommand() {
        GCommand = new GestionCommande();

    }

    private void initServeur()  {
        try {
            Gserver = new GestionServer();
            GPlayer = new GestionNbPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void onChangeServer() {
        //clean channel info
        try {
            GestionNbPlayer GPtemps = new GestionNbPlayer();

            GPlayer.NbplayerActualize(GPtemps);
            GestionServer Gtemps = new GestionServer();
            if (Gserver.GroupServerActualise(Gtemps)){
                Information.sendTyping().queue();
                Information.sendMessage(GCommand.ChangeServeur(Gserver,GPlayer,Bot).build()).queue();
            }/*else {
                String message = "Les serveurs n'ont pas changer d'état";
                if (Information.getLatestMessageId()!=null) {
                    Message msg = Information.getMessageById(Information.getLatestMessageId()).complete();
                    if (msg.getContentDisplay().contains(message)) {
                        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat h = new SimpleDateFormat("hh:mm");

                        Date currentTime_1 = new Date();

                        String dateString = d.format(currentTime_1);
                        String heureString = h.format(currentTime_1);
                        Information.sendTyping().queue();
                        msg.editMessage(" [ " + heureString + " , " + dateString + " ]"+message).queue();
                    }else
                        sendMessageNoChange(message);

                }else{
                    sendMessageNoChange(message);
                }
            }*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageNoChange(String message) {
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat h = new SimpleDateFormat("hh:mm");

        Date currentTime_1 = new Date();

        String dateString = d.format(currentTime_1);
        String heureString = h.format(currentTime_1);
        Information.sendTyping().queue();
        Information.sendMessage(" [ " + heureString + " , " + dateString + " ]"+message).queue();
    }

    private void exit() {
        Information.sendTyping().queue();
        Information.sendMessage("Bye everyone").queue();
        //Information.sendMessage(GCommand.CommandEtat(Gserver).build()).queue();
        jda.shutdown();
        stop = true;
    }

    public void run(String[] args) {
        log=new Log();
        initCommand();
        initServeur();
        try
        {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(args[0])
                    .addEventListener(this)
                    .buildBlocking();
            Information = jda.getTextChannelById("435171789589577748");
            Bot = jda.getSelfUser();
            jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT,"soulworker-wiki.com | s!help","www.soulworker-wiki.com/"));
        }
        catch (LoginException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        clean();
        Information.sendTyping().queue();
        Information.sendMessage("Hello everyone").queue();
        Information.sendMessage(GCommand.CommandEtat(Gserver,GPlayer,Bot).build()).queue();
        t = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                cleanMsg();
            }

            private void cleanMsg() {
                c.delete().reason("clean").queue();
                t.stop();
            }
        });
        Timer timer;
        timer = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                onChangeServer();
            }
        });
        timer.start();
        Timer git;
        git = new Timer(100000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (log.canPush()){
                    //log.push();
                }
            }
        });
        git.start();
        while (!stop) {
            Scanner scanner = new Scanner(System.in);
            String cmd = scanner.next();
            if (cmd.equalsIgnoreCase("stop")) {
                exit();
            }
        }
    }

    private void clean() {
        MessageHistory his = new MessageHistory(Information);
        List<Message> msg;
        msg = his.retrievePast(99).complete();
        Information.deleteMessages(msg).queue();
    }

    /**
     * NOTE THE @Override!
     * This method is actually overriding a method in the ListenerAdapter class! We place an @Override annotation
     *  right before any method that is overriding another to guarantee to ourselves that it is actually overriding
     *  a method from a super class properly. You should do this every time you override a method!
     *
     * As stated above, this method is overriding a hook method in the
     * {@link net.dv8tion.jda.core.hooks.ListenerAdapter ListenerAdapter} class. It has convience methods for all JDA events!
     * Consider looking through the events it offers if you plan to use the ListenerAdapter.
     *
     * In this example, when a message is received it is printed to the console.
     *
     * @param event
     *          An event containing information about a {@link Message Message} that was
     *          sent in a channel.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat h = new SimpleDateFormat("hh:mm");

        Date currentTime_1 = new Date();

        String dateString = d.format(currentTime_1);
        String heureString = h.format(currentTime_1);
        try {
            log.launch();
        } catch (IOException e) {
            e.printStackTrace();
        }
        User author = event.getAuthor();
        if (event.getChannel().getId().equals("435171789589577748")&&!author.equals(Bot)) {
            event.getTextChannel().sendTyping().queue();
            EmbedBuilder b = new EmbedBuilder();
            b.appendDescription(author.getName() + " il ne faux pas écrire dans ce channel !");
            b.setFooter("©By " + event.getGuild().getSelfMember().getUser().getName() + " created by EscapeMan", null);
            event.getTextChannel().sendMessage(b.build()).queue();
            List<Message> m = event.getTextChannel().getHistory().retrievePast(2).complete();
            c = m.get(0);
            t.start();
        } else {
            Message message = event.getMessage();
            MessageChannel channel = event.getChannel();
            String msg = message.getContentDisplay();
            PrivateChannel privateChannel = null;

            boolean bot = author.isBot();

            if (event.isFromType(ChannelType.TEXT)) {
                Guild guild = event.getGuild();
                TextChannel textChannel = event.getTextChannel();
                Member member = event.getMember();

                String name;
                if (message.isWebhookMessage()) {
                    name = author.getName();
                } else {
                    name = member.getEffectiveName();
                }
                System.out.printf("[%s]<%s>: %s\n", textChannel.getName(), name, msg);
                try {
                    log.write("["+heureString+" , "+dateString+"]["+textChannel.getName()+"]<"+name+">: "+msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (event.getTextChannel().getId().equals("430035981660454942") && msg.contains(event.getAuthor().getName())) {
                    event.getAuthor().openPrivateChannel().complete().sendTyping().queue();
                    event.getTextChannel().sendTyping().queue();
                    EmbedBuilder build = new EmbedBuilder();
                    build.setTitle("**Bienvenue sur le discord de YukiNoNeko**");
                    build.setAuthor(event.getGuild().getSelfMember().getNickname());
                    build.appendDescription("Bienvenue @" + event.getAuthor().getName() + " sur le discord \nRegarde tes messages privées :wink:");
                    build.setFooter("©By " + event.getGuild().getSelfMember().getUser().getName() + " created by EscapeMan", null);
                    event.getTextChannel().sendMessage(build.build()).queue();
                    event.getAuthor().openPrivateChannel().complete().sendMessage("Voici la liste de mes commandes :").queue();
                    event.getAuthor().openPrivateChannel().complete().sendMessage(GCommand.CommandHelp(Gserver, Bot).build()).queue();
                }
            } else if (event.isFromType(ChannelType.PRIVATE)) {
                privateChannel = event.getPrivateChannel();

                System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);
                try {
                    log.write("["+heureString+" , "+dateString+"][PRIV]<"+author.getName()+">: "+msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (event.isFromType(ChannelType.GROUP)) {
                Group group = event.getGroup();
                String groupName = group.getName() != null ? group.getName() : "";

                System.out.printf("[GRP: %s]<%s>: %s\n", groupName, author.getName(), msg);
                try {
                    log.write("["+heureString+" , "+dateString+"][GRP: "+groupName+"]<"+author.getName()+">: "+msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                log.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int i;
            boolean find = false;
            for (i = 0; i < GCommand.getM_command().size() && !find; i++) {
                if (msg.contains(GCommand.getPrefix() + GCommand.getM_command().get(i).getM_command())) {
                    find = true;
                }
            }
            i--;
            if (find) {
                if (i == 6 || i == 7 || i == 8) {
                    if (i == 6){
                        channel.sendTyping().queue();
                        GCommand.CommandPoll(Bot,event,msg,channel,jda);
                    }else if (i == 7) {
                        channel.sendTyping().queue();
                        channel.sendMessage(GCommand.CommandClean(event, msg, Bot).build()).queue();
                        List<Message> m = event.getTextChannel().getHistory().retrievePast(2).complete();
                        c = m.get(0);
                        t.start();
                    } else {
                        if (!event.isFromType(ChannelType.PRIVATE))
                            event.getMessage().delete().queue();
                        EmbedBuilder build = GCommand.CommandMessage(Bot, msg, event);
                        if (build == null) {
                            author.openPrivateChannel().complete().sendTyping().queue();
                            build = new EmbedBuilder();
                            build.appendDescription("Paramètre invalide \n Exemple: s!mp my title _d: my message");
                            build.setFooter("©By " + Bot.getName() + " created by EscapeMan", null);
                            author.openPrivateChannel().complete().sendMessage(build.build()).queue();
                        } else {
                            jda.getTextChannelById("429033577741811714").sendTyping().queue();
                            jda.getTextChannelById("429033577741811714").sendMessage(build.build()).queue();
                        }
                    }
                } else {
                    if (event.isFromType(ChannelType.PRIVATE)) {
                        privateChannel.sendTyping().queue();
                        switch (i) {
                            case 0:    //help
                                privateChannel.sendMessage(GCommand.CommandHelp(Gserver, Bot).build()).queue();
                                break;
                            case 1:    //état
                                privateChannel.sendMessage(GCommand.CommandEtat(Gserver, GPlayer, Bot).build()).queue();
                                break;
                            case 2:    //server
                                privateChannel.sendMessage(GCommand.CommandServer(Gserver, msg).build()).queue();
                                break;
                            case 3:    //online
                                privateChannel.sendMessage(GCommand.CommandOn(Gserver).build()).queue();
                                break;
                            case 4:    //offline
                                privateChannel.sendMessage(GCommand.CommandOff(Gserver).build()).queue();
                                break;
                            case 5:
                                privateChannel.sendMessage(GCommand.CommandPlayer(GPlayer, Bot).build()).queue();
                                break;
                        }
                    } else {
                        if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS) && event.getTextChannel().getId().equals("429046247798865920")) { // si il le bot à les droit pour écrires
                            channel.sendTyping().queue();
                            switch (i) {
                                case 0:    //help
                                    channel.sendMessage(GCommand.CommandHelp(Gserver, Bot).build()).queue();
                                    break;
                                case 1:    //état
                                    channel.sendMessage(GCommand.CommandEtat(Gserver, GPlayer, Bot).build()).queue();
                                    break;
                                case 2:    //server
                                    channel.sendMessage(GCommand.CommandServer(Gserver, msg).build()).queue();
                                    break;
                                case 3:    //online
                                    channel.sendMessage(GCommand.CommandOn(Gserver).build()).queue();
                                    break;
                                case 4:    //offline
                                    channel.sendMessage(GCommand.CommandOff(Gserver).build()).queue();
                                    break;
                                case 5:
                                    channel.sendMessage(GCommand.CommandPlayer(GPlayer, Bot).build()).queue();
                            }


                        } else { // sinon il envoie le message à la personne

                            event.getTextChannel().sendTyping().queue();
                            EmbedBuilder b = new EmbedBuilder();
                            b.appendDescription(author.getName() + " regarde tes messages privés");
                            event.getMessage().delete().reason("Pas au bon channel").queue();
                            author.openPrivateChannel().complete().sendTyping().queue();
                            switch (i) {
                                case 0:    //help
                                    author.openPrivateChannel().complete().sendMessage(GCommand.CommandHelp(Gserver, Bot).build()).queue();
                                    break;
                                case 1:    //état
                                    author.openPrivateChannel().complete().sendMessage(GCommand.CommandEtat(Gserver, GPlayer, Bot).build()).queue();
                                    break;
                                case 2:    //server
                                    author.openPrivateChannel().complete().sendMessage(GCommand.CommandServer(Gserver, msg).build()).queue();
                                    break;
                                case 3:    //online
                                    author.openPrivateChannel().complete().sendMessage(GCommand.CommandOn(Gserver).build()).queue();
                                    break;
                                case 4:    //offline
                                    author.openPrivateChannel().complete().sendMessage(GCommand.CommandOff(Gserver).build()).queue();
                                    break;
                                case 5:
                                    author.openPrivateChannel().complete().sendMessage(GCommand.CommandPlayer(GPlayer, Bot).build()).queue();
                                    break;
                            }
                            event.getTextChannel().sendMessage(b.build()).queue();
                            List<Message> m = event.getTextChannel().getHistory().retrievePast(2).complete();
                            c = m.get(0);
                            t.start();
                        }
                    }

                }
            }


        }
    }
}


