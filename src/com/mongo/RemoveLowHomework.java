package com.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import static com.mongo.Helpers.printJson;

public class RemoveLowHomework {
	public static void main(String[] args) {
		MongoClient client = new MongoClient();
		MongoDatabase database = client.getDatabase("school");
		MongoCollection<Document> collection = database.getCollection("students");

		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				Document cur = cursor.next();
				//printJson(cur);
				Integer id = (Integer) cur.get("_id");
				List<Document> scores = (List<Document>) cur.get("scores");
				System.out.println("_id="+id+", scores="+scores.size()+":");
				
				Double lowHomeWork = -1.0;
				int lowHWDocument = 0; 
				
				for (int i=0;i<scores.size();i++){
					Document score = (Document) scores.get(i);
					System.out.println("   Type: "+score.get("type")+", Score: "+ score.get("score"));
					if (score.get("type").equals("homework")){
						Double scoreValue = (Double) score.get("score");
						if (lowHomeWork == -1.0){
							lowHomeWork = scoreValue;
							lowHWDocument = i;
						} else {
							if (scoreValue<lowHomeWork){
								lowHomeWork = scoreValue;
								lowHWDocument = i;
							}
						}
					}
				}
				System.out.println("     _id:"+ id +", low Homework Document: " + lowHWDocument + ", score: "+lowHomeWork);
				// Remove lowest homework subdocument
				scores.remove(lowHWDocument);
				// Remove _id prior to a replace method
				cur.remove("_id");
				
				// Console confirmation
				printJson(cur);
				
				//collection.updateOne(Filters.eq("_id",id),cur);
				collection.replaceOne(Filters.eq("_id",id),cur);
				
			}
		} finally {
			cursor.close();
		}
	}
}