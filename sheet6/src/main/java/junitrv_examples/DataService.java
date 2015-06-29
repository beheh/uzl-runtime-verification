package junitrv_examples;

public interface DataService {
	void connect(String userID);

	void disconnect();

	String readData(String field);

	void modifyData(String field, String data);

	void commit() throws Exception;
}
