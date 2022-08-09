package xyz.mega.utilitybot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.CommandUpdateAction;
import xyz.mega.utilitybot.commands.model.AbstractCommand;
import xyz.mega.utilitybot.commands.model.CommandFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.dv8tion.jda.api.entities.Command.OptionType.INTEGER;
import static net.dv8tion.jda.api.entities.Command.OptionType.USER;

public class PurgeCommand extends AbstractCommand {

	public PurgeCommand(CommandFactory commandFactory) {
		super(commandFactory,
				new CommandUpdateAction.CommandData(
						"purge",
						"Purge all messages from a specific member")
						.addOption(
								new CommandUpdateAction.OptionData(
										USER,
										"user",
										"The user you want to purge messages from"
								).setRequired(true)
						)
						.addOption(
								new CommandUpdateAction.OptionData(
										INTEGER,
										"limit",
										"Limit of messages you want to delete"
								)
						)
				);
	}

	@Override
	public void processCommand(SlashCommandEvent event) {
		event.reply(String.format("Purging messages from @%s", event.getOption("user").getAsUser().getId())).queue();

		List<Message> messageList = new ArrayList<>();

		event.getGuild().getTextChannels().forEach(textChannel -> textChannel.getIterableHistory()
				.stream()
				.filter(message -> message.getAuthor().equals(event.getOption("user").getAsUser()))
				.forEach(message -> {
					messageList.add(message);
					System.out.println(message.getContentRaw());
				}));

		if (event.getOption("limit") != null) {
			event.getGuild().getTextChannels().forEach(
					textChannel -> System.out.println(textChannel.purgeMessages(messageList
							.stream()
							.filter(message -> message.getTextChannel().getId().equals(textChannel.getId()))
							.limit(event.getOption("limit").getAsLong())
							.collect(Collectors.toList()))
					)
			);
		} else {
			event.getGuild().getTextChannels().forEach(
					textChannel -> System.out.println(textChannel.purgeMessages(messageList
							.stream()
							.filter(message -> message.getTextChannel().getId().equals(textChannel.getId()))
							.collect(Collectors.toList())))
			);
		}

		event.getTextChannel().sendMessage(String.format("Purged %s messages from @%s", messageList.size(), event.getOption("user").getAsUser().getId())).queue();
	}

}
