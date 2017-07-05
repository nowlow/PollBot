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
	private int id;
	
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
	
	@Command(name="poll[id]",type=Executor.USER,description="The command how help you to create poll[id]")
	private boolean onpoll(User user, TextChannel channel, String[] args){
		if(!ispoll) {
			EmbedBuilder builder = new EmbedBuilder();
			poll[id] = new Poll(args);
			
			poll[id].setOptions(args, channel);
			poll[id].setTitle(args);
			poll[id].setDate();
			poll[id].setAuthor(user);
			poll[id].setAvatarUrl(user);
			
			title = poll[id].getTitle();
			options = poll[id].getOptions();
			date = poll[id].getDate();
			author = poll[id].getAuthor();
			avatarUrl = poll[id].getAvatarUrl();
			allOptionTable = poll[id].getAllOptionTable();
			
			if(allOptionTable.length < 3) {
				errorBuilder(channel, "Vous n'avez pas entré assez d'arguments");
				return false;
			}
			else {
				builder.setAuthor(author + " à crée un sondage:", null, avatarUrl);
				builder.setColor(Color.WHITE);
				builder.setTitle(title, null);
				builder.setDescription(options);
				builder.setFooter("Sondage crée le : " + date, null);
				channel.sendMessage(builder.build()).queue();
				
				ispoll = true;
				return true;
			}
		} else {
			errorBuilder(channel, "Il y a déjà un sondage en cours, si vous en êtes le responsable faites `:remove` ou `:results`\n"
					+ "Si vous êtes administrateur, vous pouvez utiliser la commande `:adminremove`");
			return false;
		}
	}
	
	@Command(name="vote",type=Executor.USER,description="The command how help you to vote")
	private boolean onVote(User user, TextChannel channel, String[] args) {
		try {
		if(ispoll) {
			
				if(args.length < 1) {
					errorBuilder(channel, "vous n'avez pas entré assez d'arguments");
					return false;
				} else {
					int vote = Integer.parseInt(args[0]);
					if(poll[id].isCorrect(vote)) {
						channel.sendMessage("Vous avez voté pour **" + vote + "** (" + poll[id].getOptionTable(vote) + ") !").queue();
						poll[id].vote(vote);
						return true;
					} else {
						errorBuilder(channel, "Le nombre entré n'est pas correct!");
						return false;
					}
				}
		} else {
			errorBuilder(channel, "Il n'y a pas de sondage en cours!");
			return true;
		}
		} catch(EventException exeption) {
				errorBuilder(channel, "Votre vote n'as pas pu être soumis du a une erreur interne!");
				return false;
		}
	}
	
	@Command(name="results",type=Executor.USER,description="The command how show you the results")
	private boolean onResults(User user, TextChannel channel, String[] args) {
		try {
		if(ispoll) {
			
				if(user.getName().equals(poll[id].getAuthor())) {
					int[] score = poll[id].getScore();
					float[] procents = poll[id].getProcents();
					EmbedBuilder builder = new EmbedBuilder();
					String results = "";
					for(int y = 1; y < score.length; y ++) {
						results = results + "La réponse **" + poll[id].getOptionTable(y) + "** à obtenu **" + score[y] + "** ("+ procents[y] + "%) voies!\n";
					}
					
					builder.setAuthor(poll[id].getAuthor() + " a demandé l'annonce des résultats!", null, poll[id].getAvatarUrl());
					builder.addField("Pour la question : " + poll[id].getTitle().toUpperCase() + "\n", results, false);
					builder.setFooter("Ce fut un sondage de " + poll[id].getAuthor() + " lancé le " + poll[id].getDate(), poll[id].getAvatarUrl());
					builder.setColor(Color.MAGENTA);
					channel.sendMessage("Voici les résultats:").queue();
					channel.sendMessage(builder.build()).queue();
					
					poll[id].remove();
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
			errorBuilder(channel, "Votre demande n'as pas pu être traitée du a une erreur interne!");
			return false;
		}
	}
	
	@Command(name="current",type=Executor.USER,description="The command how show you the current poll[id]")
	private boolean onCurrent(User user, TextChannel channel, String[] args) {
		if(ispoll) {
			
			if(args.length > 1) {
				errorBuilder(channel, "Vous n'avez pas entré assez d'arguments");
				return false;
			}
			
			EmbedBuilder builder = new EmbedBuilder();
			
			builder.setAuthor("Sondage crée par : " + poll[id].getAuthor(), null, poll[id].getAvatarUrl());
			builder.setColor(Color.WHITE);
			builder.setTitle("     " + poll[id].getTitle(), null);
			builder.setDescription(poll[id].getOptions());
			builder.setFooter("Sondage crée le : " + poll[id].getDate(), null);
			channel.sendMessage("**Sondage en cours:**").queue();
			channel.sendMessage(builder.build()).queue();
			return true;
		} else {
			errorBuilder(channel, "Il n'y a pas de sondage en cours!");
			return true;
		}
	}
	
	@Command(name="remove",type=Executor.USER,description="The command how remove your poll[id]")
	private boolean onRemove(User user, TextChannel channel, String[] args){
		if(ispoll) {
			
			if(user.getName().equals(poll[id].getAuthor())) {
				if(args.length > 1) {
					errorBuilder(channel, "Vous avez entré trop d'arguments");
					return false;
				}
				else {
						String authorName = user.getName();
						String authorID = user.getId();
						String authorMention = user.getAsMention();
						String avatarUrl = user.getAvatarUrl();
						EmbedBuilder builder = new EmbedBuilder();
						builder.setTitle("Le sondage à été supprimé!", null);
						builder.setThumbnail(avatarUrl);
						builder.setDescription("**Personne ayant supprimé le sondage:**\n" + authorName + " (" + authorMention + ")\n\n**ID:**\n" + authorID + "\n\n**Date de création du compte:**\n" + user.getCreationTime());
						builder.setColor(Color.ORANGE);
						channel.sendMessage(builder.build()).queue();
						poll[id].remove();
						poll[id].stop();
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
	
	@Command(name="adminremove",type=Executor.USER,description="The command how allow the managers to remove current poll[id]")
	private boolean onAdminRemove(User user, TextChannel channel, String[] args, Guild g){
		if(ispoll) {
			
			if(user.getName().equals("Nowlow") || user.getName().equals("NeutronStars")) {
				EmbedBuilder builder = new EmbedBuilder();
				EmbedBuilder privateBuilder = new EmbedBuilder();
				String adminName = user.getName();
				String adminMention = user.getAsMention();
				builder.setTitle("Le sondage à été supprimé par un administrateur", null);
				builder.addField("**Personne ayant supprimé le sondage:**", adminName + " (" + adminMention + ")", false);
				builder.setThumbnail("http://fr.seaicons.com/wp-content/uploads/2015/06/administrator-icon.png");
				builder.setColor(Color.GREEN);
				
				privateBuilder.setTitle("Votre sondage à été supprimé par un administrateur", null);
				privateBuilder.addField("**Administrateur ayant supprimé le sondage:**", adminName + " (" + adminMention + ")", false);
				privateBuilder.setThumbnail("http://fr.seaicons.com/wp-content/uploads/2015/06/administrator-icon.png");
				privateBuilder.setColor(Color.GREEN);
				
				poll[id].remove();
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
