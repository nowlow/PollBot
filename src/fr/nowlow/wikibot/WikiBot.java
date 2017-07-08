package fr.nowlow.wikibot;

import java.util.Scanner;

import fr.nowlow.wikibot.command.CommandManager;
import fr.nowlow.wikibot.command.CommandMap;
import fr.nowlow.wikibot.commandmanage.HelpCommand;
import fr.nowlow.wikibot.commandmanage.StopCommand;
import fr.nowlow.wikibot.commandmanage.UserCommand;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;


/**
 * Created by Naoufel Berrada on 04/05/2017. Contact: nowlow77@gmail.com. Country: France;
 */

public final class WikiBot implements Runnable{
	
	private final CommandMap commandMap = new CommandMap(this);
	private final Scanner scanner = new Scanner(System.in);
	private final Thread thread = new Thread(this, "WikiBot Thread");
	private final JDA jda;
	
	private boolean running = true;
	
	public WikiBot(String token) throws Exception{
		jda = new JDABuilder(AccountType.BOT).setToken(token).buildAsync();
		jda.addEventListener(new WikiEventListener(this));
		
		registerCommand(new UserCommand(), new StopCommand(this), new HelpCommand(this));
	}

	public JDA getJda() {
		return jda;
	}
	
	public CommandMap getCommandMap() {
		return commandMap;
	}
	
	public void registerCommand(CommandManager...commandManagers){
		for(CommandManager command : commandManagers) commandMap.registerCommand(command);
	}
	
	public void run() {
		while (running) {
			if(scanner.hasNextLine()) commandMap.commandConsole(scanner.nextLine());
		}
		stop();
	}
	
	public void stopBot(){
		running = false;
	}
	
	private void stop(){
		scanner.close();
		System.out.println("Stopping bot...");
		jda.shutdown(false);
		System.out.println("Bot shudown.");
		System.exit(0);
	}
	
	public static void main(String[] args) throws Exception{
		args = new String[]{"/*Put your token here*/"};
		if(args.length < 1) System.out.println("Please to insert the token : java -jar <Name>.jar <token>");
		WikiBot wikiBot = new WikiBot(args[0]);
		wikiBot.thread.start();
	}
}
