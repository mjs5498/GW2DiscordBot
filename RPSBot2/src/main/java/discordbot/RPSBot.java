package discordbot;

public class RPSBot {

	public static void main(String[] args) {
		RPSBotCode rpsbc = new RPSBotCode("MjkxMjgwNjM1NDM4NTYzMzQ5.C6nQ6w.PORbzC00c0DCde3XybSlFs_kjGI");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Shutdown Hook is running !"); 
				} 
			});
		System.out.println("Application Terminating ...");


	}

}
