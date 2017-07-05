package fr.nowlow.wikibot.command;

import java.lang.reflect.Method;

import fr.nowlow.wikibot.annotation.Command.Executor;

public final class SimpleCommand {

	private final String name;
	private final Executor executor;
	private final String description;
	private final CommandManager commandManager;
	private final Method method;
	
	public SimpleCommand(String name, Executor executor, String description, CommandManager commandManager, Method method){
		this.name = name;
		this.executor = executor;
		this.description = description;
		this.commandManager = commandManager;
		this.method = method;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Executor getExecutor() {
		return executor;
	}
	
	public CommandManager getCommandManager() {
		return commandManager;
	}
	
	public Method getMethod() {
		return method;
	}
}
