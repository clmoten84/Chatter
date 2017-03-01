package com.chatter.fileservice.responses;

/**
 * ServicePropsResponse
 * @author coreym
 *
 * Encapsulates data about service
 */
public class ServiceProps {

	private String env;
	private String name;
	private String description;
	private String version;
	
	public ServiceProps() { }
	
	public ServiceProps(String env, String name, String description,
			String version) {
		this.env = env;
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
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("Service ENV: ").append(this.getEnv())
			.append("\nService Name: ").append(this.getName())
			.append("\nService Version: ").append(this.getVersion())
			.append("\nService Description: ").append(this.getDescription())
			.toString();
	}
}
