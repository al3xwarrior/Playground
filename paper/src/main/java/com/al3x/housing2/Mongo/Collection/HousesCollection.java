package com.al3x.housing2.Mongo.Collection;

import com.al3x.housing2.Data.*;
import com.al3x.housing2.Mongo.AbstractCollection;
import com.al3x.housing2.Mongo.DatabaseManager;
import org.bson.Document;

public class HousesCollection extends AbstractCollection<HouseData> {
    public static HousesCollection INSTANCE;
    public HousesCollection(DatabaseManager databaseManager) {
        super("houses", databaseManager);
        INSTANCE = this;
    }

    @Override
    public HouseData updateOne(HouseData houseData) {
        collection.replaceOne(new Document("houseID", houseData.getHouseID()), houseData);
        return houseData;
    }

    @Override
    public void deleteOne(HouseData houseData) {
        collection.deleteOne(new Document("houseID", houseData.getHouseID()));
    }

    @Override
    protected void migrate() {

    }

    @Override
    protected Class<HouseData> getClazz() {
        return HouseData.class;
    }
}
