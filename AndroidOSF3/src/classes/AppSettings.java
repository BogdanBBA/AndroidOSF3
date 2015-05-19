package classes;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import android.net.Uri;

public class AppSettings
{
	public boolean EnlargeUserInterface = true;
	public boolean RememberLastSemester = true;

	public String LoadFromFile()
	{
		String phase = "initializing";
		try
		{
			phase = "checking files/folders";
			if (!new File(Paths.SettingsFile).exists())
				throw new PreventableErrorException("The file \"" + Paths.SettingsFile + "\" doesn't exist!");

			phase = "opening xml";
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Uri.fromFile(new File(Paths.SettingsFile)).toString());

			phase = "parsing xml nodes *1";
			for (int iDoc = 0; iDoc < doc.getChildNodes().item(0).getChildNodes().getLength(); iDoc++)
				if (doc.getChildNodes().item(0).getChildNodes().item(iDoc).getNodeType() == Node.ELEMENT_NODE)
				{
					phase = "parsing xml nodes *2";
					Element nDoc = (Element) doc.getChildNodes().item(0).getChildNodes().item(iDoc);
					if (nDoc.getTagName().equals("EnlargeUserInterface"))
						this.EnlargeUserInterface = Boolean.parseBoolean(nDoc.getAttribute("value"));
					else
						if (nDoc.getTagName().equals("RememberLastSemester"))
							this.RememberLastSemester = Boolean.parseBoolean(nDoc.getAttribute("value"));
				}
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

	public String SaveToFile()
	{
		String phase = "initializing";
		try
		{
			// root element
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element rootElement = doc.createElement("AndroidOSF3_Settings");
			doc.appendChild(rootElement);

			// settings
			Element setting = doc.createElement("EnlargeUserInterface");
			Attr attr = doc.createAttribute("value");
			attr.setValue(Boolean.toString(this.EnlargeUserInterface));
			setting.setAttributeNode(attr);
			rootElement.appendChild(setting);

			setting = doc.createElement("RememberLastSemester");
			attr = doc.createAttribute("value");
			attr.setValue(Boolean.toString(this.RememberLastSemester));
			setting.setAttributeNode(attr);
			rootElement.appendChild(setting);

			// write
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(Paths.SettingsFile));
			transformer.transform(source, result);
		}
		catch (Exception e)
		{
			return "An unchecked/unexpected " + e.getClass().getName() + " ERROR occured while initializing the application.\n\nPhase: " + phase + "\n\n" + e.getMessage();
		}
		return "";
	}
}
