package openrts.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Logger;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

/**
 * @author mario
 */
public class ServerTest {

	private static final Logger logger = Logger.getLogger(ServerTest.class.getName());
	protected static String mapfilename = "assets/maps/test.btf";

	// @Test
	// public void testInput() throws Exception {
	//
	// OpenRTSServer app = new OpenRTSServer();
	// app.start(JmeContext.Type.Headless);
	// if (!mapfilename.isEmpty()) {
	// ModelManager.loadBattlefield(mapfilename);
	// }
	//
	// waitUntilServerIsStarted();
	//
	// ClientEventListenerMock obj = new ClientEventListenerMock();
	// EventManager.register(obj);
	//
	// ClientAppState state = new ClientAppState("");
	// state.initialize(app.getStateManager(), app);
	//
	// int id = 1;
	// FieldComp entity = EntityManager.getEntity(id);
	// Assert.assertNotNull(entity);
	//
	// SelectEntityEvent evt = new SelectEntityEvent(id);
	// state.manageEvent(evt);
	//
	// waitUntilClientHasResponse(obj, 0);
	// NetworkEvent ev = obj.getEvent();
	// Assert.assertTrue(ev instanceof AckEvent);
	//
	// }
	//
	// @Test
	// public void testStartMap() throws Exception {
	//
	// OpenRTSServer app = new OpenRTSServer();
	// // app.start(JmeContext.Type.Headless);
	// app.start();
	//
	// waitUntilServerIsStarted();
	//
	// ClientEventListenerMock obj = new ClientEventListenerMock();
	// EventManager.register(obj);
	//
	// ClientAppState state = new ClientAppState("");
	// state.initialize(app.getStateManager(), app);
	//
	// CreateGameEvent evt = new CreateGameEvent("assets/maps/test.btf");
	// state.manageEvent(evt);
	//
	// waitUntilClientHasResponse(obj, 0);
	// NetworkEvent ev = obj.getEvent();
	// Assert.assertTrue(ev instanceof AckEvent);
	//
	// }

	private void waitUntilClientHasResponse(ClientEventListenerMock mock, int times) throws IOException {
		int waitingCounter = times;
		boolean waiting = true;
		while (waiting) {
			waitingCounter++;
			if (mock.getEvent() != null) {
				waiting = false;
			} else {
				logger.info("Waiting for answer from server...");
			}
			if (times > 0 && waitingCounter > 10) {
				Assert.fail("Client are waiting too long..");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}

	private void waitUntilServerIsStarted() throws IOException {
		boolean scanning = true;
		while (scanning) {
			Socket socket = new Socket();
			try {
				InetSocketAddress sa = new InetSocketAddress("localhost", OpenRTSServer.PORT);

				socket.connect(sa, 500);
				scanning = false;
			} catch (IOException e) {
				logger.severe("Connect failed, waiting and trying again");
			} finally {
				socket.close();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@BeforeMethod
	public void setUpMethod() throws Exception {
	}

	@AfterMethod
	public void tearDownMethod() throws Exception {
	}
}