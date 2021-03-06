package storm;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import com.dinstone.beanstalkc.BeanstalkClientFactory;
import com.dinstone.beanstalkc.Configuration;
import com.dinstone.beanstalkc.JobProducer;
import com.google.gson.Gson;

public class CounterBolt extends BaseRichBolt 
{
	private static final long serialVersionUID = 1L;
	private static final long REPORT_BOUND = 500000; 
	private OutputCollector collector;
	private HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> counter;
	private Integer reporting = 0;
	JobProducer producer;

	@SuppressWarnings("rawtypes")
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) 
	{
		Configuration config = new Configuration();
	    config.setServiceHost("127.0.0.1");
	    config.setServicePort(11300);

	    // create job producer and consumer
	    BeanstalkClientFactory factory = new BeanstalkClientFactory(config);
	    this.producer = factory.createJobProducer("count-tube");
	    
		this.counter = new HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>>();
        this.collector = collector;
    }

    public void execute(Tuple tuple) 
    {
    	String event = tuple.getString(0);
    	Integer source = tuple.getInteger(1);
    	Integer target = tuple.getInteger(2);
    	if (source > 0 && target > 0 && source != target)
    	{
    		countTuple(event, source, target);
    	}
    	if (isReporting())
    	{
    		cleanup();
    	}
    	else
    	{
    		this.collector.ack(tuple);
    	}
    }
    
    public void cleanup() 
    {
    	Gson gson = new Gson();
    	String json = gson.toJson(this.counter);
    	producer.putJob(0, 0, 0, json.getBytes());
		System.out.println(json);
		this.counter.clear();
	} 
    
    private boolean isReporting()
    {
    	if (reporting++ > REPORT_BOUND)
    	{
    		reporting = 0;
    		return true;
    	}
    	return false;
    }
    
    private void countTuple(String event, Integer source, Integer target)
    {
    	if (!this.counter.containsKey(source))
    	{
    		this.counter.put(source, new HashMap<Integer, HashMap<String, Integer>>());
    	}
    	HashMap<Integer, HashMap<String, Integer>> sourcesCount = this.counter.get(source);
    	HashMap<String, Integer> eventCount = null;
    	if (!sourcesCount.containsKey(target))
    	{
    		eventCount = new HashMap<String, Integer>();
    		eventCount.put(event, 1);
    		sourcesCount.put(target, eventCount);
    	}
    	else
    	{
    		eventCount = sourcesCount.get(target);
    		if (!eventCount.containsKey(event))
    		{
    			eventCount.put(event, 1);
    		}
    		else
    		{
    			eventCount.put(event, eventCount.get(event) + 1);
    		}
    		sourcesCount.put(target, eventCount);
    	}
    }
    
    public void declareOutputFields(OutputFieldsDeclarer declarer) 
    {
        declarer.declare(new Fields("word"));
    }    
}