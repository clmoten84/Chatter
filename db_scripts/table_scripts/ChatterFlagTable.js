/*
 * Chatter_Flag table creation script
 */
var params = {
    TableName : "Chatter_Flag",
    KeySchema: [       
        { AttributeName: "flag_id", KeyType: "HASH" } //Partition Key
    ],
    AttributeDefinitions: [       
        { AttributeName: "flag_id", AttributeType: "S" },
    ],
    ProvisionedThroughput: {       
        ReadCapacityUnits: 25, //Free (for a year anyways)
        WriteCapacityUnits: 25 //Free (for a year anyways)
    }
};

dynamodb.createTable(params, function(err, data) {
    if (err)
        console.log(JSON.stringify(err, null, 2));
    else
        console.log(JSON.stringify(data, null, 2));
});

// Global Secondary Index definitions
var params = {
    TableName: "Chatter_Flag",
    AttributeDefinitions:[
        {AttributeName: "created_by", AttributeType: "S"},
        {AttributeName: "time_stamp", AttributeType: "N"},
        {AttributeName: "comment_id", AttributeType: "S"}
    ],
    GlobalSecondaryIndexUpdates: [
        {
            Create: {
                IndexName: "created_by_index",
                KeySchema: [
                    {
                        AttributeName: "created_by", 
                        KeyType: "HASH" 
                    }
                ],
                Projection: {
                    "ProjectionType": "ALL"
                },
                ProvisionedThroughput: {
                    "ReadCapacityUnits": 25,
                    "WriteCapacityUnits": 25
                }
            }
        },
        {
            Create: {
                IndexName: "comment_id_index",
                KeySchema: [
                    {
                        AttributeName: "comment_id",
                        KeyType: "HASH"
                    },
                    {
                        AttributeName: "time_stamp",
                        KeyType: "RANGE"
                    }
                ],
                Projection: {
                    "ProjectionType": "ALL"
                },
                ProvisionedThroughput: {
                    "ReadCapacityUnits": 25,
                    "WriteCapacityUnits": 25
                }
            }
        }
    ]
};

dynamodb.updateTable(params, function(err, data) {
    if (err)
        console.log(JSON.stringify(err, null, 2));
    else
        console.log(JSON.stringify(data, null, 2));
});