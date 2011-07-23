package org.yeshira.web.controllers.model;

public class AssertionDetails {
	private String status;
	private String email;
	private String audience;
	private long valid_until;
	private String issuer;

	public String getStatus() {
		return status;
	}

	public String getEmail() {
		return email;
	}

	public String getAudience() {
		return audience;
	}

	public long getValid_until() {
		return valid_until;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public void setValid_until(long valid_until) {
		this.valid_until = valid_until;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
}
