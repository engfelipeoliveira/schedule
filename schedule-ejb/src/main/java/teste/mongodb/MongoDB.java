package teste.mongodb;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;

public class MongoDB {

	
	public static void main(String[] args) {
		MongoClient mongo = new MongoClient("localhost");
		Datastore datastore = new Morphia().createDatastore(mongo, "bandmanager");
		
		Band band = new Band();
		band.setName("Love Burger");
		band.setGenre("Rock");
		
		//datastore.save(band);
		
		Query query = datastore.createQuery(Band.class).field("name").contains("urger");
		Band cons = (Band) query.asList().get(0);
		
		System.out.println(cons);
		
	}
	
}
