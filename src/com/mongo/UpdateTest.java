package com.mongo;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongo.Helpers.printJson;

public class UpdateTest{
	public static void main(String[] args){
		MongoClient client = new MongoClient();
		MongoDatabase database = client.getDatabase("course");
		MongoCollection<Document> collection = database.getCollection("test");

		collection.drop();

		for (int i=0;i<8;i++){
			collection.insertOne(new Document()
						.append("_id", i)
						.append("x",i)
						.append("y", true));
		}

		for (Document cur : collection.find().into(new ArrayList<Document>())) {
			printJson(cur);
		}
	}
}