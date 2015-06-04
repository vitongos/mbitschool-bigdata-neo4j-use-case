package spark;

import java.util.*;

public class CypherResponse 
{
	public List<Result> results;
	
	class Result {
		public List<String> columns;
		public List<Row> data;
		
		public List<Integer> getAppIds()
		{
			List<Integer> list = new ArrayList<Integer>();
			for (Row row: data)
			{
				Integer appId = Integer.parseInt(row.row.get(0));
				list.add(appId);
			}
			return list;
		}
	}
	
	class Row {
		public List<String> row;
	}
}
