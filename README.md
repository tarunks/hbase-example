# HBASE sample
## HDFS and HBASE setup instruction

This setup instruction is for Ubuntu Linux
 
##### Reqired software

1. Java 1.7 must be installed 
2. SSH must be installed and SSHD must be running to use the Hadoop scripts that manages remote Hadoop demons

```

	# To install ssh and rsync
	$ sudo apt-get install ssh
	$ sudo apt-get install rsync

```

#### Download

To get a Hadoop distribution, download a recent stable release from one of the [Apache Hadoop Mirrors](http://www.apache.org/dyn/closer.cgi/hadoop/common/)
This example uses Hadoop-2.7.1 and Hbase 1.1.1
To get the Hbase distribution, download form [Apache Hbase Mirror](http://www.apache.org/dyn/closer.cgi/hbase/)
Extract the .tar.gz file 

```

	$ tar -xzf hadoop-2.7.1.tar.gz
	$ sudo mv hadoop-2.7.1 /usr/local/hadoop 
	$ tar -xzf hbase-1.1.1-bin.tar.gz
	$ sudo mv hbase-1.1.1 /usr/local/hbase 

``` 

#### Setup hadoop and hbase  

1. Create user group and user
2. Setup hadoop and hbase home

```

	# create user group and create user
	$ sudo addgroup hadoop
	$ sudo adduser hduser --ingroup hadoop
	# Provide sudo access to hduser
	$ sudo adduser hduser sudo
	$ su hduser
	# change the owner of hadoop and hbase location
	$ sudo chown -R hduser:hadoop /usr/local/hadoop
	$ sudo chown -R hduser:hadoop /usr/local/hbase
	# Set HADOOP_HOME and PATH, open ~/.bahrc file
	$ nano ~/.bashrc
	# Add this following lines at the end of file
		#HADOOP VARIABLES START
		export JAVA_HOME=/opt/Oracle_Java/jdk1.7.0_75/
		export PATH=$PATH:$JAVA_HOME/bin
	
		export HADOOP_HOME=/usr/local/hadoop 
		export HBASE_HOME=/usr/local/hbase
		export HADOOP_MAPRED_HOME=$HADOOP_HOME 
		export HADOOP_COMMON_HOME=$HADOOP_HOME 
		export HADOOP_HDFS_HOME=$HADOOP_HOME 
		export YARN_HOME=$HADOOP_HOME 
		export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native 
		export PATH=$PATH:$HADOOP_HOME/sbin:$HADOOP_HOME/bin:$HBASE_HOME/bin
		export HADOOP_INSTALL=$HADOOP_HOME 
		#HADOOP VARIABLES END
	 
```
 
#### Prepare to setup hadoop single node cluster

1. Make changes in hadoop-env.sh and hbase-env.shfile

```

	# setup java home path in hadoop-env.sh
	$ nano /usr/local/hadoop/etc/hadoop/hadoop-env.sh
	# change the java home
	export JAVA_HOME=/opt/Oracle_Java/jdk1.7.0_75/
	# setup java home path in hbase-env.sh
	$ nano /usr/local/hbase/hbase-env.sh
	# change the java home
	export JAVA_HOME=/opt/Oracle_Java/jdk1.7.0_75/
```
2. Create name node and datanode storage location
3. Make chnges in hdfs-site.xml file

```

	$ mkdir -p /home/hduser/hadoop_store/hdfs/namenode
	$ mkdir -p /home/hduser/hadoop_store/hdfs/datanode
	#Changes in hdfs-site.xml
	$ nano /usr/local/hadoop/etc/hadoop/hdfs-site.xml
	# Changes in configuration section
		<configuration>
		<property>
		  <name>dfs.replication</name>
		  <value>1</value>
		 </property>
		 <property>
		   <name>dfs.namenode.name.dir</name>
		   <value>file:///home/hduser/hadoop_store/hdfs/namenode</value>
		 </property>
		 <property>
		   <name>dfs.datanode.data.dir</name>
		   <value>file:///home/hduser/hadoop_store/hdfs/datanode</value>
		 </property>
		</configuration>

```
4. Changes in hbase-site.xml

```

	$ nano /usr/local/hbase/conf/hbase-site.xml
	  <configuration>
		<property>
		    <name>hbase.rootdir</name>
		    <value>file:///home/hduser/hbase</value>
		 </property>
		 <property>
		    <name>hbase.zookeeper.property.dataDir</name>
		    <value>/home/hduser/zookeeper</value>
		  </property>
		</configuration>
	

```
5. Start hdfs and hbase

```

	# To Start hdfs
	$ start-dfs.sh
	# To Stop hdfs
	$ stop-dfs.sh
	# To start hbase
	$ start-hbase.sh
	# To stop hbase
	$ stop-hbase.sh
	
```
6. Run hbase shell

    $ hbase shell
    hbase(main):001:0>
   
7. Create a table

    hbase(main):001:0> create 'test_table','column_family'


## Hadoop pseduo distributed mode

1. Change in etc/hadoop/core-site.xml

```

	<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:9000</value>
    </property>
   </configuration>
   
```
2. Change in etc/hadoop/hdfs-site.xml

```

	<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
   </configuration>
```

3. Setup passphraseless ssh

If you cannot ssh to localhost without a passphrase, execute the following commands:

```

	$ ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa
	$ cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys
	$ export HADOOP\_PREFIX=/usr/local/hadoop
	$ ssh localhost
```
4. Stop hbase and configure it for pseudo distributed mode
Changes in hbase-site.xml

```

	<property>
  		<name>hbase.cluster.distributed</name>
	    <value>true</value>
	</property>
	<property>
  		<name>hbase.rootdir</name>
  		<value>hdfs://localhost:9000/hbase</value>
	</property>	
```

## Execution of hdfs

1. Format the file system

```

	$ hdfs namenode -format
```
2. Start name node and data node

```

	$ start-dfs.sh
```
The hadoop daemon log output is written to the $HADOOP_LOG_DIR directory (defaults to $HADOOP_HOME/logs).

3. Browse the web interface for the NameNode; by default it is available at:
	
	http://localhost:50070/

4. Create hbase direstory

	$ hdfs dfs -mkdir /hbase
	$ hdfs dfs -chown hduser:hadoop /hbase

5. Run hbase shell

    $ hbase shell
    hbase(main):001:0>
   
6. Create a table

    hbase(main):001:0> create 'test_table','column_family'

## Connect hbase by thrift

##### What is Thrift?

"Thrift is a software framework for scalable cross-language services development. It combines a software stack with a code generation engine to build services that work efficiently and seamlessly between C++, Java, Python, PHP, Ruby, Erlang, Perl, Haskell, C#, Cocoa, Smalltalk, and OCaml."

##### Install thrift

1. Download thrift [Thrift Download](http://www.apache.org/dyn/closer.cgi?path=/thrift/0.9.2/thrift-0.9.2.tar.gz)
2. Extract tar file and install
	
	$ tar -xzf thrift-0.9.2.tar.gz
	$ mv thrift-0.9.2 /tmp/thrift
	$ sudo apt-get install libboost-dev libboost-test-dev libboost-program-options-dev libboost-system-dev libboost-filesystem-dev libevent-dev automake libtool flex bison pkg-config g++ libssl-dev
	$ cd /tmp/thrift/
	$ ./configure
	$ make
	$ make install

3. Run hbase thrift client

	$ hbase thrift2 start
	