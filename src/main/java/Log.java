import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Log {
    private String url="Log/dev.log";
    private File file;
    private FileWriter writer;
    private Boolean change;
    public Log(){
        file=new File(url);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        change=false;
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
            Runtime.getRuntime().exec("git add Log/dev.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void commit(){
        try {
            Runtime.getRuntime().exec("git commit -m \"log\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void push(){
        try {
            Runtime.getRuntime().exec("git pull");
            Runtime.getRuntime().exec("git push");
            change=false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
