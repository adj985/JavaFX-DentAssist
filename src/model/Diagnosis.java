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
 * Diagnosis generated by hbm2java
 */
@Entity
@Table(name = "diagnosis",
         catalog = "dentassist"
)
public class Diagnosis implements java.io.Serializable {

    private static Session session;

    private Integer iddiagnosis;
    private String diagnosisName;
    private Set interventionses = new HashSet(0);

    public Diagnosis() {
    }

    public Diagnosis(Integer iddiagnosis, String diagnosisName) {
        this.diagnosisName = diagnosisName;
        this.iddiagnosis = iddiagnosis;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "iddiagnosis", unique = true, nullable = false)
    public Integer getIddiagnosis() {
        return this.iddiagnosis;
    }

    public void setIddiagnosis(Integer iddiagnosis) {
        this.iddiagnosis = iddiagnosis;
    }

    @Column(name = "diagnosis_name", length = 45)
    public String getDiagnosisName() {
        return this.diagnosisName;
    }

    public void setDiagnosisName(String diagnosisName) {
        this.diagnosisName = diagnosisName;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "diagnosis")
    public Set getInterventionses() {
        return this.interventionses;
    }

    public void setInterventionses(Set interventionses) {
        this.interventionses = interventionses;
    }

    @Override
    public String toString() {
        return diagnosisName;
    }

    //Methods
    public static void newDiagnosis(Diagnosis d) {

        session = SessionConfig.createSession();
        Transaction tr = null;

        try {
            tr = session.beginTransaction();
            session.persist(d);
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) {
                tr.rollback();
            }
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }

    }

    public static synchronized ObservableList<Diagnosis> loadDiagnosis() {
        session = SessionConfig.createSession();
        ObservableList<Diagnosis> diagnosisList = FXCollections.observableArrayList();

        try {
            Query q = session.createQuery("SELECT d FROM Diagnosis as d ORDER BY d.diagnosisName");
            ArrayList<Diagnosis> list = (ArrayList<Diagnosis>) q.list();

            for (int i = 0; i < list.size(); i++) {
                diagnosisList.add(list.get(i));
            }
            return diagnosisList;

        } catch (HibernateException e) {
            e.getMessage();
        } finally {
            session.close();
        }
        return null;
    }

    public static void updateDiagnosis(int id, String newDiagnosisName) {
        session = SessionConfig.createSession();
        Transaction tr = null;

        try {
            Diagnosis d = (Diagnosis) session.load(Diagnosis.class, id);
            tr = session.beginTransaction();
            d.setDiagnosisName(newDiagnosisName);
            session.update(d);
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) {
                tr.rollback();
            }
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

}
