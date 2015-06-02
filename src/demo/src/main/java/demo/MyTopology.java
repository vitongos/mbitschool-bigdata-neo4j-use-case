package demo;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class MyTopology {
	public static void main(String[] args)
	{
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("pairs", new PairSpout(5000), 1);        
		builder.setBolt("count", new MyBolt(), 1)
		        .shuffleGrouping("pairs");

		Config conf = new Config();
		conf.setDebug(true);
		conf.setNumWorkers(2);

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
