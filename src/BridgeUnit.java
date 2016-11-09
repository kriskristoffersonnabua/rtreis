import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javafx.collections.FXCollections;
import models.ClockingSchedule;
import models.DailyAttendance;
import models.Department;
import org.hibernate.Query;
import models.Employee;
import models.EmploymentStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class BridgeUnit {
    
    
    private SessionFactory session_factory;
    private static Session session;
    private static BridgeUnit instance;

    public static BridgeUnit getInstance() {
        if (instance == null) {
            instance = new BridgeUnit();
        }
        return instance;
    }

    public BridgeUnit() {
        session_factory = new Configuration().configure().buildSessionFactory();
    }

    public void save(Object object) {
        try {
            session = session_factory.openSession();
            session.beginTransaction();
            session.save(object);
            session.getTransaction().commit();
            session.close();
        } catch (Exception err) {
            System.out.println("error log");
            err.printStackTrace();
            session.getTransaction().rollback();
            session.close();
        }

    }
    public void update(Object object) {
        try {
            session = session_factory.openSession();
            session.beginTransaction();
            session.update(object);
            session.getTransaction().commit();
            session.close();
        } catch (Exception err) {
            System.out.println("error log");
            err.printStackTrace();
            session.getTransaction().rollback();
            session.close();
        }

    }
    
    public void delete (Object object) {
        try {
            session = session_factory.openSession();
            session.beginTransaction();
            session.delete(object);
            session.getTransaction().commit();
            session.close();
        } catch (Exception err) {
            System.out.println("error log");
            err.printStackTrace();
            session.getTransaction().rollback();
            session.close();
        }
    }

    public Employee getEmployee(int id) {
        session = session_factory.openSession();
        session.beginTransaction();
        Employee obj = (Employee)session.get(Employee.class, id);
        return obj;
    }
    
    public ClockingSchedule getClockingSchedule(int id) {
        session = session_factory.openSession();
        session.beginTransaction();
        ClockingSchedule obj = (ClockingSchedule)session.get(ClockingSchedule.class, id);
        return obj;
    }

    public List getAllDepartments () {
        session = session_factory.openSession();
        session.beginTransaction();
        Query query = (Query) session.getNamedQuery("Department.getAll");
        List department_list = query.list();
        session.close();
        return department_list;
    }
    
    public List getAllClockingSchedule () {
        session = session_factory.openSession();
        session.beginTransaction();
        Query query = (Query) session.getNamedQuery("ClockingSchedule.getAll");
        List clocking_list = query.list();
        session.close();
        return clocking_list;
    }
    
    synchronized public List getAllEmployees () {
        session = session_factory.openSession();
        session.beginTransaction();
        String q = "from employee";
        Query query = session.createQuery(q);
        List employee_list = query.list();
        session.close();
        return employee_list;
    }
    
    public List getSchedules () {
        session = session_factory.openSession();
        session.beginTransaction();
        Query query = (Query) session.getNamedQuery("ClockingSchedule.getAll");
        List schedule_list = query.list();
        session.close();
        return schedule_list;
    }
    
    public DailyAttendance getAttendanceRecord (int emp_id, long date) {
        session = session_factory.openSession();
        session.beginTransaction();
        Query query = ( Query ) session.createQuery("select date from employee attendance_record where employee_id = ? and date = ?");
        query.setInteger(0, emp_id);
        query.setLong(1, date);
        DailyAttendance record = (DailyAttendance) query.list();
        return record;
    }
    
    public void cleanAttendanceRecord (int id) {
        session = session_factory.openSession();
        session.beginTransaction();
        Query query = ( Query ) session.createQuery("delete from attendance_pivot_table where attendanceId in ( select attendanceId from ( select max(attendanceId) as attendanceId from attendance_pivot_table where date in ( select date from ( select * from attendance_pivot_table where employee_id = :id ) as dup group by date having count(*) > 1 ) group by date ) as s )");
        query.setParameter("id", id);
        query.executeUpdate();
    }
    
    public Session getRunningSession(){
        return session;
    }
    
    public void closeSession() {
        session.close();
    }
    
    public void close () {
        session_factory.close();
    }
    
}
