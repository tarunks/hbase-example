package com.tarun.mapi;

public class ThriftApp {
	public static void main(String[] args) {
		ThriftClient client=null;
		try {
			client = new ThriftClient();
			client.start();

		} catch (Exception exp) {
			exp.printStackTrace();
		}finally{
			client.stop();
		}
	}
}
