package fr.nowlow.wikibot.command;

import java.lang.reflect.Method;

import fr.nowlow.wikibot.annotation.Command.Executor;


public final class SimpleCommand {

	private final String name, description;
	private final Executor executorType;
	private final Object object;
	private final Method method;
	
	public SimpleCommand(String name, String description, Executor executorType, Object object, Method method) {
		super();
		this.name = name;
		this.description = description;
		this.executorType = executorType;
		this.object = object;
		this.method = method;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Executor getExecutorType() {
		return executorType;
	}

	public Object getObject() {
		return object;
	}

	public Method getMethod() {
		return method;
	}
	
	
}
