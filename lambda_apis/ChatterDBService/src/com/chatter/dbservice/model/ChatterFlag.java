package com.chatter.dbservice.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * ChatterFlag
 * @author coreym
 *
 * This class represents a Flag that can be added to inappropriate comments by
 * users in a Chatter Forum instance. This class contains the following data
 * attributes:
 * 
 * - flagId (String)
 * - createdBy (String)
 * - timeStamp (Long)
 * - forumId (String)
 * - commentId (String)
 * - flagDescription (String)
 */
@DynamoDBTable(tableName = "Chatter_Flag")
public class ChatterFlag {
	
	private String flagId;
	private String createdBy;
	private Long timeStamp;
	private String forumId;
	private String commentId;
	private String flagDescription;
	
	public ChatterFlag() {}

	@DynamoDBHashKey(attributeName = "flag_id")
	@DynamoDBAutoGeneratedKey
	public String getFlagId() {
		return flagId;
	}

	public void setFlagId(String flagId) {
		this.flagId = flagId;
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "created_by_index", attributeName = "created_by")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@DynamoDBIndexRangeKey(globalSecondaryIndexName = "comment_id_index", attributeName = "time_stamp")
	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@DynamoDBAttribute(attributeName = "forum_id")
	public String getForumId() {
		return forumId;
	}

	public void setForumId(String forumId) {
		this.forumId = forumId;
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "comment_id_index", attributeName = "comment_id")
	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	@DynamoDBAttribute(attributeName = "flag_desc")
	public String getFlagDescription() {
		return flagDescription;
	}

	public void setFlagDescription(String flagDescription) {
		this.flagDescription = flagDescription;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder()
			.append("***** Chatter Flag object *****")
			.append("\nflag_id: ").append(this.flagId)
			.append("\ncreated_by: ").append(this.createdBy)
			.append("\ntime_stamp: ").append(this.timeStamp)
			.append("\nforum_id: ").append(this.forumId)
			.append("\ncomment_id: ").append(this.commentId)
			.append("\nflag_desc: ").append(this.flagDescription);
		return builder.toString();
	}
}