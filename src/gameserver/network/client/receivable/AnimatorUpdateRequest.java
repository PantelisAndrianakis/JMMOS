package gameserver.network.client.receivable;

import common.network.ReceivablePacket;
import gameserver.actor.Player;
import gameserver.holders.AnimationHolder;
import gameserver.managers.WorldManager;
import gameserver.network.client.GameClient;
import gameserver.network.client.sendable.AnimatorUpdate;

/**
 * @author Pantelis Andrianakis
 * @version February 4th 2019
 */
public class AnimatorUpdateRequest
{
	public AnimatorUpdateRequest(GameClient client, ReceivablePacket packet)
	{
		// Get player.
		final Player player = client.getActiveChar();
		if (player == null)
		{
			return;
		}
		
		// Read data.
		final float velocityX = packet.readFloat();
		final float velocityZ = packet.readFloat();
		final boolean triggerJump = packet.readByte() == 1;
		final boolean isInWater = packet.readByte() == 1;
		final boolean isGrounded = packet.readByte() == 1;
		
		// Set last known world object animations.
		player.SetAnimations(new AnimationHolder(velocityX, velocityZ, triggerJump, isInWater, isGrounded));
		
		// Broadcast movement.
		WorldManager.broadcastPacketToVisiblePlayers(player, new AnimatorUpdate(player.getObjectId(), velocityX, velocityZ, triggerJump, isInWater, isGrounded));
	}
}
