package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import utility.DateFormatter;

@Entity
@Table(
	name = "user", 
	uniqueConstraints = @UniqueConstraint(columnNames = { "email", "username" })
)
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String email;
	private String username;
	private String password;
	private String fullname;
	private Date created;
	private Date login;
	
	@Enumerated(value = EnumType.STRING)
	private Status status;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Folder> folders;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Email> emails;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Detail> details;

	public User() {
		this.email = null;
		this.username = null;
		this.password = null;
		this.fullname = null;
		this.created = null;
		this.login = null;
	}

	public User(String email, String username, String password, String fullname, Date created, Date login, Status status, List<Folder> folders, List<Email> emails, List<Detail> details) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.created = created;
		this.login = login;
		this.status = status;
		this.folders = folders;
		this.emails = emails;
		this.details = details;
	}



	public User(int id, String email, String username, String password, String fullname, Date created, Date login, Status status, List<Folder> folders, List<Email> emails, List<Detail> details) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.created = created;
		this.login = login;
		this.status = status;
		this.folders = folders;
		this.emails = emails;
		this.details = details;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLogin() {
		return login;
	}

	public void setLogin(Date login) {
		this.login = login;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<Folder> getFolders() {
		return folders;
	}

	public void setFolders(List<Folder> folders) {
		this.folders = folders;
	}

	public List<Email> getEmails() {
		return emails;
	}

	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return String.format("{\n\tId: %d,\n\tEmail: %s,\n\tUsername: %s,\n\t"
				+ "Password: %s,\n\tFullname: %s,\n\tCreated: %s,\n\tLogin: %s,\n\t"
				+ "Status: %s\n}", 
			id, email, username, password, fullname, 
			DateFormatter.toText(created), DateFormatter.toText(login), status
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(created, email, fullname, id, login, password, status, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User user = (User) obj;
		return this.id == user.getId();
	}
   
}