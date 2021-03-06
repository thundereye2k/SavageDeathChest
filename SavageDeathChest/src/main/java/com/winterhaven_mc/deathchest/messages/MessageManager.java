package com.winterhaven_mc.deathchest.messages;


import com.winterhaven_mc.deathchest.PluginMain;
import com.winterhaven_mc.deathchest.ProtectionPlugin;
import com.winterhaven_mc.util.AbstractMessageManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public final class MessageManager extends AbstractMessageManager {

	// reference to main class
	private final PluginMain plugin;

	/**
	 * Class constructor
	 * @param plugin reference to main class
	 */
	public MessageManager(final PluginMain plugin) {

		// call super class constructor
		//noinspection unchecked
		super(plugin, MessageId.class);

		this.plugin = plugin;
	}

//# Variable substitutions:
//# %PLAYER_NAME%          Player's name
//# %PLAYER_NICKNAME%      Player's nickname
//# %PLAYER_DISPLAYNAME%   Player's display name, including prefix/suffix
//# %WORLD_NAME%           World name that player is in
//# %EXPIRE_TIME%          Remaining time at chest deployment


	@Override
	protected Map<String,String> getDefaultReplacements(CommandSender recipient) {

		Map<String,String> replacements = new HashMap<>();

		// strip color codes
		replacements.put("%PLAYER_NAME%",ChatColor.stripColor(recipient.getName()));
		replacements.put("%WORLD_NAME%",ChatColor.stripColor(getWorldName(recipient)));

		// get expire time from config
		long expireTime = plugin.getConfig().getLong("expire-time");

		// convert time to milliseconds
		expireTime = TimeUnit.MINUTES.toMillis(expireTime);

		// if expire time is zero, convert to negative (allow config to specify zero for unlimited time)
		if (expireTime == 0) {
			expireTime = -1;
		}

		replacements.put("%EXPIRE_TIME%",getTimeString(expireTime));

		if (recipient instanceof Player) {
			Player player = (Player)recipient;
			replacements.put("%PLAYER_NICKNAME%",ChatColor.stripColor(player.getPlayerListName()));
			replacements.put("%PLAYER_DISPLAYNAME%",ChatColor.stripColor(player.getDisplayName()));
		}

		return replacements;
	}


	/**
	 * Send message to recipient
	 * @param recipient the recipient to whom to send a message
	 * @param messageId the message identifier
	 */
	public void sendMessage(final CommandSender recipient, final MessageId messageId) {

		// get default replacement map
		Map<String,String> replacements = getDefaultReplacements(recipient);

		// send message
		//noinspection unchecked
		sendMessage(recipient, messageId, replacements);
	}


	/**
	 * Send message to recipient
	 * @param recipient the recipient to whom to send a message
	 * @param messageId the message identifier
	 * @param protectionPlugin the protection plugin whose name will be used in the message
	 */
	public void sendMessage(final CommandSender recipient,
							final MessageId messageId,
							final ProtectionPlugin protectionPlugin) {

		// if recipient is null, do nothing and return
		if (recipient == null) {
			return;
		}

		// get default replacement map
		Map<String,String> replacements = getDefaultReplacements(recipient);

		replacements.put("%PLUGIN%",protectionPlugin.getPluginName());

		// send message
		//noinspection unchecked
		sendMessage(recipient, messageId, replacements);
	}


	/**
	 * Get sign text from language file
	 * @return List of String - lines of sign text
	 */
	public List<String> getSignText() {
		return this.messages.getStringList("SIGN_TEXT");
	}


	/**
	 * Get date format string from language file
	 * @return String - date format string
	 */
	public String getDateFormat() {
		return this.messages.getString("DATE_FORMAT");
	}

}
