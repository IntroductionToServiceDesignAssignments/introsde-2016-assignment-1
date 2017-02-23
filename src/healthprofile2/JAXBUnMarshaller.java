package healthprofile2;

import javax.xml.bind.*;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;

import healthprofile2.generated.*;

import java.io.*;
import java.util.List;
// class used to unmarshall (read get values and print out) the people.xml document
public class JAXBUnMarshaller {
	public void unMarshall(File xmlDocument) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance("healthprofile2.generated");
			//initialize unmarshaller
			Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
			// set the schema xsd for unmarsller
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");
			Schema schema = schemaFactory.newSchema(new File(
					"people.xsd"));
			unMarshaller.setSchema(schema);
			CustomValidationEventHandler validationEventHandler = new CustomValidationEventHandler();
			unMarshaller.setEventHandler(validationEventHandler);
			// start unmarshaller
			@SuppressWarnings("unchecked")
			JAXBElement<PeopleType> peopleElement = (JAXBElement<PeopleType>) unMarshaller
					.unmarshal(xmlDocument);
			//get out people in the xml 
			PeopleType people = peopleElement.getValue();
			PeopleListType peopleList=people.getPeopleList();
		
			//iterate the people taken xml document
			List<PersonType> personList = peopleList.getPerson();
			for (int i = 0; i < personList.size(); i++) {

				PersonType person = (PersonType) personList.get(i);
				
				System.out.println("Firstname: "
						+ person.getFirstname());
				
				System.out.println("Lastname: "
						+ person.getLastname());
				System.out.println("Birthdate: "
						+ person.getBirthdate());
				System.out.println("ID: "
						+ person.getId());
			HealthprofileType hp = person.getHealthprofile();
			System.out.println("helath profile:");
			System.out.println("BMI: "
					+ hp.getBmi());
			System.out.println("Height: "
					+ hp.getHeight());
			System.out.println("Weight: "
					+ hp.getWeight());
			System.out.println("Last update: "
					+ hp.getLastupdate());
			}
			
			
		} catch (JAXBException e) {
			System.out.println(e.toString());
		} catch (SAXException e) {
			System.out.println(e.toString());
		}
	}

	public static void main(String[] argv) {
		// set the document to unmarshall and run the method
		File xmlDocument = new File("people.xml");
		JAXBUnMarshaller jaxbUnmarshaller = new JAXBUnMarshaller();

		jaxbUnmarshaller.unMarshall(xmlDocument);

	}

	class CustomValidationEventHandler implements ValidationEventHandler {
		// unmarshall events checking
		public boolean handleEvent(ValidationEvent event) {
			if (event.getSeverity() == ValidationEvent.WARNING) {
				return true;
			}
			if ((event.getSeverity() == ValidationEvent.ERROR)
					|| (event.getSeverity() == ValidationEvent.FATAL_ERROR)) {

				System.out.println("Validation Error:" + event.getMessage());

				ValidationEventLocator locator = event.getLocator();
				System.out.println("at line number:" + locator.getLineNumber());
				System.out.println("Unmarshalling Terminated");
				return false;
			}
			return true;
		}

	}
}
