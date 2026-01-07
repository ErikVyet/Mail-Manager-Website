package utility;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Hibernate {

	public static EntityManager getEntityManager() {
		return Persistence.createEntityManagerFactory("Mail Manager Website").createEntityManager();
	}
	
}