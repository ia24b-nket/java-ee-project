package org.example.dailyplanner;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.*;
import java.io.IOException;
import java.time.LocalTime;

@WebServlet("/task")
public class TaskServlet extends HttpServlet {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("dailyplannerPU");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String timeSlotStr = request.getParameter("timeSlot");
        String username = (String) request.getSession().getAttribute("username");

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();

            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setTimeSlot(LocalTime.parse(timeSlotStr));
            task.setUser(user);

            em.persist(task);
            em.getTransaction().commit();

            response.sendRedirect("newTask.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        } finally {
            em.close();
        }
    }

    @Override
    public void destroy() {
        emf.close();
    }
}
