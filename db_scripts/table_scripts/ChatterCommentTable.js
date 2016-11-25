/*
 * Chatter_Comment table creation script
 */
var params = {
    TableName : "Chatter_Comment",
    KeySchema: [       
        { AttributeName: "comment_id", KeyType: "HASH" } //Partition Key
    ],
    AttributeDefinitions: [       
        { AttributeName: "comment_id", AttributeType: "S" },
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
    TableName: "Chatter_Comment",
    AttributeDefinitions:[
        {AttributeName: "created_by", AttributeType: "S"},
        {AttributeName: "time_stamp", AttributeType: "N"},
        {AttributeName: "parent_forum_id", AttributeType: "S"}
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
                IndexName: "parent_forum_index",
                KeySchema: [
                    {
                        AttributeName: "parent_forum_id",
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