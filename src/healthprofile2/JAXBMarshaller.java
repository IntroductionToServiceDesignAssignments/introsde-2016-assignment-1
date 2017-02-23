package healthprofile2;

import javax.xml.bind.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import healthprofile2.generated.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
//class used to marshall (create xml document) from value pre-setted.  The schema follow is given by the auto-generated classes
//using JAXB , following the people.xsd schema
//generate (peopleGenerated.xml)
public class JAXBMarshaller {
	public void generateXMLDocument(File xmlDocument) throws DatatypeConfigurationException {
		try {
			//initialize marshaller
			JAXBContext jaxbContext = JAXBContext.newInstance("healthprofile2.generated");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
			healthprofile2.generated.ObjectFactory factory =
					new healthprofile2.generated.ObjectFactory();

			//create xml schema
			PeopleType people = factory.createPeopleType();
			PeopleListType peopleList=factory.createPeopleListType();
			List<PersonType> personList = peopleList.getPerson();
			
			//first person
			PersonType person = factory.createPersonType();
			person.setBirthdate("24/11/92");
			person.setFirstname("Davide");
			person.setId(new Integer(1).shortValue());
			person.setLastname("Lissoni");
			HealthprofileType healthprofile= factory.createHealthprofileType();
			healthprofile.setBmi(1);
			
			healthprofile.setHeight(180);
			healthprofile.setWeight(70);
		    GregorianCalendar c = new GregorianCalendar();
		    c.setTime(new Date());
		    XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			healthprofile.setLastupdate(date2);
			person.setHealthprofile(healthprofile);
			personList.add(person);
			
			//second person
			person = factory.createPersonType();
			person.setBirthdate("17/02/63");
			person.setFirstname("Micheal");
			person.setId(new Integer(2).shortValue());
			person.setLastname("Jordan");
			healthprofile= factory.createHealthprofileType();
			healthprofile.setBmi(1);
			healthprofile.setHeight(198);
			healthprofile.setWeight(90);
		     c = new GregorianCalendar();
		    c.setTime(new Date());
		    date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			healthprofile.setLastupdate(date2);
			person.setHealthprofile(healthprofile);
			personList.add(person);
			
			//third person
			person = factory.createPersonType();
			person.setBirthdate("14/03/88");
			person.setFirstname("Stephen");
			person.setId(new Integer(3).shortValue());
			person.setLastname("Curry");
			healthprofile= factory.createHealthprofileType();
			healthprofile.setBmi(1);
			healthprofile.setHeight(191);
			healthprofile.setWeight(93);
		     c = new GregorianCalendar();
		    c.setTime(new Date());
		    date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			healthprofile.setLastupdate(date2);
			person.setHealthprofile(healthprofile);
			personList.add(person);
			
			
			
			people.setPeopleList(peopleList);
			
			//populate the xml document and start the marshaller
			JAXBElement<PeopleType> peopleElement = factory
					.createPeople(people);
			marshaller.marshal(peopleElement,
					new FileOutputStream(xmlDocument));

		} catch (IOException e) {
			System.out.println(e.toString());

		} catch (JAXBException e) {
			System.out.println(e.toString());

		}

	}

	public static void main(String[] argv) throws DatatypeConfigurationException {
		//create the new xml document and call the function to populate it
		String xmlDocument = "peopleGenerated.xml";
		JAXBMarshaller jaxbMarshaller = new JAXBMarshaller();
		jaxbMarshaller.generateXMLDocument(new File(xmlDocument));
		System.out.println("peopleGenerate.xml created");
	}
}
