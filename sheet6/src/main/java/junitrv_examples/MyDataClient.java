package junitrv_examples;

public class MyDataClient {

	private final DataService dataService;

	public MyDataClient(DataService service) {
		this.dataService = service;
	}

	public void authenticate(String user) {
		dataService.connect(user);
		dataService.modifyData("session", "initialized");
	}

	public void addPatient(String name) {
        dataService.modifyData(name, null);
	}

	public void switchToUser(String user) {
        exit();
		dataService.connect(user);
	}

	public void setPhone(String id, String number) {
		dataService.modifyData(id, "phone:" + number);
	}

	public String getPatientFile(String id) {
		return dataService.readData(id);
	}

	public void exit() {

		Boolean success = false;
		dataService.modifyData("session", "closing");

		while (!success) {
			try {
				dataService.commit();
				success = true;
			} catch (Exception e) {
				// try again
			}
		}

		dataService.disconnect();
	};
}
