package demo;

import java.util.Map;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class MyBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;

	OutputCollector _collector;

	@SuppressWarnings("rawtypes")
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) 
	{
        _collector = collector;
    }

    public void execute(Tuple tuple) 
    {
        _collector.emit(tuple, new Values(tuple.getInteger(0).toString() + "!!!"));
        _collector.ack(tuple);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) 
    {
        declarer.declare(new Fields("word"));
    }    
}