package classes;

import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import android.graphics.Color;
import android.net.Uri;

public class Database
{
	public final ArrayList<TimeInterval> TimeIntervals;
	public final ArrayList<ClassType> ClassTypes;
	public final ArrayList<Professor> Professors;
	public final ArrayList<Room> Rooms;
	public final ArrayList<Semester> Semesters;

	public Database()
	{
		this.TimeIntervals = new ArrayList<TimeInterval>();
		this.ClassTypes = new ArrayList<ClassType>();
		this.Professors = new ArrayList<Professor>();
		this.Rooms = new ArrayList<Room>();
		this.Semesters = new ArrayList<Semester>();
	}

	public TimeInterval GetTimeIntervalByID(String id)
	{
		for (TimeInterval timeInterval: TimeIntervals)
			if (timeInterval.id.equals(id))
				return timeInterval;
		return null;
	}

	public ClassType GetClassTypeByID(String id)
	{
		for (ClassType classType: ClassTypes)
			if (classType.id.equals(id))
				return classType;
		return null;
	}

	public Professor GetProfessorByID(String id)
	{
		for (Professor professor: Professors)
			if (professor.id.equals(id))
				return professor;
		return null;
	}

	public Room GetRoomByID(String id)
	{
		for (Room room: Rooms)
			if (room.id.equals(id))
				return room;
		return null;
	}

	public Semester GetSemesterByID(String id)
	{
		for (Semester semester: Semesters)
			if (semester.id.equals(id))
				return semester;
		return null;
	}

	public String OpenDatabase()
	{
		String phase = "initializing";
		try
		{
			phase = "checking files/folders";
			if (!new File(Paths.DatabaseFile).exists())
				throw new PreventableErrorException("The file \"" + Paths.DatabaseFile + "\" doesn't exist!");

			phase = "preparing log file";
			BufferedWriter buffWr = new BufferedWriter(new FileWriter(Paths.LogFile));
			buffWr.write(" # Initialized log file\n\n");

			phase = "opening xml";
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Uri.fromFile(new File(Paths.DatabaseFile)).toString());

			phase = "parsing xml nodes *1";
			for (int iDoc = 0; iDoc < doc.getChildNodes().item(0).getChildNodes().getLength(); iDoc++)
				if (doc.getChildNodes().item(0).getChildNodes().item(iDoc).getNodeType() == Node.ELEMENT_NODE)
				{
					phase = "parsing xml nodes *2";
					Element nDoc = (Element) doc.getChildNodes().item(0).getChildNodes().item(iDoc);
					buffWr.write(" # " + nDoc.getTagName() + "\n");
					if (nDoc.getTagName().equals("DateGlobale"))
					{
						for (int iDG = 0; iDG < nDoc.getChildNodes().getLength(); iDG++)
							if (nDoc.getChildNodes().item(iDG).getNodeType() == Node.ELEMENT_NODE)
							{
								phase = "parsing xml nodes *3";
								Element nDG = (Element) nDoc.getChildNodes().item(iDG);
								if (nDG.getTagName().equals("IntervaleOrare"))
								{
									phase = "decoding IntervaleOrare";
									for (int iIO = 0; iIO < nDG.getChildNodes().getLength(); iIO++)
										if (nDG.getChildNodes().item(iIO).getNodeType() == Node.ELEMENT_NODE)
										{
											Element nIO = (Element) nDG.getChildNodes().item(iIO);
											String id = nIO.getAttribute("ID");
											TimeSpan deLa = Utils.DecodeTime(nIO.getAttribute("deLa"));
											TimeSpan panaLa = Utils.DecodeTime(nIO.getAttribute("la"));
											this.TimeIntervals.add(new TimeInterval(id, deLa, panaLa));
											buffWr.write(String.format(" * %s\n", this.TimeIntervals.get(this.TimeIntervals.size() - 1).toString()));
										}
								}
								else
									if (nDG.getTagName().equals("TipuriOre"))
									{
										phase = "decoding TipuriOre";
										for (int iTO = 0; iTO < nDG.getChildNodes().getLength(); iTO++)
											if (nDG.getChildNodes().item(iTO).getNodeType() == Node.ELEMENT_NODE)
											{
												Element nTO = (Element) nDG.getChildNodes().item(iTO);
												String id = nTO.getAttribute("ID");
												String nume = nTO.getAttribute("nume");
												int col = Color.parseColor(nTO.getAttribute("col"));
												this.ClassTypes.add(new ClassType(id, nume, col));
												buffWr.write(String.format(" * %s\n", this.ClassTypes.get(this.ClassTypes.size() - 1).toString()));
											}
									}
									else
										if (nDG.getTagName().equals("Profesori"))
										{
											phase = "decoding Profesori";
											for (int iProf = 0; iProf < nDG.getChildNodes().getLength(); iProf++)
												if (nDG.getChildNodes().item(iProf).getNodeType() == Node.ELEMENT_NODE)
												{
													Element nProf = (Element) nDG.getChildNodes().item(iProf);
													String id = nProf.getAttribute("ID");
													String nume = nProf.getAttribute("nume");
													this.Professors.add(new Professor(id, nume));
													buffWr.write(String.format(" * %s\n", this.Professors.get(this.Professors.size() - 1).toString()));
												}
										}
										else
											if (nDG.getTagName().equals("Sali"))
											{
												phase = "decoding Sali";
												for (int iRoom = 0; iRoom < nDG.getChildNodes().getLength(); iRoom++)
													if (nDG.getChildNodes().item(iRoom).getNodeType() == Node.ELEMENT_NODE)
													{
														Element nRoom = (Element) nDG.getChildNodes().item(iRoom);
														String id = nRoom.getAttribute("ID");
														String nume = nRoom.getAttribute("nume");
														this.Rooms.add(new Room(id, nume));
														buffWr.write(String.format(" * %s\n", this.Rooms.get(this.Rooms.size() - 1).toString()));
													}
											}
							}
					}
					else
						if (nDoc.getTagName().equals("Semestre"))
						{
							for (int iSems = 0; iSems < nDoc.getChildNodes().getLength(); iSems++)
								if (nDoc.getChildNodes().item(iSems).getNodeType() == Node.ELEMENT_NODE)
								{
									Element nSems = (Element) nDoc.getChildNodes().item(iSems);
									if (nSems.getTagName().equals("Semestru"))
									{
										String sId = nSems.getAttribute("ID");
										String sUni = nSems.getAttribute("universitate");
										String sFac = nSems.getAttribute("facultate");
										String sGr = nSems.getAttribute("grupa");
										String sAn = nSems.getAttribute("anUniversitar");
										String sSem = nSems.getAttribute("semestru");
										Semester semester = new Semester(sId, sUni, sFac, sGr, sAn, sSem, new ArrayList<WeekCategory>(), new ArrayList<Discipline>());

										for (int iSem = 0; iSem < nDoc.getChildNodes().getLength(); iSem++)
											if (nSems.getChildNodes().item(iSem).getNodeType() == Node.ELEMENT_NODE)
											{
												Element nSem = (Element) nSems.getChildNodes().item(iSem);
												if (nSem.getTagName().equals("CategoriiSaptamani"))
												{
													phase = "decoding CategoriiSaptamani";
													for (int iCS = 0; iCS < nSem.getChildNodes().getLength(); iCS++)
														if (nSem.getChildNodes().item(iCS).getNodeType() == Node.ELEMENT_NODE)
														{
															Element nCS = (Element) nSem.getChildNodes().item(iCS);
															String id = nCS.getAttribute("ID");
															String nume = nCS.getAttribute("nume");
															ArrayList<Week> weekList = Utils.DecodeWeekList(nCS.getAttribute("incepand"));
															semester.WeekCategories.add(new WeekCategory(id, nume, weekList));
															buffWr.write(String.format(" * %s\n", semester.WeekCategories.get(semester.WeekCategories.size() - 1).toString()));
														}
												}
												else
													if (nSem.getTagName().equals("Discipline"))
													{
														phase = "decoding Discipline";
														for (int iD = 0; iD < nSem.getChildNodes().getLength(); iD++)
															if (nSem.getChildNodes().item(iD).getNodeType() == Node.ELEMENT_NODE)
															{
																Element nD = (Element) nSem.getChildNodes().item(iD);
																String id = nD.getAttribute("ID");
																String nume = nD.getAttribute("nume");
																int col1 = Color.parseColor(nD.getAttribute("col1"));
																int col2 = Color.parseColor(nD.getAttribute("col2"));
																Discipline discipline = new Discipline(id, nume, col1, col2, new ArrayList<Class>());

																phase = "decoding Discipline classes";
																for (int iDC = 0; iDC < nD.getChildNodes().getLength(); iDC++)
																	if (nD.getChildNodes().item(iDC).getNodeType() == Node.ELEMENT_NODE)
																	{
																		Element nDC = (Element) nD.getChildNodes().item(iDC);
																		String when = nDC.getAttribute("cand");
																		ClassType tip = this.GetClassTypeByID(nDC.getAttribute("tip"));
																		Professor prof = this.GetProfessorByID(nDC.getAttribute("cuCine"));
																		Room room = this.GetRoomByID(nDC.getAttribute("unde"));
																		discipline.classes.addAll(Utils.GenerateClassesForValues(discipline, when, tip, prof, room, semester.WeekCategories, this));
																	}

																phase = "finishing decoding Discipline";
																semester.Disciplines.add(discipline);
																buffWr.write(String.format(" * %s\n", semester.Disciplines.get(semester.Disciplines.size() - 1).toString()));
															}
													}
											}
										semester.ProcessData();
										this.Semesters.add(semester);
										buffWr.write(String.format(" * %s\n", semester.toString()));
									}
								}
						}
				}

			buffWr.close();
		}
		catch (PreventableErrorException pee)
		{
			return "A preventable ERROR occured while initializing the application.\n\nPhase: " + phase + "\n\n" + pee.getMessage();
		}
		catch (Exception e)
		{
			return "An unchecked/unexpected " + e.getClass().getName() + " ERROR occured while initializing the application.\n\nPhase: " + phase + "\n\n" + e.getMessage();
		}
		return "";
	}
}
