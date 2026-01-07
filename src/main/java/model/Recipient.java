package model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "recipient")
public class Recipient implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private RecipientId id;
	
	@Enumerated(value = EnumType.STRING)
	private Type type;
	
	@MapsId("email_id")
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "email_id")
	private Email email;
	
	@MapsId("user_id")
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

	public Recipient() {
		this.email = null;
		this.user = null;
	}

	public Recipient(Type type, Email email, User user) {
		this.type = type;
		this.email = email;
		this.user = user;
	}

	public Recipient(RecipientId id, Type type, Email email, User user) {
		this.id = id;
		this.type = type;
		this.email = email;
		this.user = user;
	}

	public RecipientId getId() {
		return id;
	}

	public void setId(RecipientId id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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

	@Override
	public String toString() {
		return String.format("{\n\tId: %s,\n\tEmail: %d,\n\tUser: %d\n}", 
			id.toString(), email.getId(), user.getId()
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, id, type, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recipient recipient = (Recipient) obj;
		return this.id.equals(recipient.getId());
	}

}