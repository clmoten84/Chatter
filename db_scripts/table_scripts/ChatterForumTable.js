/*
 * Chatter_Forum table creation script
 */
var params = {
    TableName : "Chatter_Forum",
    KeySchema: [       
        { 
            AttributeName: "forum_id", 
            KeyType: "HASH" 
        }
    ],
    AttributeDefinitions: [       
        {
            AttributeName: "forum_id",
            AttributeType: "S"
        }
    ],
    ProvisionedThroughput: {       
        ReadCapacityUnits: 25,
        WriteCapacityUnits: 25
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
    TableName: "Chatter_Forum",
    AttributeDefinitions:[
        {AttributeName: "created_by", AttributeType: "S"},
        {AttributeName: "time_stamp", AttributeType: "N"},
        {AttributeName: "title", AttributeType: "S"}
    ],
    GlobalSecondaryIndexUpdates: [
        {
            Create: {
                IndexName: "created_by_index",
                KeySchema: [
                    {
                        AttributeName: "created_by", 
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
        },
        {
            Create: {
                IndexName: "title_index",
                KeySchema: [
                    {
                        AttributeName: "title",
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
        }
    ]
};

dynamodb.updateTable(params, function(err, data) {
    if (err)
        console.log(JSON.stringify(err, null, 2));
    else
        console.log(JSON.stringify(data, null, 2));
});