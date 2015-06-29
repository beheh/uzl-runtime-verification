package junitrv_examples;

import java.util.Random;

public class MyDataService implements DataService {
	MyDataService(String uri) {
	}

	@Override
	public void connect(String userID) {
		System.out.println(userID + " connected.");
	}

	@Override
	public void disconnect() {
		System.out.println("Disconnected");
	}

	@Override
	public String readData(String field) {
		return "data";
	}

	@Override
	public void modifyData(String field, String data) {
		System.out.println("Data modified");
	}

	private final Random failCommit = new Random();

	@Override
	public void commit() throws Exception {
		// Fail randomly
		if (failCommit.nextBoolean()) {
			throw new Exception("Commit failed.");
		} else {
			System.out.println("Committed");
		}
	}
}
