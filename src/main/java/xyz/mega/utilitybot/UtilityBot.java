package xyz.mega.utilitybot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import xyz.mega.utilitybot.commands.model.CommandFactory;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public class UtilityBot {

	public static String DISCORD_BOT_TOKEN = "ODQwODQxMjA0MjMzMTQyMjcy.YJeEZQ.C7KmpJZ8DTwa4RhntqvqTaMWYAI";

	public static void main(String[] args) throws LoginException, InterruptedException {
		new CommandFactory(constructBot());
	}

	static JDABuilder constructBot() {
		return JDABuilder
				.createLight(DISCORD_BOT_TOKEN, EnumSet.noneOf(GatewayIntent.class));
	}

}
