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
@Table(name = "folder")
public class Folder implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private short system;
	private Date created;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
	private List<Detail> details;
	
	public Folder() {
		this.name = null;
		this.system = 0;
		this.created = null;
	}

	public Folder(String name, short system, Date created, User user, List<Detail> details) {
		this.name = name;
		this.system = system;
		this.created = created;
		this.user = user;
		this.details = details;
	}

	public Folder(int id, String name, short system, Date created, User user, List<Detail> details) {
		super();
		this.id = id;
		this.name = name;
		this.system = system;
		this.created = created;
		this.user = user;
		this.details = details;
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

	public short isSystem() {
		return system;
	}

	public void setSystem(short system) {
		this.system = system;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return String.format("{\n\tId: %d,\n\tUser: %s,\n\tName: %s,\n\tSystem: %d,\n\tCreated: %s\n}",
			id, user.getUsername(), name, system, DateFormatter.toText(created)
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(created, id, name, system);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Folder folder = (Folder) obj;
		return this.id == folder.getId();
	}

}