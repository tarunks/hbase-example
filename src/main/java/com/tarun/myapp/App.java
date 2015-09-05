package com.tarun.myapp;


public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello Hbase World!" );
        HbaseClient client=new HbaseClient();
        client.put(new Employee("1","Tarun","C-3 Secor 12 Noida"));
        client.put(new Employee("2","Varun","C-2 Secor 12 Noida"));
        client.put(new Employee("3","Aarun","C-1 Secor 12 Noida"));
        
        System.out.println(client.get("1"));
        System.out.println(client.get("2"));
        System.out.println(client.get("3"));
    }
}
