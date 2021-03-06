package gameserver.network.client;

import java.util.Set;

import common.network.ReceivablePacket;

/**
 * @author Pantelis Andrianakis
 * @since September 7th 2020
 */
public class GameClientPacketExecutePoolTask implements Runnable
{
	private final Set<GameClient> _pool;
	
	public GameClientPacketExecutePoolTask(Set<GameClient> pool)
	{
		_pool = pool;
	}
	
	@Override
	public void run()
	{
		long executionStart;
		long currentTime;
		while (true)
		{
			executionStart = System.currentTimeMillis();
			
			// No need to iterate when pool is empty.
			if (!_pool.isEmpty())
			{
				// Iterate client pool.
				ITERATE: for (GameClient client : _pool)
				{
					if (client.getChannel() == null)
					{
						_pool.remove(client);
						continue ITERATE;
					}
					
					final Set<byte[]> packetData = client.getPacketData();
					if (packetData.isEmpty())
					{
						continue ITERATE;
					}
					
					for (byte[] data : packetData)
					{
						GameClientPacketHandler.handle(client, new ReceivablePacket(data));
						packetData.remove(data);
						continue ITERATE; // Process only first.
					}
				}
			}
			
			// Prevent high CPU caused by repeatedly looping.
			currentTime = System.currentTimeMillis();
			if ((currentTime - executionStart) < 1)
			{
				try
				{
					Thread.sleep(1);
				}
				catch (Exception ignored)
				{
				}
			}
		}
	}
}
