package beanstalk;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map.Entry;

import com.dinstone.beanstalkc.BeanstalkClientFactory;
import com.dinstone.beanstalkc.Configuration;
import com.dinstone.beanstalkc.Job;
import com.dinstone.beanstalkc.JobConsumer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.neo4j.driver.v1.*;

public class Worker 
{
	public static void main(String[] args) 
	{
		Configuration config = new Configuration();
	    config.setServiceHost("127.0.0.1");
	    config.setServicePort(11300);

	    // create job producer and consumer
	    BeanstalkClientFactory factory = new BeanstalkClientFactory(config);
	    JobConsumer consumer = factory.createJobConsumer("count-tube");

	    Job job = consumer.reserveJob(0);
	    // System.out.println("Job Reserved");
	    Type counterType = new TypeToken<HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>>>() {}.getType();
	    while (job != null) {
	    	long startTime = System.nanoTime();
	    	System.out.println("Consuming Job");
	    	byte[] bytes = job.getData();
		    String s = new String(bytes, StandardCharsets.UTF_8);
		    consumer.deleteJob(job.getId());
		    // System.out.println("Job Deleted");
		    
		    Gson gson = new Gson();
		    HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> counterCollection = gson.fromJson(s, counterType);
		    
    	    Integer i = 0;
		    if (counterCollection.size() > 0)
		    {
		    	Driver driver = GraphDatabase.driver( "bolt://localhost" );
		    	try (Session session = driver.session())
		        {
		            // Wrapping Cypher in an explicit transaction provides atomicity
		            // and makes handling errors much easier.
		            try (Transaction tx = session.beginTransaction())
		            {
		            	for (Entry<Integer, HashMap<Integer, HashMap<String, Integer>>> sourceEntry: counterCollection.entrySet())
				    	{
					    	Integer source = sourceEntry.getKey();
					    	for (Entry<Integer, HashMap<String, Integer>> targetEntry: sourceEntry.getValue().entrySet())
					    	{
					    		Integer target = targetEntry.getKey();
					    		HashMap<String, Integer> count = targetEntry.getValue();
					    		incrementCounter(source, target, count, tx);
					    		i++;
					    	}
				    	}
		                tx.success();
		            }
		        }
		    	driver.close();
		    }
		    System.out.println("Updated " + i + " tuples in Neo4j");
		    long elapsedTime = System.nanoTime() - startTime;
		    System.out.println("Job Processed in " + elapsedTime/1000000000 + " seconds");
		    // System.out.println("Reserving Job");
			job = consumer.reserveJob(0);
		    // System.out.println("Job Reserved");
	    }
	    consumer.close();
	    System.out.println("Terminated");
	}
	
	private static void incrementCounter(Integer source, Integer target, HashMap<String, Integer> count, Transaction transaction)
	{
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Integer> targetEntry: count.entrySet())
    	{
    		String field = targetEntry.getKey();
    		if (field.equals("p") || field.equals("c") || field.equals("i"))
    		{
    			Integer c = targetEntry.getValue();
        		if (c > 0)
        		{
            		if (sb.length() > 0) {
            			sb.append(", ");
            		}
            		sb.append("r.");
            		sb.append(field);
            		sb.append(" = r.");
            		sb.append(field);
            		sb.append(" + ");
            		sb.append(c);
        		}
    		}
    	}
		if (sb.length() > 0) {
			String query = "MATCH (n:APP { id : {source} })-[r]->(m:APP { id : {target} }) SET " + sb.toString();
			transaction.run(query, Values.parameters( "source", source, "target", target ));
		}
	}
}
