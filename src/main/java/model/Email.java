package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import utility.DateFormatter;

@Entity
@Table(name = "email")
public class Email implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String subject;
	private String body;
	private Date sent;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "sender")
	private User user;
	
	@OneToMany(mappedBy = "email", cascade = CascadeType.ALL)
	private List<Attachment> attachments;
	
	@OneToMany(mappedBy = "email", cascade = CascadeType.ALL)
	private List<Detail> details;

	public Email() {
		this.subject = null;
		this.body = null;
		this.sent = null;
	}

	public Email(String subject, String body, Date sent, User user, List<Attachment> attachments, List<Detail> details) {
		this.subject = subject;
		this.body = body;
		this.sent = sent;
		this.user = user;
		this.attachments = attachments;
		this.details = details;
	}

	public Email(int id, String subject, String body, Date sent, User user, List<Attachment> attachments, List<Detail> details) {
		this.id = id;
		this.subject = subject;
		this.body = body;
		this.sent = sent;
		this.user = user;
		this.attachments = attachments;
		this.details = details;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getSent() {
		return sent;
	}

	public void setSent(Date sent) {
		this.sent = sent;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return String.format("{\n\tId: %d,\n\tSender: %s,\n\tSubject: %s,\n\tBody: %s,\n\tSent: %s\n}",
			id, user.getUsername(), subject, body, DateFormatter.toText(sent)
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(body, id, sent, subject, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Email email = (Email) obj;
		return this.id == email.getId();
	}
	
}