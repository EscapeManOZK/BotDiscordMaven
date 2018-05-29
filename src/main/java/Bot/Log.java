package Bot;

import java.io.*;

public class Log {
    private String url="Log/dev.log";
    private File file;
    private FileWriter writer;
    private Boolean change;
    private int i;
    public Log(){
        file=new File(url);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        change=false;
        i=0;
    }
    public void launch() throws IOException {
        writer = new FileWriter(file,true);
    }

    public void write(String msg) throws IOException {
        writer.write(msg);
        writer.write("\n");
        change=true;
    }

    public void close() throws IOException {
        writer.close();
    }
    public Boolean canPush(){
        return change;
    }
    public void push(){
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec("/home/romain/Bureau/BotDiscordMaven/script.sh");
            change=false;
        }catch(Exception e) {
            System.out.println("erreur d'execution script.sh" + e.toString());
        }
    }
}