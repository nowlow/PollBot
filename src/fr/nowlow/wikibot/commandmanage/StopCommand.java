package fr.nowlow.wikibot.commandmanage;

import fr.nowlow.wikibot.WikiBot;
import fr.nowlow.wikibot.annotation.Command;
import fr.nowlow.wikibot.annotation.Command.Executor;
import fr.nowlow.wikibot.command.CommandManager;

public class StopCommand implements CommandManager{
	
	private final WikiBot wikibot;
	
	public StopCommand(WikiBot wikibot) {
		this.wikibot = wikibot;
	}
	
	@Command(name="stop", type=Executor.CONSOLE, description="The bot wasn't stopped")
	private boolean onStop() {
		wikibot.stopBot();
		return true;
	}
}
