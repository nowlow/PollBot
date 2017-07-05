package fr.nowlow.wikibot.commandmanage;

import java.awt.Color;

import fr.nowlow.wikibot.WikiBot;
import fr.nowlow.wikibot.annotation.Command;
import fr.nowlow.wikibot.annotation.Command.Executor;
import fr.nowlow.wikibot.command.CommandManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class HelpCommand implements CommandManager {
	// private final WikiBot wikibot;
	
	public HelpCommand(WikiBot wikibot) {
		// this.wikibot = wikibot;
	}
	
	@Command(name="help",type=Executor.USER)
	public boolean onHelp(User user, TextChannel channel, String[] args) {
		if(args.length > 1) return true;
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("📖  HELP", null);
		builder.setDescription("*Créer un sondage:*\n"
				+ "```"
				+ " :poll <titre> / <p1> / <p2> ... : crée un sondage\n"
				+ " :vote <numéro de la réponse> : vote pour une réponse\n"
				+ " :results : donne les résultats du sondage\n"
				+ " :remove : supprime le sondage en cours\n"
				+ "```");
		builder.setFooter("PollBot, 2017. Naoufel BERRADA ✅", null);
		builder.setColor(Color.ORANGE);
		channel.sendMessage(builder.build()).queue();
		return true;
	}
}
