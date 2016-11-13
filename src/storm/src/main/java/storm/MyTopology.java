package storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

public class MyTopology 
{
	public static final Integer SPOUTS = 1; // 20
	public static final Integer BOLTS = 1; // 8
	public static final Integer WORKERS = 2; // 4
	
	public static void main(String[] args)
	{
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("tuples", new TupleSpout(5000), SPOUTS);        
		builder.setBolt("count", new CounterBolt(), BOLTS)
		        .shuffleGrouping("tuples");

		Config conf = new Config();
		conf.setDebug(false);
		conf.setNumWorkers( WORKERS );

		final LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("test", conf, builder.createTopology());
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				cluster.killTopology("test");
				cluster.shutdown();
			}
		});
	}
}
