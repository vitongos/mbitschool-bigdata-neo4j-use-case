package beanstalk;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import com.dinstone.beanstalkc.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.*;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;

public class MainClass 
{
	private static final String SERVER_ROOT_URI = "http://localhost:7474/db/data/";
	//private static String commitUrl = "";
	private static String transactionUri = "";
	
	public static void main(String[] args) 
	{
		Configuration config = new Configuration();
	    config.setServiceHost("127.0.0.1");
	    config.setServicePort(11300);

	    // create job producer and consumer
	    BeanstalkClientFactory factory = new BeanstalkClientFactory(config);
	    JobConsumer consumer = factory.createJobConsumer("count-tube");

	    Job job = consumer.reserveJob(0);
	    Type counterType = new TypeToken<HashMap<Integer, HashMap<Integer, Integer>>>() {}.getType();
	    while (job != null) {
	    	byte[] bytes = job.getData();
		    String s = new String(bytes, StandardCharsets.UTF_8);
		    consumer.deleteJob(job.getId());
		    
		    Gson gson = new Gson();
		    HashMap<Integer, HashMap<Integer, Integer>> counterCollection = gson.fromJson(s, counterType);
		    
		    if (counterCollection.size() > 0)
		    {
		    	try {
			    	startTransaction();
				    for (Entry<Integer, HashMap<Integer, Integer>> sourceEntry: counterCollection.entrySet())
			    	{
				    	Integer source = sourceEntry.getKey();
				    	for (Entry<Integer, Integer> targetEntry: sourceEntry.getValue().entrySet())
				    	{
				    		Integer target = targetEntry.getKey();
				    		Integer count = targetEntry.getValue();
				    		incrementCounter(source, target, count);
				    	}
			    	}
				    commitTransaction();
		    	} catch (Exception e) {
		    		rollbackTransaction();
		    	}
		    	
		    }
	
			System.out.println(s);
			job = consumer.reserveJob(0);
	    }
	    consumer.close();
	    System.out.println("Terminated");
	    
	}
	
	private static void startTransaction()
	{
		final String txUri = SERVER_ROOT_URI + "transaction";
		WebResource resource = Client.create().resource( txUri );
		String payload = "{\"statements\" : [ ]}";
		ClientResponse response = resource
		        .accept( MediaType.APPLICATION_JSON )
		        .type( MediaType.APPLICATION_JSON )
		        .entity( payload )
		        .post( ClientResponse.class );
		transactionUri = response.getLocation().toString();
	}
	
	private static void commitTransaction()
	{
		final String txUri = transactionUri + "/commit";
		WebResource resource = Client.create().resource( txUri );
		ClientResponse response = resource
		        .accept( MediaType.APPLICATION_JSON )
		        .type( MediaType.APPLICATION_JSON )
		        .entity( "" )
		        .post( ClientResponse.class );
		System.out.println( String.format(
		        "POST [%s] to [%s], status code [%d], returned data: "
		                + System.getProperty( "line.separator" ) + "%s",
		        "", transactionUri, response.getStatus(),
		        response.getEntity( String.class ) ) );
	}
	
	private static void rollbackTransaction()
	{
		WebResource resource = Client.create().resource( transactionUri );
		resource
		        .accept( MediaType.APPLICATION_JSON )
		        .type( MediaType.APPLICATION_JSON )
		        .entity( "" )
		        .delete( ClientResponse.class );
	}
	
	private static void incrementCounter(Integer source, Integer target, Integer count)
	{
		//System.out.println("Source: " + source + ", Target: " + target + " => " + count);
		WebResource resource = Client.create().resource( transactionUri );
		
		String query = "MATCH (n:APP { id : " + source + " })-[r]->(m:APP { id : " + target + " }) SET r.c = r.c + " + count + "";
		String payload = "{\"statements\" : [ {\"statement\" : \"" + query + "\"} ] }";
		resource
		        .accept( MediaType.APPLICATION_JSON )
		        .type( MediaType.APPLICATION_JSON )
		        .entity( payload )
		        .post( ClientResponse.class );
	}
}
