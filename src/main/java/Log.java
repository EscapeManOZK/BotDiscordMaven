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
    public void add(){
        try {
            Runtime.getRuntime().exec("git add *");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void commit(){
        try {
            Runtime.getRuntime().exec("git commit -m 'log'");
            i++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void push(){
        try {
            Runtime.getRuntime().exec("git push");
            change=false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
