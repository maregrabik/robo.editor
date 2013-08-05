package pl.horaczy.robocode.editor.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import pl.horaczy.robocode.editor.model.annotation.Property;

/**
 * Przechowuje podstawowe informacje projekcie.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public class Project extends Model implements Comparable<Project>, Iterable<Robot> {

	/** UID. */
	private static final long serialVersionUID = 1L;

	/** Nazwa projektu. */
	@Property(name = "Nazwa")
	private String nazwa;
	/** Roboty w projekcie. */
	private List<Robot> robots;

	/**
	 * Tworzy nienazwany projekt.
	 */
	public Project() {
		this("");
	}

	/**
	 * Tworzy projekt o podanej nazwie.
	 * 
	 * @param	pNazwa nazwa projektu (np. Konkursowe)
	 */
	public Project(String pNazwa) {
		setNazwa(pNazwa);
		this.robots = new ArrayList<>();
	}

	/**
	 * @return wartosc pola nazwa
	 */
	public String getNazwa() {
		return this.nazwa;
	}

	/**
	 * @param	pNazwa nowa wartosc pola nazwa
	 */
	public void setNazwa(String pNazwa) {
		if (pNazwa == null) {
			throw new IllegalArgumentException("Nazwa projektu jest wymagana");
		}
		firePropertyChange("nazwa", this.nazwa, this.nazwa = pNazwa);
	}

	/**
	 * @param	pRobot nowy robot
	 */
	public void add(Robot pRobot) {
		pRobot.setProject(this);
		this.robots.add(pRobot);
	}

	/**
	 * @param	pRobots nowe roboty
	 */
	public void add(Collection<Robot> pRobots) {
		for (Robot robot : pRobots) {
			add(robot);
		}
	}

	/**
	 * @return	kolekcja robotow
	 */
	public List<Robot> getRobots() {
		return this.robots;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Project pProject) {
		return this.nazwa.compareTo(pProject != null ? pProject.nazwa : "");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Robot> iterator() {
		return this.robots.iterator();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getNazwa();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.model.Model#getInputURIPart()
	 */
	@Override
	protected String getInputURIPart() {
		return String.format("/project-%s", getNazwa());
	}
}
