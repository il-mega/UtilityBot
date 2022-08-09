package xyz.mega.utilitybot.commands.model;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.CommandUpdateAction;

public abstract class AbstractCommand {

	private final CommandFactory commandFactory;
	public final CommandUpdateAction.CommandData commandData;

	public AbstractCommand(CommandFactory commandFactory, CommandUpdateAction.CommandData commandData) {
		this.commandFactory = commandFactory;
		this.commandData = commandData;

		this.commandFactory.printMessage("Command initialized: " + this.commandData.toData().getString("name"), 832074440471871488L);
	}

	public void processCommand(SlashCommandEvent event) {
		event.acknowledge(true).queue();
	}



}
