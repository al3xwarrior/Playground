package com.al3x.housing2.Mongo;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCollection <T> {
    private final String collectionName;
    protected final DatabaseManager databaseManager;

    protected MongoCollection<T> collection;

    public AbstractCollection(String collectionName, DatabaseManager databaseManager) {
        this.collectionName = collectionName;
        this.databaseManager = databaseManager;
    }

    public MongoCollection<T> startup() {
        return collection = databaseManager.getMongoDatabase().getCollection(getCollectionName(), getClazz());
    }

    public T insertOne(T t) {
        collection.insertOne(t);
        return t;
    }

    abstract public T updateOne(T t);

    abstract public void deleteOne(T t);

    public T findOne(Document document) {
        return collection.find(document).first();
    }

    public T findOne(Bson document) {
        return collection.find(document).first();
    }

    public T findMany(Document document) {
        return collection.find(document).first();
    }

    public List<T> all() {
        return collection.find().into(new ArrayList<>());
    }

    protected abstract void migrate();

    protected abstract Class<T> getClazz();

    public String getCollectionName() {
        return collectionName;
    }
}
