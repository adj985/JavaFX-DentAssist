package model;
// Generated Mar 17, 2020 11:30:23 AM by Hibernate Tools 4.3.1

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import main.SessionConfig;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Patients generated by hbm2java
 */
@Entity
@Table(name = "patients",
        catalog = "dentassist"
)
public class Patients implements java.io.Serializable {

    private static Session session;

    private Integer idpatients;
    private String firstName;
    private String lastName;
    private String icNumber;
    private String address;
    private String residence;
    private String phoneNumber;
    private String email;
    private String warningNote;
    private Set schedulings = new HashSet(0);
    private Set interventionses = new HashSet(0);

    public Patients() {
    }

    public Patients(Integer idpatients, String firstName, String lastName,String icNumber, String address, String residence, String phoneNumber, String email, String warningNote) {
        this.idpatients = idpatients;
        this.firstName = firstName;
        this.icNumber = icNumber;
        this.lastName = lastName;
        this.address = address;
        this.residence = residence;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.warningNote = warningNote;
    }

    public Patients(String firstName, String lastName, String address, String residence, String phoneNumber, String email, String warningNote, Set schedulings, Set interventionses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.residence = residence;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.warningNote = warningNote;
        this.schedulings = schedulings;
        this.interventionses = interventionses;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "idpatients", unique = true, nullable = false)
    public Integer getIdpatients() {
        return this.idpatients;
    }

    public void setIdpatients(Integer idpatients) {
        this.idpatients = idpatients;
    }

    @Column(name = "first_name", nullable = false, length = 45)
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", nullable = false, length = 45)
    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "ic_number", nullable = false, length =13)
    public String getIcNumber() {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }
    
    

    @Column(name = "address", nullable = false, length = 45)
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "residence", nullable = false, length = 45)
    public String getResidence() {
        return this.residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    @Column(name = "phone_number", nullable = false, length = 45)
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "email", nullable = false, length = 45)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "warning_note", length = 45)
    public String getWarningNote() {
        return this.warningNote;
    }

    public void setWarningNote(String warningNote) {
        this.warningNote = warningNote;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patients")
    public Set getSchedulings() {
        return this.schedulings;
    }

    public void setSchedulings(Set schedulings) {
        this.schedulings = schedulings;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patients")
    public Set getInterventionses() {
        return this.interventionses;
    }

    public void setInterventionses(Set interventionses) {
        this.interventionses = interventionses;
    }

    //methods
    public static ObservableList<Patients> loadPatients() {
        session = SessionConfig.createSession();
        ObservableList<Patients> patientsList = FXCollections.observableArrayList();

        try {
            Query q = session.createQuery("SELECT p FROM Patients as p ORDER BY p.firstName");
            ArrayList<Patients> list = (ArrayList<Patients>) q.list();
            
            for (int i = 0; i < list.size(); i++) {
                patientsList.add(list.get(i));
            }
            return patientsList;

        } catch (HibernateException e) { 
           e.getMessage();
        } finally {
                session.close();
        }
        return null;
    }

    public static Patients findPatient(int id){
        session = SessionConfig.createSession();
        try {
            Query q = session.createQuery("select p from Patients as p where idpatients = '"+id+"'");
            Patients p = (Patients) q.list().get(0);
            return p;
            
        } catch (HibernateException e) {
            e.getMessage();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return null;
    }

    public static void createPatient(Patients p) {
        session = SessionConfig.createSession();
        Transaction tr = null;

        try {
            tr = session.beginTransaction();
            session.persist(p);
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) {
                tr.rollback();
            }
        } finally {
            session.close();
        }
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + ", " + residence;
    }
    
    

}
