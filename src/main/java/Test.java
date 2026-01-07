import java.util.List;

import javax.persistence.EntityManager;

import model.Attachment;
import model.Detail;
import model.Email;
import model.Folder;
import model.Recipient;
import model.User;
import utility.Hibernate;

public class Test {

	public static void main(String[] args) {
		EntityManager em = Hibernate.getEntityManager();
		
		List<User> users = em.createQuery("FROM User", User.class).getResultList();
		for (User user : users) {
			System.out.println(user);
		}
		
		System.out.println("----------------------------------------------------------");
		
		List<Folder> folders = em.createQuery("FROM Folder", Folder.class).getResultList();
		for (Folder folder : folders) {
			System.out.println(folder);
		}
		
		System.out.println("----------------------------------------------------------");
		
		List<Email> emails = em.createQuery("FROM Email", Email.class).getResultList();
		for (Email email : emails) {
			System.out.println(email);
		}
		
		System.out.println("----------------------------------------------------------");
		
		List<Attachment> attachments = em.createQuery("FROM Attachment", Attachment.class).getResultList();
		for (Attachment attachment : attachments) {
			System.out.println(attachment);
		}
		
		System.out.println("----------------------------------------------------------");
		
		List<Recipient> recipients = em.createQuery("FROM Recipient", Recipient.class).getResultList();
		for (Recipient recipient : recipients) {
			System.out.println(recipient);
		}
		
		System.out.println("----------------------------------------------------------");
		
		List<Detail> details = em.createQuery("FROM Detail", Detail.class).getResultList();
		for (Detail detail : details) {
			System.out.println(detail);
		}
		
		em.close();
	}

}