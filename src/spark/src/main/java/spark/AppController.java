package spark;

import static spark.Spark.get;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

import javax.ws.rs.core.MediaType;

import net.spy.memcached.MemcachedClient;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class AppController 
{
	private final Configuration cfg;
	private static final String SERVER_ROOT_URI = "http://localhost:7474/db/data/";
	
	public static void main(String[] args) throws IOException 
	{
		new AppController();
    }

    public AppController() throws IOException 
    {
        cfg = createFreemarkerConfiguration();
        initializeRoutes();
    }
    
    abstract class FreemarkerBasedRoute implements Route 
    {
        final Template template;

        protected FreemarkerBasedRoute(final String templateName) throws IOException 
        {
            template = cfg.getTemplate(templateName);
        }

        public Object handle(Request request, Response response) 
        {
            StringWriter writer = new StringWriter();
            try {
                doHandle(request, response, writer);
            } catch (Exception e) {
                e.printStackTrace();
                response.redirect("/internal_error");
            }
            return writer;
        }

        protected abstract void doHandle(final Request request, final Response response, final Writer writer)
                throws IOException, TemplateException;

    }
    
    private void initializeRoutes() throws IOException 
    {
        get("/", new FreemarkerBasedRoute("web.ftl") {
            @Override
            public void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException 
            {
            	renderRelated(1, template, writer);
            }
        });
        
        get("/app/:app", new FreemarkerBasedRoute("web.ftl") {
            @Override
            public void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException 
            {
            	Integer appId = Integer.parseInt(request.params(":app"));
            	renderRelated(appId, template, writer);
            }
        });
    }
    
    private void renderRelated(Integer appId, Template template, Writer writer) throws TemplateException, IOException
    {
    	List<Integer> apps = getRelated(appId, 4);
        SimpleHash params = new SimpleHash();
        params.put("appId", appId);
        params.put("apps", apps);
        template.process(params, writer);
    }
    
    @SuppressWarnings("unchecked")
	private List<Integer> getRelated(Integer source, Integer count) throws IOException
    {
    	InetSocketAddress ia = new InetSocketAddress("localhost", 11211);
    	MemcachedClient c = new MemcachedClient(ia);
    	Object list = c.get(source.toString());
    	if (list == null)
    	{
    		list = getRelatedRemote(source, count);
    		c.set(source.toString(), 0, list);
    	}
    	return (List<Integer>) list;
    }
    
    private List<Integer> getRelatedRemote(Integer source, Integer count)
    {
    	final String txUri = SERVER_ROOT_URI + "transaction/commit";
		WebResource resource = Client.create().resource( txUri );
		
		String query = "MATCH (m:APP {id:" + source + "})-[r]->(n) RETURN n.id as id ORDER BY r.c DESC LIMIT " + count + "";
		String payload = "{\"statements\" : [ {\"statement\" : \"" + query + "\" } ] }";
		ClientResponse response = resource
		        .accept( MediaType.APPLICATION_JSON )
		        .type( MediaType.APPLICATION_JSON )
		        .entity( payload )
		        .post( ClientResponse.class );
		String s = response.getEntity( String.class );
		Gson gson = new Gson();
		CypherResponse result = gson.fromJson(s, CypherResponse.class);
		
		System.out.println( String.format(
		        "POST [%s] to [%s], status code [%d], returned data: "
		                + System.getProperty( "line.separator" ) + "%s",
		        payload, txUri, response.getStatus(), s ) );
		
    	return result.results.get(0).getAppIds();
    }
        
    private Configuration createFreemarkerConfiguration() 
    {
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(AppController.class, "/");
        return retVal;
    }
}
