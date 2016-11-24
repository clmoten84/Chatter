package com.chatter.forumservice.responses;

/**
 * ServicePropsResponse
 * @author coreym
 *
 * Encapsulates data about this service
 */
public class ServicePropsResponse {
	private String env;
	private String name;
	private String description;
	private String version;
	
	public ServicePropsResponse(String env, String name, String description, String version) {
		this.name = name;
		this.description = description;
		this.version = version;
	}
	
	public String getEnv() {
		return env;
	}
	
	public void setEnv(String env) {
		this.env = env;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
