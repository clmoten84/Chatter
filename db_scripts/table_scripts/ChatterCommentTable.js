/* JS code for creating Chatter_Comment table */
var params = {
    TableName: 'Chatter_Comment',
    KeySchema: [ 
        { // Required HASH type attribute
            AttributeName: 'comment_id',
            KeyType: 'HASH',
        }
    ],
    AttributeDefinitions: [ 
        {
            AttributeName: 'comment_id',
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
            AttributeName: 'forum_id',
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
            IndexName: 'forum_id_index',
            KeySchema: [
                {
                    AttributeName: 'forum_id',
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