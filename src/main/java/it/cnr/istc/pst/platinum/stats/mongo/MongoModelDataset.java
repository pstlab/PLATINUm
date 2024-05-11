package it.cnr.istc.pst.platinum.stats.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import it.cnr.istc.pst.platinum.stats.ModelDataset;
import it.cnr.istc.pst.platinum.stats.TokenExecutionData;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class MongoModelDataset implements ModelDataset 
{
	private MongoClient client;				// client connector
	private MongoDatabase db;				// database
	private String dataset;					// selected data-set

	/**
	 * Create a connection to the default DB server
	 * 
	 * @param name
	 * @throws UnknownHostException
	 */
	public MongoModelDataset(String name) 
			throws UnknownHostException 
	{
		// create client
		this.client = new MongoClient();
		// get a reference to the DB
		this.db = this.client.getDatabase(name);
		// set selected data-set
		this.dataset = null;
	}
	
	/**
	 * Create a connection to a DB server  
	 * 
	 * @param host
	 * @parma name 
	 * @throws UnknownHostException
	 */
	public MongoModelDataset(String host, String name) 
			throws UnknownHostException
	{
		// create client connection
		this.client = new MongoClient(host);
		// get a reference to the DB
		this.db = this.client.getDatabase(name);
	}
	
	/**
	 * 
	 */
	@Override
	public void select(String name) {
		this.dataset = name;
	}
	
	/**
	 * 
	 */
	@Override
	public List<TokenExecutionData> getTokenExecutionData() 
	{
		// check if a data-set has been selected
		if (this.dataset == null) {
			throw new RuntimeException("No Dataset selected!");
		}
		
		// get model collections
		MongoCollection<Document> collection = this.db.getCollection(this.dataset);
		// prepare execution data
		List<TokenExecutionData> list = new ArrayList<>();
		
		// get collection elements
		try (MongoCursor<Document> it = collection.find().iterator()) 
		{
			while (it.hasNext()) 
			{
				// get object
				Document obj = it.next();
				
				// create data
				TokenExecutionData data = new TokenExecutionData(
						(Long) obj.get("timestamp"), 
						(String) obj.get("name"), 
						(Long) obj.get("start"), 
						(Long) obj.get("duration"),
						(String) obj.get("component"));
				
				// add data
				list.add(data);
			}
		}
		
		// get the list
		return list;
	}

	
	/**
	 * 
	 */
	@Override
	public List<TokenExecutionData> getTokenExecutionDataByComponent(String componentName) 
	{
		// check if a data-set has been selected
		if (this.dataset == null) {
			throw new RuntimeException("No Dataset selected!");
		}
		
		// get model collection
		MongoCollection<Document> collection = this.db.getCollection(this.dataset);
		// prepare data
		List<TokenExecutionData> list = new ArrayList<>();
		// find objects that satisfy query on nested documents
		try(MongoCursor<Document> it = collection.find(Filters.eq("component", componentName)).iterator()) 
		{
			while (it.hasNext()) 
			{
				// get next object
				Document obj = it.next();
				
				// create data
				TokenExecutionData data = new TokenExecutionData(
						(Long) obj.get("timestamp"), 
						(String) obj.get("name"), 
						(Long) obj.get("start"), 
						(Long) obj.get("duration"), 
						(String) obj.get("component"));
				
				// add data
				list.add(data);
				
			}
		}
		
		// get list of data
		return list;
	}

	/**
	 * 
	 */
	@Override
	public void save(TokenExecutionData token) 
	{
		// check if a data-set has been selected
		if (this.dataset == null) {
			throw new RuntimeException("No Dataset selected!");
		}
		
		// get model collection
		MongoCollection<Document> collection = this.db.getCollection(this.dataset);
		
		
		// create basic object
		Document obj = new Document("_id", token.getTimestamp());
		// append fields
		obj.append("name", token.getName());
		obj.append("timestamp", token.getTimestamp());
		obj.append("start", token.getStart());
		obj.append("duration", token.getDuration());
		obj.append("component", token.getComponent());
		
		// insert data into collection
		collection.insertOne(obj);
	}
	
	/**
	 * 
	 */
	public void clear() 
	{
		// get model collection
		MongoCollection<Document> collection = this.db.getCollection(this.dataset);
		// clear all data of the collection
		collection.drop();
		// clear selected data-set
		this.dataset = null;
	}

}
