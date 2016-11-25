//Create Chatter_User table script
var params = {
    TableName : "Chatter_Forum",
    KeySchema: [       
        { AttributeName: "forum_id", KeyType: "HASH" } //Partition Key
    ],
    AttributeDefinitions: [       
        { AttributeName: "username", AttributeType: "S" },
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