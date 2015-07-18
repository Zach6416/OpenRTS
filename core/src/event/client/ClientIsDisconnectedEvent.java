package event.client;

public class ClientIsDisconnectedEvent extends Event {

	private final int version;
	private final String gameName;
	private final int clientId;

	public ClientIsDisconnectedEvent(int version, String gameName, int clientId) {
		super();
		this.version = version;
		this.gameName = gameName;
		this.clientId = clientId;
	}

	public int getVersion() {
		return version;
	}

	public String getGameName() {
		return gameName;
	}

	public int getClientId() {
		return clientId;
	}

}
