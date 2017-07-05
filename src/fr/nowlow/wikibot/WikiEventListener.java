package fr.nowlow.wikibot;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public final class WikiEventListener implements EventListener{

	private final WikiBot wikiBot;
	private boolean admin;
	
	public WikiEventListener(WikiBot wikiBot){
		this.wikiBot = wikiBot;
	}
	
	public void onEvent(Event e){
		if(e instanceof ReadyEvent) onReady((ReadyEvent)e);
		if(e instanceof MessageReceivedEvent) onMessageReceived((MessageReceivedEvent)e);
	}
	
	private void onReady(ReadyEvent e){
		System.out.println("The bot is Ready.");
		System.out.println("Bot connected.\nGuild List :");
		for(Guild g : wikiBot.getJda().getGuilds()){
			System.out.println("-> "+g.getName());
		}
	}
	
	private void onMessageReceived(MessageReceivedEvent e){
		if(e.getAuthor().equals(wikiBot.getJda().getSelfUser())) return;
		
		String message = e.getMessage().getContent();
		if(message.startsWith(wikiBot.getCommandMap().getTag())){
			message = message.replaceFirst(wikiBot.getCommandMap().getTag(), "");
			if(e.getMember().hasPermission(Permission.MESSAGE_MANAGE)) e.getMessage().delete().queue();
			if(e.getMember().hasPermission(Permission.ADMINISTRATOR)) admin = true;
			wikiBot.getCommandMap().commandUser(e.getAuthor(), e.getTextChannel(), e.getPrivateChannel(), message);
			return;
		}
	}
	
	public boolean getAdmin() {
		return admin;
	}
}
