package com.example.awarehouse.module.warehouse.util.factory;

public class WarehouseJsonFactory {

    public static String createWarehouseCreationJson() {
        return "{\"name\":\"warehouse1\",\"unit\":\"METER\",\"numberOfRows\":2,\"groupIds\":[1,2]}";
    }
    public static String createWarehouseCreationJsonWithInvalidName(){
        return "{\"name\":\"\",\"unit\":\"METER\",\"numberOfRows\":2,\"groupIds\":[1,2]}";
    }
    public static String createWarehouseCreationJsonWithInvalidUnit(){
        return "{\"name\":\"warehouse1\",\"unit\":\"notExistingUnit\",\"numberOfRows\":2,\"groupIds\":[1,2]}";
    }
    public static String createWarehouseCreationJsonWithInvalidNumberOfRows(){
        return "{\"name\":\"warehouse1\",\"unit\":\"METER\",\"numberOfRows\":-1,\"groupIds\":[1,2]}";
    }

}
