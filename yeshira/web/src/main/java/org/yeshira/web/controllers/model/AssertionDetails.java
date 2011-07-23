package org.yeshira.web.controllers.model;

import org.codehaus.jackson.annotate.JsonSetter;

public class AssertionDetails {
	private String status;
	private String email;
	private String audience;
	private long validUntil;
	private String issuer;
	private String reason;

	public boolean isSuccess() {
		return "okay".equals(status);
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public String getEmail() {
		return email;
	}

	public String getAudience() {
		return audience;
	}

	public long getValidUntil() {
		return validUntil;
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

	@JsonSetter(value="valid-until")
	public void setValidUntil(long validUntil) {
		this.validUntil = validUntil;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
}
