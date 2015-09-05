package com.tarun.myapp;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class HbaseClient{

	private HTableInterface hTable;

	public HbaseClient(){
		 try
	        {
        	    Configuration conf = HBaseConfiguration.create();
        	    
        	    HBaseAdmin admin = new HBaseAdmin(conf);
        	    if(!admin.isTableAvailable("employee")){
        	    HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf("employee"));
                tableDescriptor.addFamily(new HColumnDescriptor("info"));
                admin.createTable(tableDescriptor);
                boolean tableAvailable = admin.isTableAvailable("employee");
                       	    
                System.out.println("tableAvailable = " + tableAvailable);
        	    }
	            hTable = new HTable( conf, "employee");
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }

	}
	 public void close()
	 {
	        try

	        {
        	    hTable.close();
	        }
	        catch (IOException e)
	        {
        	    e.printStackTrace();
	        }
	    }
	public void put(Employee emp){
		Put put=new Put(Bytes.toBytes(emp.getId()));

		put.add(Bytes.toBytes("info"),
			Bytes.toBytes("id"),
			Bytes.toBytes(emp.getId()));
		put.add(Bytes.toBytes("info"),
                        Bytes.toBytes("name"),
                        Bytes.toBytes(emp.getName()));
		put.add(Bytes.toBytes("info"),
                        Bytes.toBytes("address"),
                        Bytes.toBytes(emp.getAddress()));

		try{

			hTable.put(put);
		}
		catch(IOException e){
			e.printStackTrace();
	
		}

	}

	public Employee get(String id){

		Get get=new Get(Bytes.toBytes(id));
		Employee emp=null;
		Result result;
		try {
			result = hTable.get(get);
			byte[] btsId=result.getValue(Bytes.toBytes("info"),Bytes.toBytes("id"));
			byte[] btsName=result.getValue(Bytes.toBytes("info"),Bytes.toBytes("name"));
			byte[] btsAddress=result.getValue(Bytes.toBytes("info"),Bytes.toBytes("address"));
			
			emp=new Employee(Bytes.toString(btsId),Bytes.toString(btsName),Bytes.toString(btsAddress));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
		return emp;
	}

	public List<Employee> scan(){

		return null;
	}
	public void delete(String id){


	}


}
