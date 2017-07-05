package fr.nowlow.pollbot.vote;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class Poll {
	private String title;
	private String options;
	private String[] optionTable;
	private String date;
	private String author;
	private String avatarUrl;
	private int[] scoreTable;
	private String message;
	private boolean isActive;
	private int currentID;
	
	public Poll(String[] args) {
		//this.score = score;
		message = "";
		for(int i = 0; i < args.length; i++) {
			message = message + " " + args[i];
		}
		optionTable = message.split("/");
	}
	
	public void setAvatarUrl(User user) {
		avatarUrl = user.getAvatarUrl();
	}
	
	public String getAvatarUrl() {
		return avatarUrl;
	}
	
	public void setAuthor(User user) {
		author = user.getName();
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setOptions(String[] args, TextChannel channel){
		if(optionTable.length < 3){
			return;
		} else {
		options = "";
		for(int i = 1; i < optionTable.length; i++) {
			options = options + "**" + i + ")** " + optionTable[i] + "\n";
		}
		scoreTable = new int[optionTable.length];
		options = "      " + options;
		}
	}
	
	public String getOptions() {
		return options;
	}
	
	public void setTitle(String args[]) {
		title = "**" + optionTable[0].toUpperCase() + "**";
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getOptionTable(int cursor){
		return optionTable[cursor];
	}
	
	public String[] getAllOptionTable(){
		return optionTable;
	}
	
	public void vote(int vote){
		int i = vote;
		scoreTable[i] = scoreTable[i] + 1;
	}
	
	public boolean isCorrect(int vote){
		if(vote > optionTable.length - 1) return false;
		else if(vote < 1) return false;
		else return true;
	}
	
	public int[] getScore(){
		return scoreTable;
	}
	
	public void setDate() {
		Date today = Calendar.getInstance().getTime();
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        date = formatter.format(today);
	}
	
	public String getDate() {
		return date;
	}
	
	public void setActive(boolean active) {
		isActive = active;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void setID(int id) {
		currentID = id;
	}
	
	public int getID() {
		return currentID;
	}
	
	public void stop() {
		isActive = false;
	}
	
	public float[] getProcents() {
		float[] procent = new float[optionTable.length];
		float[] results = new float[optionTable.length];
		float n = 0;
		for(int y = 0; y < optionTable.length; y++) {
			procent[y] = scoreTable[y];
			n = n + procent[y];
			results[y] = (procent[y]/n)*100;
		}
		return results;
	}
	
	public void remove() {
		title = "";
		options = "";
		date = "";
		author = "";
		avatarUrl = "";
		message = "";
		isActive = false;
		currentID = 0;
	}
}
