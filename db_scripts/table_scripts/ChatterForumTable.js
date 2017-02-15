/* JS code for creating Chatter_Forum table */
var params = {
    TableName: 'Chatter_Forum',
    KeySchema: [ 
        { // Required HASH type attribute
            AttributeName: 'forum_id',
            KeyType: 'HASH',
        }
    ],
    AttributeDefinitions: [ 
        {
            AttributeName: 'forum_id',
            AttributeType: 'S'
        },
        {
            AttributeName: 'time_stamp',
            AttributeType: 'N'
        },
        {
            AttributeName: 'created_by',
            AttributeType: 'S'
        },
        {
            AttributeName: 'title',
            AttributeType: 'S'
        }
    ],
    ProvisionedThroughput: { 
        ReadCapacityUnits: 25, 
        WriteCapacityUnits: 25, 
    },
    GlobalSecondaryIndexes: [ 
        { 
            IndexName: 'created_by_index', 
            KeySchema: [
                {
                    AttributeName: 'created_by',
                    KeyType: 'HASH',
                },
                {
                    AttributeName: 'time_stamp', 
                    KeyType: 'RANGE', 
                }
            ],
            Projection: {
                ProjectionType: 'ALL'
            },
            ProvisionedThroughput: { 
                ReadCapacityUnits: 25,
                WriteCapacityUnits: 25,
            },
        },
        {
            IndexName: 'title_index',
            KeySchema: [
                {
                    AttributeName: 'title',
                    KeyType: "HASH"
                }    
            ],
            Projection: {
                ProjectionType: 'ALL'
            },
            ProvisionedThroughput: {
                ReadCapacityUnits: 25,
                WriteCapacityUnits: 25
            }
        }
    ]
};

dynamodb.createTable(params, function(err, data) {
    if (err) ppJson(err); // an error occurred
    else ppJson(data); // successful response

});