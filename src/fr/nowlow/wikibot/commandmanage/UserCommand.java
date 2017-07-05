package fr.nowlow.wikibot.commandmanage;

import java.awt.Color;

import org.w3c.dom.events.EventException;

import fr.nowlow.pollbot.vote.Poll;
import fr.nowlow.wikibot.WikiBot;
import fr.nowlow.wikibot.annotation.Command;
import fr.nowlow.wikibot.annotation.Command.Executor;
import fr.nowlow.wikibot.command.CommandManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public final class UserCommand implements CommandManager {

	//private final WikiBot wikiBot;
	private boolean ispoll = false;
	private String title;
	private String options;
	private String date;
	private String author;
	private String avatarUrl;
	private Poll[] poll;
	private String[] allOptionTable;
	
	public UserCommand(WikiBot wikiBot){
		//this.wikiBot = wikiBot;
	}
	
	private void errorBuilder(TextChannel channel, String message) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("ERREUR", null);
		builder.setDescription(message);
		builder.setColor(Color.RED);
		builder.setThumbnail("https://images-ext-2.discordapp.net/external/KFVDlmbzoOmDr6LebDtMpYNVkIlxOnCo6fXPqu1YfZI/http/minuskube.fr/error_icon.png");
		channel.sendMessage(builder.build()).queue();
	}
	
	@Command(name="poll",type=Executor.USER,description="The command how help you to create poll[Integer.parseInt(guild.getId())]")
	private boolean onpoll(User user, TextChannel channel, String[] args, Guild guild){
		if(!ispoll) {
			EmbedBuilder builder = new EmbedBuilder();
			poll[Integer.parseInt(guild.getId())] = new Poll(args);
			
			poll[Integer.parseInt(guild.getId())].setOptions(args, channel);
			poll[Integer.parseInt(guild.getId())].setTitle(args);
			poll[Integer.parseInt(guild.getId())].setDate();
			poll[Integer.parseInt(guild.getId())].setAuthor(user);
			poll[Integer.parseInt(guild.getId())].setAvatarUrl(user);
			poll[Integer.parseInt(guild.getId())].setID(Integer.parseInt(guild.getId()));
			
			title = poll[Integer.parseInt(guild.getId())].getTitle();
			options = poll[Integer.parseInt(guild.getId())].getOptions();
			date = poll[Integer.parseInt(guild.getId())].getDate();
			author = poll[Integer.parseInt(guild.getId())].getAuthor();
			avatarUrl = poll[Integer.parseInt(guild.getId())].getAvatarUrl();
			allOptionTable = poll[Integer.parseInt(guild.getId())].getAllOptionTable();
			
			if(allOptionTable.length < 3) {
				errorBuilder(channel, "Vous n'avez pas entr� assez d'arguments");
				return false;
			}
			else {
				builder.setAuthor(author + " � cr�e un sondage dans la guild " + guild.getName() , null, avatarUrl);
				builder.setColor(Color.WHITE);
				builder.setTitle(title, null);
				builder.setDescription(options);
				builder.setFooter("Sondage cr�e le : " + date, null);
				channel.sendMessage(builder.build()).queue();
				
				ispoll = true;
				return true;
			}
		} else {
			errorBuilder(channel, "Il y a d�j� un sondage en cours, si vous en �tes le responsable faites `:remove` ou `:results`\n"
					+ "Si vous �tes administrateur, vous pouvez utiliser la commande `:adminremove`");
			return false;
		}
	}
	
	@Command(name="vote",type=Executor.USER,description="The command how help you to vote")
	private boolean onVote(User user, TextChannel channel, String[] args, Guild guild) {
		try {
		if(ispoll) {
			
				if(args.length < 1) {
					errorBuilder(channel, "vous n'avez pas entr� assez d'arguments");
					return false;
				} else {
					int vote = Integer.parseInt(args[0]);
					if(poll[Integer.parseInt(guild.getId())].isCorrect(vote)) {
						channel.sendMessage("Vous avez vot� pour **" + vote + "** (" + poll[Integer.parseInt(guild.getId())].getOptionTable(vote) + ") !").queue();
						poll[Integer.parseInt(guild.getId())].vote(vote);
						return true;
					} else {
						errorBuilder(channel, "Le nombre entr� n'est pas correct!");
						return false;
					}
				}
		} else {
			errorBuilder(channel, "Il n'y a pas de sondage en cours!");
			return true;
		}
		} catch(EventException exeption) {
				errorBuilder(channel, "Votre vote n'as pas pu �tre soumis du a une erreur interne!");
				return false;
		}
	}
	
	@Command(name="results",type=Executor.USER,description="The command how show you the results")
	private boolean onResults(User user, TextChannel channel, String[] args, Guild guild) {
		try {
		if(ispoll) {
			
				if(user.getName().equals(poll[Integer.parseInt(guild.getId())].getAuthor())) {
					int[] score = poll[Integer.parseInt(guild.getId())].getScore();
					float[] procents = poll[Integer.parseInt(guild.getId())].getProcents();
					EmbedBuilder builder = new EmbedBuilder();
					String results = "";
					for(int y = 1; y < score.length; y ++) {
						results = results + "La r�ponse **" + poll[Integer.parseInt(guild.getId())].getOptionTable(y) + "** � obtenu **" + score[y] + "** ("+ procents[y] + "%) voies!\n";
					}
					
					builder.setAuthor(poll[Integer.parseInt(guild.getId())].getAuthor() + " a demand� l'annonce des r�sultats!", null, poll[Integer.parseInt(guild.getId())].getAvatarUrl());
					builder.addField("Pour la question : " + poll[Integer.parseInt(guild.getId())].getTitle().toUpperCase() + "\n", results, false);
					builder.setFooter("Ce fut un sondage de " + poll[Integer.parseInt(guild.getId())].getAuthor() + " lanc� le " + poll[Integer.parseInt(guild.getId())].getDate(), poll[Integer.parseInt(guild.getId())].getAvatarUrl());
					builder.setColor(Color.MAGENTA);
					channel.sendMessage("Voici les r�sultats:").queue();
					channel.sendMessage(builder.build()).queue();
					
					poll[Integer.parseInt(guild.getId())].remove();
					ispoll = false;
					return true;
				} else {
					errorBuilder(channel, "Vous n'avez pas le droit de terminer ce sondage!");
					return false;
				}
		} else {
			errorBuilder(channel, "Il n'y a pas de sondage en cours!");
			return true;
		}
		} catch(EventException exeption) {
			errorBuilder(channel, "Votre demande n'as pas pu �tre trait�e du a une erreur interne!");
			return false;
		}
	}
	
	@Command(name="current",type=Executor.USER,description="The command how show you the current poll")
	private boolean onCurrent(User user, TextChannel channel, String[] args, Guild guild) {
		if(ispoll) {
			
			if(args.length > 1) {
				errorBuilder(channel, "Vous n'avez pas entr� assez d'arguments");
				return false;
			}
			
			EmbedBuilder builder = new EmbedBuilder();
			
			builder.setAuthor("Sondage cr�e par : " + poll[Integer.parseInt(guild.getId())].getAuthor(), null, poll[Integer.parseInt(guild.getId())].getAvatarUrl() + " " + Integer.parseInt(guild.getId()));
			builder.setColor(Color.WHITE);
			builder.setTitle("     " + poll[Integer.parseInt(guild.getId())].getTitle(), null);
			builder.setDescription(poll[Integer.parseInt(guild.getId())].getOptions());
			builder.setFooter("Sondage cr�e le : " + poll[Integer.parseInt(guild.getId())].getDate(), null);
			channel.sendMessage("**Sondage en cours:**").queue();
			channel.sendMessage(builder.build()).queue();
			return true;
		} else {
			errorBuilder(channel, "Il n'y a pas de sondage en cours!");
			return true;
		}
	}
	
	@Command(name="remove",type=Executor.USER,description="The command how remove your poll[Integer.parseInt(guild.getId())]")
	private boolean onRemove(User user, TextChannel channel, String[] args, Guild guild){
		if(ispoll) {
			
			if(user.getName().equals(poll[Integer.parseInt(guild.getId())].getAuthor())) {
				if(args.length > 1) {
					errorBuilder(channel, "Vous avez entr� trop d'arguments");
					return false;
				}
				else {
						String authorName = user.getName();
						String authorID = user.getId();
						String authorMention = user.getAsMention();
						String avatarUrl = user.getAvatarUrl();
						EmbedBuilder builder = new EmbedBuilder();
						builder.setTitle("Le sondage � �t� supprim�!", null);
						builder.setThumbnail(avatarUrl);
						builder.setDescription("**Personne ayant supprim� le sondage:**\n" + authorName + " (" + authorMention + ")\n\n**ID:**\n" + authorID + "\n\n**Date de cr�ation du compte:**\n" + user.getCreationTime());
						builder.setColor(Color.ORANGE);
						channel.sendMessage(builder.build()).queue();
						poll[Integer.parseInt(guild.getId())].remove();
						poll[Integer.parseInt(guild.getId())].stop();
						ispoll = false;
						return true;
				}
			} else {
				errorBuilder(channel, "Vous n'avez pas le droit de supprimer ce sondage!");
				return false;
			}
		} else {
			errorBuilder(channel, "Il n'y a pas de sondage en cours!");
			return true;
		}
	}
	
	@Command(name="adminremove",type=Executor.USER,description="The command how allow the managers to remove current poll[Integer.parseInt(guild.getId())]")
	private boolean onAdminRemove(User user, TextChannel channel, String[] args, Guild guild){
		if(ispoll) {
			
			if(user.getName().equals("Nowlow") || user.getName().equals("NeutronStars")) {
				EmbedBuilder builder = new EmbedBuilder();
				EmbedBuilder privateBuilder = new EmbedBuilder();
				String adminName = user.getName();
				String adminMention = user.getAsMention();
				builder.setTitle("Le sondage � �t� supprim� par un administrateur", null);
				builder.addField("**Personne ayant supprim� le sondage:**", adminName + " (" + adminMention + ")", false);
				builder.setThumbnail("http://fr.seaicons.com/wp-content/uploads/2015/06/administrator-icon.png");
				builder.setColor(Color.GREEN);
				
				privateBuilder.setTitle("Votre sondage � �t� supprim� par un administrateur", null);
				privateBuilder.addField("**Administrateur ayant supprim� le sondage:**", adminName + " (" + adminMention + ")", false);
				privateBuilder.setThumbnail("http://fr.seaicons.com/wp-content/uploads/2015/06/administrator-icon.png");
				privateBuilder.setColor(Color.GREEN);
				
				poll[Integer.parseInt(guild.getId())].remove();
				ispoll = false;
				if(args.length > 1) {
					String reason = "";
					for(int i = 0; i < args.length; i ++) {
						reason = reason + " " + args[i];
					}
					builder.addField("**Raison:**", reason, false);
					privateBuilder.addField("**Raison:**", reason, false);
				}
				channel.sendMessage(builder.build()).queue();
				return true;
			} else {
				errorBuilder(channel, "Vous ne disposez pas des droits d'administrateur!");
				return false;
			}
		} else {
			errorBuilder(channel, "Il n'y a pas de sondage en cours!");
			return true;
		}
	}
}
