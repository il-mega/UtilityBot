package xyz.mega.utilitybot.commands.model;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.CommandUpdateAction;
import xyz.mega.utilitybot.commands.PurgeCommand;

import javax.security.auth.login.LoginException;
import java.util.HashSet;
import java.util.Set;

public class CommandFactory extends ListenerAdapter {

	public JDA jda;
	private final Set<AbstractCommand> commandSet = new HashSet<>();

	public CommandFactory(JDABuilder jdaBuilder) throws LoginException, InterruptedException {
		this.jda = jdaBuilder
				.addEventListeners(this)
				.build()
				.awaitReady();

		long startConstruct = System.currentTimeMillis();
		constructCommands();
		printMessage("Finished constructing commands in " + (System.currentTimeMillis() - startConstruct) + "ms", 832074440471871488L);

		long startRegister = System.currentTimeMillis();
		registerCommands();
		printMessage("Finished registering commands in " + (System.currentTimeMillis() - startRegister) + "ms", 832074440471871488L);
	}

	protected void printMessage(String message, long textChannelID) {
		TextChannel textChannel = this.jda.getTextChannelById(textChannelID);

		if (textChannel != null) {
			textChannel.sendMessage(message).queue();
			System.out.printf("Printed message to text channel (%s): %s%n", textChannelID, message);
		}
	}

	void constructCommands() {
		this.commandSet.add(new PurgeCommand(this));
	}

	void registerCommands() {
		CommandUpdateAction commandUpdateAction = this.jda.getGuildById("794071210895736832").updateCommands();

		Set<CommandUpdateAction.CommandData> commandData = new HashSet<>();

		this.commandSet
				.forEach(abstractCommand -> commandData.add(abstractCommand.commandData));

		commandUpdateAction.addCommands(commandData);

		commandUpdateAction.queue();
	}

	@Override
	public void onSlashCommand(SlashCommandEvent event) {
		if (event.getGuild() == null) {
			return;
		}

		this.commandSet.stream()
				.filter(abstractCommand -> abstractCommand.commandData.toData().getString("name").equals(event.getName()))
				.forEach(abstractCommand -> abstractCommand.processCommand(event));
	}

}
