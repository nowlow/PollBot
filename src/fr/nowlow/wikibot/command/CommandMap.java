package fr.nowlow.wikibot.command;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import fr.nowlow.wikibot.annotation.Command;
import fr.nowlow.wikibot.annotation.Command.Executor;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public final class CommandMap {

	private final Map<String, SimpleCommand> commands = new HashMap<>();
	private String tag = ":";
	
	public CommandMap(){}
	
	public String getTag() {
		return tag;
	}
	
	/* Register une commande */
	public void registerCommand(CommandManager commandManager){
		for(Method method : commandManager.getClass().getDeclaredMethods()){
			if(method.isAnnotationPresent(Command.class)){
				Command command = method.getAnnotation(Command.class);
				method.setAccessible(true);
				commands.put(command.name(), new SimpleCommand(command.name(), command.type(), command.description(), commandManager,method));
				/* Annonce que la command est bien register */
				System.out.println("Command "+command.name()+" is registered.");
			}
		}
	}
	
	public void commandConsole(String command){
		Object[] object = getCommand(command);
		if(object[0] == null || ((SimpleCommand)object[0]).getExecutor() == Executor.USER){
			System.out.println("Command unknow.");
			return;
		}
		try{
			if(!execute(((SimpleCommand)object[0]), (String[])object[1], null, null, null)) System.out.println(((SimpleCommand)object[0]).getDescription());
		}catch(Exception exception){
			System.out.println("The method "+((SimpleCommand)object[0]).getMethod().getName()+" is not initialize correctly.");
		}
	}
	
	public void commandUser(User user, TextChannel textChannel, PrivateChannel privateChannel, String command){
		Object[] object = getCommand(command);
		if(object[0] == null || ((SimpleCommand)object[0]).getExecutor() == Executor.CONSOLE) return;
		try{
			if(!execute(((SimpleCommand)object[0]), (String[])object[1], user, textChannel, privateChannel)){
				//if(textChannel != null) textChannel.sendMessage(((SimpleCommand)object[0]).getDescription()).queue();
				//if(privateChannel != null) privateChannel.sendMessage(((SimpleCommand)object[0]).getDescription()).queue();
			}
		}catch(Exception exception){
			System.out.println("The method "+((SimpleCommand)object[0]).getMethod().getName()+" is not initialize correctly.");
			exception.printStackTrace();
		}
	}
	
	private Object[] getCommand(String command){
		String[] commandSplit = command.split(" ");
		String[] args = new String[commandSplit.length-1];
		for(int i = 1; i < commandSplit.length; i++) args[i-1] = commandSplit[i];
		SimpleCommand simpleCommand = commands.get(commandSplit[0]);
		return new Object[]{simpleCommand, args};
	}
	
	private boolean execute(SimpleCommand command, String[] args, User user, TextChannel textChannel, PrivateChannel privateChannel) throws Exception{
		Parameter[] parameters = command.getMethod().getParameters();
		Object[] objects = new Object[parameters.length];
		for(int i = 0; i < parameters.length; i++){
			if(parameters[i].getType() == String[].class) objects[i] = args;
			else if(parameters[i].getType() == User.class) objects[i] = user;
			else if(parameters[i].getType() == TextChannel.class) objects[i] = textChannel;
			else if(parameters[i].getType() == PrivateChannel.class) objects[i] = privateChannel;
		}
		return (boolean) command.getMethod().invoke(command.getCommandManager(), objects);
	}
}
