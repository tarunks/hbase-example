package com.tarun.myapp;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.SortedMap;

//import org.apache.hadoop.hbase.thrift.generated.AlreadyExists;
//import org.apache.hadoop.hbase.thrift.generated.ColumnDescriptor;
//import org.apache.hadoop.hbase.thrift.generated.Hbase;
//import org.apache.hadoop.hbase.thrift.generated.IOError;
//import org.apache.hadoop.hbase.thrift.generated.IllegalArgument;
//import org.apache.hadoop.hbase.thrift.generated.Mutation;
//import org.apache.hadoop.hbase.thrift.generated.NotFound;
//import org.apache.hadoop.hbase.thrift.generated.TCell;
//import org.apache.hadoop.hbase.thrift.generated.TRowResult;


import org.apache.hadoop.hbase.thrift2.generated.TColumnValue;
import org.apache.hadoop.hbase.thrift2.generated.TGet;
import org.apache.hadoop.hbase.thrift2.generated.THBaseService;
import org.apache.hadoop.hbase.thrift2.generated.TPut;
import org.apache.hadoop.hbase.thrift2.generated.TResult;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSaslClientTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;



public class ThriftClient {

	protected int port = 9090;
	  CharsetDecoder decoder = null;
	  TTransport transport =null;
	  public ThriftClient(){
		  decoder = Charset.forName("UTF-8").newDecoder();
	  }
	  public void start() throws Exception {
		 
		    transport = new TSocket("localhost", port);
		    TProtocol protocol = new TBinaryProtocol(transport, true, true);
		    THBaseService.Iface client = new THBaseService.Client(protocol);

		    transport.open();
		    
		    ByteBuffer table = ByteBuffer.wrap("employee".getBytes());
		    
		    
		   TPut put=getTableValue();

		    client.put(table, put);

		
		    TGet get = new TGet();
		    get.setRow("1".getBytes());

		    TResult result = client.get(table, get);

		    System.out.print("row = " + new String(result.getRow()));
		    for (TColumnValue resultColumnValue : result.getColumnValues()) {
		      System.out.println("family = " + new String(resultColumnValue.getFamily()));
		      System.out.println("qualifier = " + new String(resultColumnValue.getFamily()));
		      System.out.println("value = " + new String(resultColumnValue.getValue()));
		      System.out.println("timestamp = " + resultColumnValue.getTimestamp());
		      System.out.println("**************");
		    }

	  }
	  
	  public void stop(){
		  if(transport!=null){
			transport.close();
			System.out.println("close");
			
		  }
	  }
	  
	  private TPut getTableValue(){
		  List<TColumnValue> columnValues = new ArrayList<TColumnValue>();
		  TPut put = new TPut();
		    put.setRow("1".getBytes());

		    TColumnValue columnValue = new TColumnValue();
		    columnValue.setFamily("info".getBytes());
		    columnValue.setQualifier("id".getBytes());
		    columnValue.setValue("1".getBytes());
		    
		    columnValues.add(columnValue);
		    
		    columnValue = new TColumnValue();
		    columnValue.setFamily("info".getBytes());
		    columnValue.setQualifier("name".getBytes());
		    columnValue.setValue("Tarun".getBytes());
		    columnValues.add(columnValue);
		    
		    columnValue = new TColumnValue();
		    columnValue.setFamily("info".getBytes());
		    columnValue.setQualifier("address".getBytes());
		    columnValue.setValue("C-3 sector 125 Noida".getBytes());
		    columnValues.add(columnValue);
		    
		    
		    put.setColumnValues(columnValues);
		    
		    return put;
	  }
	  private String byteBufferToString(ByteBuffer buffer) {
			CharBuffer charBuffer = null;
			try {
				Charset charset = Charset.forName("UTF-8");
				CharsetDecoder decoder = charset.newDecoder();
				charBuffer = decoder.decode(buffer);
				buffer.flip();
				return charBuffer.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		private ByteBuffer getByteBuffer(String str) {
			return ByteBuffer.wrap(str.getBytes());
		}
	// Helper to translate byte[]'s to UTF8 strings
	  private String utf8(byte[] buf) {
	    try {
	      return decoder.decode(ByteBuffer.wrap(buf)).toString();
	    } catch (CharacterCodingException e) {
	      return "[INVALID UTF-8]";
	    }
	  }
	  
	  // Helper to translate strings to UTF8 bytes
	  private byte[] bytes(String s) {
	    try {
	      return s.getBytes("UTF-8");
	    } catch (UnsupportedEncodingException e) {
	      e.printStackTrace();
	      return null;
	    }
	  }
	  
}
