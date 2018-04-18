public class main {
    public static final void main(String[] args)  {
        MainBot bot = new MainBot();
        args[0]=process.env.TOKEN;
        bot.run(args);
    }
}
