package classes;
 
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
 
public class CommentDAO {
 
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("Project");
        EntityManager em = null;
        static CommentDAO instance = null;
       
        public static CommentDAO getInstance() {
                if(instance == null) {
                        instance = new CommentDAO();
                }
                return instance;
        }
       
        private CommentDAO() {
                em = factory.createEntityManager();
        }
       
        public void createComment (String username, String sku, String content, String title) {      
               
                UserDAO userDao = UserDAO.getInstance();
                Comment comment = new Comment(userDao.findUser(username), sku,
                                new java.sql.Timestamp(System.currentTimeMillis()), content, title);
               
                em.getTransaction().begin();
                em.persist(comment);
                em.getTransaction().commit();
        }
       
        @SuppressWarnings("unchecked")
        public List<Comment> recentComments(String username) {
                List<Comment> comments = new ArrayList<Comment>();
               
                Integer limiter = 5;
               
                em.getTransaction().begin();
                Query query = em.createQuery("SELECT c FROM Comment c WHERE c.user.username=:username ORDER BY c.date DESC").setMaxResults(limiter);
                query.setParameter("username", username);
                try {
                        comments = (List<Comment>)query.getResultList();
                        em.getTransaction().commit();
                        return comments;
                }
                catch (NoResultException e){
                		em.getTransaction().commit();
                        return null;
                }
        }
        
        @SuppressWarnings("unchecked")
        public List<Comment> recentCommentsForProduct(String productid) {
                List<Comment> comments = new ArrayList<Comment>();
               
                Integer limiter = 5;
               
                em.getTransaction().begin();
                Query query = em.createQuery("SELECT c FROM Comment c WHERE c.sku=:productid ORDER BY c.date DESC").setMaxResults(limiter);
                query.setParameter("productid", productid);
                try {
                        comments = (List<Comment>)query.getResultList();
                        em.getTransaction().commit();
                        return comments;
                }
                catch (NoResultException e){
                		em.getTransaction().commit();
                        return null;
                }
        }
 
        public static void main(String[] args) {
                CommentDAO dao = new CommentDAO();
       
                dao.createComment("username1", "123456", "test content 2","titletest");
                
                /*
                List<Comment> listOfResult = dao.recentComments("marisolf");
                for (Comment c : listOfResult) {
                        System.out.println(c.getSku());
                        System.out.println(c.getContent());
                        System.out.println(c.getDate());
                        System.out.println(c.getTitle());
                }  
                */
                
               /*
                List<Comment> comments = dao.recentCommentsForProduct("123456");
                for (Comment c : comments) {
                        System.out.println(c.getSku());
                        System.out.println(c.getContent());
                        System.out.println(c.getDate());
                } 
                
             */   
        }
}