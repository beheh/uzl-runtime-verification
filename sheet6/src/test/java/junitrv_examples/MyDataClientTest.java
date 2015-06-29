package junitrv_examples;

import junitrv_examples.DataService;
import junitrv_examples.MyDataClient;
import junitrv_examples.MyDataService;
import de.uni_luebeck.isp.rvtool.junitrv.FLTL4Monitor;
import de.uni_luebeck.isp.rvtool.junitrv.Monitor;
import de.uni_luebeck.isp.rvtool.junitrv.Monitors;
import de.uni_luebeck.isp.rvtool.junitrv.RVRunner;
import de.uni_luebeck.isp.rvtool.javamonitorinjection.api.syntax.Event;
import static de.uni_luebeck.isp.rvtool.javamonitorinjection.api.syntax.SimpleSyntax.*;

import org.junit.Test;
import org.junit.runner.RunWith;

//Use jUnitRV to monitor test cases
@RunWith(RVRunner.class)
@Monitors({ "commitBeforeClose", "openBeforeModify", "authenticateBeforeReadOrModify", "addPatientModifies"})
public class MyDataClientTest {

	// Qualified names for monitored classes
	private static final String dataServiceQname = "junitrv_examples/DataService";
	private static final String dataClientQname = "junitrv_examples/MyDataClient";

	// Event definitions for data service
	private static final Event modify = called(dataServiceQname, "modifyData");
	private static final Event committed = returned(dataServiceQname, "commit");
	private static final Event close = called(dataServiceQname, "disconnect");
	private static final Event opened = returned(dataServiceQname, "connect");
    
    private static final Event read = called(dataServiceQname, "readData");
	private static final Event authenticate = called(dataClientQname, "authenticate");
	private static final Event addPatient = called(dataClientQname, "addPatient");
	private static final Event addedPatient = returned(dataClientQname, "addPatient");

	// Monitor checking that changes are committed before closing the connection
	// to the data service.
	private static final Monitor commitBeforeClose = new FLTL4Monitor(
			Always(implies(modify, Until(not(close), committed))));

	// Monitor checking that a data service is opened before data
	// modifications are performed.
	private static final Monitor openBeforeModify = new FLTL4Monitor(
			and(WU(not(modify), opened),
					G(implies(close, WU(not(modify), opened)))));
    
    // Monitor ensuring that authenticate is called before any read or modify
    private static final Monitor authenticateBeforeReadOrModify = new FLTL4Monitor(
            WU(not(or(read, modify)), authenticate)
            );
    
    // Monitor ensuring that addPatient calls modify before returning
     private static final Monitor addPatientModifies = new FLTL4Monitor(
            Always(implies(addPatient, Until(not(addedPatient), modify)))
            );

	@Test
	public void test1() {
		DataService service = new MyDataService("http://myserver");
		MyDataClient client = new MyDataClient(service);
		client.authenticate("daniel");
		client.addPatient("Mr . Smith");
		client.switchToUser("ruth");
		client.getPatientFile("miller-2143-1");
		client.setPhone("miller-2143-1", "012345678");
		client.exit();
	}

	@Test
	public void test2() {
		DataService service = new MyDataService("http://myserver");
		MyDataClient client = new MyDataClient(service);
		client.authenticate("normann");
		client.addPatient("Mr . Smith");
		client.addPatient("Mr . Smith");
		client.exit();
    }

	@Test
	public void test3() {
		DataService service = new MyDataService("http://myserver");
		MyDataClient client = new MyDataClient(service);
        client.authenticate("normann");
		client.getPatientFile("miller-2143-1");
        client.exit();
	}

	@Test
	public void test4() {
		DataService service = new MyDataService("http://myserver");
		MyDataClient client = new MyDataClient(service);
		client.authenticate("malte");
		client.addPatient("Foo");
		client.addPatient("Foo");
		client.addPatient("Foo");
		client.switchToUser("malte");
		client.addPatient("Foo");
		client.addPatient("Foo");
		client.addPatient("Foo");
		client.addPatient("Foo");
		client.switchToUser("malte");
		client.addPatient("Foo");
		client.addPatient("Foo");
		client.addPatient("Foo");
		client.addPatient("Foo");
		client.addPatient("Foo");
		client.exit();
	}
}
