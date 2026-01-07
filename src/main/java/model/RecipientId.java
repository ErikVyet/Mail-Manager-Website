package model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class RecipientId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer email_id;
	private Integer user_id;

	public RecipientId() { }

	public RecipientId(Integer email_id, Integer user_id) {
		this.email_id = email_id;
		this.user_id = user_id;
	}

	public Integer getEmail_id() {
		return email_id;
	}

	public void setEmail_id(Integer email_id) {
		this.email_id = email_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	@Override
	public String toString() {
		return String.format("{ Email: %d, User: %d }", email_id, user_id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email_id, user_id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecipientId other = (RecipientId) obj;
		return Objects.equals(email_id, other.email_id) && Objects.equals(user_id, other.user_id);
	}
	
}