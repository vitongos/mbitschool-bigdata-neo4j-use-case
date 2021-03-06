package storm;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class TupleSpout extends BaseRichSpout
{
	private static final long serialVersionUID = 1L;
	
    private SpoutOutputCollector collector;
    static Socket clientSocket;
    static Socket serverSocket;
    static int port;
    static String host;

    public TupleSpout(int p)
    {
    	port = p;
    	host = "localhost";
    }

    @SuppressWarnings("rawtypes")
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector)
    {
    	this.collector = collector;
	    try {
	    	clientSocket = new Socket();
            try {
            	clientSocket.connect(new InetSocketAddress(host , port));
            } catch (UnknownHostException e) {
            	e.printStackTrace();
            }
	    }
        catch (IOException e) {
			e.printStackTrace();
		}
    }   

    public void nextTuple()
    {
    	try {
    	   InputStream inputStream = clientSocket.getInputStream();
    	   byte[] b = new byte[4096];
    	   inputStream.read(b);
    	   List<Values> values = parseInput(b);
    	   for (Values value: values)
    	   {
    		   this.collector.emit(value);
    	   }
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    protected List<Values> parseInput(byte[] b) throws UnsupportedEncodingException
    {
    	String input = new String(b, "UTF-8");
    	String[] tuples = input.split("\n");
    	List<Values> values = new ArrayList<Values>();
    	for (String s: tuples)
    	{
    		String[] pair = s.split(",");
	    	if ((pair.length == 3) && (pair[0].equals("p") || pair[0].equals("c") || pair[0].equals("i")))
	    	{
	    		try {
	    			values.add(new Values(pair[0], Integer.parseInt(pair[1]), Integer.parseInt(pair[2])));
	    		} catch (NumberFormatException e) { 
	    			// do nothing
	    		}
	    	}
    	}
	    return values;
    }

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("event", "source", "target"));
	}
}
