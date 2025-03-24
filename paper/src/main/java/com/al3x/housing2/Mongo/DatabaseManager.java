package com.al3x.housing2.Mongo;

import com.al3x.housing2.Mongo.Collection.HousesCollection;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class DatabaseManager {
    private List<AbstractCollection<?>> collections = new ArrayList<>();

    private String uri;
    private String database;

    public DatabaseManager(String uri, String database) {
        this.uri = uri;
        this.database = database;

        initDB();
        startup();
    }

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public void initDB() {
        collections.add(new HousesCollection(this));
    }

    public void startup() {
        mongoClient = MongoClients.create(uri);

        // Register all pojo codecs
        List<PojoCodecProvider> pojoCodecProviders = new ArrayList<>();
        for (AbstractCollection<?> collection : collections) {
            pojoCodecProviders.add(PojoCodecProvider.builder().register(collection.getClazz()).automatic(true).build());
        }

        // Create codec registry and apply to database
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProviders));
        mongoDatabase = mongoClient.getDatabase(database).withCodecRegistry(pojoCodecRegistry);

        collections.forEach(AbstractCollection::startup);
    }

    public void shutdown() {
        mongoClient.close();
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
}
