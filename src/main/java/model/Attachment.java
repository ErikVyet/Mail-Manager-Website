package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import utility.DateFormatter;

@Entity
@Table(name = "attachment")
public class Attachment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private byte[] data;
	private int size;
	private String mime;
	private Date uploaded;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "email_id")
	private Email email;

	public Attachment() {
		this.name = null;
		this.data = null;
		this.size = 0;
		this.mime = null;
		this.uploaded = null;
	}

	public Attachment(String name, byte[] data, int size, String mime, Date uploaded, Email email) {
		this.name = name;
		this.data = data;
		this.size = size;
		this.mime = mime;
		this.uploaded = uploaded;
		this.email = email;
	}

	public Attachment(int id, String name, byte[] data, int size, String mime, Date uploaded, Email email) {
		this.id = id;
		this.name = name;
		this.data = data;
		this.size = size;
		this.mime = mime;
		this.uploaded = uploaded;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getMime() {
		return mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}

	public Date getUploaded() {
		return uploaded;
	}

	public void setUploaded(Date uploaded) {
		this.uploaded = uploaded;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return String.format("{\n\tId: %d,\n\tEmail: %d,\n\tName: %s,\n\tSize: %d,\n\tMime: %s,\n\tUploaded: %s\n}",
			id, email.getId(), name, size, mime, DateFormatter.toText(uploaded)
		);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + Objects.hash(email, id, mime, name, size, uploaded);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attachment attachment = (Attachment) obj;
		return this.id == attachment.getId();
	}
	
}