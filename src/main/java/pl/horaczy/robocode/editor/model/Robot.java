package pl.horaczy.robocode.editor.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import pl.horaczy.robocode.editor.model.annotation.Property;

public class Robot extends Model implements Comparable<Robot>, Iterable<Code> {

	/** UID. */
	private static final long serialVersionUID = 1L;

	/** Projekt - rodzic. */
	private Project project;
	/** Nazwa robota. */
	@Property(name = "Nazwa")
	private String nazwa;
	/** Wersje kodow robota. */
	private List<Code> codes;

	/**
	 * Tworzy nienazwanego robota.
	 */
	public Robot() {
		this("");
	}

	/**
	 * Tworzy robota o podanej nazwie.
	 * 
	 * @param	pNazwa nazwa robota
	 */
	public Robot(String pNazwa) {
		setNazwa(pNazwa);
		this.codes = new ArrayList<>();
	}

	/**
	 * @return	wartosc pola nazwa
	 */
	public String getNazwa() {
		return this.nazwa;
	}

	/**
	 * @param	pNazwa nowa nazwa robota
	 */
	public void setNazwa(String pNazwa) {
		firePropertyChange("nazwa", this.nazwa, this.nazwa = pNazwa);
	}

	/**
	 * @param	pCode nowy kod
	 */
	public void add(Code pCode) {
		pCode.setRobot(this);
		this.codes.add(pCode);
	}

	/**
	 * @param	pCodes nowe kody
	 */
	public void add(Collection<Code> pCodes) {
		for (Code code : pCodes) {
			add(code);
		}
	}

	/**
	 * @return	kolekcja kodow robota (kolejne wersje)
	 */
	public List<Code> getCodes() {
		return this.codes;
	}

	/**
	 * @return	wartosc pola project
	 */
	public Project getProject() {
		return this.project;
	}

	/**
	 * @param	pProject nowa wartosc pola project
	 */
	public void setProject(Project pProject) {
		this.project = pProject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Robot pRobot) {
		return this.nazwa.compareTo(pRobot != null ? pRobot.nazwa : "");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Code> iterator() {
		return this.codes.iterator();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s v%d", getProject(), getNazwa());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.model.Model#getInputURIPart()
	 */
	@Override
	protected String getInputURIPart() {
		return String.format("%s/robot-%s", getProject().getInputURIPart(), getNazwa());
	}
}
