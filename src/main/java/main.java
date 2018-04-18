public class main {
    public static final void main(String[] args)  {
        MainBot bot = new MainBot();
        args[0]= System.getenv("TOKEN");
        bot.run(args);
    }
}
