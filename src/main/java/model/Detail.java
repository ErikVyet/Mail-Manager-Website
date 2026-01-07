package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import utility.DateFormatter;

@Entity
@Table(name = "detail")
public class Detail implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DetailId id;
	private short seen;
	private short starred;
	private short deleted;
	private Date received;
	
	@MapsId("email_id")
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "email_id")
	private Email email;
	
	@MapsId("user_id")
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;
	
	@MapsId("folder_id")
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "folder_id")
	private Folder folder;

	public Detail() {
		this.seen = 0;
		this.starred = 0;
		this.deleted = 0;
	}

	public Detail(short seen, short starred, short deleted, Date received) {
		this.seen = seen;
		this.starred = starred;
		this.deleted = deleted;
		this.received = received;
	}



	public Detail(DetailId id, short seen, short starred, short deleted, Date received, Email email, User user, Folder folder) {
		this.id = id;
		this.seen = seen;
		this.starred = starred;
		this.deleted = deleted;
		this.received = received;
		this.email = email;
		this.user = user;
		this.folder = folder;
	}

	public DetailId getId() {
		return id;
	}

	public void setId(DetailId id) {
		this.id = id;
	}

	public short isSeen() {
		return seen;
	}

	public void setSeen(short seen) {
		this.seen = seen;
	}

	public short isStarred() {
		return starred;
	}

	public void setStarred(short starred) {
		this.starred = starred;
	}

	public short isDeleted() {
		return deleted;
	}

	public void setDeleted(short deleted) {
		this.deleted = deleted;
	}

	public Date getReceived() {
		return received;
	}

	public void setReceived(Date received) {
		this.received = received;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	@Override
	public String toString() {
		return String.format("{\n\tId: %s,\n\tSeen: %d,\n\tStarred: %d,\n\tDeleted: %d,\n\tReceived: %s\n}",
			id.toString(), seen, starred, deleted, DateFormatter.toText(received)
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(deleted, email, folder, id, received, seen, starred, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Detail detail = (Detail) obj;
		return this.id.equals(detail.getId());
	}
	
}