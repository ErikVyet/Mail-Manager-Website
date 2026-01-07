package model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class DetailId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer email_id;
	private Integer user_id;
	private Integer folder_id;
	
	public DetailId() { }

	public DetailId(Integer email_id, Integer user_id, Integer folder_id) {
		super();
		this.email_id = email_id;
		this.user_id = user_id;
		this.folder_id = folder_id;
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

	public Integer getFolder_id() {
		return folder_id;
	}

	public void setFolder_id(Integer folder_id) {
		this.folder_id = folder_id;
	}

	@Override
	public String toString() {
		return String.format("{ Email: %d, User: %d, Folder: %d }", 
			email_id, user_id, folder_id
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email_id, folder_id, user_id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetailId other = (DetailId) obj;
		return Objects.equals(email_id, other.email_id) && Objects.equals(folder_id, other.folder_id)
				&& Objects.equals(user_id, other.user_id);
	}
	
}